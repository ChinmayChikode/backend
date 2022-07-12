package com.magicsoftware.monitor.serviceimpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.json.JSONObject;
import org.openspaces.admin.gsa.GridServiceAgent;
import org.openspaces.admin.gsa.GridServiceAgents;
import org.openspaces.admin.gsc.GridServiceContainer;
import org.openspaces.admin.gsc.GridServiceContainers;
import org.openspaces.admin.gsm.GridServiceManager;
import org.openspaces.admin.gsm.GridServiceManagers;
import org.openspaces.admin.internal.admin.DefaultAdmin;
import org.openspaces.admin.lus.LookupService;
import org.openspaces.admin.lus.LookupServices;
import org.openspaces.admin.machine.Machine;
import org.openspaces.admin.os.OperatingSystem;
import org.openspaces.admin.os.OperatingSystemDetails;
import org.openspaces.admin.os.OperatingSystemStatistics;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.admin.pu.ProcessingUnitInstance;
import org.openspaces.admin.space.SpaceInstance;
import org.openspaces.admin.vm.VirtualMachine;
import org.openspaces.admin.vm.VirtualMachineStatistics;
import org.openspaces.admin.zone.Zone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.cache.LoadingCache;
import com.magicsoftware.monitor.model.ActivityMsgFilterMetadata;
import com.magicsoftware.monitor.model.BpModel;
import com.magicsoftware.monitor.model.BpWithFlow;
import com.magicsoftware.monitor.model.FlowDetails;
import com.magicsoftware.monitor.model.FlowModel;
import com.magicsoftware.monitor.model.FlowRequestMessages;
import com.magicsoftware.monitor.model.FlowWithTrigger;
import com.magicsoftware.monitor.model.HostModel;
import com.magicsoftware.monitor.model.LicenseModel;
import com.magicsoftware.monitor.model.MagicXpiHostAndEngines;
import com.magicsoftware.monitor.model.MagicXpiSpaceInstances;
import com.magicsoftware.monitor.model.MessagesDetails;
import com.magicsoftware.monitor.model.MonitorOfflineMetadata;
import com.magicsoftware.monitor.model.PSSWithFLowName;
import com.magicsoftware.monitor.model.QueryFilters;
import com.magicsoftware.monitor.model.SchedulerData;
import com.magicsoftware.monitor.model.SchedulerDetails;
import com.magicsoftware.monitor.model.ServerAndWorkerData;
import com.magicsoftware.monitor.model.ServerDetails;
import com.magicsoftware.monitor.model.ServerInstance;
import com.magicsoftware.monitor.model.ServerInstanceDetails;
import com.magicsoftware.monitor.model.TriggerActivityGraphRes;
import com.magicsoftware.monitor.model.TriggerActivityModel;
import com.magicsoftware.monitor.model.TriggerDetails;
import com.magicsoftware.monitor.model.WorkerHistory;
import com.magicsoftware.monitor.service.SpaceService;
import com.magicsoftware.monitor.util.MagicMonitorUtilities;
import com.magicsoftware.monitor.util.WorkerlHistoryCatche;
import com.magicsoftware.xpi.info.entities.RTViewEntity;
import com.magicsoftware.xpi.server.common.ApplicationException;
import com.magicsoftware.xpi.server.common.SpaceDiscoveryObject;
import com.magicsoftware.xpi.server.data.GlobalSettings;
import com.magicsoftware.xpi.server.data.Lock;
import com.magicsoftware.xpi.server.data.helpers.Alert;
import com.magicsoftware.xpi.server.data.helpers.StatisticsHistory;
import com.magicsoftware.xpi.server.data.helpers.TriggerActivityMessages;
import com.magicsoftware.xpi.server.data.license.LicenseFeature;
import com.magicsoftware.xpi.server.data.project.BpData;
import com.magicsoftware.xpi.server.data.project.FlowData;
import com.magicsoftware.xpi.server.data.project.ProjectData;
import com.magicsoftware.xpi.server.data.project.ProjectState;
import com.magicsoftware.xpi.server.data.project.StepData;
import com.magicsoftware.xpi.server.data.project.TriggerData;
import com.magicsoftware.xpi.server.data.server.FlowSequenceData;
import com.magicsoftware.xpi.server.data.server.Scheduler;
import com.magicsoftware.xpi.server.data.server.ServerData;
import com.magicsoftware.xpi.server.data.server.WorkerData;
import com.magicsoftware.xpi.server.entities.RTVEntity;
import com.magicsoftware.xpi.server.entities.RTVEntity.MonitorLicenseState;
import com.magicsoftware.xpi.server.messages.FlowReqHistory;
import com.magicsoftware.xpi.server.messages.FlowRequest;
import com.magicsoftware.xpi.server.messages.PSSData;
import com.magicsoftware.xpi.server.spacebridge.spaceservices.Admin;
import com.magicsoftware.xpi.server.spacebridge.spaceservices.LicenseServices;
import com.magicsoftware.xpi.server.spacebridge.spaceservices.SpaceServices;

@Service
public class SpaceServiceImpl implements SpaceService {

	private MonitorLicenseState monitorLicenseState = MonitorLicenseState.UnInitialized;
	private static final int SCHEDULER_BUFFER = 1;

	private SpaceServices spaceServices = null;
	private SpaceDiscoveryObject spaceDiscoveryObject;
	public static RTViewEntity rtvInfoEntityObject;
	public static RTVEntity rtvEntity;
	private Admin admin;

	public static MonitorOfflineMetadata monitorOfflineMetadata = null;
	public static List<ActivityMsgFilterMetadata> activityMsgFilterMetadata = null;

	@Value("${magic.space.name}")
	private String spaceName;

	@Value("${magic.space.group}")
	private String spaceGroup;

	@Value("${magic.space.locators}")
	private String spaceLocators;

	@PostConstruct
	public void connect() {
		rtvEntity = new RTVEntity();
		rtvEntity.connect(spaceName, spaceGroup, spaceLocators);
		rtvEntity.isMonitorLicensed();
		spaceServices = new SpaceServices();
		spaceDiscoveryObject = new SpaceDiscoveryObject("Magic_xpi_4.13_CHINMAYCLPT",
				"localhost");
		admin = new Admin(spaceDiscoveryObject);
		rtvInfoEntityObject = getRTVEntityInfoInstance();
	}

	public synchronized static RTViewEntity getRTVEntityInfoInstance() {
		if (rtvInfoEntityObject == null)
			rtvInfoEntityObject = new RTViewEntity();
		return rtvInfoEntityObject;
	}

