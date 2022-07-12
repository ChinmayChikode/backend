package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class Flow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String projectKey;

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

}
