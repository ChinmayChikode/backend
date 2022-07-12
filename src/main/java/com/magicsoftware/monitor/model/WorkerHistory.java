package com.magicsoftware.monitor.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WorkerHistory {

	private String projectKay;
	private int running;
	private int total;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime datetime;

	
	public WorkerHistory(String projectKay, int running, int total, LocalDateTime datetime) {
		super();
		this.projectKay = projectKay;
		this.running = running;
		this.total = total;
		this.datetime = datetime;
	}

	public String getProjectKay() {
		return projectKay;
	}

	public void setProjectKay(String projectKay) {
		this.projectKay = projectKay;
	}

	public int getRunning() {
		return running;
	}

	public void setRunning(int running) {
		this.running = running;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

}
