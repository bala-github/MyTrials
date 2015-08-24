package org.bala.ESQL;

import java.util.List;

public class LogicalOperatorTokenValidator implements TokenValidator {

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(LogicalTokenTypes.getFromString(token) == null) {
			throw new TokenValidationException("Expected a logical operator");
		} else {
			return new Token(TokenType.LOGICAL_OPERATOR, token);
		}
	
	}

}
