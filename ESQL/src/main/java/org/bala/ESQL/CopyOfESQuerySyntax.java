package org.bala.ESQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;






public class CopyOfESQuerySyntax {

	private Edge startingEdge;
	
	
	private Stack<Token> condTokens;

    private Stack<Token> logicalTokens;
    
    private Stack<String> filters; 
	
	private StringBuilder query;
	
	private TokenType queryType;
	
	private void init() {
		
		
		condTokens = new Stack<Token>();
        logicalTokens = new Stack<Token>();
        filters = new Stack<String>();
		query = new StringBuilder();
		queryType = null;
	}
	
	
	public CopyOfESQuerySyntax() {
		
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
		
		selectEdge.addNeighbour(fieldEdge);
		updateEdge.addNeighbour(tableNameEdge);
		descEdge.addNeighbour(tableNameEdge);
		
		
		fieldEdge.addNeighbour(fromEdge,  new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new ContainsFilter(TokenType.SELECT)})));
		fieldEdge.addNeighbour(fieldEdge, new ListOfFilter(Arrays.asList(new Filter[] {new NotContainsFilter(TokenType.WHERE), new ContainsFilter(TokenType.SELECT)})));
		
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
		
		fieldEdge.addNeighbour(conditionOperatorEdge, new ContainsFilter(TokenType.WHERE));
		
		conditionOperatorEdge.addNeighbour(valueEdge);
		
		valueEdge.addNeighbour(terminatorEdge, new ContainsFilter(TokenType.WHERE));
		valueEdge.addNeighbour(logicalOperatorEdge, new ContainsFilter(TokenType.WHERE));
		valueEdge.addNeighbour(groupCloseEdge, new ContainsFilter(TokenType.GROUP_OPEN));
		
		groupCloseEdge.addNeighbour(logicalOperatorEdge);
		groupCloseEdge.addNeighbour(groupCloseEdge, new ContainsFilter(TokenType.GROUP_CLOSE));
		groupCloseEdge.addNeighbour(terminatorEdge, new ContainsFilter(TokenType.WHERE));
		
		logicalOperatorEdge.addNeighbour(groupOpenEdge);
		logicalOperatorEdge.addNeighbour(fieldEdge);
		
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
				
		for(LogicalTokenTypes token : LogicalTokenTypes.values()) {
			query = query.replaceAll(token.getText(), " " + token.getText() + " ");
		}
		
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
				
			for(Edge candidate : candidates.keySet()) {

				try {
					
					Filter filter = candidates.get(candidate);
					if(filter == null || filter.allowed(seenTokens)) {
						seenTokens.add(candidate.validate(token, seenTokens));
						validated = true;					
						candidates = candidate.getNeighbours();
						break;
					}
				} catch (TokenValidationException e) {
					
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
			
			//index would point to FROM now.
			
			tableName = tokens.get(index + 1).getData();
			
		
			index = index + 2;
			
			
		} 
		
		for(int i = index + 1; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			if(token.getType() == TokenType.GROUP_CLOSE) {
			    //logicalTokens.push(token);	
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
				
				
			} else {
                System.out.println("Pushing " + token.getData());
				condTokens.push(token);
			}
			
			
		}

		System.out.println(query);
		if(queryType == TokenType.SELECT) {
					
			//NOTE: In filtered query, if no query is specified it would use match_all query by default.
			query.append("{\"from\": 0, \"size\":" + Integer.MAX_VALUE + ",");
			query.append("\"query\": { \"filtered\": { \"query\": { \"match_all\": {} },\"filter\": {" );
			

			while(!logicalTokens.isEmpty() && !filters.isEmpty()) {
				Token token = logicalTokens.pop();
				
				filters.push(handleLogicalToken(token));
			}	
			String filter = filters.pop();
			if(filter.startsWith("\"should\"") || filter.startsWith("\"must\"")) {
				query.append("\"bool\": {" + filter + "}");
			} else {
				query.append(filter);
			}
			query.append(", \"fields\": [");
			
					
			for(int i = 0; i < fields.size() - 1; i++) {
				query.append("\"" + fields.get(i) + "\",");
			}
			query.append("\"" + fields.get(fields.size()-1) + "\"");
			query.append("]");
			query.append("}}}}");
			
       	}
		
		System.out.println(query.toString());
		
		return null;
	}
	
	private void compressGroup() {
		
		while(!logicalTokens.isEmpty() && !filters.isEmpty()) {
			Token token = logicalTokens.pop();
			
			filters.push(handleLogicalToken(token));
			
			if(token.getType() == TokenType.GROUP_OPEN) {
				break;
			}
			
		}	
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
			return "\"match\" :  {\"" + field + "\":\"" + value + "\"}";
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
		
		CopyOfESQuerySyntax syntax = new CopyOfESQuerySyntax();
		
	
		String query = "select email_reference from ngq_email_metadata where email_envelope_recipient == 'user1@ngq1.com' and quarantine_type == 'SPAM' or subject contains '80';";
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
