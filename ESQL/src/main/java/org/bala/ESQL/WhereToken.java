package org.bala.ESQL;

import java.util.List;

public class WhereToken implements TokenValidator {

	
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
	
		if(!token.equalsIgnoreCase("where")) {
			throw new TokenValidationException("Expected keyword 'where'");
		}

		return new Token(TokenType.WHERE, token);
	}

}
