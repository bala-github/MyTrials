package org.bala.ESQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESQuerySyntax {
	
	final static Logger logger = LoggerFactory.getLogger(ESQuerySyntax.class);
	
	private Edge startingEdge;
		
	private Stack<Token> condTokens;

    private Stack<Token> logicalTokens;
    
    private Stack<String> filters;
    
    private Stack<Token> sortTokens;
    
    private List<Token> groupTokens;
    
    private List<Token> aggTokens;
	
	private StringBuilder query;
	
	private TokenType queryType;
			
	private boolean hasContainsOperator = false;
	
	private void init() {
		
		
		condTokens = new Stack<Token>();
        logicalTokens = new Stack<Token>();
        filters = new Stack<String>();
        sortTokens = new Stack<Token>();
        groupTokens = new ArrayList<Token>();
        aggTokens = new ArrayList<Token>();
		query = new StringBuilder();
		queryType = null;
	
	}
	
	
	public ESQuerySyntax() {
		
	    init();
	    
		Edge selectEdge = new Edge(TokenType.SELECT);
		Edge descEdge = new Edge(TokenType.DESC);
		Edge fieldEdge = new Edge(TokenType.FIELD);		
		Edge fromEdge = new Edge(TokenType.FROM);		
		Edge tableNameEdge = new Edge(TokenType.TABLE_NAME);		
		Edge terminatorEdge = new Edge(TokenType.TERMINATOR);
		Edge whereEdge = new Edge(TokenType.WHERE);
		
		Edge conditionOperatorEdge = new Edge(TokenType.CONDITONAL_OPEARATOR);
		Edge valueEdge = new Edge(TokenType.VALUE);
		Edge groupOpenEdge = new Edge(TokenType.GROUP_OPEN);
		Edge groupCloseEdge = new Edge(TokenType.GROUP_CLOSE);
		Edge logicalOperatorEdge = new Edge(TokenType.LOGICAL_OPERATOR);
		
		Edge orderEdge = new Edge(TokenType.ORDER);
		Edge byEdge = new Edge(TokenType.BY);
		Edge directionEdge = new Edge(TokenType.DIRECTION);
		Edge groupEdge = new Edge(TokenType.GROUP);
		
		Edge sumEdge = new Edge(TokenType.SUM);
		Edge avgEdge = new Edge(TokenType.AVG);
		Edge maxEdge = new Edge(TokenType.MAX);
		Edge minEdge = new Edge(TokenType.MIN);
		Edge countEdge = new Edge(TokenType.COUNT);
		
		selectEdge.addNeighbour(fieldEdge);
		selectEdge.addNeighbour(sumEdge);
		selectEdge.addNeighbour(avgEdge);
		selectEdge.addNeighbour(maxEdge);
		selectEdge.addNeighbour(minEdge);
		selectEdge.addNeighbour(countEdge);
		
		descEdge.addNeighbour(tableNameEdge);
		
		
		fieldEdge.addNeighbour(fromEdge,  new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), 
				new ContainsFilter(TokenType.SELECT), new DoesNotHaveAtFilter(TokenType.GROUP_OPEN, -1)})));
		fieldEdge.addNeighbour(fieldEdge, new OneOfFilter(Arrays.asList(new Filter[] { 
				 new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new ContainsFilter(TokenType.SELECT)})),
				 new ContainsFilter(TokenType.GROUP)
		})));
		
		
		fieldEdge.addNeighbour(sumEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new DoesNotHaveAtFilter(TokenType.GROUP_OPEN, -1)})));
		fieldEdge.addNeighbour(avgEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new DoesNotHaveAtFilter(TokenType.GROUP_OPEN, -1)})));
		fieldEdge.addNeighbour(maxEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new DoesNotHaveAtFilter(TokenType.GROUP_OPEN, -1)})));
		fieldEdge.addNeighbour(minEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new DoesNotHaveAtFilter(TokenType.GROUP_OPEN, -1)})));
		fieldEdge.addNeighbour(countEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new DoesNotHaveAtFilter(TokenType.GROUP_OPEN, -1)})));
		
		sumEdge.addNeighbour(groupOpenEdge);
		avgEdge.addNeighbour(groupOpenEdge);
		maxEdge.addNeighbour(groupOpenEdge);
		minEdge.addNeighbour(groupOpenEdge);
		countEdge.addNeighbour(groupOpenEdge);

		
		
		fromEdge.addNeighbour(tableNameEdge);

		//in select queryies we definitely need a condition.
		//queries like select * from tablename or not allowed.

		tableNameEdge.addNeighbour(whereEdge, new OneOfFilter(Arrays.asList(new Filter[] {new ContainsFilter(TokenType.SELECT)})));
		

		//desc
		tableNameEdge.addNeighbour(terminatorEdge, new NotContainsFilter(TokenType.WHERE));
	
		//update
		//fieldEdge.addNeighbour(assignmentOperatorEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new ContainsFilter(TokenType.UPDATE)})));
		//setEdge.addNeighbour(fieldEdge, new ContainsFilter(TokenType.UPDATE));		
		//assignmentOperatorEdge.addNeighbour(valueEdge, new ContainsFilter(TokenType.UPDATE));
		//valueEdge.addNeighbour(fieldEdge, new ContainsFilter(TokenType.UPDATE));
		List<Filter> filters = new ArrayList<Filter>();
		//filters.add(new ContainsFilter(TokenType.UPDATE));
		filters.add(new NotContainsFilter(TokenType.WHERE));
		valueEdge.addNeighbour(whereEdge, new ListOfFilter(filters));
		
		whereEdge.addNeighbour(fieldEdge, new ContainsFilter(TokenType.WHERE));
		
		whereEdge.addNeighbour(groupOpenEdge);
		groupOpenEdge.addNeighbour(fieldEdge, new OneOfFilter(Arrays.asList(new Filter[] { new ContainsFilter(TokenType.SUM), 
				new ContainsFilter(TokenType.AVG), new ContainsFilter(TokenType.MAX), new ContainsFilter(TokenType.MIN), 
				new ContainsFilter(TokenType.COUNT), new ContainsFilter(TokenType.WHERE)})));
		
		//ToDo: Below condition allows Select Max(did), did) from employee;
		fieldEdge.addNeighbour(groupCloseEdge, new OneOfFilter(Arrays.asList(new Filter[] { new HasAtFilter(TokenType.SUM, -2), 
				new HasAtFilter(TokenType.AVG,-2), new HasAtFilter(TokenType.MAX,-2), new HasAtFilter(TokenType.MIN,-2), new HasAtFilter(TokenType.COUNT,-2)})));
		
		groupCloseEdge.addNeighbour(fieldEdge, new NotContainsFilter(TokenType.WHERE));
		groupCloseEdge.addNeighbour(sumEdge, new NotContainsFilter(TokenType.WHERE));
		groupCloseEdge.addNeighbour(avgEdge, new NotContainsFilter(TokenType.WHERE));
		groupCloseEdge.addNeighbour(maxEdge, new NotContainsFilter(TokenType.WHERE));
		groupCloseEdge.addNeighbour(minEdge, new NotContainsFilter(TokenType.WHERE));
		groupCloseEdge.addNeighbour(countEdge, new NotContainsFilter(TokenType.WHERE));
		groupCloseEdge.addNeighbour(fromEdge, new NotContainsFilter(TokenType.WHERE));
		
		//we don't allow two continue open brackets.
		
		fieldEdge.addNeighbour(conditionOperatorEdge, new ListOfFilter(Arrays.asList(new Filter[] {new ContainsFilter(TokenType.WHERE), new NotContainsFilter(TokenType.BY)})));
		
		conditionOperatorEdge.addNeighbour(valueEdge);
		
		valueEdge.addNeighbour(terminatorEdge, new ContainsFilter(TokenType.WHERE));
		valueEdge.addNeighbour(logicalOperatorEdge, new ContainsFilter(TokenType.WHERE));
		valueEdge.addNeighbour(groupCloseEdge, new ContainsFilter(TokenType.GROUP_OPEN));
		
		groupCloseEdge.addNeighbour(logicalOperatorEdge);
		groupCloseEdge.addNeighbour(groupCloseEdge, new ContainsFilter(TokenType.GROUP_CLOSE));
		groupCloseEdge.addNeighbour(terminatorEdge, new ContainsFilter(TokenType.WHERE));
		
		
		logicalOperatorEdge.addNeighbour(groupOpenEdge);
		logicalOperatorEdge.addNeighbour(fieldEdge);
		
		//order token
		groupCloseEdge.addNeighbour(orderEdge);
		tableNameEdge.addNeighbour(orderEdge);
		valueEdge.addNeighbour(orderEdge);
		
		orderEdge.addNeighbour(byEdge);
		

		
		fieldEdge.addNeighbour(directionEdge, new ListOfFilter(Arrays.asList(new Filter[] {new ContainsFilter(TokenType.ORDER), new ContainsFilter(TokenType.BY)})));
		
		
		directionEdge.addNeighbour(terminatorEdge);

		directionEdge.addNeighbour(fieldEdge);
		
		//group token
		groupCloseEdge.addNeighbour(groupEdge);
		tableNameEdge.addNeighbour(groupEdge);
		valueEdge.addNeighbour(groupEdge);
		
		groupEdge.addNeighbour(byEdge);
		
		filters = new ArrayList<Filter>();
		filters.add(new ContainsFilter(TokenType.ORDER));
		filters.add(new ContainsFilter(TokenType.GROUP));
		byEdge.addNeighbour(fieldEdge, new OneOfFilter(filters));  
		
		fieldEdge.addNeighbour(terminatorEdge, new ListOfFilter(Arrays.asList(new Filter[] { new ContainsFilter(TokenType.GROUP)})));
		fieldEdge.addNeighbour(orderEdge, new ListOfFilter(Arrays.asList(new Filter[] { new ContainsFilter(TokenType.GROUP), new NotContainsFilter(TokenType.ORDER)})));
		//NOTE: above cannot be replaced by two addNeighbour statements one for GROUP and one for ORDER
		directionEdge.addNeighbour(groupEdge);
		
		startingEdge = new Edge(TokenType.START);
		startingEdge.addNeighbour(selectEdge);
		//startingEdge.addNeighbour(updateEdge);
		//startingEdge.addNeighbour(descEdge);
		//startingEdge.addNeighbour(deleteEdge);
	}

	public List<Token> validateQuery(String query) throws TokenValidationException {

		init();
		
		Map<Edge, Filter> candidates = startingEdge.getNeighbours();
		//prepare query.
		
		query = query.replaceAll("\\(", " ( ");
		query = query.replaceAll("\\)", " ) ");
		query = query.replaceAll(";", " ; ");
		query = query.replaceAll("=", " = ");
		
		//Do we have to do the same for logical tokens.
		for(ConditionalTokenTypes token : ConditionalTokenTypes.values()) {
			query = query.replaceAll(token.getText(), " " + token.getText() + " ");
		}
		
						
		//compress space
		query = query.replaceAll(" +", " ");
		
		//compress space around comma
		query = query.replaceAll(" *, *", ",");
		
				
		query = query.replaceAll("= *=", "==");
		query = query.replaceAll("! *=", "!=");
		query = query.replaceAll("> *=", ">=");
		query = query.replaceAll("< *=", "<=");
		
		String[] tokens = query.split("[ ,]");
		int counter = 0;
		int length = 0;
		List<Token> seenTokens = new ArrayList<Token>();
		do {
			
			boolean validated = false;
			
			StringBuilder errorMsg = new StringBuilder("Error due to one of these.\n");
			
			String token  = tokens[counter++];
			
			logger.trace("--------------------");	
			logger.trace("Token:{}",token);
			
			for(Edge candidate : candidates.keySet()) {
				
				logger.trace("Token Type {} ", candidate.getTokenType());
				
				try {
					
					Filter filter = candidates.get(candidate);
					if(filter == null || filter.allowed(seenTokens)) {
						seenTokens.add(candidate.validate(token, seenTokens));
						validated = true;					
						candidates = candidate.getNeighbours();
						logger.trace("Candiates Count {}", candidates.size());
						break;
					}
				} catch (TokenValidationException e) {			
					errorMsg.append("at " + (length + counter) + " " + e.getMessage() + "\n");
					
				}																
			}
			
			
			length = length + token.length();
			
			if(!validated) {
				logger.info("Query:{}", query);
				logger.error("Invalid Token:[{}{}" , token , "]");
			
				StringBuilder errorHeader = new StringBuilder(query + "\n");
				/*
				for(int i = 0; i < (length + counter - 1); i++) {
					errorHeader.append(" ");
				}
		
				errorHeader.append("^\n");
				*/
				errorHeader.append(errorMsg.toString());
				throw new TokenValidationException(errorHeader.toString());
			} 
			
			if(counter == tokens.length && !token.equalsIgnoreCase(";")) {
				//we ran out of tokens and last token is not ';'
				throw new TokenValidationException("Unexpected End Of Input");
			}	
			
			if(token.equalsIgnoreCase(";") && counter < tokens.length) {
				//something after ';'.
				throw new TokenValidationException("Unexpected input after ';'");
			}
			
		}while(candidates.size() > 0);
		
		return seenTokens;
	}
	
	public String getQuery(List<Token> tokens) throws TokenValidationException {
		
			
		String tableName = null;
		
		int index = 1;
		List<String> fields = new ArrayList<String>();
		if(tokens.get(0).getType() == TokenType.SELECT) {
			//
			queryType = TokenType.SELECT;
			index = 1;
			
			do {

				Token token = tokens.get(index);
				if(token.getType() == TokenType.FIELD) {
					fields.add(token.getData());
					index++;
				}else if(token.getType() == TokenType.SUM || token.getType() == TokenType.AVG 
						|| token.getType() == TokenType.MAX || token.getType() == TokenType.MIN
						|| token.getType() == TokenType.COUNT){
					
					aggTokens.add(token);
					//ignoring '('
					++index;
					//get next token.
					logger.trace("Finding field on which to aggregate");
					Token aggfield = tokens.get(++index);
					if(token.getType() != TokenType.COUNT && aggfield.getData().equalsIgnoreCase("*")) {
						throw new TokenValidationException("'*' not allowed for " + token.getData());
					}
					logger.trace("{} - {}", aggfield.getData() , aggfield.getType());
					
					if(aggfield.getType() == TokenType.FIELD){
						logger.trace("Adding to agg Token");
						aggTokens.add(aggfield);
					}
					//ignoring ')'
					++index;
					index++;
				}else {
					break;
				}
			}while(true);
			
			tableName = tokens.get(index + 1).getData();
			
		
			index = index + 2;
			
			
		} 
		
		boolean endOfConditions = false;
		boolean startOfSort = false;
		boolean startOfGroup = false;
		
		for(int i = index + 1; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			if(token.getType() == TokenType.GROUP_CLOSE) {
			  	compressGroup();
			} else if(token.getType() == TokenType.GROUP_OPEN) {
			    logicalTokens.push(token);	
			    
            } else if(token.getType() == TokenType.LOGICAL_OPERATOR) {
                
				logicalTokens.push(token);
                  
			} else if (token.getType() == TokenType.CONDITONAL_OPEARATOR) {
                logger.debug("Pushing Conditional Operator:{}" , token.getData());
			    condTokens.push(token);	
			} else if(token.getType() == TokenType.VALUE) {
				
                logger.debug("Pushing Value Token:{}" , token.getData());
				condTokens.push(token);
				
				logger.debug("Cond Tokens Size:{}" , condTokens.size());
				
		        String currentfilter = handleCondition(condTokens);
		        
                logger.debug("Current Filter:{}" , currentfilter);
                logger.debug("Cond Tokens Size:{}" + condTokens.size());
                
				filters.push(currentfilter);								
			} else if(token.getType() == TokenType.FIELD && !endOfConditions){
				logger.debug("Pushing {}" + token.getData());
				condTokens.push(token);
			} else if(token.getType() == TokenType.ORDER ){
				endOfConditions = true;
				startOfSort = true;
				startOfGroup = false;				
			}else if(token.getType() == TokenType.BY){
				//
			}else if(token.getType() == TokenType.GROUP){
				endOfConditions = true;
				startOfSort = false;
				startOfGroup = true;						
			}else if(startOfSort){
				sortTokens.push(token);
			}else if(startOfGroup){
				groupTokens.add(token);
			}
			
			
		}

		logger.trace(query.toString());
		if(queryType == TokenType.SELECT) {
			
			query.append("curl -XPOST http://localhost:9200/" + tableName);
			
			boolean isCountQuery = false;
			//We use count api if there are no fields in Select statement and only Count(*) is there.
			if(fields.isEmpty() && (aggTokens.size() == 2 && aggTokens.get(0).getType() == TokenType.COUNT && aggTokens.get(1).getData().equalsIgnoreCase( "*"))) {
				query.append("/_count --data-binary '{");
				aggTokens.clear();
				isCountQuery = true;
			} else {
				query.append("/_search --data-binary '");
				//NOTE: In filtered query, if no query is specified it would use match_all query by default.
				query.append("{\"from\": 0, \"size\":" + Integer.MAX_VALUE );				
			}
            /*
			while(!logicalTokens.isEmpty() && !filters.isEmpty()) {
				Token token = logicalTokens.pop();
				
				filters.push(handleLogicalToken(token));
			}*/
			
			if(!logicalTokens.isEmpty()) {
				compressGroup();
			}
			

			if(!filters.isEmpty()){
				if(!isCountQuery) {
					query.append(",");
				}
				if(/*useFilters ||*/ !hasContainsOperator) {
					query.append("\"filter\": { " );
				} else {
					query.append("\"query\": { ");
				}

				String filter = filters.pop();
				if(filter.startsWith("\"should\"") || filter.startsWith("\"must\"") || filter.startsWith("\"must_not\"")) {
					query.append("\"bool\": {" + filter + "}");
				} else {
					query.append(filter);
				}
				
				query.append("}");
			}

			//handle sort conditions
			if(sortTokens.size() > 0) {
				query.append(", \"sort\":[");
				do {
					Token direction = sortTokens.pop();
					Token field = sortTokens.pop();
					query.append("{\""+ field.getData() + "\": \"" + direction.getData() + "\"},");
				}while(sortTokens.size() != 0);
				query.setCharAt(query.length()-1, ']');
				
			}
			
			//handle group by conditions
			if(groupTokens.size() > 0 ){
				
				for(Token field : groupTokens){
					query.append(",\"aggs\": {");							
					query.append("\"group_"+ field.getData() + "\": {");
					query.append("\"terms\" : { \"field\" :  \"" + field.getData() + "\"}");
					
				}
				
			}
			
			//handle aggregate conditions. They should be placed inside the inner most group by condition
			if(aggTokens.size() > 0){
				query.append(",\"aggs\": {");
				for(int i = 0; i < aggTokens.size(); i=i+2){
					Token aggOperation = aggTokens.get(i);
					Token aggField = aggTokens.get(i+1);
					if(aggField.getData() == "*") {
						/* 
						 * '*' is allowed only for COUNT aggregation and we don't have 
						 * to do anything special for COUNT(*)
						 * */
						continue;
					}
					query.append("\"" + aggOperation.getData() + "_"+ aggField.getData() + "\": {");
					query.append("\"" + (aggOperation.getType() == TokenType.COUNT ? "value_count":aggOperation.getData())
							+ "\" : { \"field\" :  \"" + aggField.getData() + "\"}");
					query.append("},");
				}
				query.setCharAt(query.length()-1, '}');

			}

			for(int i = 0; i < groupTokens.size()*2; i++){
				query.append("}");
			}

			
			if(fields.size() == 1 && fields.get(0).equalsIgnoreCase("*")){
			} else if (fields.size() > 0){

				query.append(", \"fields\": [");
				for(int i = 0; i < fields.size() - 1; i++) {
					query.append("\"" + fields.get(i) + "\",");
				}
				query.append("\"" + fields.get(fields.size()-1) + "\"");
				query.append("]");
			}
			
			query.append("}");
			
       	}
		
		query.append("'");
		logger.trace(query.toString());
		
		return query.toString();
	}
	
	private void compressGroup() {
        
		Stack<Token> reorderedLogicalTokens= new Stack<Token>();
		Stack<String> reorderedFilters = new Stack<String>();
		int count = 0;
		
		while(!logicalTokens.isEmpty()) {
			
			Token token = logicalTokens.pop();
			
			if(token.getType() == TokenType.GROUP_OPEN) {
				break;
			}
			
			count ++;
			reorderedLogicalTokens.push(token);
		}
		
		while(count >= 0 ) {
			String filter = filters.pop();
			reorderedFilters.push(filter);
			count --;
		}

		Token previousToken = null;
		Token token = reorderedLogicalTokens.pop();

	    logger.debug("ReOrdered Logical Tokens Size:{}" , reorderedLogicalTokens.size());
	    logger.debug("ReOrdered Filter Size:{}" , reorderedFilters.size());
	    
		while(true) {
	        count = 0; 
			do {
					previousToken = token;
					token = (reorderedLogicalTokens.isEmpty() ? null : reorderedLogicalTokens.pop());
					count ++;
	
			} while(previousToken != null && token != null && LogicalTokenTypes.getFromString(previousToken.getData()) == LogicalTokenTypes.getFromString(token.getData()));
			
			String filter = null;
			if(LogicalTokenTypes.getFromString(previousToken.getData()) ==  LogicalTokenTypes.AND) {
				filter =  "\"must\" : [" ; 
			} else if(LogicalTokenTypes.getFromString(previousToken.getData()) ==  LogicalTokenTypes.OR) {
				filter = "\"should\" : [" ; 
			}
			
			while(count >= 0) {
				
				filter = filter + "{" + reorderedFilters.pop() + "}";
				if(count > 0) {
					filter = filter + ",";
				}
				count --;
			}
			filter = filter + "]";
			reorderedFilters.push(filter);
			if(token == null) {
				break;
			}
		}	
		
		if(reorderedFilters.size() != 1) {
			throw new IllegalStateException("Expected filter size to be 1.");
		}
    	filters.push(reorderedFilters.pop());
	}
	

	private String handleCondition(Stack<Token> condTokens) throws TokenValidationException {
		
		
		if(condTokens.size() != 3) {
			throw new TokenValidationException("Invalid condition." + condTokens.size());
		}
	
		Token valueToken = condTokens.pop();
		Token condOperatorToken = condTokens.pop();
		Token fieldToken = condTokens.pop();

		//condOperatorToken.getData().equalsIgnoreCase("==")
		ConditionalTokenTypes condtoken = ConditionalTokenTypes.getFromString(condOperatorToken.getData());
		
		String value = null;
		
		if(valueToken.getData().charAt(0) == '\'') {
			value = valueToken.getData().substring(1, valueToken.getData().length() - 1);
		} else {
			value = valueToken.getData();
		}
		
	    String field = fieldToken.getData();
	
		if (condtoken == ConditionalTokenTypes.EQUALS) {
            return "\"term\" :  {\"" + field + "\":\"" + value + "\"}";            
		} else if (condtoken == ConditionalTokenTypes.CONTAINS) {
			/*
			if(useFilters) {
				return "\"query\" : { \"match\" :  {\"" + field + "\":\"" + value + "\"}}";
			} else {*/ 
				hasContainsOperator = true;
				return "\"match\" :  {\"" + field + "\":\"" + value + "\"}";
			//}
		} else if (condtoken == ConditionalTokenTypes.GREATER) {
            return "\"range\" :  {\"" + field + "\":{\"gt\":" + value + "}}";            
		} else if (condtoken == ConditionalTokenTypes.GREATER_OR_EQUAL) {
            return "\"range\" :  {\"" + field + "\":{\"gte\":" + value + "}}";            
		} else if (condtoken == ConditionalTokenTypes.LESS_OR_EQUAL) {
            return "\"range\" :  {\"" + field + "\":{\"lte\":" + value + "}}";            
		} else if (condtoken == ConditionalTokenTypes.LESS_THAN) {
            return "\"range\" :  {\"" + field + "\":{\"lt\":" + value + "}}";            
		} else if (condtoken == ConditionalTokenTypes.NOT_EQUAL) {
            return "\"must_not\" : {\"term\" :  {\"" + field + "\":\"" + value + "\"}}";
		}else {
			throw new TokenValidationException("Invalid token " + field + "found. Expected a conditional operatior");
		}
		 
		
	}
	
		
	private static class Edge {
		
		TokenType tokenType;
		TokenValidator tokenValidator;
		Map<Edge, Filter> neighbours;
		static List<String> keyWords;
		
		static{
			keyWords = new ArrayList<String>();
			for(TokenType token : TokenType.values()){
				keyWords.add(token.toString());
			}
			for(LogicalTokenTypes token : LogicalTokenTypes.values()){
				keyWords.add(token.toString());
			}

			for(ConditionalTokenTypes token : ConditionalTokenTypes.values()){
				keyWords.add(token.toString());
			}

		}
		
		private Edge(TokenType tokenType) {			
			this.tokenType = tokenType;
			this.neighbours = new HashMap<Edge, Filter>();
			switch(tokenType) {
				case START:
					tokenValidator = new TokenValidator() {
						public Token validate(String token, List<Token> seenTokens) {
							return new Token(TokenType.START, "");
						}
					};
					break;
				case FIELD:
					tokenValidator = new FieldToken(keyWords);
					break;
				case FROM:
					tokenValidator = new DefaultTokenValidator(TokenType.FROM);
					break;
				case SELECT:
					tokenValidator = new DefaultTokenValidator(TokenType.SELECT);
					break;
				case DESC:
					tokenValidator = new DefaultTokenValidator(TokenType.DESC);
					break;
				case TABLE_NAME:
					tokenValidator = new TableNameTokenValidator(keyWords);
					break;
				case TERMINATOR:
					tokenValidator = new TerminatorTokenValidator();
					break;
				case VALUE:
					tokenValidator = new ValueToken(keyWords);
					break;
				case WHERE:
					tokenValidator = new DefaultTokenValidator(TokenType.WHERE);
					break;
				case CONDITONAL_OPEARATOR:
					tokenValidator = new ConditionalOperatorValidator();
					break;
				case GROUP_OPEN:
					tokenValidator = new DefaultTokenValidator(TokenType.GROUP_OPEN);
					break;
				case GROUP_CLOSE:
					tokenValidator = new GroupCloseTokenValidator();
					break;		
				case LOGICAL_OPERATOR:
					tokenValidator = new LogicalOperatorTokenValidator();
					break;	
				case ASSIGNMENT_OPERATOR:
					tokenValidator = new DefaultTokenValidator(TokenType.ASSIGNMENT_OPERATOR);
					break;
				case SET:
					tokenValidator = new DefaultTokenValidator(TokenType.SET);
					break;
				case ORDER:
					tokenValidator = new DefaultTokenValidator(TokenType.ORDER);
					break;
				case BY:
					tokenValidator = new DefaultTokenValidator(TokenType.BY);
					break;
				case DIRECTION:
					tokenValidator = new DirectionTokenValidator();
					break;
				case GROUP:
					tokenValidator = new DefaultTokenValidator(TokenType.GROUP);
					break;
				case SUM:
					tokenValidator = new DefaultTokenValidator(TokenType.SUM);
					break;
				case AVG:
					tokenValidator = new DefaultTokenValidator(TokenType.AVG);
					break;
				case MAX:
					tokenValidator = new DefaultTokenValidator(TokenType.MAX);
					break;
				case MIN:
					tokenValidator = new DefaultTokenValidator(TokenType.MIN);
					break;
				case COUNT:
					tokenValidator = new DefaultTokenValidator(TokenType.COUNT);
					break;					
			}
			
		}
		
		private TokenType getTokenType(){
			return tokenType;
		}
		private Map<Edge, Filter> getNeighbours() {
			
			return neighbours;
		}

		private void addNeighbour(Edge neighbour, Filter filter) {
			neighbours.put(neighbour, filter);
		}
		
		private void addNeighbour(Edge neighbour) {
			addNeighbour(neighbour, null);
		}		
		
		
		private Token validate(String tokenData, List<Token> seenTokens) throws TokenValidationException {
			logger.debug("Checking {} against validator {}" , tokenData , tokenType);
			
			return tokenValidator.validate(tokenData, seenTokens);
		}
	}
	
	
	public TokenType getQueryType() {
		return queryType;
	}

	public void setQueryType(TokenType queryType) {
		this.queryType = queryType;
	}
	
	//(and)or(or)
	public static void main(String[] args) {
		
		ESQuerySyntax syntax = new ESQuerySyntax();
		
	
		//String query = "select email_reference from ngq_email_metadata where email_envelope_recipient == 'user1@ngq1.com' or (subject contains 'sdf' and  quarantine_type == 'SPAM') ;";
		
		//String query = "update ngq_email_metadata set quarantine_expire_date = 1455857954 where email_message_id == 'c48b74684b3e5f0c332b70b921d029067a75f0e5ab0b5b8e88f4f70fc29666d0';";
		
		//syntax.useFilters = true;
		String query = "select first_name from employee where first_name == 'Robert' and (middle_name == 'Edward' or last_name == 'Rogers') "
				+ "order by first_name asc, last_name dsc group by department_id,college_id;";
		
		//String query = "select user_id, master_recipient, email_size , sum (email_size) , max(email_size), min(email_size), "
		//		+ " avg (email_size) from ngq_email_metadata where customer_id != 1 order by user_id asc group by customer_id,domain_id;";
		
		//String query = "select * from employee where quarantine_expire_date == 'abc' ;";
		
		//String query = "select user_id, master_recipient, email_size , sum (email_size) , avg (email_size) from ngq_email_metadata where user_id == 1;";
		
		//String query = "select email_size from ngq_email_metadata;";
		
		//String query = "select count(email_message_id) from ngq_email_metadata where customer_id == 1 group by domain_id;";
		//String query = "Select Max(did), Min(did) from employee;";
		
		try {
			
			List<Token> tokens = syntax.validateQuery(query);
			logger.debug("Query {} is valid", query);
		  
			System.out.println("ElasticSearch Query:\n" + syntax.getQuery(tokens));		

		} catch (TokenValidationException e) {
			e.printStackTrace();
			logger.error("Query: {} \nError: {}" , query, e.getMessage());	
		}
		
	}
}



//http://stackoverflow.com/questions/22927098/show-all-elasticsearch-aggregation-results-buckets-and-not-just-10


//select user_id,master_recipient,email_size,sum ( email_size ),max ( email_size ),min ( email_size ) avg ( email_size ) from ngq_email_metadata where customer_id != 1 order by user_id asc group by customer_id,domain_id ; 

//select user_id,master_recipient,email_size,sum ( email_size ),max ( email_size ),min ( email_size ),avg ( email_size ) from ngq_email_metadata where customer_id != 1 order by user_id asc group by customer_id,domain_id ; 
