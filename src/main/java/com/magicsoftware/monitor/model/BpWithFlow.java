package com.magicsoftware.monitor.model;

import com.magicsoftware.xpi.server.data.project.BpData;
import com.magicsoftware.xpi.server.data.project.FlowData;

public class BpWithFlow {
	private BpData bpData;
	private FlowData[] flowData;

	public BpData getBpData() {
		return bpData;
	}

	public void setBpData(BpData bpData) {
		this.bpData = bpData;
	}

	public FlowData[] getFlowData() {
		return flowData;
	}

	public void setFlowData(FlowData[] flowData) {
		this.flowData = flowData;
	}

	public BpWithFlow(BpData bpData, FlowData[] flowData) {
		this.bpData = bpData;
		this.flowData = flowData;
	}

}
