package org.bala.ESQL;

import java.util.List;

public class ValueToken implements TokenValidator {

	List<String> keyWords;
	
	public ValueToken(List<String> keyWords) {
		
		this.keyWords = keyWords;
	
	}
	
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(token.charAt(0) != '\'' || token.charAt(token.length() - 1)!= '\'') {
			//if previous token was not contains
			if(!seenTokens.get(seenTokens.size()-1).getData().equalsIgnoreCase(ConditionalTokenTypes.CONTAINS.getText())) {
				try {
					Double.parseDouble(token);
				} catch(NumberFormatException e) {
				
					if(token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false")) {
						//token is boolean value.
					} else {
					
						throw new TokenValidationException("Value should be enclosed between '' or must be either double/boolean");
					}
				
				}
			} else {
			
				throw new TokenValidationException("Value should be enclosed between '' ");
			}
		}
		
		return new Token(TokenType.VALUE, token);
	}

}
