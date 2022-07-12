package com.magicsoftware.monitor.model;

import java.io.Serializable;

public class LicenseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8445931400602874429L;

	private String licenseId;
	private String licenseType;

	public String getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

}
