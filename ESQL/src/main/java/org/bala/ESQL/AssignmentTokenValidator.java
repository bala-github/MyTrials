package org.bala.ESQL;

import java.util.List;

public class AssignmentTokenValidator implements TokenValidator {

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
	
		if(!token.equalsIgnoreCase("=")) {
			
		}
		
		return new Token(TokenType.ASSIGNMENT_OPERATOR, token);
	}

}
