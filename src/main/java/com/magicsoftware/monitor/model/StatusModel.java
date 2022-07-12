package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class StatusModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1022309373239780242L;

	private String statusId;
	private String statusName;

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
