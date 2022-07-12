package com.magicsoftware.monitor.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.magicsoftware.monitor.model.ActivityMsgFilterMetadata;
import com.magicsoftware.monitor.model.BpWithFlow;
import com.magicsoftware.monitor.model.FlowDetails;
import com.magicsoftware.monitor.model.FlowWithTrigger;
import com.magicsoftware.monitor.model.MagicXpiHostAndEngines;
import com.magicsoftware.monitor.model.MagicXpiSpaceInstances;
import com.magicsoftware.monitor.model.MessagesDetails;
import com.magicsoftware.monitor.model.MonitorOfflineMetadata;
import com.magicsoftware.monitor.model.PSSWithFLowName;
import com.magicsoftware.monitor.model.QueryFilters;
import com.magicsoftware.monitor.model.SchedulerDetails;
import com.magicsoftware.monitor.model.ServerAndWorkerData;
import com.magicsoftware.monitor.model.ServerDetails;
import com.magicsoftware.monitor.model.ServerInstanceDetails;
import com.magicsoftware.monitor.model.TriggerActivityGraphRes;
import com.magicsoftware.monitor.model.TriggerDetails;
import com.magicsoftware.xpi.server.common.ApplicationException;
import com.magicsoftware.xpi.server.data.Lock;
import com.magicsoftware.xpi.server.data.helpers.Alert;
import com.magicsoftware.xpi.server.data.helpers.StatisticsHistory;
import com.magicsoftware.xpi.server.data.helpers.TriggerActivityMessages;
import com.magicsoftware.xpi.server.data.license.LicenseFeature;
import com.magicsoftware.xpi.server.data.project.BpData;
import com.magicsoftware.xpi.server.data.project.FlowData;
import com.magicsoftware.xpi.server.data.project.ProjectData;
import com.magicsoftware.xpi.server.data.project.StepData;
import com.magicsoftware.xpi.server.data.project.TriggerData;
import com.magicsoftware.xpi.server.data.server.Scheduler;
import com.magicsoftware.xpi.server.data.server.ServerData;
import com.magicsoftware.xpi.server.data.server.WorkerData;
import com.magicsoftware.xpi.server.messages.FlowReqHistory;
import com.magicsoftware.xpi.server.messages.FlowRequest;
import com.magicsoftware.xpi.server.messages.PSSData;

public interface SpaceService {

	public String newServerInstance(String projectkey, String Server_host, String Alternate_host,
			 String project_directory, int Number_of_instances, Boolean Load_triggers,
		     Boolean Load_Schedulers, Boolean Load_auto_start, int Number_of_workers);
	
	public FlowDetails flow(String projectKey, String projectLocation);

	public ProjectData[] runningProject();

	public WorkerData[] worker();
	
	public WorkerData[] worker_practice();

	public ServerData[] servers(String projectKey);

	public ServerDetails serversByProject(String projectkey);

	public TriggerData[] triggers(String projectkey);

	public TriggerDetails triggersByProject(String projectkey, String projectLocation);

	public FlowRequest[] flowReqList();

	public List<FlowWithTrigger> flowList(String projectkey);

	public BpData[] bpList(String projectkey);

	public Lock[] getWorkersLocks();

	public Lock[] getWorkersLocks(String projectkey);

	public Scheduler[] getScheduler();
	
	public SchedulerDetails getscheduler(String projectKey, String projectLocation);
	
	public int invokeFlowByScheduler(String projectKey, int bpId, int flowId, int triggerId, int schedulerId) throws InstantiationException, IllegalAccessException, ApplicationException;

	public PSSData[] getPSSData(String projectKey);

	public FlowReqHistory[] getFlowReqHistory();

	public FlowReqHistory[] getFlowReqHistory(int projectkey);

	public Alert[] getAlerts();

	public StepData[] getSteps();

	public boolean canWorkProjectRunning(String projectKey) throws ApplicationException;

	public boolean canWorkEnable(String projectKey, long bpId, long flowId);

	public boolean canWorkBufferLimitation(String projectKey, long bpId, long flowId, long triggerStepId);

	public int stopProject(String projectKey, int timeout);

	public LicenseFeature[] getLicenseInfo();

	public List<StatisticsHistory> getMessageGraphDataPerProject();

	public List<ServerAndWorkerData> workerByServer(String projectKey);

	public List<BpWithFlow> bpWithFlows(String projectkey);

	public TriggerData[] getTriggersByFlow(String projectkey, long flowID);

	public Lock[] getLocks(String projectkey);

	public MessagesDetails getAllMessages(String projectKey, String projectLocation, int thresholdMinutes);

	public List<TriggerActivityMessages> getTriggersActivityStatistics(String projectkey);

	public String readWaitingMessages(String projectkey, int thresholdMinutes);

	public FlowRequest[] getAllMessages();
	
	public List<MagicXpiSpaceInstances> getSpaceInstances();

	public Map<String, List<MagicXpiHostAndEngines>> getHostAndEnginesDetails();

	public MessagesDetails getAllMessages(String projectKey, int thresholdMinutes);

	public MonitorOfflineMetadata loadProjectMetadata(String projectKey, String projectLocation);

	public List<ActivityMsgFilterMetadata> loadActMsgFilterMetadata(QueryFilters queryFilters);

	public String writeActivityLogMsgFilters(QueryFilters queryFilters);

	Alert[] getAlertsByProject(@NotNull String projectKey);

	Object getFlowReqHistory(String projectkey, String filterType);

	TriggerActivityGraphRes getTriggersActivityStatistics(String projectkey, String filterType);

	Object getSummary(@NotNull String projectkey);

	List<PSSWithFLowName> getPSSDataWithFLowName(String projectKey, String projectLocation);

}
