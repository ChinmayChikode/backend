package com.magicsoftware.monitor.controller;

import static java.util.stream.Collectors.toList;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.magicsoftware.monitor.model.ActivityLog;
import com.magicsoftware.monitor.model.ActivityMsgFilterMetadata;
import com.magicsoftware.monitor.model.BpWithFlow;
import com.magicsoftware.monitor.model.ChartBean;
import com.magicsoftware.monitor.model.FlowDetails;
import com.magicsoftware.monitor.model.FlowWithTrigger;
import com.magicsoftware.monitor.model.LicenseDetails;
import com.magicsoftware.monitor.model.MagicXpiHostAndEngines;
import com.magicsoftware.monitor.model.MagicXpiSpaceInstances;
import com.magicsoftware.monitor.model.MessagesDetails;
import com.magicsoftware.monitor.model.MonitorOfflineMetadata;
import com.magicsoftware.monitor.model.ODSData;
import com.magicsoftware.monitor.model.ProjectBean;
import com.magicsoftware.monitor.model.ProjectOperations;
import com.magicsoftware.monitor.model.QueryFilters;
import com.magicsoftware.monitor.model.Series;
import com.magicsoftware.monitor.model.ServerAndWorkerData;
import com.magicsoftware.monitor.model.ServerDetails;
import com.magicsoftware.monitor.model.TriggerActivityGraphRes;
import com.magicsoftware.monitor.model.TriggerDetails;
import com.magicsoftware.monitor.query.QueryFactory;
import com.magicsoftware.monitor.repository.ActivityLogRepo;
import com.magicsoftware.monitor.service.ActivityLogService;
import com.magicsoftware.monitor.service.SpaceService;
import com.magicsoftware.monitor.serviceimpl.LicenseDetailsImpl;
import com.magicsoftware.monitor.serviceimpl.LicenseSummaryImpl;
import com.magicsoftware.monitor.serviceimpl.ODSDataServiceImpl;
import com.magicsoftware.monitor.serviceimpl.SpaceServiceImpl;
import com.magicsoftware.monitor.util.AllProjectsAdapter;
import com.magicsoftware.monitor.util.MagicMonitorUtilities;
import com.magicsoftware.xpi.server.common.ApplicationException;
import com.magicsoftware.xpi.server.data.Lock;
import com.magicsoftware.xpi.server.data.helpers.Alert;
import com.magicsoftware.xpi.server.data.helpers.StatisticsHistory;
import com.magicsoftware.xpi.server.data.license.LicenseFeature;
import com.magicsoftware.xpi.server.data.project.BpData;
import com.magicsoftware.xpi.server.data.project.ProjectData;
import com.magicsoftware.xpi.server.data.project.StepData;
import com.magicsoftware.xpi.server.data.project.TriggerData;
import com.magicsoftware.xpi.server.data.server.Scheduler;
import com.magicsoftware.xpi.server.data.server.ServerData;
import com.magicsoftware.xpi.server.data.server.ServerData.Status;
import com.magicsoftware.xpi.server.data.server.WorkerData;
import com.magicsoftware.xpi.server.entities.MonitorEntity;
import com.magicsoftware.xpi.server.entities.RTVEntity;
import com.magicsoftware.xpi.server.messages.FlowReqHistory;
import com.magicsoftware.xpi.server.messages.FlowRequest;
import com.magicsoftware.xpi.server.messages.PSSData;
import com.magicsoftware.xpi.server.spacebridge.spaceservices.SpaceServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/v1")
public class MonitorController {

	@Autowired
	private ActivityLogService activityLogService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ActivityLogRepo activityLogRepo;

	@Autowired
	private SpaceService spaceServiceService;

	@Autowired
	private ODSDataServiceImpl oDSDataServiceImpl;

	@Autowired
	private LicenseSummaryImpl licenseSummaryImpl;

	@Autowired
	private LicenseDetailsImpl licenseDetailsImpl;

	@GetMapping("/getLicenseProjectDetails/{projectKey:.+}/{type}")
	public ChartBean getLicenseProjectDetails(@PathVariable @NotNull String projectKey,
			@PathVariable @NotNull String type) {
		return licenseDetailsImpl.findLicenseDetails(projectKey, type);
	}

