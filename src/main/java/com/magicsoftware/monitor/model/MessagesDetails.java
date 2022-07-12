package com.magicsoftware.monitor.model;

import java.util.ArrayList;
import java.util.List;

import com.magicsoftware.xpi.server.messages.FlowRequest;

public class MessagesDetails {

	private int totalMessages;
	private int failledMessages;
	private int waitingMessages;
	private int inProcessMessages;
	private FlowRequest[] messages;
	private List<BpModel> bpList;
	private List<FlowModel> flowList;
	private List<StatusModel> msgStatus;
	private List<MsgInvocationModel> invocationType;

	public int getTotalMessages() {
		return totalMessages;
	}

	public void setTotalMessages(int totalMessages) {
		this.totalMessages = totalMessages;
	}

	public int getFailledMessages() {
		return failledMessages;
	}

	public void setFailledMessages(int failledMessages) {
		this.failledMessages = failledMessages;
	}

	public int getWaitingMessages() {
		return waitingMessages;
	}

	public void setWaitingMessages(int waitingMessages) {
		this.waitingMessages = waitingMessages;
	}

	public int getInProcessMessages() {
		return inProcessMessages;
	}

	public void setInProcessMessages(int inProcessMessages) {
		this.inProcessMessages = inProcessMessages;
	}

	public FlowRequest[] getMessages() {
		return messages;
	}

	public void setMessages(FlowRequest[] messages) {
		this.messages = messages;
	}

	public List<BpModel> getBpList() {
		return bpList;
	}

	public void setBpList(List<BpModel> bpList) {
		this.bpList = bpList;
	}

	public List<FlowModel> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<FlowModel> flowList) {
		this.flowList = flowList;
	}

	public List<StatusModel> getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(List<StatusModel> msgStatus) {
		this.msgStatus = msgStatus;
	}

	public List<MsgInvocationModel> getInvocationType() {
		return invocationType;
	}

	public void setInvocationType(List<MsgInvocationModel> invocationType) {
		this.invocationType = invocationType;
	}

}
