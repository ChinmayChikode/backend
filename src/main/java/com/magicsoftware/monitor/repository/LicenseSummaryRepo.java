package com.magicsoftware.monitor.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.magicsoftware.monitor.model.LicenseDetails;
import com.magicsoftware.monitor.model.LicenseProject;

@Repository
public interface LicenseSummaryRepo extends JpaRepository<LicenseDetails, Integer> {

//	@Query("select l , max(l.peaklicenceCount) from LicenseDetails l  where l.projectKey=?projectKey and date(l.datetime) BETWEEN fromDate AND toDate")
//	List<LicenseDetails> findAllWithWithBetweenDate(@Param("projectKey") String projectKey, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "select id,project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount, DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i')   from license_summary GROUP BY DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i'), project_key ", nativeQuery = true)
	List<LicenseDetails> findLicenseDayWise (String projectKey, Date fromDate, Date toDate);
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount, DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i') from license_summary where worker_id<>0 and project_key = ?1 and dateCreated > ?2 and dateCreated < ?3 GROUP BY project_key ", nativeQuery = true)
	List<LicenseDetails> findLicenseProjectAndDateWise (@Param("projectKey")String projectKey, @Param("startDate") Date fromDate, @Param("endDate") Date toDate);
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount,DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i') from license_summary where worker_id<>0 and dateCreated > ?1 and dateCreated < ?2 GROUP BY project_key ", nativeQuery = true)
	List<LicenseDetails> findLicenseDateWise (@Param("startDate") Date fromDate, @Param("endDate") Date toDate);
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount,DATE_FORMAT(dateCreated, '%m/%d/%Y %H')   from license_summary GROUP BY DATE_FORMAT(dateCreated, '%m/%d/%Y %H'), project_key ", nativeQuery = true)
	List<LicenseDetails> findLicenseMonthWise (String projectKey, Date fromDate, Date toDate);
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount,DATE_FORMAT(dateCreated, '%m/%d/%Y')   from license_summary GROUP BY DATE_FORMAT(dateCreated, '%m/%d/%Y'), project_key ", nativeQuery = true)
	List<LicenseDetails> findLicenseYearWise (String projectKey, Date fromDate, Date toDate);
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, project_peaklicensecount as project_peaklicensecount, total_peaklicensecount from license_summary where project_key = ?1 and project_peaklicensecount = ?2 and dateCreated > ?3 and dateCreated < ?4", nativeQuery = true)
	List<LicenseDetails> findLicenseProjectAndPeak (@Param("projectKey")String projectKey, int peakCount, Date fromDate, Date toDate);

	@Query(value = "select max(project_peaklicensecount), max(total_peaklicensecount) as total_peaklicensecount FROM license_summary where project_key = ?1 and dateCreated > ?2 and dateCreated < ?3", nativeQuery = true)
	int findMaxPeakLicenseByProject (@Param("projectKey")String projectKey, Date fromDate, Date toDate);
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, max(dateCreated) as dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount,DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i') from license_summary where worker_id=0 GROUP BY project_key", nativeQuery = true)
	List<LicenseDetails> findStartDateProject();
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, max(dateCreated) as dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount,DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i') from license_summary where worker_id<>0 and dateCreated > ?1 and dateCreated < ?2 GROUP BY project_key", nativeQuery = true)
	List<LicenseDetails> findStartDateProject(Date fromDate, Date toDate);
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, max(dateCreated) as dateCreated, DATE_FORMAT(dateCreated, '%m/%d/%Y %r') as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount,DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i') from license_summary where project_key = ?1 and worker_id=0 GROUP BY project_key", nativeQuery = true)
	LicenseDetails findStartDateByProject (@Param("projectKey")String projectKey);
	
	@Query(value = "select id,'project_key' as project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount, DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i') from license_summary where worker_id<>0 and dateCreated > ?1 and dateCreated < ?2", nativeQuery = true)
	List<LicenseDetails> findTotalLicenseDateWise(@Param("startDate") Date fromDate, @Param("endDate") Date toDate);
	
	
	@Query(value = "select id,project_key,server_id, hostId, worker_id, dateCreated, dateCreated as dateTimeString, max(project_peaklicensecount) as project_peaklicensecount, max(total_peaklicensecount) as total_peaklicensecount, DATE_FORMAT(dateCreated, '%m/%d/%Y %H:%i') from license_summary where worker_id<>0 and project_key = ?1 and dateCreated > ?2 and dateCreated < ?3 GROUP BY hostId ", nativeQuery = true)
	List<LicenseDetails> findLicenseProjectByHostAndDateWise (@Param("projectKey")String projectKey, @Param("startDate") Date fromDate, @Param("endDate") Date toDate);
	

//	@Query(
//			  value = "SELECT * FROM Users u WHERE u.status = ?1", 
//			  nativeQuery = true)
//			User findUserByStatusNative(Integer status);
}