	@Override
	public FlowDetails flow(String projectKey, String projectLocation) {

		FlowDetails flowDetails = new FlowDetails();
		FlowData[] flowData = spaceServices.getFlowsDataCommited(projectKey);

		/*
		 * MonitorOfflineMetadata monitorOfflineMetadata =
		 * MagicMonitorUtilities.readMonitorOfflineMetadata(projectKey,
		 * projectLocation);
		 */

		FlowLoop: for (int i = 0; i < flowData.length; i++) {

			BPLoop: for (int j = 0; j < monitorOfflineMetadata.getBpList().size(); j++) {
//				if (monitorOfflineMetadata.getBpList().get(j).getBpId() != null && flowData[i].getBpId() == Integer
//						.valueOf(monitorOfflineMetadata.getBpList().get(j).getBpId())) {
//
//					flowData[i].setBpName("[" + flowData[i].getBpId() + "]" + " "
//							+ monitorOfflineMetadata.getBpList().get(j).getBpName());
//					flowData[i].setFlowName("[" + flowData[i].getFlowId() + "]" + " " + flowData[i].getFlowName());
//
//				}
				if (monitorOfflineMetadata.getBpList().get(j).getBpId() != null && flowData[i].getBpId() == Integer
						.valueOf(monitorOfflineMetadata.getBpList().get(j).getBpId())) {

					flowData[i].setBpName(monitorOfflineMetadata.getBpList().get(j).getBpName());
					flowData[i].setFlowName(flowData[i].getFlowName());

				}
			}
		}

		flowDetails.setBpList(monitorOfflineMetadata.getBpList());
		flowDetails.setFlowList(monitorOfflineMetadata.getFlowList());
		flowDetails.setFlowData(flowData);
		return flowDetails;
	}

	@Override
	public ProjectData[] runningProject() {
		return spaceServices.getProjects();
	}

	@Override
	public List<StatisticsHistory> getMessageGraphDataPerProject() {
		long currentDate = new Date().getTime();
		long fromDate = currentDate - 86400000;
		return spaceServices.getStatisticsMessages(fromDate, currentDate);
	}

	@Override
	public WorkerData[] worker() {
		return spaceServices.getAllWorkers(1);
	}
	
	@Override
	public WorkerData[] worker_practice() {
		return spaceServices.getAllWorkers(1);
		//return null;
	}

	@Override
	public List<ServerAndWorkerData> workerByServer(String projectKey) {
		ServerData[] servers = new SpaceServices().getAllServers(projectKey);
		List<ServerAndWorkerData> serverAndWorkerList = new ArrayList<>();

		/*
		 * MonitorOfflineMetadata monitorOfflineMetadata =
		 * MagicMonitorUtilities.readMonitorOfflineMetadata(projectKey,
		 * "c:\\Users\\sudeepm\\Documents\\Magic\\Projects\\Project1");
		 */

		for (ServerData server : servers) {

			WorkerData[] workerDatas = spaceServices.getAllWorkers(server.getServerId());

			for (int i = 0; i < workerDatas.length; i++) {

				workerDatas[i].setWrkrCreatedTimestamp(
						MagicMonitorUtilities.formatDate(workerDatas[i].getCreatedTimestamp()));
				workerDatas[i].setWrkrLastAliveTimestamp(
						MagicMonitorUtilities.formatDate(workerDatas[i].getWorkerLastIsAlive()));
				workerDatas[i].setDisplayWrkrUpTime(
						MagicMonitorUtilities.calculateUpTime(workerDatas[i].getCreatedTimestamp()));

				for (int j = 0; j < monitorOfflineMetadata.getStepList().size(); j++) {

					ArrayList<FlowSequenceData> flowSequenceData = workerDatas[i].getFullRuntimeTree();

					if (flowSequenceData.size() > 0 && !flowSequenceData.isEmpty()) {

						if (flowSequenceData.get(0).getStepId() != null) {
							int stepId1 = flowSequenceData.get(0).getStepId();
							int stepId2 = Integer.valueOf(monitorOfflineMetadata.getStepList().get(j).getStepId());

							if (stepId1 == stepId2) {

								flowSequenceData.get(0)
										.setStepName(monitorOfflineMetadata.getStepList().get(j).getStepName());

							}
						}

						workerDatas[i].setFlowRuntimeTree(flowSequenceData);

					}
				}
				/*
				 * BPLoop: for (int j = 0; j < monitorOfflineMetadata.getBpList().size(); j++) {
				 * 
				 * ArrayList<FlowSequenceData> flowSequenceData =
				 * workerDatas[i].getFullRuntimeTree();
				 * 
				 * if (flowSequenceData.size() > 0 && !flowSequenceData.isEmpty()) {
				 * 
				 * if (flowSequenceData.get(j).getBpID() == Long
				 * .valueOf(monitorOfflineMetadata.getBpList().get(j).getBpId())) {
				 * 
				 * FlowLoop: for (int k = 0; k < monitorOfflineMetadata.getFlowList().size();
				 * k++) {
				 * 
				 * if (flowSequenceData.get(k).getFlowID() == Long
				 * .valueOf(monitorOfflineMetadata.getFlowList().get(k).getFlowId())) {
				 * 
				 * StepLoop: for (int l = 0; l < monitorOfflineMetadata.getStepList().size();
				 * l++) {
				 * 
				 * flowSequenceData.get(k)
				 * .setStepName(monitorOfflineMetadata.getStepList().get(l).getStepName());
				 * 
				 * }
				 * 
				 * } }
				 * 
				 * } } }
				 */

			}
			serverAndWorkerList.add(new ServerAndWorkerData(server, workerDatas));
		}
		return serverAndWorkerList;
	}

	@Override
	public ServerData[] servers(String projectKey) {
		return spaceServices.getAllServers(projectKey);
	}

	@Override
	public LicenseFeature[] getLicenseInfo() {
		return spaceServices.getLicenseFeaturesCommited();
	}

	@Override
	public ServerDetails serversByProject(String projectkey) {

		ServerDetails serverDetails = new ServerDetails();
		ServerData[] serverData = spaceServices.getAllServers(projectkey);
		List<HostModel> hostList = null;
		List<LicenseModel> licenseFeatureList = null;
		int totalmessageprocessed=spaceServices.totalRequestServed(projectkey);
		
		if (serverData != null) {
			
			hostList = new ArrayList<HostModel>();
			HostModel hostModel = new HostModel();
			hostModel.setHostId(null);
			hostModel.setHostName("ALL");
			hostList.add(hostModel);
			
			licenseFeatureList = new ArrayList<LicenseModel>();
			LicenseModel licenseModel = new LicenseModel();
			licenseModel.setLicenseId(null);
			licenseModel.setLicenseType("ALL");
			licenseFeatureList.add(licenseModel);
			
			for (int i = 0; i < serverData.length; i++) {

				hostModel = new HostModel();
				hostModel.setHostId(String.valueOf(i));
				hostModel.setHostName(serverData[i].getPrimaryHost());
				
				licenseModel = new LicenseModel();
				licenseModel.setLicenseId(String.valueOf(i));
				licenseModel.setLicenseType(serverData[i].getLicenseFeature());
				
				serverData[i].setStartReqTime(MagicMonitorUtilities.formatDate(serverData[i].getCreatedTimestamp()));
				serverData[i].setLastUpdtReqTime(MagicMonitorUtilities.formatDate(serverData[i].getLastUpdated()));
				
				hostList.add(hostModel);
				licenseFeatureList.add(licenseModel);
				

			}
		}
		
		serverDetails.setHostList(hostList);
		serverDetails.setStatusList(MagicMonitorUtilities.getServerStatuses());
		serverDetails.setLicenseFeatureList(licenseFeatureList);
		serverDetails.setServerData(serverData);
		serverDetails.setTotalmessagesprocessed(totalmessageprocessed);
		
		return serverDetails;
	}


