package com.magicsoftware.monitor.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.magicsoftware.xpi.server.data.helpers.Alert;

public class MagicXpiSpaceInstances implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4839581994572221205L;

	private String spaceName;
	private String spacepartitioninstance;
	private String type;
	private String runningonHost;
	private String containingGSC;
	private String ipAddress;
	private String spaceStatus;
	private boolean isContainingGSC;
	private ArrayList<Alert> alerts;

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getSpacepartitioninstance() {
		return spacepartitioninstance;
	}

	public void setSpacepartitioninstance(String spacepartitioninstance) {
		this.spacepartitioninstance = spacepartitioninstance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRunningonHost() {
		return runningonHost;
	}

	public void setRunningonHost(String runningonHost) {
		this.runningonHost = runningonHost;
	}

	public String getContainingGSC() {
		return containingGSC;
	}

	public void setContainingGSC(String containingGSC) {
		this.containingGSC = containingGSC;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSpaceStatus() {
		return spaceStatus;
	}

	public void setSpaceStatus(String spaceStatus) {
		this.spaceStatus = spaceStatus;
	}

	public boolean isContainingGSC() {
		return isContainingGSC;
	}

	public void setContainingGSC(boolean isContainingGSC) {
		this.isContainingGSC = isContainingGSC;
	}

	public ArrayList<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(ArrayList<Alert> alerts) {
		this.alerts = alerts;
	}

}
