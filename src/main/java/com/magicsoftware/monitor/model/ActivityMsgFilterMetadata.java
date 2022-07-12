package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class ActivityMsgFilterMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1048913774791946804L;

	boolean display;
	boolean write;
	Integer messageId;
	Integer module;
	String messageName = "";
	String color = "";

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getModule() {
		return module;
	}

	public void setModule(Integer module) {
		this.module = module;
	}

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
