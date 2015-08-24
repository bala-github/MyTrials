package org.bala.ESQL;

import java.util.List;

public interface TokenValidator {

	public Token validate(String token, List<Token> seenTokens) throws TokenValidationException;
}
