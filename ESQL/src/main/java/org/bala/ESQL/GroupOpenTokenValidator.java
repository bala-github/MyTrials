package org.bala.ESQL;

import java.util.List;

public class GroupOpenTokenValidator implements TokenValidator {

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase("(")) {
			throw new TokenValidationException("Expected keyword '('");
		}		
		
		return new Token(TokenType.GROUP_OPEN, token);
	}

}