	@Override
	public TriggerData[] triggers(String projectkey) {
		return spaceServices.getAllTriggers(projectkey);
	}

	@Override
	public TriggerData[] getTriggersByFlow(String projectkey, long flowID) {
		return spaceServices.getTriggersByFlow(projectkey, flowID);
	}

	@Override
	public TriggerDetails triggersByProject(String projectKey, String projectLocation) {

		TriggerDetails triggerDetails = new TriggerDetails();
		TriggerData[] triggerData = spaceServices.getAllTriggers(projectKey);

		/*
		 * MonitorOfflineMetadata monitorOfflineMetadata =
		 * MagicMonitorUtilities.readMonitorOfflineMetadata(projectKey,
		 * projectLocation);
		 */

		TriggerLoop: for (int i = 0; i < triggerData.length; i++) {

			triggerData[i].setStartedAtDate(MagicMonitorUtilities.formatDate(triggerData[i].getCreatedTimestamp()));
			triggerData[i].setLastMessagedAtDate(MagicMonitorUtilities.formatDate(triggerData[i].getLastIsAlive()));
			triggerData[i].setTriggerType(MagicMonitorUtilities.getTriggerType(triggerData[i].getTriggerTypeId()));

			if(SpaceServiceImpl.monitorOfflineMetadata != null && SpaceServiceImpl.monitorOfflineMetadata.getBpList() != null) {
				BPLoop: for (int j = 0; j < monitorOfflineMetadata.getBpList().size(); j++) {

					if (monitorOfflineMetadata.getBpList().get(j).getBpId() != null && triggerData[i].getBpId() == Long
							.valueOf(monitorOfflineMetadata.getBpList().get(j).getBpId())) {

						triggerData[i].setBpName(monitorOfflineMetadata.getBpList().get(j).getBpName());

						FlowLoop: for (int k = 0; k < monitorOfflineMetadata.getFlowList().size(); k++) {

							if (monitorOfflineMetadata.getFlowList().get(k).getFlowId() != null && triggerData[i]
									.getFlowId() == Long.valueOf(monitorOfflineMetadata.getFlowList().get(k).getFlowId())) {

								triggerData[i].setFlowName(monitorOfflineMetadata.getFlowList().get(k).getFlowName());

								continue BPLoop;
							}
						}

					}
				}
			}

		}
		if(SpaceServiceImpl.monitorOfflineMetadata != null && SpaceServiceImpl.monitorOfflineMetadata.getBpList() != null) {
			triggerDetails.setBpList(monitorOfflineMetadata.getBpList());
		}
		if(SpaceServiceImpl.monitorOfflineMetadata != null && SpaceServiceImpl.monitorOfflineMetadata.getFlowList()!= null) {
			triggerDetails.setFlowList(monitorOfflineMetadata.getFlowList());
		}
		
		triggerDetails.setTriggerTypes(MagicMonitorUtilities.getTriggerTypes());
		triggerDetails.setTriggerStates(MagicMonitorUtilities.getTriggerStates());
		triggerDetails.setTriggerData(triggerData);	

		return triggerDetails;
	}

	@Override
	public List<TriggerActivityMessages> getTriggersActivityStatistics(String projectkey) {
		return spaceServices.getTriggersActivityStatistics(projectkey);
	}

	@Override
	public FlowRequest[] flowReqList() {
		return spaceServices.getAllMessages();
	}

	@Override
	public FlowRequest[] getAllMessages() {
		return spaceServices.getAllMessages();
	}

	@Override
	public List<FlowWithTrigger> flowList(String projectkey) {
		List<FlowWithTrigger> flowWithTrigger = new ArrayList<>();
		FlowData[] flowData = spaceServices.getFlowsDataCommited(projectkey);
		for (FlowData flow : flowData) {
			flowWithTrigger
					.add(new FlowWithTrigger(flow, spaceServices.getTriggersByFlow(projectkey, flow.getFlowId())));
		}
		return flowWithTrigger;
	}

	@Override
	public List<BpWithFlow> bpWithFlows(String projectkey) {
		BpData[] bpList = spaceServices.getBpDataCommited(projectkey);
		List<BpWithFlow> bpWithFlow = new ArrayList<>();
		for (BpData bp : bpList) {
			FlowData[] flowData = spaceServices.getFlowsDataCommited(projectkey, bp.getBpId());
			bpWithFlow.add(new BpWithFlow(bp, flowData));
		}
		return bpWithFlow;
	}

	@Override
	public BpData[] bpList(String projectkey) {
		return spaceServices.getBpDataCommited(projectkey);
	}

	@Override
	public Lock[] getWorkersLocks() {
		return spaceServices.getWorkersLocks();
	}

	@Override
	public MessagesDetails getAllMessages(String projectKey, String projectLocation, int thresholdMinutes) {

		MessagesDetails message = new MessagesDetails();
		FlowRequest[] flowRequest = spaceServices.getAllMessages(projectKey);
		message.setTotalMessages(flowRequest.length);
		message.setWaitingMessages(spaceServices.readWaitingMessages(projectKey, thresholdMinutes));
		message.setFailledMessages(spaceServices.getFailledMessagesStatus(projectKey));
		message.setInProcessMessages(spaceServices.getInProcessMessages(projectKey));
		message.setMessages(spaceServices.getAllMessages(projectKey));

		/*
		 * MonitorOfflineMetadata monitorOfflineMetadata =
		 * MagicMonitorUtilities.readMonitorOfflineMetadata(projectKey,
		 * projectLocation);
		 */
		MessageLoop: for (int i = 0; i < flowRequest.length; i++) {

			flowRequest[i]
					.setDisplayFormattedDate(MagicMonitorUtilities.formatDate(flowRequest[i].getCreatedTimestamp()));
			flowRequest[i].setInvokeCompType(
					MagicMonitorUtilities.getInvocationType(flowRequest[i].getInvokeComponentType()));

			BPLoop: for (int j = 0; j < monitorOfflineMetadata.getBpList().size(); j++) {
				if (monitorOfflineMetadata.getBpList().get(j).getBpId() != null && flowRequest[i].getBpId() == Integer
						.valueOf(monitorOfflineMetadata.getBpList().get(j).getBpId())) {
					flowRequest[i].setBpName("[" + monitorOfflineMetadata.getBpList().get(j).getBpId() + "]" + " "
							+ monitorOfflineMetadata.getBpList().get(j).getBpName());
					// flowRequest[i].setBpName(monitorOfflineMetadata.getBpList().get(j).getBpName());
					FlowLoop: for (int k = 0; k < monitorOfflineMetadata.getFlowList().size(); k++) {
						if (monitorOfflineMetadata.getFlowList().get(k).getFlowId() != null
								&& flowRequest[i].getFlowId() == Integer
										.valueOf(monitorOfflineMetadata.getFlowList().get(k).getFlowId())) {
							flowRequest[i].setFlowName("[" + monitorOfflineMetadata.getFlowList().get(k).getFlowId()
									+ "]" + " " + monitorOfflineMetadata.getFlowList().get(k).getFlowName());
							continue BPLoop;
						}
					}
				}
			}
		}

		/*
		 * for (int i = 0; i < flowRequest.length; i++) { for(int j = 0; j <
		 * monitorOfflineMetadata.getFlowList().size(); j++) {
		 * if(flowRequest[i].getFlowId() ==
		 * Integer.valueOf(monitorOfflineMetadata.getFlowList().get(j).getFlowId())) {
		 * flowRequest[i].setFlowName(monitorOfflineMetadata.getFlowList().get(j).
		 * getFlowName()); } } }
		 * 
		 * for (int i = 0; i < flowRequest.length; i++) { for(int j = 0; j <
		 * monitorOfflineMetadata.getStepList().size(); j++) {
		 * if(flowRequest[i].getStepId() ==
		 * Integer.valueOf(monitorOfflineMetadata.getStepList().get(j).getStepId())) {
		 * flowRequest[i].setStepName(monitorOfflineMetadata.getStepList().get(j).
		 * getStepName()); } } }
		 */
		/*
		 * if(message.getBpList().size() > 0) { message.getBpList().clear(); }
		 * if(message.getFlowList().size() > 0) { message.getFlowList().clear(); }
		 */		
		message.setMsgStatus(MagicMonitorUtilities.getMsgStatuses());
		message.setInvocationType(MagicMonitorUtilities.getMsgInvoTypes());
		message.setBpList(monitorOfflineMetadata.getBpList());
		message.setFlowList(monitorOfflineMetadata.getFlowList());
		message.setMessages(flowRequest);

		return message;
	}

