package org.bala.MFAnalytics.common;

public enum MFAnalyticsErrorCode {

	DATA_FORMAT_EXCEPTION ("Data format was not as expected.")
	;
	
	private String message;

	MFAnalyticsErrorCode(String message) {
		
		this.message = message;
	}
	
	public String getMessage() {
		
		return message;
	}
}
