package com.magicsoftware.monitor.model;

import java.util.List;

import com.magicsoftware.xpi.server.data.project.FlowData;

public class FlowDetails {

	private FlowData[] flowData;
	private List<BpModel> bpList;
	private List<FlowModel> flowList;

	public FlowData[] getFlowData() {
		return flowData;
	}

	public void setFlowData(FlowData[] flowData) {
		this.flowData = flowData;
	}

	public List<BpModel> getBpList() {
		return bpList;
	}

	public void setBpList(List<BpModel> bpList) {
		this.bpList = bpList;
	}

	public List<FlowModel> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<FlowModel> flowList) {
		this.flowList = flowList;
	}

}
