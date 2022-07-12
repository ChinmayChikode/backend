package com.magicsoftware.monitor.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.magicsoftware.xpi.server.data.helpers.Alert;

public class MagicXpiHostAndEngines implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7208831330993754312L;

	private String name;
	private String type;
	private Long PID;
	private String zones;
	private String usedMemory;
	private Integer usedCpu;
	private String hostName;
	private String CPUUtlization;
	private String machineUtilization;
	private String hostUtilization;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getPID() {
		return PID;
	}

	public void setPID(Long pID) {
		PID = pID;
	}

	public String getZones() {
		return zones;
	}

	public void setZones(String zones) {
		this.zones = zones;
	}

	public String getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}

	public Integer getUsedCpu() {
		return usedCpu;
	}

	public void setUsedCpu(Integer usedCpu) {
		this.usedCpu = usedCpu;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getCPUUtlization() {
		return CPUUtlization;
	}

	public void setCPUUtlization(String cPUUtlization) {
		CPUUtlization = cPUUtlization;
	}

	public String getMachineUtilization() {
		return machineUtilization;
	}

	public void setMachineUtilization(String machineUtilization) {
		this.machineUtilization = machineUtilization;
	}

	public String getHostUtilization() {
		return hostUtilization;
	}

	public void setHostUtilization(String hostUtilization) {
		this.hostUtilization = hostUtilization;
	}

}
