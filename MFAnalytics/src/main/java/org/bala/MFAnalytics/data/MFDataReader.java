package org.bala.MFAnalytics.data;

import org.bala.MFAnalytics.common.MFAnalyticsException;

public interface MFDataReader {

	public boolean hasMFData() throws MFAnalyticsException;
	
	public MFData getMFData() throws MFAnalyticsException;
}