	@GetMapping("/getLicenseListProjectDetails/{projectKey:.+}/{type}")
	public List<LicenseDetails> getLicenseListProjectDetails(@PathVariable @NotNull String projectKey,
			@PathVariable @NotNull String type) {
		return licenseDetailsImpl.findLicenseByProjectAndPeak(projectKey, type);
	}

	@GetMapping("/getLicenseDefaultGraph/{projectKey:.+}")
	public ChartBean getLicenseDefaultGraph(@PathVariable @NotNull String projectKey) {
		return licenseDetailsImpl.findLicenseDetailsForDefaultGraph(projectKey);
	}

	@GetMapping("/getPeaklicenseByHost/{projectKey:.+}")
	public List<Series> getPeaklicenseByHost(@PathVariable @NotNull String projectKey) {
		return licenseDetailsImpl.findLicenseDetailsForHost(projectKey);
	}

	@GetMapping("/getPeaklicenseByHostByRange/{projectKey:.+}")
	public List<Series> getPeaklicenseByHostByRange(@PathVariable @NotNull String projectKey,
			@RequestParam("sDate") @NotNull Date sDate, @RequestParam("eDate") @NotNull Date eDate) {
		return licenseDetailsImpl.findLicenseDetailsForHostByRange(projectKey, sDate, eDate);
	}

	@GetMapping("/getTotalPeaklicense/{projectKey:.+}")
	public List<Series> getTotalPeaklicense(@PathVariable @NotNull String projectKey) {
		return licenseDetailsImpl.findTotalLicenseDetails(projectKey, "Total Peak License");
	}

	@GetMapping("/getDefaultPeaklicenseByRange/{projectKey}")
	public ChartBean getDefaultPeaklicenseByRange(@PathVariable @NotNull String projectKey,
			@RequestParam("sDate") @NotNull Date sDate, @RequestParam("eDate") @NotNull Date eDate) {
		return licenseDetailsImpl.findLicenseDetailsForDefaultGraphByRage(projectKey, sDate, eDate);
	}

	@GetMapping("/getLicenseProjectDetails/{type}")
	public ChartBean getLicenseDetails(@PathVariable @NotNull String type) {
		return licenseDetailsImpl.findLicenseDetails(type);
	}

	@GetMapping("/getLicenseListProjectDetails/{projectKey}")
	public List<LicenseDetails> getLicenseListProjectDetails(@PathVariable @NotNull String projectKey,
			@RequestParam("sDate") @NotNull Date sDate, @RequestParam("eDate") @NotNull Date eDate) {
		return licenseDetailsImpl.findLicenseListDetailsByDate(projectKey, sDate, eDate);
	}

	@GetMapping("/getLicenseProjectDetailsByDate")
	public ChartBean getLicenseDetails(@RequestParam("sDate") @NotNull Date sDate,
			@RequestParam("eDate") @NotNull Date eDate) {
		return licenseDetailsImpl.findLicenseDetails("custom", sDate, eDate);
	}

	@GetMapping("/odsDataByProjectkey/{projectkey}")
	public List<ODSData> odsDataByProjectkey(@PathVariable @NotNull String projectkey) {
		return oDSDataServiceImpl.findByprojectKey(projectkey);
	}

	/*
	 * @GetMapping("/odsDataByName/{odsname}") public List<ODSData>
	 * odsDataByOdsname(@PathVariable @NotNull String odsname) {
	 * 
	 * return oDSDataServiceImpl.findByuserKey(odsname); }
	 */

	@GetMapping("/odsDataByName")
	public List<ODSData> odsDataByOdsname(@RequestParam String odsname) {
		return oDSDataServiceImpl.findByuserKey(odsname);
	}

	@GetMapping("/messageGraphDataPerProject")
	public List<StatisticsHistory> getMessageGraphDataPerProject() {
		return spaceServiceService.getMessageGraphDataPerProject();
	}

	@GetMapping("/licenseInfo")
	public LicenseFeature[] getLicenseInfo() {
		return spaceServiceService.getLicenseInfo();
	}

	@GetMapping("/serversByProject/{projectkey}")
	public ServerDetails serversByProject(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.serversByProject(projectkey);
	}

	@GetMapping("/flow")
	public FlowDetails flow(@RequestParam String projectKey, @RequestParam String projectLocation) {
		return spaceServiceService.flow(projectKey, projectLocation);
	}

