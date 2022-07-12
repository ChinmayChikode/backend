package com.magicsoftware.monitor.model;

import java.util.List;

import com.magicsoftware.xpi.server.data.server.ServerData;

public class ServerDetails {

	private ServerData[] serverData;
	private List<HostModel> hostList;
	private List<StatusModel> statusList;
	private List<LicenseModel> licenseFeatureList;
	private Integer totalmessagesprocessed;

	public Integer getTotalmessagesprocessed() {
		return totalmessagesprocessed;
	}

	public void setTotalmessagesprocessed(Integer totalmessagesprocessed) {
		this.totalmessagesprocessed = totalmessagesprocessed;
	}

	public ServerData[] getServerData() {
		return serverData;
	}

	public void setServerData(ServerData[] serverData) {
		this.serverData = serverData;
	}

	public List<HostModel> getHostList() {
		return hostList;
	}

	public void setHostList(List<HostModel> hostList) {
		this.hostList = hostList;
	}

	public List<StatusModel> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusModel> statusList) {
		this.statusList = statusList;
	}

	public List<LicenseModel> getLicenseFeatureList() {
		return licenseFeatureList;
	}

	public void setLicenseFeatureList(List<LicenseModel> licenseFeatureList) {
		this.licenseFeatureList = licenseFeatureList;
	}

}
