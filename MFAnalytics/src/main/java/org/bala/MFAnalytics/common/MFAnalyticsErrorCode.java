package org.bala.MFAnalytics.common;

public enum MFAnalyticsErrorCode {

	DATA_FORMAT_EXCEPTION ("DATA_FORMAT_EXCEPTION.") ,
	ERORR_READING_DATA("ERORR_READING_DATA.")
	;
	
	private String errorCode;

	MFAnalyticsErrorCode(String errorCode) {
		
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		
		return errorCode;
	}
}
