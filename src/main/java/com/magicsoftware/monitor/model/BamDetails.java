package com.magicsoftware.monitor.model;

import java.util.List;


public class BamDetails {
	
//	private FlowData[] flowData;
	private List<ActivityLog> bamData;
	private List<String> category;
	private List<String> priority;
	private List<String> status;
	
	public List<ActivityLog> getBamData() {
		return bamData;
	}
	public void setBamData(List<ActivityLog> bamData) {
		this.bamData = bamData;
	}
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public List<String> getPriority() {
		return priority;
	}
	public void setPriority(List<String> priority) {
		this.priority = priority;
	}
	public List<String> getStatus() {
		return status;
	}
	public void setStatus(List<String> status) {
		this.status = status;
	}
	

}
