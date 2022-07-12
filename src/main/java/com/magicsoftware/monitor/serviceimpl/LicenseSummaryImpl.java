package com.magicsoftware.monitor.serviceimpl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magicsoftware.monitor.model.LicenseSummary;
import com.magicsoftware.monitor.model.WorkerHistory;
import com.magicsoftware.monitor.repository.LicenseDetailsRepo;
import com.magicsoftware.monitor.util.WorkerlHistoryCatche;

@Service
public class LicenseSummaryImpl {

	@Autowired
	private LicenseDetailsRepo licenseDetailsRepo;
	private LocalDateTime dateTime = LocalDateTime.now();

	public List<com.magicsoftware.monitor.model.LicenseSummary> findLicenseDetails(@NotNull String projectKey,
			@NotNull String type) {
		// String[] dates = getBetweenDates(type);
		java.sql.Date startDate = java.sql.Date.valueOf("2020-03-29"); // NOT USED IN ANY FILTER IGNORE
		java.sql.Date enddate = java.sql.Date.valueOf("2020-03-29");// NOT USED IN ANY FILTER IGNORE
		if (type.equalsIgnoreCase("DAY")) {
			return licenseDetailsRepo.findLicenseDayWise(projectKey, startDate, enddate);
		} else if (type.equalsIgnoreCase("MONTH")) {
			return licenseDetailsRepo.findLicenseMonthWise(projectKey, startDate, enddate);
		} else if (type.equalsIgnoreCase("YEAR")) {
			return licenseDetailsRepo.findLicenseYearWise(projectKey, startDate, enddate);
		}
		return null;
	}

	public HashMap<String, Object> findByProjectKeyAndDateCreated(String projectKey, String filterType) {
		dateTime = LocalDateTime.now();
		// TODO :- GANESH REMOVE HARD CODED FILTER

		int skipSeconds = 0;
		if (filterType.equalsIgnoreCase("10_MINUTE")) {
			dateTime = LocalDateTime.now().minus(Duration.of(10, ChronoUnit.MINUTES));
		} else if (filterType.equalsIgnoreCase("30_MINUTE")) {
			dateTime = LocalDateTime.now().minus(Duration.of(30, ChronoUnit.MINUTES));
			skipSeconds = 100;
		} else if (filterType.equalsIgnoreCase("1_HOUR")) {
			dateTime = LocalDateTime.now().minus(Duration.of(1, ChronoUnit.HOURS));
			skipSeconds = 150;
		} else if (filterType.equalsIgnoreCase("1_DAY")) {
			dateTime = LocalDateTime.now().minus(Duration.of(1, ChronoUnit.DAYS));
			skipSeconds = 500;
		} else if (filterType.equalsIgnoreCase("3_DAY")) {
			dateTime = LocalDateTime.now().minus(Duration.of(3, ChronoUnit.DAYS));
			skipSeconds = 600;
		}

		List<WorkerHistory> workerCatche = null;
		try {
			workerCatche = WorkerlHistoryCatche.getInstance(projectKey).getClient().get(projectKey);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		List<WorkerHistory> workerCatcheHistory = workerCatche.stream()
				.filter(data -> data.getDatetime().isAfter(dateTime)).collect(Collectors.toList());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		List<Integer> projectLicense = new ArrayList<Integer>();
		List<Integer> worker = new ArrayList<Integer>();
		List<String> time = new ArrayList<String>();
		LocalDateTime lastDateTime = null;
		int lastcount = 0;

		for (int i = 0; i < workerCatcheHistory.size(); i++) {
			WorkerHistory data = workerCatcheHistory.get(i);
			if (null == lastDateTime) {
				lastcount = data.getRunning();
				lastDateTime = data.getDatetime();
			}
			if (workerCatcheHistory.size() > 22) {
				long seconds = ChronoUnit.SECONDS.between(lastDateTime, data.getDatetime());
				if (seconds > skipSeconds || data.getRunning() > lastcount) {
					lastDateTime = data.getDatetime();
					lastcount = data.getRunning();
				} else {
					continue;
				}
			}
			projectLicense.add(data.getRunning());
			worker.add(data.getTotal());
			time.add(data.getDatetime().format(formatter));
		}

			String lastValue = "";
			for (int i = 0; i < time.size(); i++) {
				if (lastValue.equalsIgnoreCase(time.get(i))) {
					lastValue = time.get(i);
					time.set(i, "");
				}
				if (lastValue.isEmpty())
					lastValue = time.get(i);
			}

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("projectLicense", projectLicense);
		response.put("time", time);
		response.put("worker", worker);
		return response;
	}

	public static <T> Collector<T, ?, List<T>> lastN(int n) {
		return Collector.<T, Deque<T>, List<T>>of(ArrayDeque::new, (acc, t) -> {
			if (acc.size() == n)
				acc.pollFirst();
			acc.add(t);
		}, (acc1, acc2) -> {
			while (acc2.size() < n && !acc1.isEmpty()) {
				acc2.addFirst(acc1.pollLast());
			}
			return acc2;
		}, ArrayList::new);
	}
}
