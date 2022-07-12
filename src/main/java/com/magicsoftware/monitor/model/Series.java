package com.magicsoftware.monitor.model;

import java.util.List;

public class Series {

	private String name;
	private List<Integer> data;
	
	public Series(String name, List<Integer> data) {
		super();
		this.name = name;
		this.data = data;
	}
	
	public Series() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}
}
