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
	public void test() throws IOException, MFAnalyticsException {
		
		InputStream in = this.getClass().getResourceAsStream("/InvalidTextFormat.txt");

		MFDataReader datareader = new TextFormatMFDataReader(in);
		
	}

}
