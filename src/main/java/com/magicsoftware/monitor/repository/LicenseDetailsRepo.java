package com.magicsoftware.monitor.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.magicsoftware.monitor.model.LicenseSummary;

@Repository
public interface LicenseDetailsRepo extends JpaRepository<LicenseSummary, Integer> {

	// @Query("select l , max(l.peaklicenceCount) from LicenseDetails l where l.projectKey=?projectKey and date(l.datetime) BETWEEN fromDate AND toDate")
	// List<LicenseDetails> findAllWithWithBetweenDate(@Param("projectKey") String projectKey, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "select id,project_key,server_id, host_id, datetime,worker_id, max(peak_licence_count) as peak_licence_count,DATE_FORMAT(datetime, '%m/%d/%Y %H:%i'),worker_status   from license_details GROUP BY DATE_FORMAT(datetime, '%m/%d/%Y %H:%i'), project_key ", nativeQuery = true)
	List<LicenseSummary> findLicenseDayWise(String projectKey, Date fromDate, Date toDate);

	@Query(value = "select id,project_key,server_id, host_id, datetime,worker_id, max(peak_licence_count) as peak_licence_count,DATE_FORMAT(datetime, '%m/%d/%Y %H'),worker_status   from license_details GROUP BY DATE_FORMAT(datetime, '%m/%d/%Y %H'), project_key ", nativeQuery = true)
	List<LicenseSummary> findLicenseMonthWise(String projectKey, Date fromDate, Date toDate);

	@Query(value = "select id,project_key,server_id, host_id, datetime,worker_id, max(peak_licence_count) as peak_licence_count,DATE_FORMAT(datetime, '%m/%d/%Y'),worker_status   from license_details GROUP BY DATE_FORMAT(datetime, '%m/%d/%Y'), project_key ", nativeQuery = true)
	List<LicenseSummary> findLicenseYearWise(String projectKey, Date fromDate, Date toDate);

	List<LicenseSummary>  findByProjectKeyAndDateCreatedGreaterThanEqual(@Param("projectKey") String projectKey, @Param("dateCreated") Timestamp dateCreated);

}
