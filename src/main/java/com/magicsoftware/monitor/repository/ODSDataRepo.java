package com.magicsoftware.monitor.repository;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.magicsoftware.monitor.model.ODSData;

@Repository
public interface ODSDataRepo extends JpaRepository<ODSData, Long> {

	public ArrayList<ODSData> findByprojectKey(@Param("projectKey") String projectKey);

	public ArrayList<ODSData> findByuserKey(@Param("userKey") String userKey);

}