	@GetMapping("/triggersByProject")
	public TriggerDetails triggersByProject(@RequestParam String projectKey, @RequestParam String projectLocation) {
		return spaceServiceService.triggersByProject(projectKey, projectLocation);
	}

	@GetMapping("/worker")
	public WorkerData[] worker() {
		return spaceServiceService.worker();
	}

	@GetMapping("/workerByServerId/{serverId}")
	public WorkerData[] workerbyServerId(@PathVariable @NotNull Integer serverId) {

		WorkerData[] allServersWorkerData = spaceServiceService.worker();
		List<WorkerData> workerDatabyServerIdlist = new ArrayList<WorkerData>();

		for (int i = 0; i < allServersWorkerData.length; i++) {
			if (allServersWorkerData[i].getServerId() == serverId) {
				workerDatabyServerIdlist.add(allServersWorkerData[i]);
			}
		}

		WorkerData[] workerDatabyServerId = new WorkerData[workerDatabyServerIdlist.size()];

		for (int j = 0; j < workerDatabyServerIdlist.size(); j++) {
			workerDatabyServerId[j] = workerDatabyServerIdlist.get(j);
		}

		return workerDatabyServerId;
	}

	@GetMapping("/workerByServer/{projectkey}")
	public List<ServerAndWorkerData> workerByServer(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.workerByServer(projectkey);
	}

	@GetMapping("/servers/{projectkey}")
	public ServerData[] servers(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.servers(projectkey);
	}

	@GetMapping("/triggers/{projectkey}")
	public TriggerData[] triggers(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.triggers(projectkey);
	}

	@GetMapping("/triggersActivityStatistics/{projectkey}")
	public TriggerData[] getTriggersActivityStatistics(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.triggers(projectkey);
	}

	@GetMapping("/triggersByFlow/{projectkey}/{flowID}")
	public TriggerData[] triggersByFlow(@PathVariable @NotNull String projectkey, @PathVariable @NotNull long flowID) {
		return spaceServiceService.getTriggersByFlow(projectkey, flowID);
	}

	@GetMapping("/allMessages")
	public FlowRequest[] getAllMessages() {
		return spaceServiceService.getAllMessages();
	}

	@GetMapping("/flowReqList")
	public FlowRequest[] flowReqList() {
		return spaceServiceService.flowReqList();
	}

	@GetMapping("/flowList/{projectkey}")
	public List<FlowWithTrigger> flowList(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.flowList(projectkey);
	}

	@GetMapping("/bpWithFlow/{projectkey}")
	public List<BpWithFlow> bpWithFlows(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.bpWithFlows(projectkey);
	}

	@GetMapping("/bpList/{projectkey}")
	public BpData[] bpList(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.bpList(projectkey);
	}

	@GetMapping("/workersLocks")
	public Lock[] getWorkersLocks() {
		return spaceServiceService.getWorkersLocks();
	}

	/*
	 * @GetMapping("/allMessage/{projectKey}/{thresholdMinutes}") public
	 * MessagesDetails getAllMessages(@PathVariable String projectKey, @PathVariable
	 * int thresholdMinutes) { return spaceServiceService.getAllMessages(projectKey,
	 * thresholdMinutes); }
	 */

	@GetMapping("/allMessage")
	public MessagesDetails getAllMessages(@RequestParam String projectKey, @RequestParam String projectLocation,
			@RequestParam int thresholdMinutes) {
		return spaceServiceService.getAllMessages(projectKey, projectLocation, thresholdMinutes);
	}

	@GetMapping("/readWaitingMessages/{projectKey}/{thresholdMinutes}")
	public String readWaitingMessages(@PathVariable String projectKey, @PathVariable int thresholdMinutes) {
		return spaceServiceService.readWaitingMessages(projectKey, thresholdMinutes);
	}

	@GetMapping("/locking/{projectkey}")
	public Lock[] locking(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.getLocks(projectkey);
	}

	@GetMapping("/workersLocks/{projectkey}")
	public Lock[] getWorkersLocks(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.getWorkersLocks(projectkey);
	}

	@GetMapping("/scheduler")
	public Scheduler[] getScheduler() {
		return spaceServiceService.getScheduler();
	}

	@GetMapping("/PSSData/{projectkey}")
	public PSSData[] getPSSData(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.getPSSData(projectkey);
	}

