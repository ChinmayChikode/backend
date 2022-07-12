package com.magicsoftware.monitor.controller;

import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.magicsoftware.monitor.model.ActivityLog;
import com.magicsoftware.monitor.model.ActivityLogColor;
import com.magicsoftware.monitor.model.ActivityMsgFilterMetadata;
import com.magicsoftware.monitor.model.BamDetails;
import com.magicsoftware.monitor.model.BamFilters;
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
import com.magicsoftware.monitor.model.OdsDetails;
import com.magicsoftware.monitor.model.PSSWithFLowName;
import com.magicsoftware.monitor.model.ProjectBean;
import com.magicsoftware.monitor.model.ProjectOperations;
import com.magicsoftware.monitor.model.QueryFilters;
import com.magicsoftware.monitor.model.SchedulerDetails;
import com.magicsoftware.monitor.model.Series;
import com.magicsoftware.monitor.model.ServerAndWorkerData;
import com.magicsoftware.monitor.model.ServerDetails;
import com.magicsoftware.monitor.model.ServerInstanceDetails;
import com.magicsoftware.monitor.model.TriggerActivityGraphRes;
import com.magicsoftware.monitor.model.TriggerDetails;
import com.magicsoftware.monitor.query.QueryFactory;
import com.magicsoftware.monitor.service.ActivityLogService;
import com.magicsoftware.monitor.service.SpaceService;
import com.magicsoftware.monitor.serviceimpl.LicenseDetailsImpl;
import com.magicsoftware.monitor.serviceimpl.LicenseSummaryImpl;
import com.magicsoftware.monitor.serviceimpl.ODSDataServiceImpl;
import com.magicsoftware.monitor.util.AllProjectsAdapter;
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
	private SpaceService spaceServiceService;

	@Autowired
	private ODSDataServiceImpl oDSDataServiceImpl;

	@Autowired
	private LicenseSummaryImpl licenseSummaryImpl;
	
	@Autowired
	private LicenseDetailsImpl licenseDetailsImpl;

	@GetMapping("/getLicenseProjectDetails/{projectKey:.+}/{type}")
	public ChartBean getLicenseProjectDetails(@PathVariable @NotNull String projectKey, @PathVariable @NotNull String type) {
		return licenseDetailsImpl.findLicenseDetails(projectKey, type);
	}
	
	@GetMapping("/getLicenseListProjectDetails/{projectKey:.+}/{type}")
	public List<LicenseDetails> getLicenseListProjectDetails(@PathVariable @NotNull String projectKey, @PathVariable @NotNull String type) {
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
	public List<Series> getPeaklicenseByHostByRange(@PathVariable @NotNull String projectKey, @RequestParam("sDate") @NotNull Date sDate, 
			@RequestParam("eDate") @NotNull Date eDate) {
			return licenseDetailsImpl.findLicenseDetailsForHostByRange(projectKey, sDate, eDate);
	}
	
	@GetMapping("/getTotalPeaklicense/{projectKey:.+}")
	public List<Series> getTotalPeaklicense(@PathVariable @NotNull String projectKey) {
		return licenseDetailsImpl.findTotalLicenseDetails(projectKey, "Total Peak License");
	}
	
	@GetMapping("/getDefaultPeaklicenseByRange/{projectKey}")
	public ChartBean getDefaultPeaklicenseByRange(@PathVariable @NotNull String projectKey, @RequestParam("sDate") @NotNull Date sDate, 
			@RequestParam("eDate") @NotNull Date eDate) {
		return licenseDetailsImpl.findLicenseDetailsForDefaultGraphByRage(projectKey, sDate, eDate);
	}
	
	
	@GetMapping("/getLicenseProjectDetails/{type}")
	public ChartBean getLicenseDetails(@PathVariable @NotNull String type) {
		return licenseDetailsImpl.findLicenseDetails(type);
	}
	
	@GetMapping("/getLicenseListProjectDetails/{projectKey}")
	public List<LicenseDetails> getLicenseListProjectDetails(@PathVariable @NotNull String projectKey, @RequestParam("sDate") @NotNull Date sDate, 
			@RequestParam("eDate") @NotNull Date eDate) {
		return licenseDetailsImpl.findLicenseListDetailsByDate(projectKey, sDate, eDate);
	}
	
	@GetMapping("/getLicenseProjectDetailsByDate")
	public ChartBean getLicenseDetails(@RequestParam("sDate") @NotNull Date sDate, 
			@RequestParam("eDate") @NotNull Date eDate) {
		return licenseDetailsImpl.findLicenseDetails("custom", sDate, eDate);
	}
	
	@GetMapping("/odsDataByProjectkey/{projectkey}")
	public OdsDetails odsDataByProjectkey(@PathVariable @NotNull String projectkey) {
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
	
//	@GetMapping("/userblob") 
//	 public byte[] getUserBlob(@RequestParam String projectkey,@RequestParam int msgid)
//	 { 
//		 return activityLogService.findBlob(projectkey, msgid);
//	 }
	
	
	@GetMapping("/startServer/{projectKey}/{serverID}")
	public String startServer(@PathVariable String projectKey, @PathVariable int serverID) {
		SpaceServices spaceServices = new SpaceServices();
		List<Status> statuses = Arrays.asList(Status.STOPPED);
		spaceServices.startServer(projectKey, serverID, statuses);
		return "";
	}
	
	
	
	@RequestMapping(value="/demo")
	public String demo() {
		return "user_blob_file";
	}
	

	@GetMapping("/displayblob")
	public HashMap<String, String> getblob(@RequestParam @NotNull String projectKey,@RequestParam @NotNull int msgid){

		 HashMap<String,String> result=new HashMap<String, String>();
		 result.put("format", activityLogService.getBlob(projectKey,msgid));
		 return result;

	}
	
	@GetMapping("/displayOdsBlob")
	public void getOdsBlob(@RequestParam @NotNull String projectKey,@RequestParam @NotNull double usernumber){

		System.out.println("Call in dispay blob");
		 oDSDataServiceImpl.getBlob(projectKey,usernumber);
			System.out.println("Call done" );
		 //return null;

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

	@GetMapping("/schedulers")
	public Scheduler[] getScheduler() {
		return spaceServiceService.getScheduler();
	}

	@GetMapping("/scheduler")
	public SchedulerDetails getScheduler(@RequestParam String projectKey, @RequestParam String projectLocation) {
		return spaceServiceService.getscheduler(projectKey,projectLocation);
	}
		
	@GetMapping("/PSSData/")
	public List<PSSWithFLowName> getPSSData(@RequestParam String projectKey,
			@RequestParam String projectLocation) {
		return spaceServiceService.getPSSDataWithFLowName(projectKey,projectLocation);
		
	}

	@GetMapping("/activityLog/{projectkey}") 
	 public BamDetails activityLog(@PathVariable @NotNull String projectkey)
	 { 
		 return activityLogService.findByProjectkey(projectkey);
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
		System.out.println("execting can work enable");
		return spaceServiceService.canWorkEnable(projectKey, bpId, flowId);
	}

	@GetMapping("/canWorkBufferLimitation/{projectKey}/{bpId}/{flowId}/{triggerStepId}")
	public boolean canWorkBufferLimitation(@PathVariable @NotNull String projectKey, @PathVariable long bpId,
			@PathVariable long flowId, @PathVariable long triggerStepId) {
		return spaceServiceService.canWorkBufferLimitation(projectKey, bpId, flowId, triggerStepId);
	}

	
//	  @GetMapping("/activityLog/{projectkey}/{page}/{size}") public
//	  List<ActivityLog> activityLog(@PathVariable @NotNull String
//	  projectkey, @PathVariable Integer page, @PathVariable Integer size) { return
//	  activityLogService.findByProjectkey(projectkey, PageRequest.of(page, size));
//	  }
	 

	@RequestMapping(path = "/activityLog", method = RequestMethod.POST, consumes = "application/json")
	public List<ActivityLogColor> activityLog(@RequestBody QueryFilters queryFiltersDTO) {
		try {
			return activityLogService.getMonitorActivityLog(queryFiltersDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	@GetMapping("/deleteServer/{projectKey}/{serverID}")
	public void deleteServer(@PathVariable String projectKey, @PathVariable int serverID )
	{
		System.out.println(projectKey+serverID);
		SpaceServices spaceServices = new SpaceServices();
		List<Status> statuses = Arrays.asList(Status.STOPPED);
		try {
			spaceServices.deleteServer(projectKey,serverID,statuses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(path = "/startProject", method = RequestMethod.POST, consumes = "application/json")
	public String startProject(@RequestBody ProjectOperations projectOperations) {
		RTVEntity r = connect();
		PrintStream logs = null;
		// filePath += "\\" + projectKey + "\\start.xml";
		System.out.println(projectOperations.projectKey);
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

	@GetMapping("/updateFlowStatus")
	public boolean updateFlowStatus(@RequestParam String projectKey, @RequestParam int bpId, @RequestParam int flowId,
			@RequestParam boolean isEnable) {
		SpaceServices spaceServices = new SpaceServices();
		try {
			return spaceServices.updateFlowEnableStatus(projectKey, bpId, flowId, isEnable);

		} catch (ApplicationException e) {
			e.printStackTrace();
			return false;// TODO ganesh :- need to change it with logger
		}

	}
	
//	@PatchMapping("/updateFlowStatus/{projectKey}/{bpId}/{flowId}/{isEnable}")
//	public String updateFlowStatus(@PathVariable String projectKey, @PathVariable int bpId, @PathVariable int flowId,
//			@PathVariable boolean isEnable) {
//		SpaceServices spaceServices = new SpaceServices();
//		try {
//			spaceServices.updateFlowEnableStatus(projectKey, bpId, flowId, isEnable);
//			return "true";
//		} catch (ApplicationException e) {
//			e.printStackTrace(); 
//			return "didnt get executed";// TODO ganesh :- need to change it with logger
//		}
//		
//	}

	@GetMapping("/stopServer/{projectKey}/{serverID}/{timeout}")
	public String stopServer(@PathVariable String projectKey, @PathVariable int serverID, @PathVariable int timeout) {
		SpaceServices spaceServices = new SpaceServices();
		List<Status> statuses = Arrays.asList(Status.RUNNING);
		spaceServices.stopServer(projectKey, serverID, timeout, statuses);
		return "";
	}

	private RTVEntity connect() {// TODO ganesh :- remove this method
		RTVEntity r = new RTVEntity();
		r.connect("MAGIC_SPACE", "Magic_xpi_4.13_CHINMAYCLPT", "localhost");
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
			//projectBean.setProjectLocation("");
			//projectBean.setWorkers(serverChild.getNumberOfWorkers() + "");
			projectBean.setWorkers("");
			projectBean.setReserverLivenseThread(projectData.getReservedLicenseThreads() + "");
			projectBean.setStatus(projectData.getProjectState().toString());
			projectBean.setUptime(QueryFactory.defaultLocalFormat.format(projectData.getCreatedTimestamp()) + "");
			projectBean.setEngines("");
			projectBean.setMessages("");
			projectBean.setAlert("");
			projectBean.setCurrentLicenseThread("");
			//projectBean.setLoadInDebugMode(serverChild.getLoadInDebugMode());
			projectBean.setLoadInDebugMode(false);
			projectBean.setServerdetail(serverdetail);
			//projectBean.setWorkerDetais(spaceServiceService.worker());
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
	
//		@GetMapping("/activityLog/{projectkey}") 
//		 public ArrayList<ActivityLog> bamDetails(@PathVariable @NotNull String projectkey)
//		 { 
//			 return activityLogService.getBamDetails(projectkey);
//		 }
		
		//method to get data according to filter
		
//		@RequestMapping(path = "/filteredBamLog", method = RequestMethod.POST, consumes = "application/json")
//		public List<ActivityLog> filteredBamDetails(@RequestBody QueryFilters queryFiltersDTO) {	
//				String userkey1 = queryFiltersDTO.getUserkey1();
//				String userkey2 = queryFiltersDTO.getUserkey2();
//				String projectkey = queryFiltersDTO.getProjKey();
//				String fromDate = queryFiltersDTO.getLblFromDateValue();
//				String toDate = queryFiltersDTO.getLblToDateValue();
//				
//				System.out.println("values to be filtered are : "+ userkey1 + userkey2 + projectkey+fromDate+toDate);
//				return activityLogService.getFilteredBamDetails(projectkey,userkey1,userkey2);				
//				return activityLogService.getFilteredBamDetails(projectkey,userkey1,userkey2,fromDate,toDate);
//		}
			
//			try {
//				return activityLogService.getMonitorActivityLog(queryFiltersDTO);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//			
//		}
	
	
	//Method for BAM filte
		@RequestMapping(path = "/filteredBamLog", method = RequestMethod.POST, consumes = "application/json")
		public List<ActivityLog> filteredBamDetails(@RequestBody BamFilters bamFilters) throws ParseException{
			 
//			System.out.println(data);
//			BamFilters bamFilters=new BamFilters();
			System.out.println(bamFilters.getProjKey());	
			
			String ProjKey = bamFilters.getProjKey() ;
		//	String ProjLocation = bamFilters.getProjLocation() ;
			String userkey1 = bamFilters.getUserkey1() ;
			String userkey2 = bamFilters.getUserkey2() ;
			String lblFromDateValue = bamFilters.getLblFromDateValue() ;
			String lblToDateValue = bamFilters.getLblToDateValue() ;
			
				 System.out.println("lblFromDateValue is :::::"+lblFromDateValue+":::end");	
				 System.out.println("lblToDateValue is :::::"+lblToDateValue+":::end");	
				
				 //filter according to dates
				if(lblFromDateValue.length() != 0 && lblToDateValue.length() != 0 && userkey1.length() == 0 && userkey2.length() == 0) {
					System.out.println("filtering by date called...");
					return activityLogService.getFilteredBamDetailsOnDate(ProjKey,lblFromDateValue,lblToDateValue);
				}
				
				//filter on userkey
				else if(lblFromDateValue.length() == 0 && lblToDateValue.length() == 0 && userkey1.length() != 0 && userkey2.length() != 0) {
					 System.out.println("filtering by userkey called...");
					return activityLogService.getFilteredBamDetailsOnUserkey(ProjKey,userkey1,userkey2);
				}
				
				//filter on all parameters
				else if(lblFromDateValue.length() != 0 && lblToDateValue.length() != 0 && userkey1.length() != 0 && userkey2.length() != 0) {
					 System.out.println("filtering on all parameters...");
					 return activityLogService.getFilteredBamDetailsOnAll(ProjKey,lblFromDateValue,lblToDateValue,userkey1,userkey2);
					//return activityLogService.getFilteredBamDetailsOnUserkey(projKey,userkey1,userkey2);
				}
				else 
				return null;
				
		//	return activityLogService.getFilteredBamDetails(projKey,userkey1,userkey2);
				
		 }
		@GetMapping("/newServerInstance/{projectkey}")
		public String newServerInstancetest(@PathVariable("projectkey") @NotNull String projectkey,@RequestParam String Server_host, @RequestParam String Alternate_host,
				@RequestParam String project_directory,@RequestParam int Number_of_instances,@RequestParam Boolean Load_triggers,
				@RequestParam Boolean Load_Schedulers,@RequestParam Boolean Load_auto_start,@RequestParam int Number_of_workers){
			
//			System.out.println(projectkey+" "+ Server_host +" " + Alternate_host + " " + project_directory + " " + 
//					Number_of_instances + " " + Load_triggers + " " + Load_Schedulers + " " + Load_auto_start + " " + Number_of_workers);

			
			return spaceServiceService.newServerInstance(projectkey,Server_host, Alternate_host,
					 project_directory,Number_of_instances, Load_triggers,Load_Schedulers, Load_auto_start, Number_of_workers);

			
		}
		
		
		@GetMapping("/invokeFlowByScheduler/")
		public int invokeFlowByScheduler(@RequestParam String projectKey,@RequestParam int bpId,@RequestParam int flowId
		,@RequestParam int triggerId,@RequestParam int schedulerId) throws InstantiationException, IllegalAccessException, ApplicationException {
			
			System.out.println("projectKey:"+projectKey+" "+"BpId:"+bpId +" "+"FlowId:"+flowId+" "+"triggerId:"+triggerId+" "+"schedulerId:"+schedulerId);
			
			return spaceServiceService.invokeFlowByScheduler(projectKey, bpId, flowId, triggerId, schedulerId);
		}


			
}


