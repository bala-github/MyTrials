package org.bala.ESQL;

import java.util.List;

public class GroupCloseTokenValidator implements TokenValidator {

	public Token validate(String tokenData, List<Token> seenTokens) throws TokenValidationException {
		
		int openCount = 0;
		int closeCount = 0;
		
		if(!tokenData.equalsIgnoreCase(TokenType.GROUP_CLOSE.toString())) {
			throw new TokenValidationException("Expected keyword " + TokenType.GROUP_CLOSE);
		}		
		
		for(Token token : seenTokens) {
			
			if(token.getType() == TokenType.GROUP_OPEN) {
				openCount++;
			} else if(token.getType() == TokenType.GROUP_CLOSE) {
				closeCount++;
			}
		}
		
		closeCount++;
		
	
		if(closeCount > openCount) {
			throw new TokenValidationException("UnExpected " + TokenType.GROUP_CLOSE);
		}
		return new Token(TokenType.GROUP_CLOSE, tokenData);	
	}

}