	@GetMapping("/flowReqHistory")
	public FlowReqHistory[] getFlowReqHistory() {
		return spaceServiceService.getFlowReqHistory();
	}

	@GetMapping("/flowReqHistory/{sinceSec}")
	public FlowReqHistory[] getFlowReqHistory(@PathVariable int sinceSec) {
		return spaceServiceService.getFlowReqHistory(sinceSec);
	}

	@GetMapping("/alerts")
	public Alert[] getAlerts() {
		return spaceServiceService.getAlerts();
	}

	@GetMapping("/steps")
	public StepData[] getSteps() {

		return spaceServiceService.getSteps();
	}

	@GetMapping("/canWorkProjectRunning/{projectKey}")
	public boolean canWorkProjectRunning(@PathVariable @NotNull String projectKey) throws ApplicationException {
		return spaceServiceService.canWorkProjectRunning(projectKey);
	}

	@GetMapping("/canWorkEnable/{projectKey}/{bpId}/{flowId}")
	public boolean canWorkEnable(@PathVariable @NotNull String projectKey, @PathVariable long bpId,
			@PathVariable long flowId) {
		return spaceServiceService.canWorkEnable(projectKey, bpId, flowId);
	}

	@GetMapping("/canWorkBufferLimitation/{projectKey}/{bpId}/{flowId}/{triggerStepId}")
	public boolean canWorkBufferLimitation(@PathVariable @NotNull String projectKey, @PathVariable long bpId,
			@PathVariable long flowId, @PathVariable long triggerStepId) {
		return spaceServiceService.canWorkBufferLimitation(projectKey, bpId, flowId, triggerStepId);
	}

	/*
	 * @GetMapping("/activityLog/{projectkey}/{page}/{size}") public
	 * List<ActivityLog> activityLog(@PathVariable @NotNull String
	 * projectkey, @PathVariable Integer page, @PathVariable Integer size) { return
	 * activityLogService.findByProjectkey(projectkey, PageRequest.of(page, size));
	 * }
	 */

