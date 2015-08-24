package org.bala.ESQL;

import java.util.List;

public class ListOfFilter implements Filter {

	private List<Filter> filters;
	
	public ListOfFilter(List<Filter> filters) {
		
		this.filters = filters;
	}
	
	public boolean allowed(List<Token> seenTokens) {
		
		for(Filter filter : filters) {
			
			if(!filter.allowed(seenTokens)) {
				return false;
			}
		}
		
		return true;
	}

}
