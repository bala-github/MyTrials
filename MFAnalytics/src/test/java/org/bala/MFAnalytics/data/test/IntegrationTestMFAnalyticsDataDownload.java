package org.bala.MFAnalytics.data.test;

import java.io.IOException;
import java.io.InputStream;

import org.bala.MFAnalytics.MFAnalyticsDataDownload;
import org.bala.MFAnalytics.common.MFAnalyticsException;
import org.junit.Test;

public class IntegrationTestMFAnalyticsDataDownload {

	@Test
	public void testDownload() throws IOException, MFAnalyticsException {
		InputStream in = this.getClass().getResourceAsStream("/ValidTextFormat.txt");
		MFAnalyticsDataDownload downloader = new MFAnalyticsDataDownload("output");
		
		downloader.download(in);
	}

}
