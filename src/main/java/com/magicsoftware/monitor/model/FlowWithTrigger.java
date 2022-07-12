package com.magicsoftware.monitor.model;

import com.magicsoftware.xpi.server.data.project.FlowData;
import com.magicsoftware.xpi.server.data.project.TriggerData;

public class FlowWithTrigger {

	private FlowData flowData;
	private TriggerData[] triggerData;

	public FlowData getFlowData() {
		return flowData;
	}

	public void setFlowData(FlowData flowData) {
		this.flowData = flowData;
	}

	public TriggerData[] getTriggerData() {
		return triggerData;
	}

	public void setTriggerData(TriggerData[] triggerData) {
		this.triggerData = triggerData;
	}

	public FlowWithTrigger(FlowData flowData, TriggerData[] triggerData) {
		super();
		this.flowData = flowData;
		this.triggerData = triggerData;
	}

	public FlowWithTrigger() {
		// TODO Auto-generated constructor stub
	}

}
