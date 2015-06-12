package org.bala.MFAnalytics.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_DEFAULT)
//@JsonIgnoreProperties({"date"})
public class MFData {

	private long code;
	private String amc;
	private String isinDivPayoutOrGrowth;
	private String isinDivReinvestment;
	private String name;
	private double nav;
	private double rp;
	private double sp;
	
	
	private String date;

	public long getCode() {
		return code;
	}
	
	public void setCode(long code) {
		this.code = code;
	}

	public String getAmc() {
		return amc;
	}

	public void setAmc(String amc) {
		this.amc = amc;
	}

	public String getIsinDivPayoutOrGrowth() {
		return isinDivPayoutOrGrowth;
	}

	public void setIsinDivPayoutOrGrowth(String isinDivPayoutOrGrowth) {
		this.isinDivPayoutOrGrowth = isinDivPayoutOrGrowth;
	}

	public String getIsinDivReinvestment() {
		return isinDivReinvestment;
	}

	public void setIsinDivReinvestment(String isinDivReinvestment) {
		this.isinDivReinvestment = isinDivReinvestment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNav() {
		return nav;
	}

	public void setNav(double nav) {
		this.nav = nav;
	}

	public double getRp() {
		return rp;
	}

	public void setRp(double rp) {
		this.rp = rp;
	}

	public double getSp() {
		return sp;
	}

	public void setSp(double sp) {
		this.sp = sp;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String toString() {
		
		return amc + code + isinDivPayoutOrGrowth + isinDivReinvestment + name + nav + rp + sp + date.toString();
	}
	
}
