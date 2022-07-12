package com.magicsoftware.monitor.query;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.magicsoftware.monitor.model.ActivityLog;

public class CustomActivityLogMapper implements RowMapper<ActivityLog> {

	@Override
	public ActivityLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		ActivityLog activityLog = new ActivityLog();
		
		activityLog.setServerid(rs.getInt("SERVERID"));
		activityLog.setMsgid(rs.getInt("MSGID"));
		activityLog.setBpid(rs.getInt("BPID"));
		activityLog.setFlowid(rs.getInt("FLOWID"));
		activityLog.setFsid(rs.getLong("FSID"));
		activityLog.setFsstep(rs.getInt("FSSTEP"));
		activityLog.setMessagetypeid(rs.getInt("MESSAGETYPEID"));
		activityLog.setMessagestring(rs.getString("MESSAGESTRING"));
		//activityLog.setUserblob(rs.getBytes("USERBLOB"));
		//activityLog.setUsercode(rs.getInt("USERCODE"));
		activityLog.setCreateTimeStamp(rs.getTimestamp("CREATETIMESTAMP"));
		activityLog.setObjectlevel(rs.getInt("OBJECTLEVEL"));
		//activityLog.setCategory(rs.getString("CATEGORY"));
		//activityLog.setUserkey1(rs.getString("USERKEY1"));
		//activityLog.setUserkey2(rs.getString("USERKEY2"));
		//activityLog.setVersionkey(rs.getString("VERSIONKEY"));
		//activityLog.setStatuscode(rs.getInt("STATUSCODE"));
		//activityLog.setSeverity(rs.getInt("SEVERITY"));
		//activityLog.setExtension(rs.getString("EXTENSION"));
		activityLog.setProjectkey(rs.getString("PROJECTKEY"));
		activityLog.setBlobexists(rs.getInt("BLOBEXISTS"));
		activityLog.setRootfsid(rs.getLong("ROOTFSID"));
		activityLog.setFlowrequestid(rs.getInt("FLOWREQUESTID"));
		//activityLog.setFilelocation(rs.getString("FILELOCATION"));
		//activityLog.setRunId(rs.getString("RUNID"));
		
		return activityLog;
	}

}
