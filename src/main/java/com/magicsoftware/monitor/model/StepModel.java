package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class StepModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4050263897252573578L;

	private String stepId;
	private String stepName;

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

}
