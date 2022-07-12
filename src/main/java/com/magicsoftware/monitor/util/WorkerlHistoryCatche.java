package com.magicsoftware.monitor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.magicsoftware.monitor.model.WorkerHistory;

public class WorkerlHistoryCatche {

	private static WorkerlHistoryCatche inst = null;

	private LoadingCache<String, List<WorkerHistory>> workerCatche;

	public static WorkerlHistoryCatche getInstance(String projectKey) {
		if (null == inst)
			inst = new WorkerlHistoryCatche(projectKey);
		return inst;
	}

	WorkerlHistoryCatche(String projectKey) {
		workerCatche = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.DAYS)
				.build(new CacheLoader<String, List<WorkerHistory>>() {
					@Override
					public List<WorkerHistory> load(String projectKey) throws Exception {
						return new ArrayList<WorkerHistory>();
					}
				});
	}

	public synchronized LoadingCache<String, List<WorkerHistory>> getClient() {
		return workerCatche;
	}
}
