package org.bala.ESQL;

import java.util.List;

public class DeleteTokenValidator implements TokenValidator {

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		if(!token.equalsIgnoreCase("delete")) {
			throw new TokenValidationException("Expected keyword 'delete'");
		}
		
		return new Token(TokenType.DELETE, token);
	}
}
