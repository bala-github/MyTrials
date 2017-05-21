package org.bala.ESQL;

import java.util.List;

public class DoesNotHaveAtFilter implements Filter{

	TokenType typeToCheck;
	
	int at;
	
	DoesNotHaveAtFilter(TokenType type, int at) {
		this.typeToCheck = type;
		this.at = at;
	}
	
	public boolean allowed(List<Token> seenTokens) {

		if((seenTokens.size() - 1) + (at) < 0) {
			return true;
		}		
		
		if(seenTokens.get((seenTokens.size() - 1) + (at)).getType() != typeToCheck ) {
			return true;
		}
		return false;
	}		
}