	@Override
	public Lock[] getWorkersLocks(String projectkey) {
		return spaceServices.getWorkersLocks();
	}

	@Override
	public Lock[] getLocks(String projectkey) {
		return spaceServices.getLocks(projectkey);
	}

	@Override
	public Scheduler[] getScheduler() {
		return spaceServices.getSchedulers("");
	}
	
	@Override
	public SchedulerDetails getscheduler(String projectKey, String projectLocation) 
	   {
			//SchedulerArray schedulerfinalarray=new SchedulerArray();
			SchedulerDetails schedulerdetails = new SchedulerDetails();
	    	Scheduler [] schedulers=spaceServices.getSchedulers("");	
	    	
	    	//SchedulerData schedulerData =new SchedulerData();
	    	List<Scheduler []> SchedulerList=new ArrayList<>();
	    	SchedulerList.add(schedulers);

	    	List<SchedulerData> SchedulerDataList=new ArrayList<>();
	    	
	    	MonitorOfflineMetadata monitorOfflineMetadata =
	    			  MagicMonitorUtilities.readMonitorOfflineMetadata(projectKey,
	    			  projectLocation);
	    	
	        for (int i = 0; i < schedulers.length; i++) {
	        			SchedulerData schedulerChild=new SchedulerData();
	        			schedulerChild.setBpId(schedulers[i].getBpId());
	    	        	schedulerChild.setFlowId(schedulers[i].getFlowId());
	    	        	schedulerChild.setServerId(schedulers[i].getServerId());
	    	        	schedulerChild.setIsn(schedulers[i].getIsn());
	    	        	schedulerChild.setType(schedulers[i].getType());
	    	        	schedulerChild.setExecDataTime(schedulers[i].getExecDataTime());
	    	        	schedulerChild.setSchedulerId(schedulers[i].getSchedulerId());
	    	        	schedulerChild.setStepId(schedulers[i].getStepId());
	    	        	schedulerChild.setVersionKey(schedulers[i].getVersionKey());
	    	        	schedulerChild.setSchedulerName(schedulers[i].getSchedulerName());
	    	        	schedulerChild.setUid(schedulers[i].getUid());	        
	    	        	  try {
	  						schedulerChild.setExecDateTimeOrg(MagicMonitorUtilities.changeStringtoDatefinal(schedulers[i].getExecDateTimeOrg()));
	  					} catch (ParseException e) {
	  						// TODO Auto-generated catch block
	  						e.printStackTrace();
	  					}
	    	        	
	    	        	
				BPLoop: for (int j = 0; j <monitorOfflineMetadata.getBpList().size(); j++) {
					if (monitorOfflineMetadata.getBpList().get(j).getBpId() != null && schedulers[i].getBpId() == Long
							.valueOf(monitorOfflineMetadata.getBpList().get(j).getBpId())) {
					
		 			    
						
						schedulerChild.setBpName(monitorOfflineMetadata.getBpList().get(j).getBpId()+ " "
								+ monitorOfflineMetadata.getBpList().get(j).getBpName());
						schedulerChild.setBpName(monitorOfflineMetadata.getBpList().get(j).getBpName());
						
						FlowLoop: for (int k = 0; k < monitorOfflineMetadata.getFlowList().size(); k++) {
							if (monitorOfflineMetadata.getFlowList().get(k).getFlowId() != null
									&& schedulers[i].getFlowId() == Long
											.valueOf(monitorOfflineMetadata.getFlowList().get(k).getFlowId())) {
								schedulerChild.setFlowName(monitorOfflineMetadata.getFlowList().get(k).getFlowName());
							
								continue BPLoop;
							}
						}
					}
				}
	    		SchedulerDataList.add(schedulerChild);
	    	}
	        

	        schedulerdetails.setScheduler(SchedulerDataList);
		    schedulerdetails.setSchedulermain(schedulers);
		    //schedulerdetails.setCombined(combined);
		    
		    return schedulerdetails;  	
	   }
	
	@Override
	public int invokeFlowByScheduler(String projectKey, int bpId, int flowId, int triggerId, int schedulerId) throws InstantiationException, IllegalAccessException, ApplicationException {	
		
	return rtvEntity.invokeFlowByScheduler(projectKey,bpId,flowId,triggerId,schedulerId);
	}
	


//	@Override
//	public PSSData[] getPSSData(String projectKey) {
//		return spaceServices.getProjectPssData(projectKey);
//	}
	
	//New method 1 for subscription data
		//@Override
		public List<PSSWithFLowName> getPSSDataWithFLowName(String projectKey, String projectLocation) {
			
			  MonitorOfflineMetadata monitorOfflineMetadata =
			  MagicMonitorUtilities.readMonitorOfflineMetadata(projectKey,projectLocation);
			
			 List<BpModel> bpList = monitorOfflineMetadata.getBpList();
			 List<FlowModel> flowList = monitorOfflineMetadata.getFlowList();
			 
			 PSSData[] data = spaceServices.getProjectPssData(projectKey);
			 List<PSSWithFLowName> pssData = new ArrayList<PSSWithFLowName>();
			 
		 
			 for(PSSData item:data) {

				 PSSWithFLowName newdata = new PSSWithFLowName();
				 String  uid = item.getUid();
				 newdata.setUid(uid);
				 
				 String  pk = item.getProjectKey();
				 newdata.setProjectKey(pk);
				 
				 int  bpid = item.getBpId();
				 newdata.setBpId(bpid);
				 
				 int fid = item.getFlowId();
				 newdata.setFlowId(fid);
				 
				 String  pss = item.getPssName();
				 newdata.setPssName(pss);
				 
				 boolean  ot = item.getOneTime();
				 newdata.setOneTime(ot);
				 
				 Date  ts = item.getCreatedTimestamp();
				 newdata.setCreatedTimestamp(ts);
				 
				 newdata.setFlowName(getFlowName(item.getFlowId() , flowList));
				 newdata.setBpName(getbpName(item.getBpId() , bpList));	
				 
				 pssData.add(newdata);
			 }
			 
			 
			 for(PSSWithFLowName item:pssData) {
				 System.out.println(item.getFlowName());
			 }

			return pssData;
			
		}
		
