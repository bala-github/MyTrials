package org.bala.MFAnalytics.data.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bala.MFAnalytics.common.MFAnalyticsErrorCode;
import org.bala.MFAnalytics.common.MFAnalyticsException;
import org.bala.MFAnalytics.data.MFData;
import org.bala.MFAnalytics.data.MFDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextFormatMFDataReader implements MFDataReader {

    
	private static final String FORMAT_LINE = "Scheme Code;ISIN Div Payout/ ISIN Growth;ISIN Div Reinvestment;Scheme Name;Net Asset Value;Repurchase Price;Sale Price;Date";
	private static Logger logger = LoggerFactory.getLogger(TextFormatMFDataReader.class);
	
	private BufferedReader br;
	
	public TextFormatMFDataReader(InputStream in) throws IOException, MFAnalyticsException {
		
		br = new BufferedReader(new InputStreamReader(in));
		
		String format = br.readLine();
		
		if(!format.equalsIgnoreCase(FORMAT_LINE)) {
			logger.error("Format not as expected. String read " + format);
			throw new MFAnalyticsException(MFAnalyticsErrorCode.DATA_FORMAT_EXCEPTION);
		}
		
	}
	
	public boolean hasMFData() {
		return false;
	}

	public MFData getMFData() {

		return null;
	}

}
