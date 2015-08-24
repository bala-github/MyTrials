package org.bala.ESQL;

import java.util.ArrayList;
import java.util.List;

public class FieldToken implements TokenValidator {

	List<String> keyWords;
		public FieldToken(List<String> keyWords) {
		this.keyWords = keyWords;
		
	}

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {
		
		if(token == null || token.length() == 0) {
			throw new TokenValidationException("Expected field name");
		}
		
		if(keyWords.contains(token)) {
			throw new TokenValidationException("UnExpected keyword '"+ token +"'");
		}
		
		return new Token(TokenType.FIELD, token);
	}

}
