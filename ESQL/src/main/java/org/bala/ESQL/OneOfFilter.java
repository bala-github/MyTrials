package org.bala.ESQL;

import java.util.List;

public class OneOfFilter implements Filter {

	private List<Filter> filters;
	
	public OneOfFilter(List<Filter> filters) {
		
		this.filters = filters;
	}
	
	public boolean allowed(List<Token> seenTokens) {
		
		for(Filter filter : filters) {
			
			if(filter.allowed(seenTokens)) {
				return true;
			}
		}
		
		return false;
	}

}
