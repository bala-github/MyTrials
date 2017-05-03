package org.bala.ESQL;

import java.util.List;

public class OrderTokenValidator implements TokenValidator {

	@Override
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase("order")) {
			throw new TokenValidationException("Expected keyword 'order'");
		}

		return new Token(TokenType.ORDER, token);
	}

}
