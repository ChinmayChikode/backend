package com.magicsoftware.monitor.model;

public class TriggerActivityModel {

	private String name;
	private long data[];
	
	public TriggerActivityModel(String name, long[] data) {
		super();
		this.name = name;
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long[] getData() {
		return data;
	}
	public void setData(long[] data) {
		this.data = data;
	}
	
}
