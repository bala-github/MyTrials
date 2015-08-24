package org.bala.ESQL;

import java.util.List;

public class ContainsFilter implements Filter {

	TokenType typeToCheck;
	public ContainsFilter(TokenType type) {
	
		this.typeToCheck = type;
	}	
   
	public boolean allowed(List<Token> seenTokens) {
		
		for(Token token : seenTokens) {
			
			if(token.getType() == typeToCheck) {
				return true;
			}
		}
		return false; 
	}

}
