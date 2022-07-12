package com.magicsoftware.monitor.model;

import java.io.Serializable;
import java.util.List;

import com.magicsoftware.xpi.server.data.server.Scheduler;

public class SchedulerData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6641256848061119584L;
	private String bpName;
	private String flowName;
	private Integer serverId;
    private Long isn;
    private Integer type;
    private String execDataTime;
    private Long bpId; 
    private Long flowId;
    private Long schedulerId;
    private Long stepId;
    private String versionKey;
    private String execDateTimeOrg;
    private String schedulerName;
    private String uid;
//    private Scheduler [] schedulers;



    
//	public SchedulerData(String bpName,String flowName,Integer serverId,Long isn)
//	{
//	  	
//	}
	
	public String getBpName() {
		return bpName;
	}
	public void setBpName(String bpName) {
		this.bpName = bpName;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public Long getIsn() {
		return isn;
	}
	public void setIsn(Long isn) {
		this.isn = isn;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getExecDataTime() {
		return execDataTime;
	}
	public void setExecDataTime(String execDataTime) {
		this.execDataTime = execDataTime;
	}
	public Long getBpId() {
		return bpId;
	}
	public void setBpId(Long bpId) {
		this.bpId = bpId;
	}
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public Long getSchedulerId() {
		return schedulerId;
	}
	public void setSchedulerId(Long schedulerId) {
		this.schedulerId = schedulerId;
	}
	public Long getStepId() {
		return stepId;
	}
	public void setStepId(Long stepId) {
		this.stepId = stepId;
	}
	public String getVersionKey() {
		return versionKey;
	}
	public void setVersionKey(String versionKey) {
		this.versionKey = versionKey;
	}
	public String getExecDateTimeOrg() {
		return execDateTimeOrg;
	}
	public void setExecDateTimeOrg(String execDateTimeOrg) {
		this.execDateTimeOrg = execDateTimeOrg;
	}
	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
	

}
