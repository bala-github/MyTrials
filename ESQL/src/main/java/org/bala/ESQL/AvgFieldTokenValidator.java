package org.bala.ESQL;

import java.util.List;

public class AvgFieldTokenValidator implements TokenValidator {



	@Override
	public Token validate(String token, List<Token> seenTokens)	throws TokenValidationException {
		
		if(token.startsWith("avg")){
			
			return new Token(TokenType.AVG, token);
			
		}
		
		throw new TokenValidationException("Unexpected keyword " + token);
	}
	
	

}
