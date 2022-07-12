package com.magicsoftware.monitor.serviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magicsoftware.monitor.model.ChartBean;
import com.magicsoftware.monitor.model.LicenseDetails;
import com.magicsoftware.monitor.model.Series;
import com.magicsoftware.monitor.repository.LicenseDetailsRepo;
import com.magicsoftware.monitor.repository.LicenseSummaryRepo;
import com.magicsoftware.monitor.util.Constants;
import com.magicsoftware.monitor.util.DateUtility;

@Service
public class LicenseDetailsImpl {

	@Autowired
	private LicenseSummaryRepo licenseDetailsRepo;

	public ChartBean findLicenseDetails(@NotNull String projectKey, @NotNull String type) {
		Calendar cal = Calendar.getInstance();
		Date enddate = new Date();// NOT USED IN ANY FILTER IGNORE
		if (type.equalsIgnoreCase("sStart")) {
			cal.add(Calendar.MONTH, -6);
		} else if (type.equalsIgnoreCase("MONTH")) {
			cal.add(Calendar.MONTH, -1);
		} else if (type.equalsIgnoreCase("WEEK")) {
			cal.add(Calendar.DATE, -7);
		} else if (type.equalsIgnoreCase("DEFAULT")) {
			cal.add(Calendar.YEAR, -7);
		}
		Date startDate = cal.getTime();
		List<LicenseDetails> result = licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, startDate, enddate);
		
