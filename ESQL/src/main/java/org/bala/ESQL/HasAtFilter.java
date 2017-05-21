package org.bala.ESQL;

import java.util.List;

public class HasAtFilter implements Filter {

	TokenType typeToCheck;
	
	int at;
	
	HasAtFilter(TokenType type, int at) {
		this.typeToCheck = type;
		this.at = at;
	}
	
	public boolean allowed(List<Token> seenTokens) {
	
		//System.out.println("Index:" + ((seenTokens.size() - 1) + (at)));
		
		if((seenTokens.size() - 1) + (at) < 0) {
			return false;
		}

		//System.out.println("Token:" + seenTokens.get((seenTokens.size() - 1) + (at)).getType() + "TokenToCheck:" + typeToCheck);
		
		if(seenTokens.get((seenTokens.size() - 1) + (at)).getType() == typeToCheck ) {
			return true;
		}
		return false;
	}	
}
