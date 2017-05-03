package org.bala.ESQL;

import java.util.List;

public class GroupTokenValidator implements TokenValidator {

	@Override
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(!token.equalsIgnoreCase("group")) {
			throw new TokenValidationException("Expected keyword 'group'");
		}

		return new Token(TokenType.GROUP, token);
	}

}

