package com.magicsoftware.monitor.model;

import java.util.ArrayList;
import java.util.List;

public class ChartBean {
	
	private List<String> categories;
	private List<Series> series;
	
	public ChartBean(List<String> categories, List<Series> series) {
		super();
		this.categories = categories;
		this.series = series;
	}
	
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = new ArrayList<String>(categories);
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
	

}
