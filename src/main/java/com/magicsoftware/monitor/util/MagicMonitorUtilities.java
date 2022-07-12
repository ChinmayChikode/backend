package com.magicsoftware.monitor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.magicsoftware.monitor.model.ActivityMsgFilterMetadata;
import com.magicsoftware.monitor.model.BpModel;
import com.magicsoftware.monitor.model.FlowModel;
import com.magicsoftware.monitor.model.MonitorOfflineMetadata;
import com.magicsoftware.monitor.model.MsgInvocationModel;
import com.magicsoftware.monitor.model.QueryFilters;
import com.magicsoftware.monitor.model.StatusModel;
import com.magicsoftware.monitor.model.StepModel;
import com.magicsoftware.monitor.serviceimpl.SpaceServiceImpl;
import com.magicsoftware.xpi.server.common.ApplicationException;
import com.magicsoftware.xpi.server.data.server.ServerData.Status;

public class MagicMonitorUtilities {

	public enum MessageStatus {
		ABORTED, CHECKING, DONE, FAILED, IN_PROCESS, NEW, READY_FOR_USE, RECOVERING, UNDEFINED
	}

	public enum MsgInvocationType {
		
		AUTO_REPEAT("Auto Repeat"),
		AUTO_START("Auto Start"),
		CLEAN_UP_RECOVERY("Clean Up Recovery"),
		HL7("HL7"),
		HTTP("HTTP"),
		INVOKE_FLOW_ASYNC("Invoke Flow(Async)"),
		PSS("PSS"),
		PARALLEL("Parallel"),
		RESTART_TIME_RECOVERY("Restart(Timeout Recovery)"),
		RESTART_CRASH_RECOVERY("Restart(Crash Recovery)"),
		SAP_R3("SAP R3"),
		SDK_EXTERNAL("SDK External"),
		SCHEDULER("Scheduler"),
		STAND_ALONE("Stand Alone"),
		TCP("TCP"),
		TRIGGER_ASYNC("Trigger(Async)"),
		TRIGGER_SYNC("Trigger(Sync)"),
		WS("WS");

		private final String msgInvocationType;

		MsgInvocationType(String msgInvocationType) {
			this.msgInvocationType = msgInvocationType;
		}
		
		@Override
		public String toString() {
		    return msgInvocationType;
		}
	}

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM,dd yyyy HH:mm:ss.SSS");
	private static BufferedReader fileReader;

