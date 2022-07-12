package com.magicsoftware.monitor.model;

import com.magicsoftware.xpi.server.data.server.ServerData;
import com.magicsoftware.xpi.server.data.server.WorkerData;

public class ProjectBean {
	private String projectKey;
	private String status;
	private String alert;
	private String messages;
	private String uptime;
	private String engines;
	private String workers;
	private String reserverLivenseThread;
	private String currentLicenseThread;
	private String projectLocation;
	private boolean loadInDebugMode;
	private ServerDetails serverdetail;
	private WorkerData[] workerDetais;
	
	private String projectStartXml;
	private String projectINIFilePath;
	private String projectpathWithStartXml;

	public WorkerData[] getWorkerDetais() {
		return workerDetais;
	}

	public void setWorkerDetais(WorkerData[] workerDetais) {
		this.workerDetais = workerDetais;
	}

	public ServerDetails getServerdetail() {
		return serverdetail;
	}

	public void setServerdetail(ServerDetails serverdetail2) {
		this.serverdetail = serverdetail2;
	}

	public boolean isLoadInDebugMode() {
		return loadInDebugMode;
	}

	public void setLoadInDebugMode(boolean loadInDebugMode) {
		this.loadInDebugMode = loadInDebugMode;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getEngines() {
		return engines;
	}

	public void setEngines(String engines) {
		this.engines = engines;
	}

	public String getWorkers() {
		return workers;
	}

	public void setWorkers(String workers) {
		this.workers = workers;
	}

	public String getReserverLivenseThread() {
		return reserverLivenseThread;
	}

	public void setReserverLivenseThread(String reserverLivenseThread) {
		this.reserverLivenseThread = reserverLivenseThread;
	}

	public String getCurrentLicenseThread() {
		return currentLicenseThread;
	}

	public void setCurrentLicenseThread(String currentLicenseThread) {
		this.currentLicenseThread = currentLicenseThread;
	}

	public String getProjectLocation() {
		return projectLocation;
	}

	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}

	public String getProjectStartXml() {
		return projectStartXml;
	}

	public void setProjectStartXml(String projectStartXml) {
		this.projectStartXml = projectStartXml;
	}

	public String getProjectINIFilePath() {
		return projectINIFilePath;
	}

	public void setProjectINIFilePath(String projectINIFilePath) {
		this.projectINIFilePath = projectINIFilePath;
	}

	public String getProjectpathWithStartXml() {
		return projectpathWithStartXml;
	}

	public void setProjectpathWithStartXml(String projectpathWithStartXml) {
		this.projectpathWithStartXml = projectpathWithStartXml;
	}

}
