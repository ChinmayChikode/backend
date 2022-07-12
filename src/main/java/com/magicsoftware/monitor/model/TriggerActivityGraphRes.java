package com.magicsoftware.monitor.model;

import java.util.List;

public class TriggerActivityGraphRes {

	private List<TriggerActivityModel> series;

	private List<String> categories;

	public List<TriggerActivityModel> getSeries() {
		return series;
	}

	public void setSeries(List<TriggerActivityModel> series) {
		this.series = series;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public TriggerActivityGraphRes(List<TriggerActivityModel> series, List<String> categories) {
		super();
		this.series = series;
		this.categories = categories;
	}

}
