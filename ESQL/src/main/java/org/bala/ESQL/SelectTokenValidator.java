package org.bala.ESQL;

import java.util.List;

public class SelectTokenValidator implements TokenValidator {

	public SelectTokenValidator() {
		
	}

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase("select")) {
			throw new TokenValidationException("Expected keyword 'select'");
		}
		
		return new Token(TokenType.SELECT, token);
	}

}
