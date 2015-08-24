package org.bala.ESQL;

import java.util.List;

public interface Filter {

	public boolean allowed(List<Token> seenTokens);
}
