package org.bala.ESQL;

import java.util.List;

public class TerminatorTokenValidator implements TokenValidator {

	public TerminatorTokenValidator() {
		
	}

	public Token validate(String tokenData, List<Token> seenTokens) throws TokenValidationException {

		if(!tokenData.equalsIgnoreCase(";")) {
			throw new TokenValidationException("Expected  ';'");
		}		
		
		int openCount = 0;
		int closeCount = 0;
		
		
		for(Token token : seenTokens) {
			
			if(token.getType() == TokenType.GROUP_OPEN) {
				openCount++;
			} else if(token.getType() == TokenType.GROUP_CLOSE) {
				closeCount++;
			}
		}
		
		if(openCount != closeCount) {
			throw new TokenValidationException("Unbalanced  no. of brackets '()'");
		}
		return new Token(TokenType.TERMINATOR, tokenData);
	}

}
