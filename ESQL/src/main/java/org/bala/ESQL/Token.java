package org.bala.ESQL;

public class Token {

	private TokenType type;
	private String data;
	
	public Token(TokenType type, String data) {
		this.setType(type);
		this.setData(data);
		
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object arg) {
		
		if(arg == null) {
			return false;
		}
		
		if(this.getClass() != arg.getClass()) {
			return false;
		}
		
		if(type != ((Token)arg).getType()) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		
		return type.hashCode() + data.hashCode();
	}

}
