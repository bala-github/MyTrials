package org.bala.ESQL;

import java.util.List;

public class SumFieldTokenValidator implements TokenValidator {

	@Override
	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(token.startsWith("sum")){
			
				return new Token(TokenType.SUM, token);
			
		}
		
		throw new TokenValidationException("Unexpected keyword " + token);
	}

}
