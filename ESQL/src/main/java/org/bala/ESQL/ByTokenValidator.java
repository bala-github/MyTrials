package org.bala.ESQL;

import java.util.List;

public class ByTokenValidator implements TokenValidator {

	@Override
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase("by")) {
			throw new TokenValidationException("Expected keyword 'by'");
		}

		return new Token(TokenType.BY, token);		
	}

}