		//new Method 2
		public String getFlowName(int flowid, List<FlowModel> flowList)
		{
			String flowName = "";
			for(FlowModel item:flowList)
			{
				if(String.valueOf(flowid).equals(item.getFlowId()))
				{
					System.out.println("Match found for flow id:=" + flowid);
					System.out.println("Flowname is :=" + item.getFlowName());
					flowName = item.getFlowName();
					break;
				}
			
			}
			return flowName;
		}
		
		//new method 3
	private String getbpName(int bpId, List<BpModel> bpList) {	
			
		String bpName = "";
		for(BpModel item:bpList)
		{
			if(String.valueOf(bpId).equals(item.getBpId()))
			{
				bpName = item.getBpName();
				break;
			}
		
		}
		return bpName;
		}



	@Override
	public FlowReqHistory[] getFlowReqHistory() {
		return spaceServices.getFlowReqHistory();
	}

	@Override
	public FlowReqHistory[] getFlowReqHistory(int sinceSec) {
		return spaceServices.getFlowReqHistory(sinceSec);
	}

	@Override
	public String readWaitingMessages(String projectkey, int thresholdMinutes) {
		return new JSONObject().put("waitingMessages", spaceServices.readWaitingMessages(projectkey, thresholdMinutes))
				.toString();
	}

	@Override
	public Alert[] getAlerts() {
		return spaceServices.getAlerts();
	}

	@Override
	public StepData[] getSteps() {
		LicenseServices lic = spaceServices.getLicenseServices();
		LicenseFeature[] licc = spaceServices.getLicenseFeaturesCommited();
		for (int i = 0; i < licc.length; i++) {
			LicenseFeature l1 = licc[i];
		}
		return spaceServices.getSteps();
	}

	@Override
	public boolean canWorkProjectRunning(String projectKey) throws ApplicationException {
		return spaceServices.getProjectState(projectKey).equals(ProjectState.RUNNING.toString());
	}

	@Override
	public boolean canWorkEnable(String projectKey, long bpId, long flowId) {
		return spaceServices.isFlowEnable(projectKey, bpId, flowId);
	}

	@Override
	public boolean canWorkBufferLimitation(String projectKey, long bpId, long flowId, long triggerStepId) {
		int bufferUsage = spaceServices.getBufferUsage(projectKey, bpId, flowId, triggerStepId);
		if (bufferUsage >= SCHEDULER_BUFFER)
			return false;

		return true;
	}

	@Override
	public int stopProject(String projectKey, int timeout) {
		return spaceServices.stopProject(projectKey, timeout);
	}

	@Override
	public List<MagicXpiSpaceInstances> getSpaceInstances() {
		// TODO Auto-generated method stub

		List<MagicXpiSpaceInstances> magicXpiSpaceInstances = new ArrayList<>();
		MagicXpiSpaceInstances magicXpiSpaceInstance = null;

		String gsSpaceStatus = "N/A";
		String infoSpaceStatus = "N/A";
		String mirrorSpaceStatus = "N/A";

		GridServiceContainers gridServiceContainers = admin.getAdmin().getGridServiceContainers();

		if (gridServiceContainers != null) {

			if (gridServiceContainers.getContainers().length > 0) {

				for (GridServiceContainer gridServiceContainer : gridServiceContainers) {

					String gridContainerName = "gsc-" + gridServiceContainer.getAgentId() + "("
							+ gridServiceContainer.getVirtualMachine().getDetails().getPid() + ")";

					ProcessingUnitInstance[] processingUnitInstances = gridServiceContainer
							.getProcessingUnitInstances();

					ProcessingUnit gsProcessingUnit = admin.getAdmin().getProcessingUnits()
							.getProcessingUnit("mgxpi-gs");
					ProcessingUnit infoProcessingUnit = admin.getAdmin().getProcessingUnits()
							.getProcessingUnit("mginfo-gs");
					ProcessingUnit mirrorProcessingUnit = admin.getAdmin().getProcessingUnits()
							.getProcessingUnit("mgmirror-gs");

					if (gsProcessingUnit != null) {
						gsSpaceStatus = admin.getAdmin().getProcessingUnits().getProcessingUnit("mgxpi-gs").getStatus()
								.name();
					}

					if (infoProcessingUnit != null) {
						infoSpaceStatus = admin.getAdmin().getProcessingUnits().getProcessingUnit("mginfo-gs")
								.getStatus().name();
					}

					if (mirrorProcessingUnit != null) {
						mirrorSpaceStatus = admin.getAdmin().getProcessingUnits().getProcessingUnit("mgmirror-gs")
								.getStatus().name();
					}

					if (processingUnitInstances.length > 0) {

						for (ProcessingUnitInstance processingUnitInstance : processingUnitInstances) {

							SpaceInstance[] spaceInstance = processingUnitInstance.getSpaceInstances();

							for (SpaceInstance spaceInstance2 : spaceInstance) {

								magicXpiSpaceInstance = new MagicXpiSpaceInstances();

								magicXpiSpaceInstance.setSpaceName(spaceInstance2.getSpace().getName());
								magicXpiSpaceInstance.setSpacepartitioninstance(spaceInstance2.getSpaceInstanceName());
								magicXpiSpaceInstance.setType(spaceInstance2.getMode().toString());
								magicXpiSpaceInstance.setRunningonHost(spaceInstance2.getMachine().getHostName());

								magicXpiSpaceInstance.setContainingGSC(gridContainerName);
								magicXpiSpaceInstance.setIpAddress(spaceInstance2.getMachine().getHostAddress());
								if (spaceInstance2.getSpace().getName().equalsIgnoreCase("MAGIC_SPACE")) {
									magicXpiSpaceInstance.setSpaceStatus(gsSpaceStatus);
								} else if (spaceInstance2.getSpace().getName().equalsIgnoreCase("MAGIC_INFO")) {
									magicXpiSpaceInstance.setSpaceStatus(infoSpaceStatus);
								} else if (spaceInstance2.getSpace().getName().equalsIgnoreCase("MIRROR-SERVICE")) {
									magicXpiSpaceInstance.setSpaceStatus(mirrorSpaceStatus);
								}
								magicXpiSpaceInstance.setContainingGSC(true);

								magicXpiSpaceInstances.add(magicXpiSpaceInstance);
							}
						}
					}
				}
			}
		}
		return magicXpiSpaceInstances;

	}

