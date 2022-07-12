package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class FlowModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3687651237199926993L;

	private String flowId;
	private String flowName;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

}
