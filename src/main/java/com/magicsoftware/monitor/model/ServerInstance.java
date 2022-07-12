package com.magicsoftware.monitor.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.magicsoftware.xpi.server.data.project.BaseData;
import com.magicsoftware.xpi.server.data.server.TriggerLoadData;

import java.util.Calendar;

public class ServerInstance extends BaseData{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum Status {
        START_REQUESTED, SERVER_INITIALIZING, START_IN_PROGRESS, START_FAILED, RUNNING, STOP_REQUESTED, STOP_IN_PROGRESS, STOPPED, STOPPED_FAILED
    }

	
    public ServerInstance()
    {
    	System.out.println("Inside ServerInstnace constructor");
    }
    
    
    
    private Integer serverId; 
    private Integer numberOfWorkers;
    private String licenseFeature;
    private String licenseSN;

    private String serverName;
    private List<String> servers;
    private Integer cycleRetryCount;
    private ArrayList<Integer> previousServersId;
    private Boolean onHold;

    private Integer processId;

    private Boolean loadTriggers;
    private Boolean loadScheduler;
    private Boolean isDebugMode = false;
    private Boolean autoStartFlow;
    private Boolean autoStartFlowExecuted;


    private Date validateStateTime;
    private Date startRequestedTime;
    private Date stopRequestedTime;
    
    private Integer retryCount;
    private String failureReason;

    private String projectsDirectory;
    private Integer gsaID;

    private com.magicsoftware.xpi.server.data.server.ServerData.Status status;

    private Date lastUpdated = Calendar.getInstance().getTime();
    private Integer restartTimes = 0;
    private Boolean loadInDebugMode = false;

    private List<TriggerLoadData> triggerLoadData;

    private Double cpu;
    private Integer memoryMB;

    private String startReqTime;
	private String lastUpdtReqTime;
	
	
	
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public Integer getNumberOfWorkers() {
		return numberOfWorkers;
	}
	public void setNumberOfWorkers(Integer numberOfWorkers) {
		this.numberOfWorkers = numberOfWorkers;
	}
	public String getLicenseFeature() {
		return licenseFeature;
	}
	public void setLicenseFeature(String licenseFeature) {
		this.licenseFeature = licenseFeature;
	}
	public String getLicenseSN() {
		return licenseSN;
	}
	public void setLicenseSN(String licenseSN) {
		this.licenseSN = licenseSN;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public List<String> getServers() {
		return servers;
	}
	public void setServers(List<String> servers) {
		this.servers = servers;
	}
	public Integer getCycleRetryCount() {
		return cycleRetryCount;
	}
	public void setCycleRetryCount(Integer cycleRetryCount) {
		this.cycleRetryCount = cycleRetryCount;
	}
	public ArrayList<Integer> getPreviousServersId() {
		return previousServersId;
	}
	public void setPreviousServersId(ArrayList<Integer> previousServersId) {
		this.previousServersId = previousServersId;
	}
	public Boolean getOnHold() {
		return onHold;
	}
	public void setOnHold(Boolean onHold) {
		this.onHold = onHold;
	}
	public Integer getProcessId() {
		return processId;
	}
	public void setProcessId(Integer processId) {
		this.processId = processId;
	}
	public Boolean getLoadTriggers() {
		return loadTriggers;
	}
	public void setLoadTriggers(Boolean loadTriggers) {
		this.loadTriggers = loadTriggers;
	}
	public Boolean getLoadScheduler() {
		return loadScheduler;
	}
	public void setLoadScheduler(Boolean loadScheduler) {
		this.loadScheduler = loadScheduler;
	}
	public Boolean getIsDebugMode() {
		return isDebugMode;
	}
	public void setIsDebugMode(Boolean isDebugMode) {
		this.isDebugMode = isDebugMode;
	}
	public Boolean getAutoStartFlow() {
		return autoStartFlow;
	}
	public void setAutoStartFlow(Boolean autoStartFlow) {
		this.autoStartFlow = autoStartFlow;
	}
	public Boolean getAutoStartFlowExecuted() {
		return autoStartFlowExecuted;
	}
	public void setAutoStartFlowExecuted(Boolean autoStartFlowExecuted) {
		this.autoStartFlowExecuted = autoStartFlowExecuted;
	}
	public Date getValidateStateTime() {
		return validateStateTime;
	}
	public void setValidateStateTime(Date validateStateTime) {
		this.validateStateTime = validateStateTime;
	}
	public Date getStartRequestedTime() {
		return startRequestedTime;
	}
	public void setStartRequestedTime(Date startRequestedTime) {
		this.startRequestedTime = startRequestedTime;
	}
	public Date getStopRequestedTime() {
		return stopRequestedTime;
	}
	public void setStopRequestedTime(Date stopRequestedTime) {
		this.stopRequestedTime = stopRequestedTime;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public String getProjectsDirectory() {
		return projectsDirectory;
	}
	public void setProjectsDirectory(String projectsDirectory) {
		this.projectsDirectory = projectsDirectory;
	}
	public Integer getGsaID() {
		return gsaID;
	}
	public void setGsaID(Integer gsaID) {
		this.gsaID = gsaID;
	}
	public com.magicsoftware.xpi.server.data.server.ServerData.Status getStatus() {
		return status;
	}
	public void setStatus(com.magicsoftware.xpi.server.data.server.ServerData.Status status2) {
		this.status = status2;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Integer getRestartTimes() {
		return restartTimes;
	}
	public void setRestartTimes(Integer restartTimes) {
		this.restartTimes = restartTimes;
	}
	public Boolean getLoadInDebugMode() {
		return loadInDebugMode;
	}
	public void setLoadInDebugMode(Boolean loadInDebugMode) {
		this.loadInDebugMode = loadInDebugMode;
	}
	public List<TriggerLoadData> getTriggerLoadData() {
		return triggerLoadData;
	}
	public void setTriggerLoadData(List<TriggerLoadData> triggerLoadData) {
		this.triggerLoadData = triggerLoadData;
	}
	public Double getCpu() {
		return cpu;
	}
	public void setCpu(Double cpu) {
		this.cpu = cpu;
	}
	public Integer getMemoryMB() {
		return memoryMB;
	}
	public void setMemoryMB(Integer memoryMB) {
		this.memoryMB = memoryMB;
	}
	public String getStartReqTime() {
		return startReqTime;
	}
	public void setStartReqTime(String startReqTime) {
		this.startReqTime = startReqTime;
	}
	public String getLastUpdtReqTime() {
		return lastUpdtReqTime;
	}
	public void setLastUpdtReqTime(String lastUpdtReqTime) {
		this.lastUpdtReqTime = lastUpdtReqTime;
	}

	
	
	
	
	

}
