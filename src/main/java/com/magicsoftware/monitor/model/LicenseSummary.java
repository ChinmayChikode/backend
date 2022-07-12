package com.magicsoftware.monitor.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "license_summary")
public class LicenseSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6139388355374415881L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "project_key")
	private String projectKey;

	@Column(name = "server_id")
	private int serverId;

	private String hostId;
	
	@Column(name = "worker_id")
	private int workerId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp dateCreated;

	@Column(name = "total_peaklicensecount")
	private int totalPeaklicense;

	@Column(name = "project_peaklicensecount")
	private int projectePeaklicense;

	private String runid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public int getTotalPeaklicense() {
		return totalPeaklicense;
	}

	public void setTotalPeaklicense(int totalPeaklicense) {
		this.totalPeaklicense = totalPeaklicense;
	}

	public int getProjectePeaklicense() {
		return projectePeaklicense;
	}

	public void setProjectePeaklicense(int projectePeaklicense) {
		this.projectePeaklicense = projectePeaklicense;
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

	
	public int getWorkerId() {
		return workerId;
	}
	
	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	public String getRunid() {
		return runid;
	}

	public void setRunid(String runid) {
		this.runid = runid;
	}

}