	@Override
	public Map<String, List<MagicXpiHostAndEngines>> getHostAndEnginesDetails() {

		DefaultAdmin adminData = null;
		GridServiceAgents gridServiceAgents = null;
		GridServiceManagers gridServiceManagers = null;
		GridServiceContainers gridServiceContainers = null;
		LookupServices lookupServices = null;

		Map<String, List<MagicXpiHostAndEngines>> magicXpiHostAndEngines = new HashMap<>();
		List<MagicXpiHostAndEngines> magicXpiSpaceComponents = new ArrayList<>();
		MagicXpiHostAndEngines magicXpiHostAndEngine = null;

		if (admin != null) {
			adminData = (DefaultAdmin) admin.getAdmin();
		}

		if (adminData != null) {
			gridServiceAgents = adminData.getGridServiceAgents();
			if (gridServiceAgents != null) {

				GridServiceAgent[] gridServiceAgentsList = gridServiceAgents.getAdmin().getGridServiceAgents()
						.getAgents();

				if (gridServiceAgentsList != null) {

					magicXpiHostAndEngine = new MagicXpiHostAndEngines();

					for (int i = 0; i < gridServiceAgentsList.length; i++) {

						magicXpiHostAndEngine.setName("gsa");
						magicXpiHostAndEngine.setType("GSA");

						GridServiceAgent gridServiceAgent = gridServiceAgentsList[i];

						String zones = "";

						if (gridServiceAgent != null) {

							VirtualMachine vm = gridServiceAgent.getVirtualMachine();
							magicXpiHostAndEngine.setPID(vm.getDetails() != null ? vm.getDetails().getPid() : 0);

							if (vm != null) {
								VirtualMachineStatistics vmStatistics = vm.getStatistics();
								if (vmStatistics != null) {
									magicXpiHostAndEngine
											.setUsedMemory(String.valueOf((int) vmStatistics.getMemoryHeapUsedInMB()));
									magicXpiHostAndEngine
											.setCPUUtlization(String.valueOf((int) vmStatistics.getCpuPerc() * 100));
								}
								if (vm.getMachine() != null)
									magicXpiHostAndEngine.setHostName(
											vm.getMachine().getHostName() != null ? vm.getMachine().getHostName() : "");
							}
							Map<String, Zone> zonesList = gridServiceAgent.getZones();

							if (zonesList != null) {

								for (Map.Entry entry : zonesList.entrySet())
									zones = zones + entry.getValue() + ",";
							}
							magicXpiHostAndEngine.setZones(zones);
						}
					}

					magicXpiSpaceComponents.add(magicXpiHostAndEngine);

				}
			}

			gridServiceManagers = adminData.getGridServiceManagers();
			if (gridServiceManagers != null) {

				magicXpiHostAndEngine = new MagicXpiHostAndEngines();

				for (GridServiceManager gridServiceManager : gridServiceManagers) {

					magicXpiHostAndEngine.setName("gsm-" + gridServiceManager.getAgentId());
					magicXpiHostAndEngine.setType("GSM");

					VirtualMachine vm = gridServiceManager.getVirtualMachine();
					if (vm != null) {
						VirtualMachineStatistics vmStatistics = vm.getStatistics();
						if (vmStatistics != null) {
							magicXpiHostAndEngine
									.setPID(vmStatistics.getDetails() != null ? vmStatistics.getDetails().getPid() : 0);
							magicXpiHostAndEngine
									.setUsedMemory(String.valueOf((int) vmStatistics.getMemoryHeapUsedInMB()));
							magicXpiHostAndEngine.setUsedCpu((int) (vmStatistics.getCpuPerc() * 100));
						}
						if (vm.getMachine() != null)
							magicXpiHostAndEngine.setHostName(
									vm.getMachine().getHostName() != null ? vm.getMachine().getHostName() : "");
					}
					Map<String, Zone> zonesList = gridServiceManager.getZones();
					String zones = "";
					if (zonesList != null) {

						for (Map.Entry entry : zonesList.entrySet())
							zones = zones + entry.getValue() + ",";
					}
					magicXpiHostAndEngine.setZones(zones);
				}

				magicXpiSpaceComponents.add(magicXpiHostAndEngine);

			}

			gridServiceContainers = adminData.getGridServiceContainers();
			if (gridServiceContainers != null) {

				magicXpiHostAndEngine = new MagicXpiHostAndEngines();

				for (GridServiceContainer gridServiceContainer : gridServiceContainers) {

					magicXpiHostAndEngine.setName("gsc-" + gridServiceContainer.getAgentId());
					magicXpiHostAndEngine.setType("GSC");

					VirtualMachine vm = gridServiceContainer.getVirtualMachine();
					if (vm != null) {
						VirtualMachineStatistics vmStatistics = gridServiceContainer.getVirtualMachine()
								.getStatistics();
						if (vmStatistics != null) {
							magicXpiHostAndEngine
									.setPID(vmStatistics.getDetails() != null ? vmStatistics.getDetails().getPid() : 0);
							magicXpiHostAndEngine
									.setUsedMemory(String.valueOf((int) vmStatistics.getMemoryHeapUsedInMB()));
							magicXpiHostAndEngine
									.setCPUUtlization(String.valueOf((int) (vmStatistics.getCpuPerc() * 100)));
						}
						if (vm.getMachine() != null)
							magicXpiHostAndEngine.setHostName(
									vm.getMachine().getHostName() != null ? vm.getMachine().getHostName() : "");
					}

					Map<String, Zone> zonesList = gridServiceContainer.getZones();
					String zones = "";
					if (zonesList != null) {

						for (Map.Entry entry : zonesList.entrySet())
							zones = zones + entry.getValue() + ",";
					}

					magicXpiHostAndEngine.setZones(zones);
				}

				magicXpiSpaceComponents.add(magicXpiHostAndEngine);

			}

			lookupServices = adminData.getLookupServices();
			if (lookupServices != null) {

				magicXpiHostAndEngine = new MagicXpiHostAndEngines();

				for (LookupService lookupService : lookupServices) {

					magicXpiHostAndEngine.setName("lus-" + lookupService.getAgentId());
					magicXpiHostAndEngine.setName("LUS");

					VirtualMachine vm = lookupService.getVirtualMachine();
					if (vm != null) {
						VirtualMachineStatistics vmStatistics = vm.getStatistics();
						if (vmStatistics != null) {
							magicXpiHostAndEngine
									.setPID(vmStatistics.getDetails() != null ? vmStatistics.getDetails().getPid() : 0);
							magicXpiHostAndEngine
									.setUsedMemory(String.valueOf((int) vmStatistics.getMemoryHeapUsedInMB()));
							magicXpiHostAndEngine
									.setCPUUtlization(String.valueOf((int) (vmStatistics.getCpuPerc() * 100)));
						}
						if (vm.getMachine() != null)
							magicXpiHostAndEngine.setHostName(
									vm.getMachine().getHostName() != null ? vm.getMachine().getHostName() : "");
					}
					Map<String, Zone> zonesList = lookupService.getZones();
					String zones = "";
					if (zonesList != null) {

						for (Map.Entry entry : zonesList.entrySet())
							zones = zones + entry.getValue() + ",";
					}

					magicXpiHostAndEngine.setZones(zones);

				}

				magicXpiSpaceComponents.add(magicXpiHostAndEngine);

			}

			if (adminData.getMachines() != null) {
				Machine[] machineList = adminData.getMachines().getMachines();
				if (machineList != null) {

					List<MagicXpiHostAndEngines> magicXpiHostMachineDetails = new ArrayList<>();
					magicXpiHostAndEngine = new MagicXpiHostAndEngines();

					for (int i = 0; i < machineList.length; i++) {
						Machine machine2 = machineList[i];
						if (machine2 != null) {
							OperatingSystem operSystem = machine2.getOperatingSystem();
							if (operSystem != null) {
								OperatingSystemDetails operSysDetails = operSystem.getDetails();
								OperatingSystemStatistics operSysStatistics = operSystem.getStatistics();
								if (operSysDetails != null && operSysStatistics != null) {
									double total = operSysDetails.getTotalPhysicalMemorySizeInMB() / 1000;
									double useMemory = (operSysDetails.getTotalPhysicalMemorySizeInMB()
											- operSysStatistics.getFreePhysicalMemorySizeInMB()) / 1000;
									double cpuUtilization = operSysStatistics.getCpuPerc() * 60;
									int percentage = (int) (useMemory / total * 100);

									Double usedMemory = null;

									if (useMemory > 0)
										usedMemory = new BigDecimal(useMemory).setScale(1, BigDecimal.ROUND_DOWN)
												.doubleValue();

									Double totalMemory = null;
									if (total > 0)
										totalMemory = new BigDecimal(total).setScale(1, BigDecimal.ROUND_DOWN)
												.doubleValue();

									Double usedCPU = null;
									if (cpuUtilization > 0)
										usedCPU = new BigDecimal(cpuUtilization).setScale(1, BigDecimal.ROUND_DOWN)
												.doubleValue();

									String hostName = machine2.getOperatingSystem().getDetails().getHostName();

									String memoryUtilization = usedMemory + " Of " + totalMemory + " GB (" + percentage
											+ "%)";
									if (usedCPU != null)
										magicXpiHostAndEngine.setCPUUtlization(usedCPU + "%");
									if (memoryUtilization != null)
										magicXpiHostAndEngine.setUsedMemory(memoryUtilization);

									magicXpiHostAndEngine.setHostName(hostName);
								}
							}
						}
					}

					magicXpiHostMachineDetails.add(magicXpiHostAndEngine);
					magicXpiHostAndEngines.put("GsaGsmGscLusDetails", magicXpiSpaceComponents);
					magicXpiHostAndEngines.put("HostMachineDetails", magicXpiHostMachineDetails);

				}
			}
		}
		return magicXpiHostAndEngines;
	}

