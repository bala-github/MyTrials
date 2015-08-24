package org.bala.ESQL;

import java.util.List;

public class NotContainsFilter implements Filter {

	TokenType typeToCheck;
	
	public NotContainsFilter(TokenType type) {
		
		this.typeToCheck = type;
	}

	public boolean allowed(List<Token> seenTokens) {
		
	for(Token token : seenTokens) {
			
			if(token.getType() == typeToCheck) {
				return false;
			}
		}
		return true; 
	}

}
