package com.magicsoftware.monitor.repository;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.magicsoftware.monitor.model.ActivityLog;
import com.magicsoftware.monitor.model.QueryFilters;

@Repository
public interface ActivityLogRepo extends JpaRepository<ActivityLog, Long> {

	ArrayList<ActivityLog> findByProjectkey(@Param("projectkey") String projectkey, Pageable pageable);

//	@Query("SELECT a.msgid, a.bpid, a.flowid, a.fsid, a.fsstep, a.messagetypeid, a.messagestring, a.createTimeStamp, a.category, "
//			+ "a.userkey1, a.userkey2, a.severity, a.statuscode, a.projectkey, a.blobexists, a.rootfsid, a.filelocation, a.flowrequestid"
//			+ "FROM ActivityLog a WHERE a.projectkey =?1 and a.userkey1 LIKE 'Uk%'  ")
//	ArrayList<ActivityLog> findAllSearch(@Param("projectkey") String projectkey);
	
	@Query("SELECT a FROM ActivityLog a WHERE a.projectkey =:projectkey and a.userkey1 LIKE 'Uk%'  ")
	ArrayList<ActivityLog> findAllSearch(@Param("projectkey") String projectkey);	
	
	@Query("SELECT a.userblob, a.extension FROM ActivityLog a WHERE a.projectkey =:projectkey and a.msgid= :msgid")
	List<Object[]> displayBamBlob(@Param("projectkey") String projectkey, @Param("msgid") int msgid);
	
	 @Query("SELECT a FROM ActivityLog a WHERE a.projectkey =:projectkey and a.userkey1=:userkey1 and a.userkey2=:userkey2 and a.createTimeStamp >= :fromDate and a.createTimeStamp <= :toDate ")
	ArrayList<ActivityLog> findFilteredSearchOnAll(@Param("projectkey") String projectkey,@Param("userkey1") String userkey1,@Param("userkey2") String userkey2,@Param("fromDate") Date lblFromDateValue2,@Param("toDate") Date lblToDateValue2);

	@Query("SELECT a FROM ActivityLog a WHERE a.projectkey = :projectkey and a.userkey1= :userkey1 and a.userkey2= :userkey2 ")
	ArrayList<ActivityLog> findFilteredSearchOnUserkey(@Param("projectkey") String projKey,@Param("userkey1") String userkey1,@Param("userkey2") String userkey2);
	
	@Query("SELECT a FROM ActivityLog a WHERE a.projectkey = :projectkey and a.createTimeStamp >= :fromDate and a.createTimeStamp <= :toDate and a.userkey1 LIKE 'Uk%'")
	ArrayList<ActivityLog> findFilteredSearchOnDateTime(@Param("projectkey") String projKey,@Param("fromDate") Date lblFromDateValue2,@Param("toDate") Date lblToDateValue2);
	
}
