package com.magicsoftware.monitor.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryFilters {

	@JsonProperty("ServerID")
	private String ServerID = "";

	@JsonProperty("BPID")
	private String BPID = "";

	@JsonProperty("FlowID")
	private String FlowID = "";

	@JsonProperty("StepID")
	private String StepID = "";

	@JsonProperty("errorsAndUserMessages")
	private String errorsAndUserMessages = "";

	@JsonProperty("lblFilterRootFSIDValue")
	private String lblFilterRootFSIDValue = "";

	@JsonProperty("lblFilterFlowReqIDValue")
	private String lblFilterFlowReqIDValue = "";

	@JsonProperty("lblFilterFSIDValue")
	private String lblFilterFSIDValue = "";

	@JsonProperty("messageTypeIDString")
	private String messageTypeIDString = "";

	@JsonProperty("lblFromDateValue")
	private String lblFromDateValue = "";

	@JsonProperty("lblToDateValue")
	private String lblToDateValue = "";

	@JsonProperty("lblFromTimeValue")
	private String lblFromTimeValue = "";

	@JsonProperty("lblToTimeValue")
	private String lblToTimeValue = "";

	@JsonProperty("ProjKey")
	private String ProjKey = "";

	@JsonProperty("ProjLocation")
	private String ProjLocation = "";

	@JsonProperty("Previous")
	private Integer Previous = 0;

	@JsonProperty("Next")
	private Integer Next = 0;

	@JsonProperty("userkey1")
	private String userkey1 = "";

	@JsonProperty("userkey2")
	private String userkey2 = "";

	@JsonProperty("statusCode")
	private String statusCode = "";

	@JsonProperty("category")
	private String category = "";

	@JsonProperty("StringForCategoryTable")
	private String StringForCategoryTable = "";

	@JsonProperty("StringForSeverityTable")
	private String StringForSeverityTable = "";

	@JsonProperty("StringForStatusTable")
	private String StringForStatusTable = "";

	@JsonProperty("ODSType")
	private String ODSType = "";

	@JsonProperty("UserKeyODS")
	private String UserKeyODS = "";

	@JsonProperty("TypeODS")
	private String TypeODS = "";

	@JsonProperty("PreviousODS")
	private String PreviousODS = "";

	@JsonProperty("NextODS")
	private String NextODS = "";

	@JsonProperty("RunId")
	private String RunId = "";

	@JsonProperty("actLogDeletionDate")
	private String actLogDeletionDate = "";

	private StringBuilder activityLogQuery;
	private StringBuilder activityLogCountQuery;
	private StringBuilder activityLogDeleteQuery;

	@JsonProperty("actMsgFiltersMetadata")
	private List<ActivityMsgFilterMetadata> actMsgFiltersMetadata;

	// private boolean displayFileExistsFlag = false;

	@JsonProperty("page")
	private Integer page = 0;

	@JsonProperty("size")
	private Integer size = 0;

	public String getUserKeyODS() {
		return UserKeyODS;
	}

	public void setUserKeyODS(String userKeyODS) {
		UserKeyODS = userKeyODS;
	}

	public String getTypeODS() {
		return TypeODS;
	}

	public void setTypeODS(String typeODS) {
		TypeODS = typeODS;
	}

	public String getPreviousODS() {
		return PreviousODS;
	}

	public void setPreviousODS(String previousODS) {
		PreviousODS = previousODS;
	}

	public String getNextODS() {
		return NextODS;
	}

	public void setNextODS(String nextODS) {
		NextODS = nextODS;
	}

	public String getODSType() {
		return ODSType;
	}

	public void setODSType(String type) {
		ODSType = type;
	}

	public String getStringForCategoryTable() {
		return StringForCategoryTable;
	}

	public void setStringForCategoryTable(String stringForCategoryTable) {
		StringForCategoryTable = stringForCategoryTable;
	}

	public String getStringForSeverityTable() {
		return StringForSeverityTable;
	}

	public void setStringForSeverityTable(String stringForSeverityTable) {
		StringForSeverityTable = stringForSeverityTable;
	}

	public String getStringForStatusTable() {
		return StringForStatusTable;
	}

	public void setStringForStatusTable(String stringForStatusTable) {
		StringForStatusTable = stringForStatusTable;
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

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProjKey() {
		return ProjKey;
	}

	public void setProjKey(String projKey) {
		ProjKey = projKey;
	}

	public String getProjLocation() {
		return ProjLocation;
	}

	public void setProjLocation(String projLocation) {
		ProjLocation = projLocation;
	}

	public String getServerID() {
		return ServerID;
	}

	public void setServerID(String serverID) {
		ServerID = serverID;
	}

	public String getBPID() {
		return BPID;
	}

	public void setBPID(String bpid) {
		BPID = bpid;
	}

	public String getFlowID() {
		return FlowID;
	}

	public void setFlowID(String flowID) {
		FlowID = flowID;
	}

	public String getStepID() {
		return StepID;
	}

	public void setStepID(String stepID) {
		StepID = stepID;
	}

	public String getErrorsAndUserMessages() {
		return errorsAndUserMessages;
	}

	public void setErrorsAndUserMessages(String errorsAndUserMessages) {
		this.errorsAndUserMessages = errorsAndUserMessages;
	}

	public String getLblFilterRootFSIDValue() {
		return lblFilterRootFSIDValue;
	}

	public void setLblFilterRootFSIDValue(String lblFilterRootFSIDValue) {
		this.lblFilterRootFSIDValue = lblFilterRootFSIDValue;
	}

	public String getLblFilterFlowReqIDValue() {
		return lblFilterFlowReqIDValue;
	}

	public void setLblFilterFlowReqIDValue(String lblFilterFlowReqIDValue) {
		this.lblFilterFlowReqIDValue = lblFilterFlowReqIDValue;
	}

	public String getLblFilterFSIDValue() {
		return lblFilterFSIDValue;
	}

	public void setLblFilterFSIDValue(String lblFilterFSIDValue) {
		this.lblFilterFSIDValue = lblFilterFSIDValue;
	}

	public String getMessageTypeIDString() {
		return messageTypeIDString;
	}

	public void setMessageTypeIDString(String messageTypeIDString) {
		this.messageTypeIDString = messageTypeIDString;
	}

	public String getLblFromDateValue() {
		return lblFromDateValue;
	}

	public void setLblFromDateValue(String lblFromDateValue) {
		this.lblFromDateValue = lblFromDateValue;
	}

	public String getLblToDateValue() {
		return lblToDateValue;
	}

	public void setLblToDateValue(String lblToDateValue) {
		this.lblToDateValue = lblToDateValue;
	}

	public Integer getPrevious() {
		return Previous;
	}

	public void setPrevious(Integer previous) {
		Previous = previous;
	}

	public Integer getNext() {
		return Next;
	}

	public void setNext(Integer next) {
		Next = next;
	}

	public String getLblFromTimeValue() {
		return lblFromTimeValue;
	}

	public void setLblFromTimeValue(String lblFromTimeValue) {
		this.lblFromTimeValue = lblFromTimeValue;
	}

	public String getLblToTimeValue() {
		return lblToTimeValue;
	}

	public void setLblToTimeValue(String lblToTimeValue) {
		this.lblToTimeValue = lblToTimeValue;
	}

	public String getRunId() {
		return RunId;
	}

	public void setRunId(String runId) {
		RunId = runId;
	}

	public StringBuilder getActivityLogQuery() {
		return activityLogQuery;
	}

	public void setActivityLogQuery(StringBuilder activityLogQuery) {
		this.activityLogQuery = activityLogQuery;
	}

	public StringBuilder getActivityLogCountQuery() {
		return activityLogCountQuery;
	}

	public void setActivityLogCountQuery(StringBuilder activityLogCountQuery) {
		this.activityLogCountQuery = activityLogCountQuery;
	}

	public String getActLogDeletionDate() {
		return actLogDeletionDate;
	}

	public void setActLogDeletionDate(String actLogDeletionDate) {
		this.actLogDeletionDate = actLogDeletionDate;
	}

	public StringBuilder getActivityLogDeleteQuery() {
		return activityLogDeleteQuery;
	}

	public void setActivityLogDeleteQuery(StringBuilder activityLogDeleteQuery) {
		this.activityLogDeleteQuery = activityLogDeleteQuery;
	}

	public List<ActivityMsgFilterMetadata> getActMsgFiltersMetadata() {
		return actMsgFiltersMetadata;
	}

	public void setActMsgFiltersMetadata(List<ActivityMsgFilterMetadata> actMsgFiltersMetadata) {
		this.actMsgFiltersMetadata = actMsgFiltersMetadata;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

//	public boolean isDisplayFileExistsFlag() {
//		return displayFileExistsFlag;
//	}
//
//	public void setDisplayFileExistsFlag(boolean displayFileExistsFlag) {
//		this.displayFileExistsFlag = displayFileExistsFlag;
//	}

}
