package com.magicsoftware.monitor.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ifs_actlog")
public class ActivityLog {

	private Integer serverid;

	@Id
	private Integer msgid;
	private Integer bpid;
	private Integer flowid;
	private Long fsid;
	private Integer fsstep;
	private Integer messagetypeid;
	private String messagestring;
	private byte[] userblob;
	private Integer usercode;

	@Column(name = "CREATETIMESTAMP")
	private Timestamp createTimeStamp;

	private Integer objectlevel;
	private String category;
	private String userkey1;
	private String userkey2;
	private String versionkey;
	private Integer statuscode;
	private Integer severity;
	private String extension;
	private String projectkey;
	private Integer blobexists;
	private Long rootfsid;
	private Integer flowrequestid;
	private String filelocation;
	private String runId;
	
	@Transient
	private String color;

	@Transient
	private String serverName;

	@Transient
	private String bpName;

	@Transient
	private String flowName;

	@Transient
	private String stepName;

	@Transient
	private String messageType;

	@Transient
	private String displayCreatedTime;

	@Transient
	private Long totalNumberOfRecords;

	@Transient
	private java.util.Date dateOfWritingToSpace = new java.util.Date();
	
	public ActivityLog(ActivityLog activityLog) {
		this.msgid = activityLog.getMsgid();
		this.bpid = activityLog.getBpid();
		this.flowid = activityLog.getFlowid();
		this.fsid = activityLog.getFsid();
		this.fsstep = activityLog.getFsstep();
		this.messagetypeid = activityLog.getMessagetypeid();
		this.messagestring = activityLog.getMessagestring();
		this.userblob = activityLog.getUserblob();
		this.usercode = activityLog.getUsercode();
		this.createTimeStamp = activityLog.getCreateTimeStamp();
		this.objectlevel = activityLog.getObjectlevel();
		this.category = activityLog.getCategory();
		this.userkey1 = activityLog.getUserkey1();
		this.userkey2 = activityLog.getUserkey2();
		this.versionkey = activityLog.getVersionkey();
		this.statuscode = activityLog.getStatuscode();
		this.severity = activityLog.getSeverity();
		this.extension = activityLog.getExtension();
		this.projectkey = activityLog.getProjectkey();
		this.blobexists = activityLog.getBlobexists();
		this.rootfsid = activityLog.getRootfsid();
		this.flowrequestid = activityLog.getFlowrequestid();
		this.filelocation = activityLog.getFilelocation();
		this.runId = activityLog.getRunId();
		this.color = activityLog.getColor();
		this.serverName = activityLog.getServerName();
		this.bpName = activityLog.getBpName();
		this.flowName = activityLog.getFlowName();
		this.stepName = activityLog.getStepName();
		this.messageType = activityLog.getMessageType();
		this.displayCreatedTime = activityLog.getDisplayCreatedTime();
		this.totalNumberOfRecords = activityLog.getTotalNumberOfRecords();
		this.dateOfWritingToSpace = activityLog.getDateOfWritingToSpace();
	}
	
	public ActivityLog() {
		
	}

	public Integer getServerid() {
		return serverid;
	}

	public void setServerid(Integer serverid) {
		this.serverid = serverid;
	}

	public Integer getMsgid() {
		return msgid;
	}

	public void setMsgid(Integer msgid) {
		this.msgid = msgid;
	}

	public Integer getBpid() {
		return bpid;
	}

	public void setBpid(Integer bpid) {
		this.bpid = bpid;
	}

	public Integer getFlowid() {
		return flowid;
	}

	public void setFlowid(Integer flowid) {
		this.flowid = flowid;
	}

	public Long getFsid() {
		return fsid;
	}

	public void setFsid(Long fsid) {
		this.fsid = fsid;
	}

	public Integer getFsstep() {
		return fsstep;
	}

	public void setFsstep(Integer fsstep) {
		this.fsstep = fsstep;
	}

	public Integer getMessagetypeid() {
		return messagetypeid;
	}

	public void setMessagetypeid(Integer messagetypeid) {
		this.messagetypeid = messagetypeid;
	}

	public String getMessagestring() {
		return messagestring;
	}

	public void setMessagestring(String messagestring) {
		this.messagestring = messagestring;
	}

	public byte[] getUserblob() {
		return userblob;
	}

	public void setUserblob(byte[] userblob) {
		this.userblob = userblob;
	}

	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	public Timestamp getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Timestamp createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Integer getObjectlevel() {
		return objectlevel;
	}

	public void setObjectlevel(Integer objectlevel) {
		this.objectlevel = objectlevel;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUserkey1() {
		return userkey1;
	}

	public void setUserkey1(String userkey1) {
		this.userkey1 = userkey1;
	}

	public String getUserkey2() {
		return userkey2;
	}

	public void setUserkey2(String userkey2) {
		this.userkey2 = userkey2;
	}

	public String getVersionkey() {
		return versionkey;
	}

	public void setVersionkey(String versionkey) {
		this.versionkey = versionkey;
	}

	public Integer getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(Integer statuscode) {
		this.statuscode = statuscode;
	}

	public Integer getSeverity() {
		return severity;
	}

	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getProjectkey() {
		return projectkey;
	}

	public void setProjectkey(String projectkey) {
		this.projectkey = projectkey;
	}

	public Integer getBlobexists() {
		return blobexists;
	}

	public void setBlobexists(Integer blobexists) {
		this.blobexists = blobexists;
	}

	public Long getRootfsid() {
		return rootfsid;
	}

	public void setRootfsid(Long rootfsid) {
		this.rootfsid = rootfsid;
	}

	public Integer getFlowrequestid() {
		return flowrequestid;
	}

	public void setFlowrequestid(Integer flowrequestid) {
		this.flowrequestid = flowrequestid;
	}

	public String getFilelocation() {
		return filelocation;
	}

	public void setFilelocation(String filelocation) {
		this.filelocation = filelocation;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

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

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getDisplayCreatedTime() {
		return displayCreatedTime;
	}

	public void setDisplayCreatedTime(String displayCreatedTime) {
		this.displayCreatedTime = displayCreatedTime;
	}

	public java.util.Date getDateOfWritingToSpace() {
		return dateOfWritingToSpace;
	}

	public void setDateOfWritingToSpace(java.util.Date dateOfWritingToSpace) {
		this.dateOfWritingToSpace = dateOfWritingToSpace;
	}

	public Long getTotalNumberOfRecords() {
		return totalNumberOfRecords;
	}

	public void setTotalNumberOfRecords(Long totalNumberOfRecords) {
		this.totalNumberOfRecords = totalNumberOfRecords;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
