package org.bala.ESQL;

import java.util.List;

public class ConditionalOperatorValidator implements TokenValidator {

	
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(ConditionalTokenTypes.getFromString(token.toLowerCase()) == null) {
			throw new TokenValidationException("Expected a conditional operator");
		} else {
			return new Token(TokenType.CONDITONAL_OPEARATOR, token.toLowerCase());
		}
	
		
	}

}
