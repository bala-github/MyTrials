package org.bala.ESQL;

public enum ConditionalTokenTypes {
	
	NOT_EQUAL("!="),
	EQUALS("=="),
	LESS_OR_EQUAL("<="),
	GREATER_OR_EQUAL(">="),
	LESS_THAN("<"),
	GREATER(">"),
	CONTAINS("contains");
	
	private String text;
	
	ConditionalTokenTypes(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public static ConditionalTokenTypes getFromString(String text) {
		
		if(text != null) {
			
			for(ConditionalTokenTypes token : ConditionalTokenTypes.values()) {
				if(text.equalsIgnoreCase(token.getText())) {
					return token;
				}
			}
		}
		
		return null;
	}
}
