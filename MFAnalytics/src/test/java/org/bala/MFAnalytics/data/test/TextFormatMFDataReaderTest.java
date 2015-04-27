package org.bala.MFAnalytics.data.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.bala.MFAnalytics.common.MFAnalyticsException;
import org.bala.MFAnalytics.data.MFDataReader;
import org.bala.MFAnalytics.data.impl.TextFormatMFDataReader;
import org.junit.Test;

public class TextFormatMFDataReaderTest {

	@Test(expected = MFAnalyticsException.class)
	public void testInvalidData() throws IOException, MFAnalyticsException {
		
		InputStream in = this.getClass().getResourceAsStream("/InvalidTextFormat.txt");

		MFDataReader datareader = new TextFormatMFDataReader(in);
		
	}
	
	@Test
	public void testValidData() throws  MFAnalyticsException, IOException {
		
		InputStream in = this.getClass().getResourceAsStream("/ValidTextFormat.txt");
		
		MFDataReader datareader = new TextFormatMFDataReader(in);
		
		while(datareader.hasMFData()) {
			
			System.out.println(datareader.getMFData().toString());
		}
		
	}

}
