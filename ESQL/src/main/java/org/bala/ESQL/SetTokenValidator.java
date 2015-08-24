package org.bala.ESQL;

import java.util.List;

public class SetTokenValidator implements TokenValidator {

	public SetTokenValidator() {
		
	}

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		
		if(!token.equalsIgnoreCase("set")) {
			throw new TokenValidationException("Expected keyword 'set'");
		}		
		
		return new Token(TokenType.SET, token);
	}

}
