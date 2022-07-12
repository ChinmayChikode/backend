package com.magicsoftware.monitor.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FlowRequestMessages {
	private String projectKey;
	private Long messageId;
	private Long rootMessageId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime incomeDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime inProgressDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate;
	private String endReason;

	public FlowRequestMessages(String projectKey, Long messageId, Long rootMessageId, LocalDateTime incomeDate, LocalDateTime inProgressDate,
			LocalDateTime endDate, String endReason) {
		super();
		this.projectKey = projectKey;
		this.messageId = messageId;
		this.rootMessageId = rootMessageId;
		this.incomeDate = incomeDate;
		this.inProgressDate = inProgressDate;
		this.endDate = endDate;
		this.endReason = endReason;
	}

	public FlowRequestMessages() {
		// TODO Auto-generated constructor stub
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getRootMessageId() {
		return rootMessageId;
	}

	public void setRootMessageId(Long rootMessageId) {
		this.rootMessageId = rootMessageId;
	}

	public LocalDateTime getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(LocalDateTime incomeDate) {
		this.incomeDate = incomeDate;
	}

	public LocalDateTime getInProgressDate() {
		return inProgressDate;
	}

	public void setInProgressDate(LocalDateTime inProgressDate) {
		this.inProgressDate = inProgressDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public String getEndReason() {
		return endReason;
	}

	public void setEndReason(String endReason) {
		this.endReason = endReason;
	}

}
