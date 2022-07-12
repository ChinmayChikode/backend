package com.magicsoftware.monitor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "license_details")
public class LicenseDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long  id;

	@Column(name = "project_key")
	public String projectKey;

	@Column(name = "server_id")
	private int serverId;

//	@Column(name = "host_id")
	private String hostId;
	
	@Column(name = "dateCreated")
	private Date datetime;

	@Column(name = "project_peaklicensecount")
	public int peaklicenceCount;

//	@Column(name = "worker_status")
//	private String workerStatus;
	
	@Column(name = "total_peaklicensecount")
	public int totalPeakLicenseCount;
	
	
	@Column(name = "worker_id")
	private int workerId;
	
	private String dateTimeString;


	public String getDateTimeString() {
		return dateTimeString;
	}

	public void setDateTimeString(String dateTimeString) {
		this.dateTimeString = dateTimeString;
	}

	public Long  getId() {
		return id;
	}

	public void setId(Long  id) {
		this.id = id;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public int getPeaklicenceCount() {
		return peaklicenceCount;
	}

	public void setPeaklicenceCount(int peaklicenceCount) {
		this.peaklicenceCount = peaklicenceCount;
	}

//	public String getWorkerStatus() {
//		return workerStatus;
//	}
//
//	public void setWorkerStatus(String workerStatus) {
//		this.workerStatus = workerStatus;
//	}

	public void setTotalPeakLicenseCount(int totalPeakLicenseCount) {
		this.totalPeakLicenseCount = totalPeakLicenseCount;
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

}
