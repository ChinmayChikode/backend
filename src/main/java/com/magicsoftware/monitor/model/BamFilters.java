package com.magicsoftware.monitor.model;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestBody;

public class BamFilters {

	String ProjKey;
	String ProjLocation;
	String userkey1;
	String userkey2;
	String lblFromDateValue;
	String lblToDateValue;
	
	public String getProjKey() {
		return "ODSTest_RtView_Automation";
		//return ProjKey;
	}
	public void setProjKey(String ProjKey) {
		this.ProjKey = ProjKey;
	}
	public String getProjLocation() {
		return ProjLocation;
	}
	public void setProjLocation(String ProjLocation) {
		this.ProjLocation = ProjLocation;
	}
	public String getUserkey1() {
		return userkey1;
	}
	public void setUserkey1(String userkey1) {
		this.userkey1 = userkey1;
	}
	public String getUserkey2() {
		return userkey2;
	}
	public void setUserkey2(String userkey2) {
		this.userkey2 = userkey2;
	}
	public String getLblFromDateValue() {
		return lblFromDateValue;
	}
	public void setLblFromDateValue(String lblFromDateValue) {
		this.lblFromDateValue = lblFromDateValue;
	}
	public String getLblToDateValue() {
		return lblToDateValue;
	}
	public void setLblToDateValue(String lblToDateValue) {
		this.lblToDateValue = lblToDateValue;
	}
	
	
}
