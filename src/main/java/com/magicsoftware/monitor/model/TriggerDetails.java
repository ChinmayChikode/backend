package com.magicsoftware.monitor.model;

import java.util.List;

import com.magicsoftware.xpi.server.data.project.TriggerData;

public class TriggerDetails {

	private TriggerData[] triggerData;
	private List<BpModel> bpList;
	private List<FlowModel> flowList;
	private List<String> triggerTypes;
	private List<String> serverName;
	private List<String> triggerStates;

	public TriggerData[] getTriggerData() {
		return triggerData;
	}

	public void setTriggerData(TriggerData[] triggerData) {
		this.triggerData = triggerData;
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

	public List<String> getTriggerTypes() {
		return triggerTypes;
	}

	public void setTriggerTypes(List<String> triggerTypes) {
		this.triggerTypes = triggerTypes;
	}

	public List<String> getServerName() {
		return serverName;
	}

	public void setServerName(List<String> serverName) {
		this.serverName = serverName;
	}

	public List<String> getTriggerStates() {
		return triggerStates;
	}

	public void setTriggerStates(List<String> triggerStates) {
		this.triggerStates = triggerStates;
	}

}
