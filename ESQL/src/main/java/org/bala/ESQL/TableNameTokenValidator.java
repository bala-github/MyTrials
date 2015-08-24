package org.bala.ESQL;

import java.util.List;

public class TableNameTokenValidator implements TokenValidator {

	List<String> keyWords;
	
	public TableNameTokenValidator(List<String> keyWords) {
		this.keyWords = keyWords;
	}

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException {

		for(String keyWord : keyWords) {
			
			if(token.equalsIgnoreCase(keyWord)) {
				throw new TokenValidationException("UnExpected keyword '"+ keyWord +"'");
			}
		}

		return new Token(TokenType.TABLE_NAME, token);
	}

}