	public static String readPropertyValueFromIFSINIFile(String filename, String groupname, String property) {

		String propertyValue = "";

		try {

			File file = new File(filename);

			if (file.exists()) {

				fileReader = new BufferedReader(new FileReader(filename));
				String inputIterator;

				while ((inputIterator = fileReader.readLine()) != null) {
					if (inputIterator.length() > 0) {
						String[] keyValuePair = inputIterator.split("=");
						if (keyValuePair.length == 2
								&& keyValuePair[0].trim().contains(("[" + groupname + "]" + property))) {
							propertyValue = keyValuePair[1].trim();
							break;
						}
					}
				}

				fileReader.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return propertyValue;
	}

	public static MonitorOfflineMetadata readMonitorOfflineMetadata(String projectKey, String projectLocation) {

		MonitorOfflineMetadata monitorOfflineMetadata = null;

		List<BpModel> bpList = null;
		List<FlowModel> flowList = null;
		List<StepModel> stepList = null;

		if (projectLocation.length() > 0) {
			projectLocation = projectLocation.substring(0, projectLocation.length());
		}

		String seperator = System.getProperty("file.separator");
		String actlogFilterXMLAtProjectLevel = projectLocation + seperator + "MonitorOfflineMetadata.xml";

		File fXmlFileDisplay = new File(actlogFilterXMLAtProjectLevel);

		if (!fXmlFileDisplay.exists()) {
			actlogFilterXMLAtProjectLevel = projectLocation + seperator + projectKey + seperator
					+ "MonitorOfflineMetadata.xml";
		}

		fXmlFileDisplay = new File(actlogFilterXMLAtProjectLevel);

		if (fXmlFileDisplay.exists()) {

			Document docDisplay = readActivityLogFilterXMLDocument(fXmlFileDisplay);

			NodeList listOfBPs = docDisplay.getElementsByTagName("BusinessProcess");

			monitorOfflineMetadata = new MonitorOfflineMetadata();

			bpList = new ArrayList<BpModel>();
			flowList = new ArrayList<FlowModel>();
			stepList = new ArrayList<StepModel>();

			BpModel bpModel = new BpModel();
			bpModel.setBpId(null);
			bpModel.setBpName("ALL");
			bpList.add(bpModel);

			FlowModel flowModel = new FlowModel();
			flowModel.setFlowId(null);
			flowModel.setFlowName("ALL");
			flowList.add(flowModel);

			for (int bpCounter = 0; bpCounter < listOfBPs.getLength(); bpCounter++) {
				bpModel = new BpModel();

				Node firstBPNode = listOfBPs.item(bpCounter);

				if (firstBPNode.getNodeType() == Node.ELEMENT_NODE) {
					Element bpElement = (Element) firstBPNode;

					NodeList bpIdList1 = bpElement.getElementsByTagName("ID");
					Element bpIdElement = (Element) bpIdList1.item(0);
					NodeList bpIdList2 = bpIdElement.getChildNodes();

					NodeList bpNameList1 = bpElement.getElementsByTagName("Name");
					Element bpNameElement = (Element) bpNameList1.item(0);
					NodeList bpNameList2 = bpNameElement.getChildNodes();

					System.out.println("BP-ID=>" + ((Node) bpIdList2.item(0)).getNodeValue().trim());
					System.out.println("BP-Name=>" + ((Node) bpNameList2.item(0)).getNodeValue().trim());

					bpModel.setBpId(((Node) bpIdList2.item(0)).getNodeValue().trim());
					bpModel.setBpName(((Node) bpNameList2.item(0)).getNodeValue().trim());

					bpList.add(bpModel);

					monitorOfflineMetadata.setBpList(bpList);

					NodeList listOfFlows = bpElement.getElementsByTagName("Flow");

					for (int flowCounter = 0; flowCounter < listOfFlows.getLength(); flowCounter++) {

						Node firstFlowNode = listOfFlows.item(flowCounter);

						flowModel = new FlowModel();

						if (firstFlowNode.getNodeType() == Node.ELEMENT_NODE) {

							Element flowElement = (Element) firstFlowNode;

							NodeList flowIdList1 = flowElement.getElementsByTagName("ID");
							Element flowIdElement = (Element) flowIdList1.item(0);
							NodeList flowIdList2 = flowIdElement.getChildNodes();

							NodeList flowNameList1 = flowElement.getElementsByTagName("Name");
							Element flowNameElement = (Element) flowNameList1.item(0);
							NodeList flowNameList2 = flowNameElement.getChildNodes();

							System.out.println("Flow-ID=>" + ((Node) flowIdList2.item(0)).getNodeValue().trim());
							System.out.println("Flow-Name=>" + ((Node) flowNameList2.item(0)).getNodeValue().trim());

							flowModel.setFlowId(((Node) flowIdList2.item(0)).getNodeValue().trim());
							flowModel.setFlowName(((Node) flowNameList2.item(0)).getNodeValue().trim());

							flowList.add(flowModel);

							monitorOfflineMetadata.setFlowList(flowList);

							NodeList listOfStepNames = flowElement.getElementsByTagName("Step");

							for (int i = 0; i < listOfStepNames.getLength(); i++) {

								Node firstStepNode = listOfStepNames.item(i);

								StepModel stepModel = new StepModel();

								if (firstStepNode.getNodeType() == Node.ELEMENT_NODE) {

									Element StepElement = (Element) firstStepNode;

									NodeList stepIdList1 = StepElement.getElementsByTagName("ID");
									Element stepIdElement = (Element) stepIdList1.item(0);
									NodeList stepIdList2 = stepIdElement.getChildNodes();

									NodeList stepNameList1 = StepElement.getElementsByTagName("Name");
									Element stepNameElement = (Element) stepNameList1.item(0);
									NodeList stepNameList2 = stepNameElement.getChildNodes();

									System.out
											.println("Step-ID=>" + ((Node) stepIdList2.item(0)).getNodeValue().trim());
									System.out.println(
											"Step-Name=>" + ((Node) stepNameList2.item(0)).getNodeValue().trim());

									stepModel.setStepId(((Node) stepIdList2.item(0)).getNodeValue().trim());
									stepModel.setStepName(((Node) stepNameList2.item(0)).getNodeValue().trim());

									stepList.add(stepModel);

									monitorOfflineMetadata.setStepList(stepList);
								}
							}
						}
					}
				}
				System.out.println("---------------------------");
			}

		}

		return monitorOfflineMetadata;
	}

	public static Document readActivityLogFilterXMLDocument(File fXmlFileDisplay) {

		DocumentBuilderFactory dbFactoryDisplay;
		DocumentBuilder dBuilderDisplay;
		Document docDisplay = null;

		try {
			dbFactoryDisplay = DocumentBuilderFactory.newInstance();
			dBuilderDisplay = dbFactoryDisplay.newDocumentBuilder();
			docDisplay = dBuilderDisplay.parse(fXmlFileDisplay.getAbsolutePath());
			docDisplay.getDocumentElement().normalize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return docDisplay;
	}

	public static String getInvocationType(int invokeType) {
		String invocationType = "";
		switch (invokeType) {

		case -60:
			invocationType = "SDK External";
			break;

		case -61:
			invocationType = "HTTP";
			break;

		case -62:
			invocationType = "WS";
			break;

		case -63:
			invocationType = "TCP";
			break;

		case -64:
			invocationType = "SAP R3";
			break;

		case -65:
			invocationType = "HL7";
			break;

		case -58:
			invocationType = "Restart (Timeout recovery)";
			break;

		case -57:
			invocationType = "Restart (crash recovery)	";
			break;

		case -56:
			invocationType = "Invoke Flow(Async)";
			break;

		case -55:
			invocationType = "CleanUpRecovery";
			break;

		case -54:
			invocationType = "PSS";
			break;

		case -53:
			invocationType = "Auto Repeat";
			break;

		case -52:
			invocationType = "Auto Start";
			break;

		case -51:
			invocationType = "Trigger (Async)";
			break;

		case -50:
			invocationType = "Scheduler";
			break;

		case -2:
			invocationType = "Error Flow";
			break;

		case -1:
			invocationType = "Logic Flow";
			break;

		case 1:
			invocationType = "Stand alone";
			break;

		case 2:
			invocationType = "Empty (Call Flow (Deprecated))-NOT ALLOW";
			break;

		case 3:
			invocationType = "Empty (used by Recovery Flow , but not any more)";
			break;

		case 4:
			invocationType = "Stand alone";
			break;

		case 5:
			invocationType = "Empty (HTTP Recovery (Deprecated))-NOT ALLOW";
			break;

		case 6:
			invocationType = "Invoke Flow";
			break;

		case 7:
			invocationType = "Call Flow (Mapper)";
			break;

		case 8:
			invocationType = "Error Flow (Mapper)";
			break;

		case 9:
			invocationType = "parallel";
			break;

		case 15:
			invocationType = "Trigger (Sync)";
			break;
		default:
			invocationType = "";
			break;
		}
		return invocationType;
	}

	public static String formatDate(Date date) {
		return simpleDateFormat.format(date);
	}
	
	public static String changeStringtoDatefinal(String string) throws ParseException {
		String createTime = string;
		String year = createTime.substring(0,4);
		String month = createTime.substring(4,6);
		String day = createTime.substring(6,8);
		String hr = createTime.substring(8,10);
		Integer hr1=Integer.valueOf(hr);
		Integer hrf=hr1-12;
		String min = createTime.substring(10,12);
		String sec = createTime.substring(12,14);		
		String createTime1 =year+"-"+month+"-"+day+" " + hr+":"+ min+":"+ sec + " AM";
	//	System.out.println(createTime1);
		
		if(hrf>=0)
		{
			if(hrf==0) 
			{
				String createTime2 =year+"-"+month+"-"+day+" " + hr+":"+ min+":"+ sec + " PM";
				//System.out.println(createTime2);
				return createTime2;
			}
			else
			{
				if(hr1<=21)
				{
					String createTime2 =year+"-"+month+"-"+day+" " +"0"+ hrf+":"+ min+":"+ sec + " PM";
					//System.out.println(createTime2);
					return  createTime2;
				}
				else 
				{
					String createTime2 =year+"-"+month+"-"+day+" " + hrf+":"+ min+":"+ sec + " PM";
					//System.out.println(createTime2);
					return  createTime2;			   
				}
				
			}
			
		}
		else
			{
			  if(hr1==00)
			  {
				  int hr2=hr1;
				  hr2+=12;
				  String createTime3 =year+"-"+month+"-"+day+" " + hr2+":"+ min+":"+ sec + " AM";
				  return createTime3;
			  }	  
			  else
			  {
				  return createTime1;  
			  }
			  
		}
	}		
	
	
	

	public static String getTriggerType(int typeId) {
		String triggerType = "";

		switch (Math.abs(typeId)) {
		case 0:
			triggerType = "Auto Start";
			break;
		case 8:
			triggerType = "Scheduler";
			break;
		case 25:
			triggerType = "Web Services";
			break;
		case 103:
			triggerType = "Directory Scanner";
			break;
		case 104:
			triggerType = "Domino";
			break;
		case 106:
			triggerType = "Email";
			break;
		case 110:
			triggerType = "HTTP";
			break;
		case 111:
			triggerType = "System i Connector";
			break;
		case 115:
			triggerType = "JMS";
			break;
		case 117:
			triggerType = "WebSphere MQ";
			break;
		case 118:
			triggerType = "MSMQ";
			break;
		case 128:
			triggerType = "SAPB1 2004";
			break;
		case 133:
			triggerType = "SAPB1 2005";
			break;
		case 141:
			triggerType = "SAPB1 2006";
			break;
		case 160:
			triggerType = "SAPB1 8.8";
			break;
		case 134:
			triggerType = "W4";
			break;
		case 140:
			triggerType = "SAP R/3";
			break;
		case 143:
			triggerType = "Salesforce";
			break;
		case 149:
			triggerType = "HL7";
			break;
		case 153:
			triggerType = "SAP A1";
			break;
		case 157:
			triggerType = "Exchange 2007";
			break;
		case 162:
			triggerType = "SugarCRM";
			break;
		case 18:
			triggerType = "Scheduler Service";
			break;
		case 102:
			triggerType = "COM";
			break;
		case 105:
			triggerType = "EJB";
			break;
		case 158:
			triggerType = "TCP Listener";
			break;
		default:
			if (Math.abs(typeId) >= 1000)
				triggerType = "Component SDK";
			else
				triggerType = "";
			break;
		}
		return triggerType;
	}

	public static String calculateUpTime(Date createdTimestamp) {

		Date currentTimestamp = new Date();
		String displayWrkrUpTime;
		try {

			long diff = currentTimestamp.getTime() - createdTimestamp.getTime();
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			displayWrkrUpTime = diffDays + " Days and " + diffHours + " hours";

		} finally {
			currentTimestamp = null;
			createdTimestamp = null;
		}
		return displayWrkrUpTime;
	}

	public static String getMessageType(int messageTypeId) {
		String messageType = "";
		switch (messageTypeId) {
		case 1:
			messageType = "Server started";
			break;
		case 2:
			messageType = "Recover flow";
			break;
		case 3:
			messageType = "Server stopped";
			break;
		case 4:
			messageType = "Flow started";
			break;
		case 5:
			messageType = "Flow completed";
			break;
		case 6:
			messageType = "Request to abort flow";
			break;
		case 7:
			messageType = "Flow aborted";
			break;
		case 8:
			messageType = "Flow timeout";
			break;
		case 9:
			messageType = "Error flow called";
			break;
		case 10:
			messageType = "Logic flow called";
			break;
		case 11:
			messageType = "Flow component started";
			break;
		case 12:
			messageType = "Flow component completed";
			break;
		case 13:
			messageType = "Flow component timeout";
			break;
		case 14:
			messageType = "User-defined message";
			break;
		case 15:
			messageType = "Error";
			break;
		case 16:
			messageType = "ODS record created";
			break;
		case 17:
			messageType = "ODS record updated";
			break;
		case 18:
			messageType = "ODS record deleted";
			break;
		case 19:
			messageType = "ODS Flow sequence records cleared";
			break;
		case 20:
			messageType = "Event published";
			break;
		case 21:
			messageType = "Flow subscribed";
			break;
		case 22:
			messageType = "Subscription removed";
			break;
		case 23:
			messageType = "Resource locked";
			break;
		case 24:
			messageType = "Failed to unlock a resource";
			break;
		case 25:
			messageType = "Resource unlocked";
			break;
		case 26:
			messageType = "Failed to lock a resource";
			break;
		case 29:
			messageType = "Jump steps in flow";
			break;
		case 30:
			messageType = "Flow restarted";
			break;
		case 31:
			messageType = "Flow component skipped due to recovery";
			break;
		case 32:
			messageType = "Flow component reexecuted due to recovery";
			break;
		case 33:
			messageType = "Start commit for flow";
			break;
		case 34:
			messageType = "Start rollback for flow";
			break;
		case 35:
			messageType = "Commit flow component";
			break;
		case 36:
			messageType = "Rollback flow component";
			break;
		case 42:
			messageType = "Scheduled flow started";
			break;
		case 43:
			messageType = "Waiting for completion of previous steps";
			break;
		case 44:
			messageType = "Routed flow started";
			break;
		case 45:
			messageType = "Recovery data cleared";
			break;
		case 46:
			messageType = "Flow Cleanup program called";
			break;
		case 47:
			messageType = "Save-point program called";
			break;
		case 48:
			messageType = "Restore save-point data program called";
			break;
		case 49:
			messageType = "Save-point recovered";
			break;
		case 50:
			messageType = "BAM message";
			break;
		case 51:
			messageType = "Call flow information";
			break;
		case 52:
			messageType = "Process has been terminated";
			break;
		case 53:
			messageType = "Disabled flow called";
			break;
		case 54:
			messageType = "Flow has been disabled";
			break;
		case 55:
			messageType = "Flow has been enabled";
			break;
		case 56:
			messageType = "Component Logging";
			break;
		case 57:
			messageType = "Jms listener thread has been loaded";
			break;
		case 58:
			messageType = "Trigger Started";
			break;
		case 59:
			messageType = "Rollback DB Transaction for Flow";
			break;
		case 60:
			messageType = "Commit DB Transaction for Flow";
			break;
		case 61:
			messageType = "Inactive/NonExisting Flow called";
			break;
		}
		return messageType;
	}
	
public static List<String> getTriggerTypes(){
		
		List<String> allTriggerTypes = new ArrayList<String>();
		allTriggerTypes.add("ALL");
		allTriggerTypes.add("Web Services");
		allTriggerTypes.add("Directory Scanner");
		allTriggerTypes.add("Email");
		allTriggerTypes.add("HTTP");
		allTriggerTypes.add("IBM i");
		allTriggerTypes.add("JMS");
		allTriggerTypes.add("WebSphere MQ");
		allTriggerTypes.add("MSMQ");
		allTriggerTypes.add("SAPB1 2004");
		allTriggerTypes.add("SAPB1 2005");
		allTriggerTypes.add("SAPB1 2007");
		allTriggerTypes.add("SAPB1 8.8");
		allTriggerTypes.add("SAP R/3");
		allTriggerTypes.add("Salesforce");
		allTriggerTypes.add("HL7");
		allTriggerTypes.add("SAP A1");
		allTriggerTypes.add("Exchange");
		allTriggerTypes.add("Sugar");
		allTriggerTypes.add("Scheduler Utility");
		allTriggerTypes.add("Scheduler Service");
		allTriggerTypes.add("TCP Listener");
		allTriggerTypes.add("Component SDK");
		
		return allTriggerTypes;
		
	}
	
	public static List<String> getTriggerStates(){
		
		List<String> allTriggerStates = new ArrayList<String>();
		allTriggerStates.add("ALL");
		allTriggerStates.add("UNDEFINED");
		allTriggerStates.add("REGISTERED");
		allTriggerStates.add("RUNNING");
		allTriggerStates.add("CRASHED");
		allTriggerStates.add("WAITING");
		
		return allTriggerStates;
	}

	public static List<ActivityMsgFilterMetadata> readActMsgFiltersMetadata(QueryFilters queryFilters) {
		// TODO Auto-generated method stub

		/*
		 * GmsTabularData resultTable = new GmsTabularData();
		 * 
		 * resultTable.addColumn("MessageID", GmsTabularData.INTEGER);
		 * resultTable.addColumn("Write", GmsTabularData.BOOLEAN);
		 * resultTable.addColumn("Name", GmsTabularData.STRING);
		 * resultTable.addColumn("Module", GmsTabularData.INTEGER);
		 */

		/*
		 * int messageIDColumnIndex = resultTable.getColumnIndex("MessageID"); int
		 * writeColumnIndex = resultTable.getColumnIndex("Write"); int nameColumnIndex =
		 * resultTable.getColumnIndex("Name"); int messageModuleColumnIndex =
		 * resultTable.getColumnIndex("Module");
		 */

		try {

			String projectKey = queryFilters.getProjKey();// functionDesc.getArgStringValue("ProjectKey", functionIcon);
			String projectPath = queryFilters.getProjLocation();// functionDesc.getArgStringValue("ProjectPath",
																// functionIcon);

			/*if (projectPath.length() > 0) {
				projectPath = projectPath.substring(0, projectPath.length() - 1);
			}*/
			String seperator = System.getProperty("file.separator");
			String actlogWriteXMLAtProjectLevel = projectPath + seperator + "config" + seperator
					+ "Actlog_Write_settings.xml";
			String actlogDisplayXMLAtProjectLevel = projectPath + seperator + "config" + seperator
					+ "Actlog_Display_settings_" + "admin" + ".xml";

			String actlogWriteXMLAtRootLevel = ".." + seperator + ".." + seperator + "Actlog_Write_settings.xml";

			File fXmlFileWrite = new File(actlogWriteXMLAtProjectLevel);
			File fXmlFileDisplay = new File(actlogDisplayXMLAtProjectLevel);

//			if(!fXmlFileDisplay.exists()) {
//				queryFilters.setDisplayFileExistsFlag(true);
//			}

			if (fXmlFileWrite.exists()) {

				readActivityLogFilterWriteXML(fXmlFileWrite, queryFilters);
				readActivityLogFilterDisplayXML(fXmlFileDisplay, queryFilters);

			} else {

				File fXmlFileWriteAtRoot = new File(actlogWriteXMLAtRootLevel);

				if (fXmlFileWriteAtRoot.exists()) {

					String configDirectoryPath = projectPath + seperator + "config";
					File configDirectory = new File(configDirectoryPath);

					// Sudeep : If the config folder does not exist, create it.
					if (!configDirectory.exists()) {
						configDirectory.mkdir();
					}

					copyFileFromRoot(actlogWriteXMLAtRootLevel, actlogWriteXMLAtProjectLevel);

					readActivityLogFilterWriteXML(fXmlFileWrite, queryFilters);

				} else {

					System.out.println("Write XML doesn't exists in root location");

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryFilters.getActMsgFiltersMetadata();

	}

	private static void copyFileFromRoot(String actlogWriteXMLAtRootLevel, String actlogWriteXMLAtProjectLevel)
			throws IOException {

		FileChannel source = new FileInputStream(actlogWriteXMLAtRootLevel).getChannel();
		FileChannel destination = new FileOutputStream(actlogWriteXMLAtProjectLevel).getChannel();

		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}

	}

	private static void readActivityLogFilterWriteXML(File fXmlFileWrite, QueryFilters queryFilters)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactoryWrite = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderWrite = dbFactoryWrite.newDocumentBuilder();
		Document docWrite = dBuilderWrite.parse(fXmlFileWrite.getAbsolutePath());

		docWrite.getDocumentElement().normalize();

		NodeList nodeListWrite = docWrite.getElementsByTagName("Message");

		for (int i = 0; i < nodeListWrite.getLength(); i++) {

			ActivityMsgFilterMetadata activityMsgFilterMetadata = new ActivityMsgFilterMetadata();

			Node nNode = nodeListWrite.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

//				if(queryFilters.isDisplayFileExistsFlag()) {
//					activityMsgFilterMetadata.setDisplay(true);
//				}

				Element eElement = (Element) nNode;

				// resultTable.addRow("");

				// resultTable.setCellValue(Integer.parseInt(eElement.getAttribute("id")), i,
				// messageIDColumnIndex);
				activityMsgFilterMetadata.setMessageId(Integer.parseInt(eElement.getAttribute("id")));
				// 0 = True AND 1 = False
				if (eElement.getAttribute("suspend").equalsIgnoreCase("0")) {
					// resultTable.setCellValue(true, i, writeColumnIndex);
					activityMsgFilterMetadata.setWrite(true);
				} else {
					// resultTable.setCellValue(false, i, writeColumnIndex);
					activityMsgFilterMetadata.setWrite(false);
				}
				// resultTable.setCellValue(eElement.getTextContent(), i, nameColumnIndex);
				activityMsgFilterMetadata.setMessageName(eElement.getTextContent());
				// resultTable.setCellValue(Integer.parseInt(eElement.getAttribute("module")),
				// i,
				// messageModuleColumnIndex);
				activityMsgFilterMetadata.setModule(Integer.parseInt(eElement.getAttribute("module")));

			}
			queryFilters.getActMsgFiltersMetadata().add(activityMsgFilterMetadata);
		}

	}

	private static void readActivityLogFilterDisplayXML(File fXmlFileDisplay, QueryFilters queryFilters)
			throws SAXException, IOException, ParserConfigurationException {

		DocumentBuilderFactory dbFactoryDisplay = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderDisplay = dbFactoryDisplay.newDocumentBuilder();
		Document docDisplay = dBuilderDisplay.parse(fXmlFileDisplay.getAbsolutePath());

		docDisplay.getDocumentElement().normalize();

		NodeList nodeListDisplay = docDisplay.getElementsByTagName("Message");

		for (int i = 0; i < nodeListDisplay.getLength(); i++) {

			Node nNode = nodeListDisplay.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				// resultTable.addRow("");

				// resultTable.setCellValue(Integer.parseInt(eElement.getAttribute("id")), i,
				// messageIDColumnIndex);

				// 0 = True AND 1 = False
				if (eElement.getAttribute("display").equalsIgnoreCase("0")) {
					// resultTable.setCellValue(true, i, projectKeyColumnIndex);
					queryFilters.getActMsgFiltersMetadata().get(i).setDisplay(true);
				} else {
					// resultTable.setCellValue(false, i, projectKeyColumnIndex);
					queryFilters.getActMsgFiltersMetadata().get(i).setDisplay(false);
				}
				// resultTable.setCellValue(eElement.getAttribute("color"), i,
				// colorColumnIndex);
				queryFilters.getActMsgFiltersMetadata().get(i).setColor(eElement.getAttribute("color"));
				// resultTable.setCellValue(Integer.parseInt(eElement.getAttribute("module")),
				// i,
				// messageModuleColumnIndex);
				queryFilters.getActMsgFiltersMetadata().get(i)
						.setModule(Integer.parseInt(eElement.getAttribute("module")));

			}
		}

	}

	private Map<String, String> readActLogDisplaySettingsXMLForColor(File fXmlFileDisplay)
			throws ParserConfigurationException, SAXException, IOException {

		Map<String, String> actLogFiltersColorMap = new HashMap<String, String>();

		DocumentBuilderFactory dbFactoryDisplay = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderDisplay = dbFactoryDisplay.newDocumentBuilder();
		Document docDisplay = dBuilderDisplay.parse(fXmlFileDisplay.getAbsolutePath());

		docDisplay.getDocumentElement().normalize();

		NodeList nodeListDisplay = docDisplay.getElementsByTagName("Message");

		for (int i = 0; i < nodeListDisplay.getLength(); i++) {

			Node nNode = nodeListDisplay.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				actLogFiltersColorMap.put(eElement.getAttribute("id"), eElement.getAttribute("color"));

			}
		}

		return actLogFiltersColorMap;
	}

	public void writeActLogFilterTableToXML(QueryFilters queryFilters) {

		/*
		 * String args[] = commandValues.split("!#!");
		 * 
		 * if (args.length > 1) {
		 * 
		 * if (args[0] != null && !(args[0].equalsIgnoreCase(""))) {
		 * 
		 * String projectKey = args[0].trim();
		 * 
		 * if (args[1] != null && !(args[1].equalsIgnoreCase(""))) {
		 * 
		 * String username = args[1].trim();
		 * 
		 * if (args[2] != null && !(args[2].equalsIgnoreCase(""))) {
		 * 
		 * String tableName = args[2].trim();
		 * 
		 * if (rtvp == null) { return; }
		 * 
		 * GmsNetObject mytable = findNetObjectByName(rtvp, tableName);
		 * 
		 * if (mytable == null) { return; }
		 * 
		 * GmsTabularData data = (GmsTabularData) mytable.getGmsArrayVar("valueTable");
		 * 
		 * if (data == null) { return; }
		 */

		try {

			/* if (args[3] != null && !(args[3].equalsIgnoreCase(""))) { */

			// String projectPath = args[3].trim();
			String projectPath = queryFilters.getProjLocation();
			projectPath = projectPath.replace("\\\\", "\\");
			String actlogDisplayXMLFileName = "Actlog_Display_settings_" + "admin" + ".xml";

			String seperator = System.getProperty("file.separator");

			if (!(projectPath.endsWith("\\") || projectPath.endsWith("/")))
				projectPath = projectPath + seperator;

			String actlogWriteXMLAtProjectLevel = projectPath + "config" + seperator + "Actlog_Write_settings.xml";

			String actlogDisplayXMLAtProjectLevel = projectPath + "config" + seperator + actlogDisplayXMLFileName;

//								int displayColIndex = data.getColumnIndex("Display");
//								int colorColIndex = data.getColumnIndex("Color");
//								int writeColIndex = data.getColumnIndex("Write");

			// Display XML
			writeActLogFiltersDataToDisplayXML(actlogDisplayXMLAtProjectLevel, queryFilters);

			// Write XML
			writeActLogFiltersDataToWriteXML(actlogWriteXMLAtProjectLevel, queryFilters);

			// Call Enable Logging Function : Temporary Disable on : 04-04-2020
			enableLogging(actlogWriteXMLAtProjectLevel, queryFilters.getProjKey());

			/* } */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		/*
		 * } } } }
		 */
	}

	private void enableLogging(String filePath, String projectKey)
			throws InterruptedException, ApplicationException, InstantiationException, IllegalAccessException {
		SpaceServiceImpl.rtvInfoEntityObject.loadMytp(filePath, projectKey);
		SpaceServiceImpl.rtvEntity.reloadMessagesTypes(projectKey);
	}

	private void writeActLogFiltersDataToDisplayXML(String actlogDisplayXMLAtProjectLevel, QueryFilters queryFilters)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		File fXmlFileDisplayD = new File(actlogDisplayXMLAtProjectLevel);
		DocumentBuilderFactory dbFactoryDisplay = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderDisplay = dbFactoryDisplay.newDocumentBuilder();
		Document docDisplay = dBuilderDisplay.parse(fXmlFileDisplayD);

		docDisplay.getDocumentElement().normalize();

		for (int i = 0; i < queryFilters.getActMsgFiltersMetadata().size(); i++) {

			// String display = data.getCellValue(i, displayColIndex);
			// String color = data.getCellValue(i, colorColIndex);

			String display = String.valueOf((queryFilters.getActMsgFiltersMetadata().get(i).isDisplay()));
			String color = queryFilters.getActMsgFiltersMetadata().get(i).getColor();

			Node message = docDisplay.getElementsByTagName("Message").item(i);
			NamedNodeMap attr = message.getAttributes();
			Node nodeAttrDisplay = attr.getNamedItem("display");

			if (display.equalsIgnoreCase("true")) {
				nodeAttrDisplay.setTextContent("0");
			} else if (display.equalsIgnoreCase("false")) {
				nodeAttrDisplay.setTextContent("1");
			}

			Node nodeAttrColor = attr.getNamedItem("color");
			nodeAttrColor.setTextContent(color);

		}

		TransformerFactory transformerFactoryD = TransformerFactory.newInstance();
		Transformer transformerD = transformerFactoryD.newTransformer();
		DOMSource sourceD = new DOMSource(docDisplay);
		StreamResult resultD = new StreamResult(fXmlFileDisplayD.getAbsolutePath());
		transformerD.transform(sourceD, resultD);

	}

	private void writeActLogFiltersDataToWriteXML(String actlogWriteXMLAtProjectLevel, QueryFilters queryFilters)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		File fXmlFileDisplayW = new File(actlogWriteXMLAtProjectLevel);
		DocumentBuilderFactory dbFactoryWrite = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderWrite = dbFactoryWrite.newDocumentBuilder();
		Document docWrite = dBuilderWrite.parse(fXmlFileDisplayW);

		docWrite.getDocumentElement().normalize();

		for (int i = 0; i < queryFilters.getActMsgFiltersMetadata().size(); i++) {

			// String write = data.getCellValue(i, writeColIndex);

			String write = String.valueOf(queryFilters.getActMsgFiltersMetadata().get(i).isWrite());

			Node message = docWrite.getElementsByTagName("Message").item(i);
			NamedNodeMap attr = message.getAttributes();
			Node nodeAttr = attr.getNamedItem("suspend");

			if (write.equalsIgnoreCase("true")) {
				nodeAttr.setTextContent("0");
			} else if (write.equalsIgnoreCase("false")) {
				nodeAttr.setTextContent("1");
			}

		}

		TransformerFactory transformerFactoryW = TransformerFactory.newInstance();
		Transformer transformerW = transformerFactoryW.newTransformer();
		DOMSource sourceW = new DOMSource(docWrite);
		StreamResult resultW = new StreamResult(fXmlFileDisplayW.getAbsolutePath());
		transformerW.transform(sourceW, resultW);

	}

