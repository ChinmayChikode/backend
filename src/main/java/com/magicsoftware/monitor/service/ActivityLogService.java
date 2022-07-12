package com.magicsoftware.monitor.service;

import java.util.List;

import com.magicsoftware.monitor.model.ActivityLog;
import com.magicsoftware.monitor.model.QueryFilters;

public interface ActivityLogService {

	List<ActivityLog> getMonitorActivityLog(QueryFilters queryFiltersDTO) throws Exception;

	int getMonitorActivityLogCount(QueryFilters queryFiltersDTO) throws Exception;

	int deleteActivityLog(QueryFilters queryFiltersDTO);

	/*
	 * public ActivityLog findOne(Long id);
	 * 
	 * public List<ActivityLog> findAll();
	 * 
	 * public ArrayList<ActivityLog> findByProjectkey(String projectkey, Pageable
	 * pageable);
	 */

}
