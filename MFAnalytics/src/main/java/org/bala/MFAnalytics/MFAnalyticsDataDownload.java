package org.bala.MFAnalytics;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.bala.MFAnalytics.common.MFAnalyticsException;
import org.bala.MFAnalytics.data.MFData;
import org.bala.MFAnalytics.data.MFDataReader;
import org.bala.MFAnalytics.data.impl.TextFormatMFDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MFAnalyticsDataDownload {

	private static final int ROLL_UP_LIMIT = 30;

	private static Logger logger = LoggerFactory.getLogger("");
	
	private String currentDataDir;
	
	private Map<String, List<String>> schemes;
	
	private ObjectMapper mapper = new ObjectMapper();

	private String rollUpDataDir;
	
	public MFAnalyticsDataDownload (String outputDir) {
		this.currentDataDir = outputDir + "/current";		
		this.rollUpDataDir = outputDir + "/rollup";
		schemes = new TreeMap<String, List<String>>();
	}
	
	
	public void download(InputStream in) throws  IOException, MFAnalyticsException {

			
		MFDataReader mfreader = new TextFormatMFDataReader(in);
		
		List<MFData> mflist = new LinkedList<MFData>();
		
		while(mfreader.hasMFData()) {
			
			MFData mfdata = null;
			try {
				mfdata = mfreader.getMFData();
			} catch (MFAnalyticsException e) {
				
				logger.error("Skipping an entry due to error " + e.getMessage());
				continue;
			}
			
			mflist.add(mfdata);	 
		}
		
		logger.info("No. of schemes  found " + mflist.size());
		
		
		
	
		FileInputStream fis = null;
		
		
		Files.createDirectories(FileSystems.getDefault().getPath(currentDataDir));
		Files.createDirectories(FileSystems.getDefault().getPath(rollUpDataDir));
		Set<String> amcs = new HashSet<String>();
		
		for(MFData mfdata : mflist) {
		
			logger.trace("MFData is " + mapper.writeValueAsString(mfdata));
			
			amcs.add(mfdata.getAmc());
			
			addScheme(mfdata.getAmc(), mfdata.getName());	 
			
			try {
				String mfname = mfdata.getName().replaceAll(" -* *|-| */ *", "_");
				String mfcurrentdir = currentDataDir + "/" + mfdata.getAmc();
				
				Files.createDirectories(FileSystems.getDefault().getPath(mfcurrentdir));
				writeDataToFile(mfcurrentdir + "/"+ mfname, mfdata);
				
				//read mf file from rollupdir.
				
				LinkedList<MFData> rollUpData = new LinkedList<MFData>();
				String mfrollupdir = rollUpDataDir + "/" + mfdata.getAmc() ;
				Files.createDirectories(FileSystems.getDefault().getPath(mfrollupdir));
				try {
					fis = new FileInputStream(mfrollupdir + "/" + mfname);
					if(fis != null) {
						rollUpData = mapper.readValue(fis,new TypeReference<LinkedList<MFData>>(){});
					}
				} catch(Exception e) {
					logger.error("Error reading rollUpData for scheme " + mfdata.getName());
				} finally {
					if(fis != null) {
						fis.close();
					}
				}
				
				if(rollUpData == null || rollUpData.isEmpty() || !rollUpData.getLast().getDate().equals(mfdata.getDate())) {
					//append new data.
					rollUpData.add(mfdata);
					
					//remove older one if limit reached.
					if(rollUpData.size() > ROLL_UP_LIMIT) {
						rollUpData.remove(0);
					}
					//write update mf file to rollupdir.
					writeDataToFile(mfrollupdir + "/"+ mfname, rollUpData);
				}
	
			} catch (IOException  e) {
				logger.error("Error writing data for scheme " + mfdata.getName());
				e.printStackTrace();
			} 	
		}
		
		//Write List of AMC's
		writeDataToFile(currentDataDir + "/amcs.json", amcs);
		
		for(String amc : amcs) {
			//For each AMC write list of schemes.
			writeDataToFile(currentDataDir + "/" + amc + "_" + "schemes.json", schemes.get(amc));
		}

	}
	
	private void writeDataToFile(String filename, Object data) throws IOException {
	
		FileOutputStream fos = new FileOutputStream(filename);
		mapper.writeValue(fos, data);
		fos.close();
	}
	
	private void addScheme(String amc, String name) {
		
		List<String> mflist = schemes.get(amc);

		if(mflist == null) {
			logger.info("Intializing MF list for scheme " + amc);
			mflist = new LinkedList<String>();
			schemes.put(amc, mflist);
		}
		
		mflist.add(name);
		
	}


	public static void main(String[] args) throws ClientProtocolException, IOException, MFAnalyticsException {
		
		if(args.length != 1) {
			
			System.out.println("GithubPages repository directory required.");
			System.exit(1);
		}
		
		HttpResponse response = Request.Get("http://portal.amfiindia.com/spages/NAV0.txt").execute().returnResponse();
		
		InputStream in = response.getEntity().getContent();
		
		MFAnalyticsDataDownload downloader = new MFAnalyticsDataDownload(args[0]);
		
		downloader.download(in);
		
		
	}
}
