package org.bala.ESQL;

public enum LogicalTokenTypes {

	AND("and"),
	OR("or");
	
	private String text;
	
	LogicalTokenTypes(String text) {
		this.text = text;
	}
	
	public String getText() {
		
		return text;
	}
	
	public static LogicalTokenTypes getFromString(String text) {
		
		if(text != null) {
			
			for(LogicalTokenTypes token : LogicalTokenTypes.values()) {
				if(text.equalsIgnoreCase(token.getText())) {
					return token;
				}
			}
		}
	
		return null;
	}
}
