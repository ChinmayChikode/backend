package com.magicsoftware.monitor.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.magicsoftware.monitor.model.ActivityLog;
import com.magicsoftware.monitor.model.ActivityLogColor;
import com.magicsoftware.monitor.model.BamDetails;
import com.magicsoftware.monitor.model.QueryFilters;

public interface ActivityLogService {
	
	List<ActivityLogColor> getMonitorActivityLog(QueryFilters queryFiltersDTO) throws Exception;

	int getMonitorActivityLogCount(QueryFilters queryFiltersDTO) throws Exception;

	int deleteActivityLog(QueryFilters queryFiltersDTO);

	BamDetails findByProjectkey(@NotNull String projectkey);
		
	String getBlob(@NotNull String projectKey,@NotNull int msgid);
	
	// 	List<ActivityLog> getFilteredBamDetails(@NotNull String projectkey,@NotNull String userkey1,@NotNull String userkey2,@NotNull String fromDate,@NotNull String toDate);

	//	ArrayList<ActivityLog> getBamDetails(@NotNull String projectKey);

	List<ActivityLog> getBamDetails(@NotNull String projectkey); // new method for BAM

	List<ActivityLog> getFilteredBamDetailsOnUserkey(@NotNull String projKey,@NotNull String userkey1,@NotNull String userkey2);

	List<ActivityLog> getFilteredBamDetailsOnDate(@NotNull String projKey,@NotNull String lblFromDateValue,@NotNull String lblToDateValue) throws ParseException;
	
	List<ActivityLog> getFilteredBamDetailsOnAll(@NotNull String projKey,@NotNull String lblFromDateValue,@NotNull String lblToDateValue,@NotNull String userkey1,@NotNull String userkey2) throws ParseException;
 
}
