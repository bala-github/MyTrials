package org.bala.ESQL;

import java.util.List;

public class DirectionTokenValidator implements TokenValidator {

	@Override
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase(TokenType.ASC.toString()) && !token.equalsIgnoreCase(TokenType.DSC.toString())) {
			throw new TokenValidationException("Expected keyword " + TokenType.ASC.toString() + " or " + TokenType.DSC.toString());
		}

		return new Token(TokenType.DIRECTION, token);
	}

}
