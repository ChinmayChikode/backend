package com.magicsoftware.monitor.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
		Date date = new Date(1585378062309l);
		System.out.println(simpleDateFormat.format(date));
		
		/*MonitorOfflineMetadata monitorOfflineMetadata = null;
		
		List<BpModel> bpList = null;
		List<FlowModel> flowList = null;
		List<StepModel> stepList = null;

		String actlogFilterXMLAtProjectLevel = "c:\\Users\\sudeepm\\Documents\\Magic\\Projects\\Project1\\Project1\\MonitorOfflineMetadata.xml";

		File fXmlFileDisplay = new File(actlogFilterXMLAtProjectLevel);


			if (fXmlFileDisplay.exists()) {

				Document docDisplay = readActivityLogFilterXMLDocument(fXmlFileDisplay);

				NodeList listOfBPs = docDisplay.getElementsByTagName("BusinessProcess");

				for (int bpCounter = 0; bpCounter < listOfBPs.getLength(); bpCounter++) 
				{
					monitorOfflineMetadata = new MonitorOfflineMetadata();
					
					bpList = new ArrayList<BpModel>();
					BpModel bpModel = new BpModel();
					
					Node firstBPNode = listOfBPs.item(bpCounter);
					
					if (firstBPNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						Element bpElement = (Element) firstBPNode;
						
						NodeList bpIdList1 = bpElement.getElementsByTagName("ID");
						Element bpIdElement = (Element) bpIdList1.item(0);
						NodeList bpIdList2 = bpIdElement.getChildNodes();
						
						NodeList bpNameList1 = bpElement.getElementsByTagName("Name");
						Element bpNameElement = (Element) bpNameList1.item(0);
						NodeList bpNameList2 = bpNameElement.getChildNodes();

						System.out.println("BP-ID=>"+((Node) bpIdList2.item(0)).getNodeValue().trim());
						System.out.println("BP-Name=>"+((Node) bpNameList2.item(0)).getNodeValue().trim());
						
						bpModel.setBpId(((Node) bpIdList2.item(0)).getNodeValue().trim());
						bpModel.setBpName(((Node) bpNameList2.item(0)).getNodeValue().trim());
						
						bpList.add(bpModel);
						
						monitorOfflineMetadata.setBpList(bpList);
						
						NodeList listOfFlows = bpElement.getElementsByTagName("Flow");
						
						for (int flowCounter = 0; flowCounter < listOfFlows.getLength(); flowCounter++) {
							
							Node firstFlowNode = listOfFlows.item(flowCounter);
							
							flowList = new ArrayList<FlowModel>();
							FlowModel flowModel = new FlowModel();
							
							if (firstFlowNode.getNodeType() == Node.ELEMENT_NODE) {

								Element flowElement = (Element) firstFlowNode;

								NodeList flowIdList1 = flowElement.getElementsByTagName("ID");
								Element flowIdElement = (Element) flowIdList1.item(0);
								NodeList flowIdList2 = flowIdElement.getChildNodes();
								
								NodeList flowNameList1 = flowElement.getElementsByTagName("Name");
								Element flowNameElement = (Element) flowNameList1.item(0);
								NodeList flowNameList2 = flowNameElement.getChildNodes();
								
								System.out.println("Flow-ID=>"+((Node) flowIdList2.item(0)).getNodeValue().trim());
								System.out.println("Flow-Name=>"+((Node) flowNameList2.item(0)).getNodeValue().trim());

								flowModel.setFlowId(((Node) flowIdList2.item(0)).getNodeValue().trim());
								flowModel.setFlowName(((Node) flowNameList2.item(0)).getNodeValue().trim());
								
								flowList.add(flowModel);
								
								monitorOfflineMetadata.setFlowList(flowList);
								
								NodeList listOfStepNames = flowElement.getElementsByTagName("Step");

								for (int i = 0; i < listOfStepNames.getLength(); i++) {

									Node firstStepNode = listOfStepNames.item(i);
									
									stepList = new ArrayList<StepModel>();
									StepModel stepModel = new StepModel();

									if (firstStepNode.getNodeType() == Node.ELEMENT_NODE) {

										Element StepElement = (Element) firstStepNode;

										NodeList stepIdList1 = StepElement.getElementsByTagName("ID");
										Element stepIdElement = (Element) stepIdList1.item(0);
										NodeList stepIdList2 = stepIdElement.getChildNodes();

										NodeList stepNameList1 = StepElement.getElementsByTagName("Name");
										Element stepNameElement = (Element) stepNameList1.item(0);
										NodeList stepNameList2 = stepNameElement.getChildNodes();
										
										System.out.println("Step-ID=>"+((Node) stepIdList2.item(0)).getNodeValue().trim());
										System.out.println("Step-Name=>"+((Node) stepNameList2.item(0)).getNodeValue().trim());
										
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
		return docDisplay;*/
	}

}
