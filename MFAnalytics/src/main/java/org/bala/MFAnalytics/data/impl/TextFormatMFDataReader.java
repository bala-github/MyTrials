package org.bala.MFAnalytics.data.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.bala.MFAnalytics.common.MFAnalyticsErrorCode;
import org.bala.MFAnalytics.common.MFAnalyticsException;
import org.bala.MFAnalytics.data.MFData;
import org.bala.MFAnalytics.data.MFDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextFormatMFDataReader implements MFDataReader {

	private static final String FORMAT_LINE = "Scheme Code;ISIN Div Payout/ ISIN Growth;ISIN Div Reinvestment;Scheme Name;Net Asset Value;Repurchase Price;Sale Price;Date";
	                                           
	
	private static final int SCHEME_CODE_INDEX = 0;
	private static final int ISIN_DIVPAYOUTORGROWTH_INDEX = 1;
	private static final int ISIN_DIVREINVESTMENT_INDEX = 2;
	private static final int NAME_INDEX = 3;
	private static final int NAV_INDEX = 4;
	private static final int RP_INDEX = 5;
	private static final int SP_INDEX = 6;
	private static final int DATE_INDEX = 7;
	
	//is it thread safe. Yes See http://www.slf4j.org/faq.html#declared_static
	private static Logger logger = LoggerFactory.getLogger(TextFormatMFDataReader.class);
	
	//it is thread safe. See https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
	private static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
	
	private BufferedReader br;
	
	private String amc = "";
	
	private String[] mfdata = null;
	
	public TextFormatMFDataReader(InputStream in) throws IOException, MFAnalyticsException {
		
		br = new BufferedReader(new InputStreamReader(in));
		
		String format = br.readLine();
		
		if(!format.equalsIgnoreCase(FORMAT_LINE)) {
			logger.error("Format not as expected. String read " + format);
			throw new MFAnalyticsException(MFAnalyticsErrorCode.DATA_FORMAT_EXCEPTION);
		}
		
	
	}
	
	public boolean hasMFData() throws MFAnalyticsException {
		
		String line  = null;
		
		while(true) {
			
			try {
			
				line = br.readLine();
                
				if(line == null) {		
				   return false;
				} else if((mfdata = line.split(";")).length == 8) {
				   return true;
				} else 	if(line.endsWith("Mutual Fund")) {
				   amc = line;		
		    	}
			}	
		    catch(IOException e) {
		    	logger.error("Error reading MF data. " + e.getMessage() );
		    	throw new MFAnalyticsException(MFAnalyticsErrorCode.ERORR_READING_DATA, e);
		    }
		}
		
	}

	public MFData getMFData() throws MFAnalyticsException {
        
		if(amc == null || amc.isEmpty()) {
			logger.error("No amc information found.");
			throw new MFAnalyticsException(MFAnalyticsErrorCode.DATA_FORMAT_EXCEPTION);
		}
		
		MFData data = new MFData();
		
		data.setAmc(amc);
		data.setCode(Long.parseLong(mfdata[SCHEME_CODE_INDEX]));
		data.setIsinDivPayoutOrGrowth(mfdata[ISIN_DIVPAYOUTORGROWTH_INDEX]);
		data.setIsinDivReinvestment(mfdata[ISIN_DIVREINVESTMENT_INDEX]);
		data.setName(mfdata[NAME_INDEX]);
		data.setNav(Double.parseDouble(mfdata[NAV_INDEX]));
		data.setRp(Double.parseDouble(mfdata[RP_INDEX]));
		data.setSp(Double.parseDouble(mfdata[SP_INDEX]));
		data.setDate(LocalDate.parse(mfdata[DATE_INDEX], df));

		return data;
	}

}
