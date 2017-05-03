package org.bala.ESQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ESQuerySyntax {

	private Edge startingEdge;
	
	
	private Stack<Token> condTokens;

    private Stack<Token> logicalTokens;
    
    private Stack<String> filters;
    
    private Stack<Token> sortTokens;
    
    private List<Token> groupTokens;
	
	private StringBuilder query;
	
	private TokenType queryType;
	
	private boolean useFilters = false;
	
	private boolean hasContainsOperator = false;
	
	private void init() {
		
		
		condTokens = new Stack<Token>();
        logicalTokens = new Stack<Token>();
        filters = new Stack<String>();
        sortTokens = new Stack<Token>();
        groupTokens = new ArrayList<Token>();
		query = new StringBuilder();
		queryType = null;
	}
	
	
	public ESQuerySyntax() {
		
	    init();
	    
		Edge selectEdge = new Edge(TokenType.SELECT);
		Edge updateEdge = new Edge(TokenType.UPDATE);
		Edge deleteEdge = new Edge(TokenType.DELETE);
		Edge descEdge = new Edge(TokenType.DESC);
		Edge fieldEdge = new Edge(TokenType.FIELD);		
		Edge fromEdge = new Edge(TokenType.FROM);		
		Edge tableNameEdge = new Edge(TokenType.TABLE_NAME);		
		Edge terminatorEdge = new Edge(TokenType.TERMINATOR);
		Edge whereEdge = new Edge(TokenType.WHERE);
		Edge setEdge = new Edge(TokenType.SET);
		
		Edge conditionOperatorEdge = new Edge(TokenType.CONDITONAL_OPEARATOR);
		Edge valueEdge = new Edge(TokenType.VALUE);
		Edge groupOpenEdge = new Edge(TokenType.GROUP_OPEN);
		Edge groupCloseEdge = new Edge(TokenType.GROUP_CLOSE);
		Edge logicalOperatorEdge = new Edge(TokenType.LOGICAL_OPERATOR);
		Edge assignmentOperatorEdge = new Edge(TokenType.ASSIGNMENT_OPERATOR);
		
		Edge orderEdge = new Edge(TokenType.ORDER);
		Edge byEdge = new Edge(TokenType.BY);
		Edge directionEdge = new Edge(TokenType.DIRECTION);
		Edge groupEdge = new Edge(TokenType.GROUP);
		
		selectEdge.addNeighbour(fieldEdge);
		updateEdge.addNeighbour(tableNameEdge);
		descEdge.addNeighbour(tableNameEdge);
		
		
		fieldEdge.addNeighbour(fromEdge,  new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new ContainsFilter(TokenType.SELECT)})));
		fieldEdge.addNeighbour(fieldEdge, new OneOfFilter(Arrays.asList(new Filter[] { 
				 new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new ContainsFilter(TokenType.SELECT)})),
				 new ContainsFilter(TokenType.GROUP)
		})));
		
		deleteEdge.addNeighbour(fromEdge);  //delete query
		fromEdge.addNeighbour(tableNameEdge);

		//in select queryies we definitely need a condition.
		//queries like select * from tablename or not allowed.

		tableNameEdge.addNeighbour(whereEdge, new OneOfFilter(Arrays.asList(new Filter[] {new ContainsFilter(TokenType.SELECT), new ContainsFilter(TokenType.DELETE)})));
		tableNameEdge.addNeighbour(setEdge, new ContainsFilter(TokenType.UPDATE));

		//desc
		tableNameEdge.addNeighbour(terminatorEdge, new ContainsFilter(TokenType.DESC));
	
		//update
		fieldEdge.addNeighbour(assignmentOperatorEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new ContainsFilter(TokenType.UPDATE)})));
		setEdge.addNeighbour(fieldEdge, new ContainsFilter(TokenType.UPDATE));		
		assignmentOperatorEdge.addNeighbour(valueEdge, new ContainsFilter(TokenType.UPDATE));
		valueEdge.addNeighbour(fieldEdge, new ContainsFilter(TokenType.UPDATE));
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new ContainsFilter(TokenType.UPDATE));
		filters.add(new NotContainsFilter(TokenType.WHERE));
		valueEdge.addNeighbour(whereEdge, new ListOfFilter(filters));
		
		whereEdge.addNeighbour(fieldEdge, new ContainsFilter(TokenType.WHERE));
		
		whereEdge.addNeighbour(groupOpenEdge);
		groupOpenEdge.addNeighbour(fieldEdge, new ContainsFilter(TokenType.WHERE));
		
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
		startingEdge.addNeighbour(updateEdge);
		startingEdge.addNeighbour(descEdge);
		startingEdge.addNeighbour(deleteEdge);
	}

	public List<Token> validateQuery(String query) throws TokenValidationException {

		init();
		
		Map<Edge, Filter> candidates = startingEdge.getNeighbours();
		//prepare query.
		
		query = query.replaceAll("\\(", " ( ");
		query = query.replaceAll("\\)", " ) ");
		query = query.replaceAll(";", " ; ");
		query = query.replaceAll("=", " = ");
		
		/*	
		 * Is it needed.	
		for(LogicalTokenTypes token : LogicalTokenTypes.values()) {
			query = query.replaceAll(token.getText(), " " + token.getText() + " ");
		}*/
		
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
			
			//System.out.println("--------------------");	
			//System.out.println("Token:" + token);
			int exceptionCount = 0;
			for(Edge candidate : candidates.keySet()) {
				
				//System.out.println(candidate.getTokenType());
				
				try {
					
					Filter filter = candidates.get(candidate);
					if(filter == null || filter.allowed(seenTokens)) {
						seenTokens.add(candidate.validate(token, seenTokens));
						validated = true;					
						candidates = candidate.getNeighbours();
						//System.out.println("Candiates Count" + candidates.size());
						break;
					}
				} catch (TokenValidationException e) {
					//System.out.println(++exceptionCount);
					errorMsg.append("at " + (length + counter) + " " + e.getMessage() + "\n");
					
				}																
			}
			
			length = length + token.length();
			
			if(!validated) {
				System.out.println("Query:" + query);
				System.out.println("Invalid Token:[" + token + "]");
				throw new TokenValidationException(errorMsg.toString());
			} 
			
			if(candidates.size() == 0) {
				break;
			} else if(counter >= tokens.length) {
				//we ran out of tokens
				throw new TokenValidationException("Unexpected end of input.");
			}
			
		}while(true);
		
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
				} else {
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
                System.out.println("Pushing Conditional Operator." + token.getData());
			    condTokens.push(token);	
			} else if(token.getType() == TokenType.VALUE) {
				
                System.out.println("Pushing Value Token." + token.getData());
				condTokens.push(token);
				System.out.println("Cond Tokens Size:" + condTokens.size());
		        String currentfilter = handleCondition(condTokens);
                System.out.println("Current Filter:" + currentfilter);
                System.out.println("Cond Tokens Size:" + condTokens.size());
				filters.push(currentfilter);								
			} else if(token.getType() == TokenType.FIELD && !endOfConditions){
                System.out.println("Pushing " + token.getData());
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

		System.out.println(query);
		if(queryType == TokenType.SELECT) {
					
			//NOTE: In filtered query, if no query is specified it would use match_all query by default.
			query.append("{\"from\": 0, \"size\":" + Integer.MAX_VALUE + ",");
			if(/*useFilters ||*/ !hasContainsOperator) {
				query.append("\"filter\": { " );
			} else {
				query.append("\"query\": { ");
			}
            /*
			while(!logicalTokens.isEmpty() && !filters.isEmpty()) {
				Token token = logicalTokens.pop();
				
				filters.push(handleLogicalToken(token));
			}*/
			
			if(!logicalTokens.isEmpty()) {
				compressGroup();
			}
			
			if(filters.size() != 1) {
				throw new IllegalStateException("Expected filter size to be 1");
			}
			
			String filter = filters.pop();
			if(filter.startsWith("\"should\"") || filter.startsWith("\"must\"")) {
				query.append("\"bool\": {" + filter + "}");
			} else {
				query.append(filter);
			}
			
			query.append("}");
			
			if(sortTokens.size() > 0) {
				query.append(", \"sort\":[");
				do {
					Token direction = sortTokens.pop();
					Token field = sortTokens.pop();
					query.append("{\""+ field.getData() + "\": \"" + direction.getData() + "\"},");
				}while(sortTokens.size() != 0);
				query.setCharAt(query.length()-1, ']');
				
			}
			
			if(groupTokens.size() > 0){
				
				for(Token field : groupTokens){
					query.append(",\"aggs\": {");
					query.append("\""+ field.getData() + "\": {");
					query.append("\"terms\" : { \"field\" :  \"" + field.getData() + "\"}");
				}
				for(int i = 0; i < groupTokens.size()*2; i++){
					query.append("}");
				}
				
			}
			query.append(", \"fields\": [");
			
					
			for(int i = 0; i < fields.size() - 1; i++) {
				query.append("\"" + fields.get(i) + "\",");
			}
			query.append("\"" + fields.get(fields.size()-1) + "\"");
			query.append("]");
			
			query.append("}");
			
       	}
		
		System.out.println(query.toString());
		
		return null;
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

	    System.out.println("Size:" + reorderedLogicalTokens.size());
	    System.out.println("Size:" + reorderedFilters.size());
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
	
	private String handleLogicalToken(Token token) {
		
		if(token.getType() == TokenType.LOGICAL_OPERATOR) {
			String filter1 = filters.pop();
			String filter2 = filters.pop();
			if(LogicalTokenTypes.getFromString(token.getData()) ==  LogicalTokenTypes.AND) {
				return "\"must\" : [" +  "{" + filter1 + "},{" + filter2 + "}"+ "]";
			} else if(LogicalTokenTypes.getFromString(token.getData()) ==  LogicalTokenTypes.OR) {
				return "\"should\" : [" +  "{" + filter1 + "},{" + filter2 + "}"+ "]";
			}
		} else if (token.getType() == TokenType.GROUP_OPEN) {
			String filter = filters.pop();
			return "\"bool\": {" + filter + "}";			
		}
		
		return null;
		
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
    
		} 
	
        return null;	
	}
	
		
	public static class Edge {
		
		TokenType tokenType;
		TokenValidator tokenValidator;
		Map<Edge, Filter> neighbours;
		
		public Edge(TokenType tokenType) {
			
			List<String> keyWords = new ArrayList<String>();
			keyWords.add(";");
			keyWords.add("select");
			keyWords.add("from");
			keyWords.add("where");
			keyWords.add("==");
			keyWords.add("(");
			keyWords.add(")");
			keyWords.add("and");
			keyWords.add("or");
			keyWords.add("=");
			keyWords.add("set");
			keyWords.add("desc");
			keyWords.add("delete");
			keyWords.add("order");
			keyWords.add("by");
			keyWords.add("group");
			
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
					tokenValidator = new FromTokenValidator();
					break;
				case SELECT:
					tokenValidator = new SelectTokenValidator();
					break;
				case UPDATE:
					tokenValidator = new UpdateTokenValidator();
					break;
				case DESC:
					tokenValidator = new DescTokenValidator();
					break;
				case DELETE:
					tokenValidator = new DeleteTokenValidator();
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
					tokenValidator = new WhereToken();
					break;
				case CONDITONAL_OPEARATOR:
					tokenValidator = new ConditionalOperatorValidator();
					break;
				case GROUP_OPEN:
					tokenValidator = new GroupOpenTokenValidator();
					break;
				case GROUP_CLOSE:
					tokenValidator = new GroupCloseTokenValidator();
					break;		
				case LOGICAL_OPERATOR:
					tokenValidator = new LogicalOperatorTokenValidator();
					break;	
				case ASSIGNMENT_OPERATOR:
					tokenValidator = new AssignmentTokenValidator();
					break;
				case SET:
					tokenValidator = new SetTokenValidator();
					break;
				case ORDER:
					tokenValidator = new OrderTokenValidator();
					break;
				case BY:
					tokenValidator = new ByTokenValidator();
					break;
				case DIRECTION:
					tokenValidator = new DirectionTokenValidator();
					break;
				case GROUP:
					tokenValidator = new GroupTokenValidator();
					break;
			}
		}
		
		public Map<Edge, Filter> getNeighbours() {
			
			return neighbours;
		}

		public void addNeighbour(Edge neighbour, Filter filter) {
			neighbours.put(neighbour, filter);
		}
		
		public void addNeighbour(Edge neighbour) {
			addNeighbour(neighbour, null);
		}		
		
		public TokenType getTokenType() {
			return tokenType;
		}
		
		public Token validate(String tokenData, List<Token> seenTokens) throws TokenValidationException {
			//System.out.println("Checking " + tokenData + "against validator" + tokenType);
			
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
				+ "order by first_name asc, last_name desc group by department_id,college_id;";
		//String query = "select first_name from employee where quarantine_expire_date == 123 ;";
		try {
			
			List<Token> tokens = syntax.validateQuery(query);
			System.out.println("Query " + query + "is valid");
			
			System.out.println("MetaData Service Query:");  
			syntax.getQuery(tokens);

		} catch (TokenValidationException e) {
			e.printStackTrace();
			System.out.println("Query: " + query + "\nError: " + e.getMessage());		}
	}
}
