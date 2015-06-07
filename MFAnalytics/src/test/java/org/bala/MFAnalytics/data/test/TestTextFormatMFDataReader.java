package org.bala.MFAnalytics.data.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bala.MFAnalytics.common.MFAnalyticsException;
import org.bala.MFAnalytics.data.MFData;
import org.bala.MFAnalytics.data.MFDataReader;
import org.bala.MFAnalytics.data.impl.TextFormatMFDataReader;
import org.junit.Test;

public class TestTextFormatMFDataReader {

	@Test(expected = MFAnalyticsException.class)
	public void testInvalidData() throws IOException, MFAnalyticsException {
		
		InputStream in = this.getClass().getResourceAsStream("/InvalidTextFormat.txt");

	    new TextFormatMFDataReader(in);
		
	}
	
	@Test
	public void testValidData() throws  MFAnalyticsException, IOException {
		
		InputStream in = this.getClass().getResourceAsStream("/ValidTextFormat.txt");
		
		MFDataReader datareader = new TextFormatMFDataReader(in);
		
		List<MFData>  mflist = new ArrayList<MFData>();
		
		while(datareader.hasMFData()) {
			
			mflist.add(datareader.getMFData());
		}
		
		assertTrue(mflist.size() == 19);
	}
	
	@Test
	public void testMfNameRegex() {
		
		String name = "Tata Floating Rate Fund - Long Term Option Bonus/ Dividend";
		String mfname = name.replaceAll(" -* *|-| */ *", "_");
		
		System.out.println(mfname);
		
		
		
	}

}
