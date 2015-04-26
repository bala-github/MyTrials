package org.bala.MFAnalytics.common;

public class MFAnalyticsException extends Exception {

	private static final long serialVersionUID = -5827045330788008863L;

	public MFAnalyticsException(MFAnalyticsErrorCode errorCode) {
		
		super(errorCode.getMessage());
		
	}
}
