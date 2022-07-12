package com.magicsoftware.monitor.model;

import com.magicsoftware.xpi.server.messages.PSSData;

public class PSSWithFLowName extends PSSData {
	

	//private String flowId;
	//PSSWithFLowName(){}
	
	private String flowName;
	private String bpName;

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
}
