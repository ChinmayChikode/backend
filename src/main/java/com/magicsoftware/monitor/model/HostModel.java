package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class HostModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3056275049794472234L;

	private String hostId;
	private String hostName;

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

}
