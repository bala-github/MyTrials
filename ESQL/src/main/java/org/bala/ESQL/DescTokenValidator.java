package org.bala.ESQL;

import java.util.List;

public class DescTokenValidator implements TokenValidator {

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		if(!token.equalsIgnoreCase("desc")) {
			throw new TokenValidationException("Expected keyword 'desc'");
		}
		
		return new Token(TokenType.DESC, token);
	}

}
