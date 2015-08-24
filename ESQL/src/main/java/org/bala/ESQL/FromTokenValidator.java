package org.bala.ESQL;

import java.util.List;

public class FromTokenValidator implements TokenValidator {

	public FromTokenValidator() {
		
	}

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		
		if(!token.equalsIgnoreCase("from")) {
			throw new TokenValidationException("Expected keyword 'from'");
		}		
		
		return new Token(TokenType.FROM, token);
	}

}
