package com.magicsoftware.monitor.model;

import java.io.Serializable;
import java.util.List;

public class MonitorOfflineMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6256458879336558410L;

	private List<BpModel> bpList;
	private List<FlowModel> flowList;
	private List<StepModel> stepList;

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

	public List<StepModel> getStepList() {
		return stepList;
	}

	public void setStepList(List<StepModel> stepList) {
		this.stepList = stepList;
	}

}
