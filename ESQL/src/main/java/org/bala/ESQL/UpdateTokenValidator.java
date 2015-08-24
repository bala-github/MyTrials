package org.bala.ESQL;

import java.util.List;

public class UpdateTokenValidator implements TokenValidator {

	public UpdateTokenValidator() {
		
	}

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase("update")) {
			throw new TokenValidationException("Expected keyword 'update'");
		}
		
		return new Token(TokenType.UPDATE, token);
	}

}
