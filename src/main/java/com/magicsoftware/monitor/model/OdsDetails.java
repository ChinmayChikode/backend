package com.magicsoftware.monitor.model;

import java.util.List;

public class OdsDetails {
	private List<ODSData> odsData;
	private List<String> userKeyType;
	
	
	public List<String> getUserKeyType() {
		return userKeyType;
	}
	public void setUserKeyType(List<String> userKeyType) {
		this.userKeyType = userKeyType;
	}
	public List<ODSData> getOdsData() {
		return odsData;
	}
	public void setOdsData(List<ODSData> odsData) {
		this.odsData = odsData;
	}
	
//	public List<String> getType() {
//		return userKeyType;
//	}
//	public void setType(List<String> userKeyType) {
//		this.userKeyType = userKeyType;
//	}
	
 
}