	@Override
	public MessagesDetails getAllMessages(String projectKey, int thresholdMinutes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonitorOfflineMetadata loadProjectMetadata(String projectKey, String projectLocation) {
		// TODO Auto-generated method stub

		monitorOfflineMetadata = MagicMonitorUtilities.readMonitorOfflineMetadata(projectKey, projectLocation);

		return monitorOfflineMetadata;
	}

	@Override
	public List<ActivityMsgFilterMetadata> loadActMsgFilterMetadata(QueryFilters queryFilters) {
		// TODO Auto-generated method stub
		queryFilters.setActMsgFiltersMetadata(new ArrayList<ActivityMsgFilterMetadata>());
		activityMsgFilterMetadata = MagicMonitorUtilities.readActMsgFiltersMetadata(queryFilters);

		return activityMsgFilterMetadata;
	}

	@Override
	public String writeActivityLogMsgFilters(QueryFilters queryFilters) {
		// TODO Auto-generated method stub
		MagicMonitorUtilities magicMonitorUtilities = new MagicMonitorUtilities();
		magicMonitorUtilities.writeActLogFilterTableToXML(queryFilters);
		return "";
	}

	@Override
	public TriggerActivityGraphRes getTriggersActivityStatistics(String projectkey, String filterType) {

		LocalDateTime dateTime = LocalDateTime.now();
		// TODO :- GANESH REMOVE HARD CODED FILTER AND REFACTOR THE CODE

		if (filterType.equalsIgnoreCase("10_MINUTE")) {
			dateTime = LocalDateTime.now().minus(Duration.of(10, ChronoUnit.MINUTES));
		} else if (filterType.equalsIgnoreCase("30_MINUTE")) {
			dateTime = LocalDateTime.now().minus(Duration.of(30, ChronoUnit.MINUTES));
		} else if (filterType.equalsIgnoreCase("1_HOUR")) {
			dateTime = LocalDateTime.now().minus(Duration.of(1, ChronoUnit.HOURS));
		} else if (filterType.equalsIgnoreCase("1_DAY")) {
			dateTime = LocalDateTime.now().minus(Duration.of(1, ChronoUnit.DAYS));
		} else if (filterType.equalsIgnoreCase("3_DAY")) {
			dateTime = LocalDateTime.now().minus(Duration.of(3, ChronoUnit.DAYS));
		}
		long milliSeconds = Timestamp.valueOf(dateTime).getTime();
		List<TriggerActivityModel> res = new ArrayList<TriggerActivityModel>();  //series
		List<String> res1 = new ArrayList<String>();  // categories
		List<TriggerActivityMessages> result = spaceServices.getTriggersActivityStatistics(projectkey, milliSeconds);

		result.forEach(action -> {
			long[] count = { action.getProcessedMessages() };
			res.add(new TriggerActivityModel(action.getTriggerName(), count));
			res1.add(action.getTriggerName());
		});
		return new TriggerActivityGraphRes(res, res1);
	}

	@Override
	public Alert[] getAlertsByProject(@NotNull String projectKey) {
		return spaceServices.getAlertsByProject(projectKey);
	}

	@Override
	public Object getFlowReqHistory(String projectkey, String filterType) {

		// TODO :- GANESH REMOVE HARD CODED FILTER AND REFACTOR THE CODE
		int sinseSec = 600, skipCount = 0;
		if (filterType.equalsIgnoreCase("10_MINUTE")) {
			sinseSec = 600;
		} else if (filterType.equalsIgnoreCase("30_MINUTE")) {
			sinseSec = 1800;
			skipCount = 2;
		} else if (filterType.equalsIgnoreCase("1_HOUR")) {
			sinseSec = 3600;
			skipCount = 3;
		} else if (filterType.equalsIgnoreCase("1_DAY")) {
			sinseSec = 86400;
			skipCount = 10;
		} else if (filterType.equalsIgnoreCase("3_DAY")) {
			sinseSec = 259200;
			skipCount = 10;
		}
		FlowReqHistory[] result = spaceServices.getFlowReqHistory(sinseSec, projectkey);
		List<FlowReqHistory> list = Arrays.asList(result);

		List<FlowRequestMessages> returnResult = list.stream()
				.filter(flow -> flow.getIncomeDate() != null && flow.getInProgressDate() != null && flow.getEndDate()!=null)
				.map(flow -> new FlowRequestMessages(flow.getProjectKey(), flow.getMessageId(), flow.getRootMessageId(),
						parse(flow.getIncomeDate()), parse(flow.getInProgressDate()),
						parse(flow.getEndDate() == null ? 0 : flow.getEndDate()), flow.getEndReason().toString()))
				.sorted((e1, e2) -> e1.getIncomeDate().compareTo(e2.getIncomeDate())).collect(Collectors.toList());

		List<String> arrivedMessages = returnResult.stream()
				.map(n -> n.getIncomeDate().format(DateTimeFormatter.ofPattern("HH:mm"))).collect(Collectors.toList());

		List<String> processeMessages = returnResult.stream()
				.filter(predicate -> predicate.getEndDate() != null && predicate.getRootMessageId() > 0)
				.map(n -> n.getEndDate().format(DateTimeFormatter.ofPattern("HH:mm"))).collect(Collectors.toList());

		List<String> pendingMessages = returnResult.stream()
				.filter(predicate -> predicate.getInProgressDate() != null && predicate.getRootMessageId() <= 0)
				.map(n -> n.getInProgressDate().format(DateTimeFormatter.ofPattern("HH:mm")))
				.collect(Collectors.toList());

		TreeMap<String, Long> arrivedRes = countByForEachLoopWithGetOrDefault(arrivedMessages);
		TreeMap<String, Long> processeRes = countByForEachLoopWithGetOrDefault(processeMessages);
		TreeMap<String, Long> pending = countByForEachLoopWithGetOrDefault(pendingMessages);

		List<String> timeSeries = new ArrayList<String>(new TreeSet(arrivedMessages));

		if (!filterType.equalsIgnoreCase("10_MINUTE")) {
			int counter = 0;
			for (int i = 0; i < timeSeries.size(); i++) {
				if (counter == 0 || counter == skipCount) {
					counter = 0;
				} else {
					timeSeries.set(i, "");
				}
				counter++;
			}
		}

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("arrived", new ArrayList<>(arrivedRes.values()));
		response.put("processed", new ArrayList<>(processeRes.values()));
		response.put("pending", new ArrayList<>(pending.values()));
		response.put("time", timeSeries);
		response.put("timeSeries", timeSeries);
		return response;
	}

	public TreeMap<String, Long> countByForEachLoopWithGetOrDefault(List<String> inputList) {
		TreeMap<String, Long> resultMap = new TreeMap<>();
		inputList.forEach(e -> resultMap.put(e, resultMap.getOrDefault(e, 0L) + 1L));
		return resultMap;
	}

	public LocalDateTime parse(long time) {
		if (time == 0)
			return null;

		return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	@Override
	public Object getSummary(@NotNull String projectkey) {
		HashMap<String, Object> summary = new HashMap<String, Object>();
		try {
			
			if (spaceServices.getProjectState(projectkey).equals(ProjectState.RUNNING.toString())) {
				TriggerData[] trigger = spaceServices.getTriggers(projectkey);

				WorkerData[] worker = spaceServices.workerByProjet(projectkey);
				ProjectData pro = spaceServices.getProject(projectkey);
				Date latestWorker = Arrays.asList(worker).stream().map(n -> n.getWorkerLastIsAlive())
						.max(Date::compareTo).get();
				Date latestTriggerActivity = Arrays.asList(trigger).stream().map(n -> n.getLastWriteMessage())
						.max(Date::compareTo).get();

				summary.put("serverCount", spaceServices.getServerCount(projectkey) + "");
				summary.put("workeCount", spaceServices.getWorkersCount(projectkey) + "");
				summary.put("triggerCount", trigger.length + "");
				summary.put("requestServed", spaceServices.totalRequestServed(projectkey) + "");
				summary.put("pendingRequest", spaceServices.totalPendingRequest(projectkey) + "");
				summary.put("projectNoOfThreads", spaceServices.getProjectNoOfThreads(projectkey) + "");
				summary.put("reservedLicenseThreads", pro.getReservedLicenseThreads() + "");
				summary.put("totalWorker", spaceServices.getWorkersCount(projectkey) + "");
				summary.put("startedAt", parseDate(pro.getCreatedTimestamp()));
				summary.put("lastTriggerActivity", parseDate(latestTriggerActivity));
				summary.put("latestWorker", parseDate(latestWorker));
				summary.put("ProjectState", spaceServices.getProjectState(projectkey).toString());
			} else {
				
				summary.put("serverCount", "0");
				summary.put("workeCount", "0");
				summary.put("triggerCount", "0");
				summary.put("requestServed", "0");
				summary.put("pendingRequest", "0");
				summary.put("projectState", spaceServices.getProjectState(projectkey).toString());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return summary;
	}

	public String parseDate(Date dt) {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a").format(dt);
	}

	@Scheduled(fixedDelay = 20000)
	public void scheduleFixedRateWithInitialDelayTask() {
		if (spaceServices != null) {
			ProjectData[] projects = spaceServices.getProjects();
			List<WorkerHistory> workerCatch = null;
			LoadingCache<String, List<WorkerHistory>> workerCatche = null;
			for (ProjectData pro : projects) {

				try {
					workerCatche = WorkerlHistoryCatche.getInstance(pro.getProjectKey()).getClient();
					workerCatch = workerCatche.get(pro.getProjectKey());

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null == workerCatch) {
					workerCatch = new ArrayList<WorkerHistory>();
				}

				int totalWorker = spaceServices.getWorkersCount(pro.getProjectKey());
				int running = spaceServices.getWorkersRunnigCount(pro.getProjectKey());

				System.out.println(workerCatche.size());
				if (workerCatch.size() > 1) {

					long diffInSeconds = ChronoUnit.SECONDS
							.between(workerCatch.get(workerCatch.size() - 1).getDatetime(), LocalDateTime.now());

					if (diffInSeconds < 35 && workerCatch.get(workerCatch.size() - 1).getRunning() == running) {

					} else {
						workerCatch
								.add(new WorkerHistory(pro.getProjectKey(), running, totalWorker, LocalDateTime.now()));

						workerCatche.put(pro.getProjectKey(), workerCatch);
					}
				} else {

					workerCatche.put(pro.getProjectKey(), workerCatch);
					workerCatch.add(new WorkerHistory(pro.getProjectKey(), running, totalWorker, LocalDateTime.now()));
				}

			}
		}
	}

	@Override
	public PSSData[] getPSSData(String projectKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String newServerInstance(String projectkey, String Server_host, String Alternate_host,
			 String project_directory, int Number_of_instances, Boolean Load_triggers,
		     Boolean Load_Schedulers, Boolean Load_auto_start, int Number_of_workers) {
		
		System.out.println("Inside"+ projectkey+" "+Server_host+" "+Alternate_host+" "+
				 project_directory+ " "+ Number_of_instances+" "+Load_triggers+" "+
			     Load_Schedulers+" "+Load_auto_start+" "+Number_of_workers);
		
		for(int i=0;i<Number_of_instances;i++)
		{
			try {
				rtvEntity.runNewServer(projectkey, Server_host, project_directory,Load_triggers, Load_Schedulers, Load_auto_start,
				         Number_of_workers, Alternate_host);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		return null;

	}

	
}
