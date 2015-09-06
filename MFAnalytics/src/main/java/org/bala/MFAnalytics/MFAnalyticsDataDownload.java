package org.bala.MFAnalytics;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	private Map<String, Map<Long, SchemeInfo>> schemes;
	
	private ObjectMapper mapper = new ObjectMapper();

	private String rollUpDataDir;
	
	public static  class SchemeInfo {
		
		private String name;
		
		private int index;
		
		SchemeInfo() {
			
		}
		
		SchemeInfo(String name, int index) {
			this.name = name;
			this.index = index;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		
	}
	public MFAnalyticsDataDownload (String outputDir) {
		this.currentDataDir = outputDir + "/current";		
		this.rollUpDataDir = outputDir + "/rollup";
		schemes = new TreeMap<String, Map<Long, SchemeInfo>>();
	}
	
	
	public void download(InputStream in) throws  IOException, MFAnalyticsException {

		//Download latest NAV and parse the information.	
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
		List<String> existingtamcs = null;
	
		try {
			fis = new FileInputStream(currentDataDir + "/amcs.json");
			existingtamcs = mapper.readValue(fis, new TypeReference<List<String>>(){});			
			fis.close();
		} catch(FileNotFoundException e) {
			logger.info("No list of existing amcs found." + e.getMessage());
		}
		
		Map<String, Map<Long, SchemeInfo>>  existingSchemesInfo = new TreeMap<String, Map<Long, SchemeInfo>>();

		if(existingtamcs != null) {
			for(String amc: existingtamcs) {
				
				//for each amc , get existing list of scheme names and index.
				logger.debug("Reading existing schemeinfo for amc " + amc);
				try {
					fis = new FileInputStream(currentDataDir + "/" + amc + "_" + "schemes.json");
					Map<Long, SchemeInfo> scheme = mapper.readValue(fis, new TypeReference<Map<Long, SchemeInfo>>(){});
					existingSchemesInfo.put(amc, scheme);
				}catch(IOException e) {
					logger.error("Error reading current scheme info for amc " + amc + " Exception:" + e.getMessage());
				} finally {
					if(fis != null) {
						fis.close();
					}
				}
				
			}
		}
		
		
				
		Files.createDirectories(FileSystems.getDefault().getPath(currentDataDir));
		Files.createDirectories(FileSystems.getDefault().getPath(rollUpDataDir));
		Set<String> amcs = new HashSet<String>();
		
		for(MFData mfdata : mflist) {
		
			logger.trace("MFData is " + mapper.writeValueAsString(mfdata));
			
			amcs.add(mfdata.getAmc());
			
			SchemeInfo existingSchemeInfo = null;
			if(existingSchemesInfo.get(mfdata.getAmc()) != null) {
				existingSchemeInfo = existingSchemesInfo.get(mfdata.getAmc()).get(mfdata.getCode());
			}
			SchemeInfo schemeinfo = addScheme(mfdata.getAmc(), mfdata.getName(), mfdata.getCode());	 
			
			try {
				String mfname = mfdata.getName().replaceAll(" -* *|-| */ *", "_");
				String mfcurrentdir = currentDataDir + "/" + mfdata.getAmc() + "/" + schemeinfo.getIndex();
				
				logger.debug("Writing current data to dir " + mfcurrentdir + " for scheme " + mfname);
				Files.createDirectories(FileSystems.getDefault().getPath(mfcurrentdir));
				writeDataToFile(mfcurrentdir + "/"+ mfname, mfdata);
				
				
				//read mf file from rollupdir.
				LinkedList<MFData> rollUpData = null;
				if(existingSchemeInfo != null) {				
					String mfrollupdir = rollUpDataDir + "/" + mfdata.getAmc() + "/" + existingSchemeInfo.getIndex();
					logger.debug("Reading rollup data from dir " + mfrollupdir + " for scheme " + mfname);
					
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
					
					if(schemeinfo.index != existingSchemeInfo.index) {
						//delte older files.
						Files.deleteIfExists(FileSystems.getDefault().getPath(mfrollupdir, mfname));
						Files.deleteIfExists(FileSystems.getDefault().getPath(currentDataDir, String.valueOf(existingSchemeInfo.index), mfname));
					}
				}
				
				if(rollUpData == null) {
					rollUpData = new LinkedList<MFData>();
				}
				if(rollUpData.isEmpty() || !rollUpData.getLast().getDate().equals(mfdata.getDate())) {
					//append new data.
					rollUpData.add(mfdata);
					
					//remove older one if limit reached.
					if(rollUpData.size() > ROLL_UP_LIMIT) {
						rollUpData.remove(0);
					}
					//write update mf file to rollupdir.
					String mfrollupdir = rollUpDataDir + "/" + mfdata.getAmc() + "/" + schemeinfo.getIndex();
					Files.createDirectories(FileSystems.getDefault().getPath(mfrollupdir));
					logger.debug("Writing rollup data to dir " + mfrollupdir + " for scheme " + mfname + "Size:" + rollUpData.size()) ;
					writeDataToFile(mfrollupdir + "/"+ mfname, rollUpData);
				}
	
			} catch (IOException  e) {
				logger.error("Error writing data for scheme " + mfdata.getName() + "Exception:" + e.getMessage());
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
	
	private SchemeInfo addScheme(String amc, String name, Long code) {
		
		Map<Long, SchemeInfo> mflist = schemes.get(amc);

		if(mflist == null) {
			logger.info("Intializing MF list for scheme " + amc);
			mflist = new TreeMap<Long, SchemeInfo>();
			schemes.put(amc, mflist);
		}
		
		SchemeInfo schemeinfo = new SchemeInfo(name, (mflist.size()/1000) + 1);
		
		mflist.put(code, schemeinfo);
		
		return schemeinfo;
		
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
