package org.bala.ESQL;

import java.util.List;

public class GroupCloseTokenValidator implements TokenValidator {

	public Token validate(String tokenData, List<Token> seenTokens) throws TokenValidationException {
		
		int openCount = 0;
		int closeCount = 0;
		
		if(!tokenData.equalsIgnoreCase(")")) {
			throw new TokenValidationException("Expected keyword ')'");
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
			throw new TokenValidationException("UnExpected ')'");
		}
		return new Token(TokenType.GROUP_CLOSE, tokenData);	
	}

}