	@RequestMapping(path = "/activityLog", method = RequestMethod.POST, consumes = "application/json")
	public List<ActivityLog> activityLog(@RequestBody QueryFilters queryFiltersDTO) {
		try {
			return activityLogService.getMonitorActivityLog(queryFiltersDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(path = "/activityLogPages", method = RequestMethod.POST, consumes = "application/json")
	public Page<ActivityLog> activityLogPages(@RequestBody QueryFilters queryFilters) {
		
		if (queryFilters.getPage() == 0 && queryFilters.getSize() == 0) {
			queryFilters.setPage(1);
			queryFilters.setSize(50);
		}
		
		PageRequest pageRequest = PageRequest.of(queryFilters.getPage(), queryFilters.getSize(), Sort.by("msgid").descending());
		Page<ActivityLog> pageResult = activityLogRepo.findAll(pageRequest);
		List<ActivityLog> activityLogList = pageResult.stream().map(ActivityLog::new).collect(toList());
		
		queryFilters.setActMsgFiltersMetadata(new ArrayList<ActivityMsgFilterMetadata>());
		List<ActivityMsgFilterMetadata> activityMsgFilterMetadata = MagicMonitorUtilities
				.readActMsgFiltersMetadata(queryFilters);
		String messageTypeIdString = "";
		for (int i = 0; i < activityMsgFilterMetadata.size(); i++) {
			if (!activityMsgFilterMetadata.get(i).isDisplay()) {
				messageTypeIdString = messageTypeIdString + activityMsgFilterMetadata.get(i).getMessageId() + ",";
			}
		}
		
		/*int totalRecordsCount = jdbcTemplate.queryForObject(queryFilters.getActivityLogCountQuery().toString(),
				new Object[] {}, Integer.class);*/
		
		for (int i = 0; i < activityLogList.size(); i++) {

			for (int j = 0; j < SpaceServiceImpl.monitorOfflineMetadata.getStepList().size(); j++) {
				if (activityLogList.get(i).getFsstep() == Integer
						.valueOf(SpaceServiceImpl.monitorOfflineMetadata.getStepList().get(j).getStepId())) {
					activityLogList.get(i)
							.setStepName(SpaceServiceImpl.monitorOfflineMetadata.getStepList().get(j).getStepName());
				}
			}
			activityLogList.get(i)
					.setMessageType(MagicMonitorUtilities.getMessageType(activityLogList.get(i).getMessagetypeid()));

			activityLogList.get(i).setDisplayCreatedTime(
					MagicMonitorUtilities.formatDate(activityLogList.get(i).getCreateTimeStamp()));

			//activityLogList.get(i).setTotalNumberOfRecords(0L);

			for (int j = 0; j < activityMsgFilterMetadata.size(); j++) {
				if (activityLogList.get(i).getMessagetypeid() == activityMsgFilterMetadata.get(j).getMessageId()) {
					activityLogList.get(i).setColor(activityMsgFilterMetadata.get(j).getColor());
				}
			}

			BPLoop: for (int j = 0; j < SpaceServiceImpl.monitorOfflineMetadata.getBpList().size(); j++) {
				if (SpaceServiceImpl.monitorOfflineMetadata.getBpList().get(j).getBpId() != null
						&& activityLogList.get(i).getBpid() == Integer
								.valueOf(SpaceServiceImpl.monitorOfflineMetadata.getBpList().get(j).getBpId())) {
					activityLogList.get(i)
							.setBpName(SpaceServiceImpl.monitorOfflineMetadata.getBpList().get(j).getBpName());
					FlowLoop: for (int k = 0; k < SpaceServiceImpl.monitorOfflineMetadata.getFlowList().size(); k++) {
						if (SpaceServiceImpl.monitorOfflineMetadata.getFlowList().get(k).getFlowId() != null
								&& activityLogList.get(i).getFlowid() == Integer.valueOf(
										SpaceServiceImpl.monitorOfflineMetadata.getFlowList().get(k).getFlowId())) {
							activityLogList.get(i).setFlowName(
									SpaceServiceImpl.monitorOfflineMetadata.getFlowList().get(k).getFlowName());
							continue BPLoop;
						}
					}
				}
			}

			if (activityLogList.get(i).getServerid() != null) {
				activityLogList.get(i).setServerName("Server_" + activityLogList.get(i).getServerid());
			}

		}

		return new PageImpl<>(activityLogList, pageRequest, pageResult.getTotalElements());

	}

	@RequestMapping(path = "/activityLogCount", method = RequestMethod.POST, consumes = "application/json")
	public int activityLogCount(@RequestBody QueryFilters queryFiltersDTO) {
		try {
			return activityLogService.getMonitorActivityLogCount(queryFiltersDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@RequestMapping(path = "/deleteActivityLog", method = RequestMethod.POST, consumes = "application/json")
	public int deleteActivityLog(@RequestBody QueryFilters queryFiltersDTO) {
		try {
			return activityLogService.deleteActivityLog(queryFiltersDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@RequestMapping(path = "/startProject", method = RequestMethod.POST, consumes = "application/json")
	public String startProject(@RequestBody ProjectOperations projectOperations) {
		RTVEntity r = connect();
		PrintStream logs = null;
		// filePath += "\\" + projectKey + "\\start.xml";
		r.startProject(projectOperations.getFilePath(), projectOperations.isLoadInDebugMode(), logs);
		return "";
	}

	@GetMapping("/stopRequest/{projectKey}/{timeOut}")
	public int worker1(@PathVariable @NotNull String projectKey, @PathVariable Integer timeOut) {
		return spaceServiceService.stopProject(projectKey, timeOut);
	}

	@RequestMapping(path = "/restartRequest", method = RequestMethod.POST, consumes = "application/json")
	public String restartProject(@RequestBody ProjectOperations projectOperations) {

		spaceServiceService.stopProject(projectOperations.getProjectKey(), projectOperations.getTimeout());

		try {
			Thread.sleep(10000);
			System.out.println("Wait To Restart..");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RTVEntity r = connect();
		PrintStream logs = null;
		// filePath += "\\" + projectKey + "\\start.xml";
		r.startProject(projectOperations.getFilePath(), projectOperations.isLoadInDebugMode(), logs);

		return "Success";
	}

	@GetMapping("/clearLocks/{projectKey}")
	public String clearLocks(@PathVariable String projectKey) {
		SpaceServices spaceServices = new SpaceServices();
		spaceServices.clearProjectWorkersLockObjects(projectKey);
		return "";
	}

	@GetMapping("/clearLocks/{projectKey}/{lockName}")
	public String clearLock(@PathVariable String projectKey, @PathVariable String lockName) {
		SpaceServices spaceServices = new SpaceServices();
		spaceServices.clearLock(projectKey, lockName);
		return "";
	}

	@GetMapping("/clearLocks/{projectKey}/{name}")
	public String clearPSS(@PathVariable String projectKey, @PathVariable String name) {
		SpaceServices spaceServices = new SpaceServices();
		spaceServices.clearPSS(projectKey, name);
		return "";
	}

	@GetMapping("/clearAllLocks/{projectKey}")
	public String clearAllPSS(@PathVariable String projectKey) {
		SpaceServices spaceServices = new SpaceServices();
		spaceServices.clearAllPSS(projectKey);
		return "";
	}

	@GetMapping("/testAllPro")
	public ServerData startProject() {
		MonitorEntity e = new MonitorEntity();
		return e.startServer("Project21", 1);
	}

	@GetMapping("/stopwork")
	public String stopProject() {
		spaceServiceService.getSteps();
		return "0";
	}

	@GetMapping("/projectComittedState/{projectKey}")
	public String getProjectComittedState(@PathVariable String projectKey) {
		SpaceServices spaceServices = new SpaceServices();
		spaceServices.getProjectComittedState(projectKey);
		return "";
	}

	@GetMapping("/updateFlowStatus/{projectKey}/{bpId}/{flowId}/{isEnable}")
	public String updateFlowStatus(@PathVariable String projectKey, @PathVariable int bpId, @PathVariable int flowId,
			@PathVariable boolean isEnable) {
		SpaceServices spaceServices = new SpaceServices();
		try {
			spaceServices.updateFlowEnableStatus(projectKey, bpId, flowId, isEnable);
		} catch (ApplicationException e) {
			e.printStackTrace(); // TODO ganesh :- need to change it with logger
		}
		return "";
	}

	@GetMapping("/stopServer/{projectKey}/{serverID}/{timeout}")
	public String stopServer(@PathVariable String projectKey, @PathVariable int serverID, @PathVariable int timeout) {
		SpaceServices spaceServices = new SpaceServices();
		List<Status> statuses = Arrays.asList(Status.RUNNING);
		spaceServices.stopServer(projectKey, serverID, timeout, statuses);
		return "";
	}

	private RTVEntity connect() {// TODO ganesh :- remove this method
		RTVEntity r = new RTVEntity();
		r.connect("MAGIC_SPACE", "Magic_xpi_4.13_SUDEEPMLPT", "SUDEEPMLPT");
		r.isMonitorLicensed();
		return r;
	}

	@GetMapping("/space")
	public com.magicsoftware.xpi.server.spacebridge.spaceservices.Space updateFlowStatus1() {
		SpaceServices spaceServices = new SpaceServices();
		return spaceServices.getSpace();
	}

	private List<ProjectBean> runningProjectList() {

		List<ProjectBean> projectList = new ArrayList<>();
		ProjectData[] projects = spaceServiceService.runningProject();
		for (ProjectData projectData : projects) {
			ProjectBean projectBean = new ProjectBean();
			ServerDetails serverdetail = spaceServiceService.serversByProject(projectData.getProjectKey());
			ServerData serverChild = serverdetail.getServerData()[0];
			projectBean.setProjectKey(projectData.getProjectKey());
			projectBean.setProjectLocation(serverChild.getProjectsDirectory() + "\\" + serverChild.getProjectKey());
			// projectBean.setProjectLocation("");
			// projectBean.setWorkers(serverChild.getNumberOfWorkers() + "");
			projectBean.setWorkers("");
			projectBean.setReserverLivenseThread(projectData.getReservedLicenseThreads() + "");
			projectBean.setStatus(projectData.getProjectState().toString());
			projectBean.setUptime(QueryFactory.defaultLocalFormat.format(projectData.getCreatedTimestamp()) + "");
			projectBean.setEngines("");
			projectBean.setMessages("");
			projectBean.setAlert("");
			projectBean.setCurrentLicenseThread("");
			// projectBean.setLoadInDebugMode(serverChild.getLoadInDebugMode());
			projectBean.setLoadInDebugMode(false);
			projectBean.setServerdetail(serverdetail);
			// projectBean.setWorkerDetais(spaceServiceService.worker());
			projectList.add(projectBean);
		}
		return projectList;
	}

	@GetMapping("/getAllProjectsList")
	public List<ProjectBean> getAllProjectsList() {

		List<ProjectBean> stoppedProjectList = new ArrayList<>();
		List<ProjectBean> runningProjectList = new ArrayList<>();

		AllProjectsAdapter allProjectsAdapter = new AllProjectsAdapter();
		stoppedProjectList = allProjectsAdapter.getAllMagicXpiProjects();
		runningProjectList = runningProjectList();

		for (ProjectBean projectBeanStopped : stoppedProjectList) {

			if (projectBeanStopped.getStatus() == null) {
				projectBeanStopped.setStatus("STOPPED");
			}

			for (ProjectBean projectBeanRunning : runningProjectList) {
				if (projectBeanStopped.getStatus() != null && projectBeanRunning.getStatus() != null
						&& projectBeanStopped.getProjectKey().equals(projectBeanRunning.getProjectKey())
						&& !projectBeanStopped.getStatus().equals(projectBeanRunning.getStatus())) {

					if ((projectBeanRunning.getProjectKey()).equalsIgnoreCase(projectBeanStopped.getProjectKey())) {
						projectBeanRunning.setProjectpathWithStartXml(projectBeanStopped.getProjectpathWithStartXml());
					}

					stoppedProjectList.set(stoppedProjectList.indexOf(projectBeanStopped), projectBeanRunning);

				}
			}
		}

		return stoppedProjectList;
	}

	@GetMapping("/getSpaceInstances")
	public List<MagicXpiSpaceInstances> getSpaceInstances() {
		return spaceServiceService.getSpaceInstances();
	}

	@GetMapping("/getHostAndEnginesDetails")
	public Map<String, List<MagicXpiHostAndEngines>> getHostAndEnginesDetails() {
		return spaceServiceService.getHostAndEnginesDetails();
	}

	@GetMapping("/loadProjectMetadata")
	public MonitorOfflineMetadata loadProjectMetadata(@RequestParam String projectKey,
			@RequestParam String projectLocation) {
		return spaceServiceService.loadProjectMetadata(projectKey, projectLocation);
	}

	@RequestMapping(path = "/getActivityLogMsgFilters", method = RequestMethod.POST, consumes = "application/json")
	public List<ActivityMsgFilterMetadata> loadActMsgFilterMetadata(@RequestBody QueryFilters queryFilters) {
		return spaceServiceService.loadActMsgFilterMetadata(queryFilters);
	}

	@RequestMapping(path = "/writeActivityLogMsgFilters", method = RequestMethod.POST, consumes = "application/json")
	public String writeActivityLogMsgFilters(@RequestBody QueryFilters queryFilters) {
		return spaceServiceService.writeActivityLogMsgFilters(queryFilters);
	}

	@GetMapping("/triggersActivityStatistics/{projectkey}/{type}")
	public TriggerActivityGraphRes getTriggersActivityStatistics(@PathVariable @NotNull String projectkey,
			@PathVariable @NotNull String type) {
		return spaceServiceService.getTriggersActivityStatistics(projectkey, type);
	}

	@GetMapping("/alertsByProject/{projectKey}")
	public Alert[] getAlertsByProject(@PathVariable @NotNull String projectKey) {
		return spaceServiceService.getAlertsByProject(projectKey);
	}

	@GetMapping("/flowReqHistory/{projectkey}/{type}")
	public Object getFlowReqHistory(@PathVariable @NotNull String projectkey, @PathVariable String type) {
		return spaceServiceService.getFlowReqHistory(projectkey, type);
	}

	@GetMapping("/licenseDetails/{projectKey}/{filterType}")
	public HashMap<String, Object> getLicenseDetailsByProjectKeyAndDateCreated(@PathVariable @NotNull String projectKey,
			@PathVariable @NotNull String filterType) {
		return licenseSummaryImpl.findByProjectKeyAndDateCreated(projectKey, filterType);
	}

	@GetMapping("/summary/{projectkey}")
	public Object getFlowReqHistory(@PathVariable @NotNull String projectkey) {
		return spaceServiceService.getSummary(projectkey);
	}

}
