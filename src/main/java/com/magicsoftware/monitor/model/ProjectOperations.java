package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class ProjectOperations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1287268661750370120L;

	private String projectKey;
	private String filePath;
	private boolean loadInDebugMode;
	private int timeout;

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isLoadInDebugMode() {
		return loadInDebugMode;
	}

	public void setLoadInDebugMode(boolean loadInDebugMode) {
		this.loadInDebugMode = loadInDebugMode;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
