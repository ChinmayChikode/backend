package com.magicsoftware.monitor.model;

import java.util.List;

public class LicenseGraphData {
	
	String projectName;
	List<Integer> data;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}

}
