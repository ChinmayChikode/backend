package com.magicsoftware.monitor.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.magicsoftware.monitor.model.ActivityLog;

@Repository
public interface ActivityLogRepo extends PagingAndSortingRepository<ActivityLog, Long> {
	
}
