package org.bala.devtools.models;

public class Base64ActionRequest  {

	private String input;
	
	private boolean urlsafe;
	
	public boolean isUrlSafe() {
		return urlsafe;
	}
	public void setUrlSafe(boolean isUrlSafe) {
		this.urlsafe = isUrlSafe;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	
	
}
