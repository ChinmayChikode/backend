package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class MsgInvocationModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5300084012988090277L;

	private String msgInvoTypeId;
	private String msgInvoType;

	public String getMsgInvoTypeId() {
		return msgInvoTypeId;
	}

	public void setMsgInvoTypeId(String msgInvoTypeId) {
		this.msgInvoTypeId = msgInvoTypeId;
	}

	public String getMsgInvoType() {
		return msgInvoType;
	}

	public void setMsgInvoType(String msgInvoType) {
		this.msgInvoType = msgInvoType;
	}

}
