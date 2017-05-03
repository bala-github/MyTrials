package org.bala.ESQL;

import java.util.List;

public class DirectionTokenValidator implements TokenValidator {

	@Override
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase("asc") && !token.equalsIgnoreCase("desc")) {
			throw new TokenValidationException("Expected keyword 'asc' or 'desc'");
		}

		return new Token(TokenType.DIRECTION, token);
	}

}
