package com.magicsoftware.monitor.model;

import java.util.List;

import com.magicsoftware.xpi.server.data.server.Scheduler;

public class SchedulerDetails {
	
	private Scheduler[] schedulermain;
	//private List<Scheduler []> schedulermain1;
	private List<SchedulerData>  schedulers;
	//private List<Object> combined;
	
	
	
	public Scheduler[] getSchedulermain() {
		return schedulermain;
	}
	public void setSchedulermain(Scheduler[] schedulermain) {
		this.schedulermain = schedulermain;
	}
	public List<SchedulerData>  getScheduler() {
		return schedulers;
	}
	public void setScheduler(List<SchedulerData>  scheduler) {
		this.schedulers = scheduler;
	}


}
