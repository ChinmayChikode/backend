package com.magicsoftware.monitor.model;

import com.magicsoftware.xpi.server.data.server.ServerData;
import com.magicsoftware.xpi.server.data.server.WorkerData;

public class ServerAndWorkerData {

	private ServerData server;
	private WorkerData[] workers;

	public ServerAndWorkerData(ServerData server, WorkerData[] workers) {
		this.server = server;
		this.workers = workers;
	}

	public ServerData getServers() {
		return server;
	}

	public void setServers(ServerData servers) {
		this.server = servers;
	}

	public WorkerData[] getWorkers() {
		return workers;
	}

	public void setWorkers(WorkerData[] workers) {
		this.workers = workers;
	}

}
