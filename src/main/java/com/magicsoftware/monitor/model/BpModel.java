package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class BpModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1981614203762704603L;

	private String bpId;
	private String bpName;

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

}
