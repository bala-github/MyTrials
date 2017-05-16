package org.bala.ESQL;

import java.util.List;

public class DefaultTokenValidator implements TokenValidator {

	TokenType tokenType;
	
	DefaultTokenValidator(TokenType tokenType){
	
		this.tokenType = tokenType;
	}
	
	@Override
	public Token validate(String token, List<Token> seenTokens)	throws TokenValidationException {
		
		if(!token.equalsIgnoreCase(tokenType.toString())) {
			throw new TokenValidationException("Exepcted keyword " + tokenType);
		}
		
		return new Token(tokenType, token);
	}

}