	    return returnChartBean(result);
	}

	public List<LicenseDetails> findLicenseDetails(@NotNull String projectKey, @NotNull String type, Date fromDate, Date toDate) {
		return licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, fromDate, toDate);
	}

	public ChartBean findLicenseDetails(@NotNull String type) {
		List<LicenseDetails> fResult = getLicenseDetailsBySinceStart();
	    return returnChartBean(fResult);
	}

	public ChartBean findLicenseDetails(@NotNull String type, Date fromDate, Date toDate) {
		toDate =  DateUtility.addDatePart(toDate, Calendar.HOUR, 24);
		List<LicenseDetails> fResult = getLicenseDetailsByDate(fromDate, toDate);
	    return returnChartBean(fResult);
	}
	
	public ChartBean findLicenseDetailsForDefaultGraph(@NotNull String projectKey) {
		Date dStartDate = DateUtility.addDatePart(Calendar.DATE, -1);
		Date wStartDate = DateUtility.addDatePart(Calendar.DATE, -7);
		Date mStartDate = DateUtility.addDatePart(Calendar.MONTH, -1);
		Date yStartDate = DateUtility.addDatePart(Calendar.YEAR, -1); 
		Date endDate = DateUtility.addDatePart(Calendar.DATE, 0);
		List<LicenseDetails> fResult = new ArrayList<LicenseDetails>();
		List<LicenseDetails> dResult = licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, dStartDate, endDate);
		List<LicenseDetails> wResult = licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, wStartDate, endDate);
		List<LicenseDetails> mResult = licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, mStartDate, endDate);
		List<LicenseDetails> yResult = licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, yStartDate, endDate);
		List<LicenseDetails> sResult = getLicenseDetailsByProjectSinceStart(projectKey);
		
		LicenseDetails defaultLicense = new LicenseDetails();
		defaultLicense.setProjectKey(projectKey);
		defaultLicense.setPeaklicenceCount(0);
		defaultLicense.setTotalPeakLicenseCount(0);
	
		fResult.addAll(dResult);
		if (dResult.size() == 0) {
			fResult.add(defaultLicense);
		}
		
		fResult.addAll(wResult);
		fResult.addAll(mResult);
		fResult.addAll(yResult);
		fResult.addAll(sResult);
		ChartBean fChartBean = returnChartBean(fResult);
		fChartBean.setCategories(Constants.DEFAULTCATEGORYLISTLICENSE);
		
		if (yResult.size() == 0) {
			fChartBean.getCategories().remove("Yearly");
		}
		if(mResult.size() == 0) {
			fChartBean.getCategories().remove("Monthly");
		} 
		if(wResult.size() == 0) {
			fChartBean.getCategories().remove("Weekly");
		}
		if(sResult.size() == 0) {
			fChartBean.getCategories().remove("Since Startup");
		}
		
	    return fChartBean;
	}
	
	public ChartBean findLicenseDetailsForDefaultGraphByRage(@NotNull String projectKey, Date fromDate, Date toDate) {

		List<LicenseDetails> fResult = licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, fromDate, toDate);
		
		ChartBean fChartBean = returnChartBean(fResult);
		
	    return fChartBean;
	}
	
	
	public List<Series> findLicenseDetailsForHost(@NotNull String projectKey) {
		Date dStartDate = DateUtility.addDatePart(Calendar.DATE, -1);
		Date wStartDate = DateUtility.addDatePart(Calendar.DATE, -7);
		Date mStartDate = DateUtility.addDatePart(Calendar.MONTH, -1);
		Date yStartDate = DateUtility.addDatePart(Calendar.YEAR, -1); 
		Date endDate = DateUtility.addDatePart(Calendar.DATE, 0);
		List<LicenseDetails> fResult = new ArrayList<LicenseDetails>();
		List<LicenseDetails> dResult = licenseDetailsRepo.findLicenseProjectByHostAndDateWise(projectKey, dStartDate, endDate);
		List<LicenseDetails> wResult = licenseDetailsRepo.findLicenseProjectByHostAndDateWise(projectKey, wStartDate, endDate);
		List<LicenseDetails> mResult = licenseDetailsRepo.findLicenseProjectByHostAndDateWise(projectKey, mStartDate, endDate);
		List<LicenseDetails> yResult = licenseDetailsRepo.findLicenseProjectByHostAndDateWise(projectKey, yStartDate, endDate);
		List<LicenseDetails> sResult = getHostLicenseDetailsByProjectSinceStart(projectKey);
		
		LicenseDetails defaultLicense = new LicenseDetails();
		defaultLicense.setProjectKey(projectKey);
		defaultLicense.setPeaklicenceCount(0);
		defaultLicense.setTotalPeakLicenseCount(0);
	
		fResult.addAll(dResult);
		if (dResult.size() < yResult.size()) {
			for (LicenseDetails licenseDetails : yResult) {
				if(!containsHost(dResult, licenseDetails.getHostId())) {
					defaultLicense = new LicenseDetails();
					defaultLicense.setProjectKey(licenseDetails.getProjectKey());
					defaultLicense.setHostId(licenseDetails.getHostId());
					fResult.add(0,defaultLicense);
				}
			}
		}
		
		fResult.addAll(wResult);
		fResult.addAll(mResult);
		fResult.addAll(yResult);
		fResult.addAll(sResult);
		
	    return getListSeriesByHost(fResult);
	}
	
	public List<Series> findLicenseDetailsForHostByRange(@NotNull String projectKey, Date fromDate, Date toDate) {
		List<LicenseDetails> fResult = licenseDetailsRepo.findLicenseProjectByHostAndDateWise(projectKey, fromDate, toDate);
	    return getListSeriesByHost(fResult);
	}
	
	public List<Series> findTotalLicenseDetails(String projectKey, String seriesName) {
		Date dStartDate = DateUtility.addDatePart(Calendar.DATE, -1);
		Date wStartDate = DateUtility.addDatePart(Calendar.DATE, -7);
		Date mStartDate = DateUtility.addDatePart(Calendar.MONTH, -1);
		Date yStartDate = DateUtility.addDatePart(Calendar.YEAR, -1); 
		Date endDate = DateUtility.addDatePart(Calendar.DATE, 0);
		List<LicenseDetails> fResult = new ArrayList<LicenseDetails>();
		List<LicenseDetails> dResult = licenseDetailsRepo.findTotalLicenseDateWise(dStartDate, endDate);
		List<LicenseDetails> wResult = licenseDetailsRepo.findTotalLicenseDateWise(wStartDate, endDate);
		List<LicenseDetails> mResult = licenseDetailsRepo.findTotalLicenseDateWise(mStartDate, endDate);
		List<LicenseDetails> yResult = licenseDetailsRepo.findTotalLicenseDateWise(yStartDate, endDate);
		List<LicenseDetails> sResult = getTotalLicenseDetailsByProjectSinceStart(projectKey);
		LicenseDetails defaultLicense = new LicenseDetails();
		defaultLicense.setProjectKey("project_key");
		defaultLicense.setPeaklicenceCount(0);
		defaultLicense.setTotalPeakLicenseCount(0);
	
		fResult.addAll(dResult);
		if (dResult.size() == 0) {
			fResult.add(defaultLicense);
		}
		
		fResult.addAll(wResult);
		fResult.addAll(mResult);
		fResult.addAll(yResult);
		fResult.addAll(sResult);
		List<Series> result = new ArrayList<Series>();
		result.add(getSeries(fResult, seriesName));
	    return result;
	}
	
	public List<LicenseDetails> findLicenseByProjectAndPeak(@NotNull String projectKey,
			@NotNull String type) {
		Calendar cal = Calendar.getInstance();
		Date enddate = new Date();// NOT USED IN ANY FILTER IGNORE
		if (type.equalsIgnoreCase("monthly")) {
			cal.add(Calendar.MONTH, -1);
		} else if (type.equalsIgnoreCase("weekly")) {
			cal.add(Calendar.DATE, -7);
		} else if (type.equalsIgnoreCase("quarterly")) {
			cal.add(Calendar.MONTH, -3);
		} else if (type.equalsIgnoreCase("yearly")) {
			cal.add(Calendar.YEAR, -1);
		}
		Date startDate = cal.getTime();
		if (type.equalsIgnoreCase("sStart")) {
			startDate = getProjectLastStartDate(projectKey);
		}
		
		int peak = licenseDetailsRepo.findMaxPeakLicenseByProject(projectKey, startDate, enddate);
		System.out.println(peak);
		List<LicenseDetails> fResult = licenseDetailsRepo.findLicenseProjectAndPeak(projectKey, peak, startDate, enddate);
		
		
		return fResult;
	}

	private ChartBean returnChartBean(List<LicenseDetails> result) {
		
		List<Series> series = new ArrayList<Series>();
		List<String> categories = new ArrayList<String>();
		List<Integer> peakCount = new ArrayList<Integer>(); 
		List<Integer> totalPeakCount = new ArrayList<Integer>(); 
		
		Map<String, List<Integer>> projectLicenseCount = result.parallelStream().collect(Collectors.groupingBy(p -> p.projectKey, 
        		Collectors.mapping((p) -> p.peaklicenceCount , Collectors.toList())));
		
		projectLicenseCount.forEach((c,v)->{
			peakCount.addAll(v);
			categories.add(c);
		});
		
		Map<String, List<Integer>> totalLicenseCount = result.parallelStream().collect(Collectors.groupingBy(p -> p.projectKey, 
        		Collectors.mapping((p) -> p.totalPeakLicenseCount , Collectors.toList())));
		totalLicenseCount.forEach((c,v)->{
			totalPeakCount.addAll(v);
		});
		
		series.add(new Series("Project wise Peak Count", peakCount));
		
		series.add(new Series("Total Peak Count", totalPeakCount));
		
	    return new ChartBean(categories, series);
		
	}
	
	private Series getSeries(List<LicenseDetails> result, String seriesName) {
		
		List<Integer> peakCount = new ArrayList<Integer>(); 
		Map<String, List<Integer>> projectLicenseCount = result.parallelStream().collect(Collectors.groupingBy(p -> p.projectKey, 
        		Collectors.mapping((p) -> p.totalPeakLicenseCount , Collectors.toList())));
		
		projectLicenseCount.forEach((c,v)->{
			peakCount.addAll(v);
		});
		
	    return new Series(seriesName, peakCount);
		
	}
	
	private Series getSeriesHostCount(List<LicenseDetails> result, String seriesName) {
		
		List<Integer> peakCount = new ArrayList<Integer>(); 
		Map<String, List<Integer>> projectLicenseCount = result.parallelStream().collect(Collectors.groupingBy(p -> p.projectKey, 
        		Collectors.mapping((p) -> p.peaklicenceCount , Collectors.toList())));
		
		projectLicenseCount.forEach((c,v)->{
			peakCount.addAll(v);
		});
		
	    return new Series(seriesName, peakCount);
		
	}
	
	private List<Series> getListSeriesByHost(List<LicenseDetails> lisenseList) {
		
		List<Series> result = new ArrayList<Series>();
		
		Map<Object, List<LicenseDetails>> projectLicenseCount = lisenseList.parallelStream().collect(Collectors.groupingBy(p -> Pair.of(p.projectKey, p.getHostId()), 
        		Collectors.mapping((p) -> p , Collectors.toList())));
		
		projectLicenseCount.forEach((c,v)->{
			result.add(getSeriesHostCount(v, StringUtils.chop(c.toString().split(",")[1])));
		});
		
	    return result;
		
	}
	
	
	private List<LicenseDetails> getLicenseDetailsBySinceStart() {
		List<LicenseDetails> sResult = new ArrayList<LicenseDetails>();
		List<LicenseDetails> sProjectDateList = licenseDetailsRepo.findStartDateProject();
		for (LicenseDetails licenseDetails : sProjectDateList) {
			sResult.addAll(licenseDetailsRepo.findLicenseProjectAndDateWise(licenseDetails.getProjectKey(), licenseDetails.getDatetime(), new Date()));
		}
	    return sResult;
	}
	
	private List<LicenseDetails> getLicenseDetailsByDate(Date sDate, Date eDate) {
		List<LicenseDetails> sResult = new ArrayList<LicenseDetails>();
		List<LicenseDetails> sProjectDateList = licenseDetailsRepo.findStartDateProject(sDate, eDate);
		for (LicenseDetails licenseDetails : sProjectDateList) {
			sResult.addAll(licenseDetailsRepo.findLicenseProjectAndDateWise(licenseDetails.getProjectKey(), sDate, eDate));
		}
	    return sResult;
	}
	
	private List<LicenseDetails> getLicenseDetailsByProjectSinceStart(String projectKey) {
		List<LicenseDetails> sResult = new ArrayList<LicenseDetails>();
		LicenseDetails sProjectDate = licenseDetailsRepo.findStartDateByProject(projectKey);
		if(sProjectDate != null) {
			sResult = licenseDetailsRepo.findLicenseProjectAndDateWise(projectKey, sProjectDate.getDatetime(), new Date());
		}
	    return sResult;
	}
	
	private List<LicenseDetails> getHostLicenseDetailsByProjectSinceStart(String projectKey) {
		List<LicenseDetails> sResult = new ArrayList<LicenseDetails>();
		LicenseDetails sProjectDate = licenseDetailsRepo.findStartDateByProject(projectKey);
		if(sProjectDate != null) {
			sResult = licenseDetailsRepo.findLicenseProjectByHostAndDateWise(projectKey, sProjectDate.getDatetime(), new Date());
		}
	    return sResult;
	}
	
	private List<LicenseDetails> getTotalLicenseDetailsByProjectSinceStart(String projectKey) {
		List<LicenseDetails> sResult = new ArrayList<LicenseDetails>();
		LicenseDetails sProjectDate = licenseDetailsRepo.findStartDateByProject(projectKey);
		if(sProjectDate != null) {
			sResult = licenseDetailsRepo.findTotalLicenseDateWise(sProjectDate.getDatetime(), new Date());
		}
	    return sResult;
	}
	
	private Date getProjectLastStartDate(String projectKey) {
		LicenseDetails sProjectDate = licenseDetailsRepo.findStartDateByProject(projectKey);
		if(sProjectDate != null) {
			return sProjectDate.getDatetime();
		}
		return new Date();
	}

	public List<LicenseDetails> findLicenseListDetailsByDate(@NotNull String projectKey, Date sDate, Date eDate) {
		eDate =  DateUtility.addDatePart(eDate, Calendar.HOUR, 24);
		int peak = licenseDetailsRepo.findMaxPeakLicenseByProject(projectKey, sDate, eDate);
		List<LicenseDetails> fResult = licenseDetailsRepo.findLicenseProjectAndPeak(projectKey, peak, sDate, eDate);
		return fResult;
	}
	
	public boolean containsHost(final List<LicenseDetails> list, final String name){
	    return list.stream().filter(o -> o.getHostId().equals(name)).findFirst().isPresent();
	}
	
}