	public static List<StatusModel> getServerStatuses() {

		List<StatusModel> serverStatuses = new ArrayList<StatusModel>();

		StatusModel statusModel = new StatusModel();
		statusModel.setStatusId(null);
		statusModel.setStatusName("ALL");
		serverStatuses.add(statusModel);

		for (int i = 0; i < Status.values().length; i++) {
			statusModel = new StatusModel();
			statusModel.setStatusId(String.valueOf(i));
			statusModel.setStatusName(Status.values()[i].name());
			serverStatuses.add(statusModel);
		}

		return serverStatuses;
	}

	public static List<StatusModel> getMsgStatuses() {

		List<StatusModel> msgStatuses = new ArrayList<StatusModel>();

		StatusModel statusModel = new StatusModel();
		statusModel.setStatusId(null);
		statusModel.setStatusName("ALL");
		msgStatuses.add(statusModel);

		for (int i = 0; i < MessageStatus.values().length; i++) {
			statusModel = new StatusModel();
			statusModel.setStatusId(String.valueOf(i));
			statusModel.setStatusName(MessageStatus.values()[i].name());
			msgStatuses.add(statusModel);
		}

		return msgStatuses;
	}
	
	public static List<MsgInvocationModel> getMsgInvoTypes() {

		List<MsgInvocationModel> msgInvoTypes = new ArrayList<MsgInvocationModel>();

		MsgInvocationModel msgInvoTypeModel = new MsgInvocationModel();
		msgInvoTypeModel.setMsgInvoTypeId(null);
		msgInvoTypeModel.setMsgInvoType("ALL");
		msgInvoTypes.add(msgInvoTypeModel);

		for (int i = 0; i < MsgInvocationType.values().length; i++) {
			msgInvoTypeModel = new MsgInvocationModel();
			msgInvoTypeModel.setMsgInvoTypeId(String.valueOf(i));
			msgInvoTypeModel.setMsgInvoType(MsgInvocationType.values()[i].msgInvocationType);
			msgInvoTypes.add(msgInvoTypeModel);
		}

		return msgInvoTypes;
	}
public static List<String> getBamCategory(){
		
		List<String> allBamCategory = new ArrayList<String>();
		allBamCategory.add("All");
		allBamCategory.add("ONE");
		allBamCategory.add("3");
		
		
		return allBamCategory;
	}

	public static List<String> getBamPriority(){
	
		List<String> allBamPriority = new ArrayList<String>();
		allBamPriority.add("All");
		allBamPriority.add("1");
		allBamPriority.add("2");
		
		
		return allBamPriority;
	}

	public static List<String> getOdsType(){
	
	List<String> allOdsType = new ArrayList<String>();
	allOdsType.add("ALL");
	allOdsType.add("Global");
	allOdsType.add("Local");
	
	return allOdsType;
	}

}

