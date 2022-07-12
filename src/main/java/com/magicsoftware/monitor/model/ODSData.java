package com.magicsoftware.monitor.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.magicsoftware.xpi.server.common.Utils;

@Entity
@Table(name = "ifs_ods")
public class ODSData {

	@Id
	private String odsIdx;
	private Long fsId;
	private String projectKey;
	private Long ODSIndex;
	private String userKey;
	private Long serverId;
	private String userString;
	private Double userNumber;
	private Long userLogical;
	private Date userDate;
	private String userTime;
	private byte[] userBlob;
	private Long global;
	private Date createDate;
	private String createTime;
	private Date modifyDate;
	private String modifyTime;
	private Long ODSServerId;
	private String versionKey;

	@Transient
	private String userKeyType;
	
//	private String createdDateTime;
//	private String modifiedDateTime;

	/**
	 * Constructor
	 * 
	 * @param serverId
	 * @param fsId
	 * @param userKey
	 * @param userString
	 * @param userNumber
	 * @param userLogical
	 * @param userDate
	 * @param userTime
	 * @param userBlob
	 * @param global
	 * @param createDate
	 * @param createTime
	 * @param modifyDate
	 * @param modifyTime
	 * @param ODSServerId
	 * @param versionKey
	 * @param ODSIndex
	 * @param projectKey
	 */
	/**
	 * 
	 * @return
	 */
	
	public String getUserKeyType() {
		if(global == 1) {
			this.userKeyType = "Global";
			}
			else {
				this.userKeyType = "Local";
			}
		return userKeyType;
	}

	public void setUserKeyType(String userKeyType) {
		this.userKeyType = userKeyType;
	}
	
	@Column(name = "serverid", scale = 0)
	public Long getServerId() {
		return this.serverId;
	}

	/**
	 * 
	 * @param serverId
	 */
	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "userstring")
	public String getUserString() {
		return this.userString;
	}

	/**
	 * 
	 * @param userString
	 */
	public void setUserString(String userString) {
		this.userString = userString;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "usernumber", scale = 0)
	public Double getUserNumber() {
		return this.userNumber;
	}

	/**
	 * 
	 * @param userNumber
	 */
	public void setUserNumber(Double userNumber) {
		this.userNumber = userNumber;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "userlogical")
	public Long getUserLogical() {
		return this.userLogical;
	}

	/**
	 * 
	 * @param userLogical
	 */
	public void setUserLogical(Long userLogical) {
		this.userLogical = userLogical;
	}

	/**
	 * 
	 * @return
	 */
	@JsonFormat(pattern="yyyy-dd-MM")
	@Column(name = "userdate", length = 23)
	public Date getUserDate() {
		return this.userDate;
	}

	@Transient
	public String getUserDateStr() {
		if (userDate != null)
			return Utils.formatDateOnly(userDate);
		return "";
	}

	/**
	 * 
	 * @param userDate
	 */
	public void setUserDate(Date userDate) {
		this.userDate = userDate;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "usertime", length = 6)
	public String getUserTime() {
		return this.userTime;
	}

	/**
	 * 
	 * @param userTime
	 */
	public void setUserTime(String userTime) {
		this.userTime = userTime;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "userblob")
	public byte[] getUserBlob() {
		return this.userBlob;
	}

	/**
	 * 
	 * @param userBlob
	 */
	public void setUserBlob(byte[] userBlob) {
		this.userBlob = userBlob;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "global")
	public Long getGlobal() {
		return this.global;
	}

	/**
	 * 
	 * @param global
	 */
	public void setGlobal(Long global) {
		this.global = global;
	}

	/**
	 * 
	 * @return
	 */
	@JsonFormat(pattern="yyyy-dd-MM HH:mm:ss")
	@Column(name = "createdate", length = 23)
	public Date getCreateDate() {
//		System.out.println("Create Date value :-"+this.createDate);
//		String receivedToDateFromRTViewUser = this.createDate ;
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
//		Date parsedDateObject = sdf.parse(receivedToDateFromRTViewUser);
		return this.createDate;
	}

	/**
	 * 
	 * @param createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "createtime", length = 6)
	public String getCreateTime() {
		return this.createTime;
	}

	/**
	 * 
	 * @param createTime
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 
	 * @return
	 */
	@JsonFormat(pattern="yyyy-dd-MM HH:mm:ss")
	@Column(name = "modifydate", length = 23)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	/**
	 * 
	 * @param modifyDate
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "modifytime", length = 6)
	public String getModifyTime() {
//		System.out.println("modified time "+this.modifyTime);
		return this.modifyTime;
	}

	/**
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(String modifyTime) {
		
		this.modifyTime = modifyTime;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "odsserverid", precision = 53, scale = 0)
	public Long getODSServerId() {
		return this.ODSServerId;
	}

	/**
	 * 
	 * @param ODSServerId
	 */
	public void setODSServerId(Long ODSServerId) {
		this.ODSServerId = ODSServerId;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "versionkey", length = 30)
	public String getVersionKey() {
		return this.versionKey;
	}

	/**
	 * 
	 * @param versionKey
	 */
	public void setVersionKey(String versionKey) {
		this.versionKey = versionKey;
	}

	/**
	 * 
	 * @param odsIdx
	 */
	public void setOdsIdx(String odsIdx) {
		this.odsIdx = odsIdx;
	}

	/**
	 * 
	 * @return
	 */
	@SpaceId
	@Column(name = "odsIdx")
	public String getOdsIdx() {
		return odsIdx;
	}

	/**
	 * 
	 */

	/**
	 * 
	 * @param fsId
	 */
	public void setFsId(Long fsId) {
		this.fsId = fsId;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "fsid", scale = 0)
	public Long getFsId() {
		return fsId;
	}

	/**
	 * 
	 * @param projectKey
	 */
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "projectkey", nullable = false, length = 30)
	@SpaceRouting
	public String getProjectKey() {
		return projectKey;
	}

	/**
	 * 
	 * @param oDSIndex
	 */
	public void setODSIndex(Long oDSIndex) {
		ODSIndex = oDSIndex;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "odsindex", scale = 0)
	public Long getODSIndex() {
		return ODSIndex;
	}

	/**
	 * 
	 * @param userKey
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "userkey", length = 30)
	public String getUserKey() {
		return userKey;
	}
	
//	
//	public String getCreatedDateTime() {
//		return createdDateTime;
//	}
//
//	public void setCreatedDateTime(String createdDateTime) {
//		this.createdDateTime = createdDateTime;
//	}
//
//	public String getModifiedDateTime() {
//		return modifiedDateTime;
//	}
//
//	public void setModifiedDateTime(String modifiedDateTime) {
//		this.modifiedDateTime = modifiedDateTime;
//	}

}
