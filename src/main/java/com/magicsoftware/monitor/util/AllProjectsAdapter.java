package com.magicsoftware.monitor.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.magicsoftware.monitor.model.Project;
import com.magicsoftware.monitor.model.ProjectBean;
import com.magicsoftware.monitor.model.ProjectsGroup;

public class AllProjectsAdapter {
	// public static int refCounter = 0;

	public List<ProjectBean> getAllMagicXpiProjects() {

		List<ProjectBean> projectList = new ArrayList<>();
		int counter = 0;
		Hashtable<String, Integer> projectHash = null;

		try {
				String projectsXMLFolderPath = "c:\\Magic xpi 4.13\\Runtime\\Config\\";

				String seperator = System.getProperty("file.separator");

				/*if (projectsXMLFolderPath == null || projectsXMLFolderPath.equalsIgnoreCase("")) {
					projectsXMLFolderPath = ".." + seperator + ".." + seperator + "config" + seperator;
				}*/

				/* Check Folder is Exist or Not */
				if (!isExist(projectsXMLFolderPath)) {
					throw new Exception("Invalid folder path");
				} else {
					if (!(isExist(projectsXMLFolderPath + seperator + "ApplicationsList.xml"))) {
					} else {

						ApplicationsList applicationList = deserialize(projectsXMLFolderPath + seperator + "ApplicationsList.xml");
						projectHash = new Hashtable<String, Integer>();
						if (applicationList != null) {
							String projectName = "";
							String projectPath = "";
							String startXML = "";
							Hashtable<String, String> projectKeys = new Hashtable<String, String>();

							List<ProjectsGroup> projectsGroups = applicationList.getProjectsGroup();

							if (projectsGroups != null) {

								for (ProjectsGroup projectsGroup : projectsGroups) {

									List<Project> projects = projectsGroup.getProject();
									if (projects != null) {

										if (!(projectsGroup.getGroupName().equalsIgnoreCase("default"))) {

											for (int i = 0; i < projects.size(); i++) {

												String validateProjectPath = recursiveCheckForEndsWith(projects.get(i).getProjectPath());

												String projectNameInPath = (validateProjectPath).substring((validateProjectPath).lastIndexOf(seperator) + 1);

												if (!(projectNameInPath.equalsIgnoreCase(projects.get(i).getProjectName()))) {
													projects.remove(i);
												}

											}
										}

						ProjectsLoop:for (Project project : projects) {
								
											ProjectBean projectBean = new ProjectBean();
							
											projectName = project.getProjectName();

											if (projectName != null) {
												if (projectKeys.containsKey(projectName)) {
													//Logger.logMessage(LogLevel.LEVEL_ERROR, LogModules.RTVIEW_PROJECT, "Project Group :" + "There is more than one project with the same name");
													//throw new Exception("There is more than one project with the same name");
													continue ProjectsLoop;
												}
											}
											
											projectBean.setProjectKey(projectName);
											
											projectPath = project.getProjectPath();
											projectBean.setProjectLocation(projectPath);
											
											startXML = project.getStartFile();
											projectBean.setProjectStartXml(startXML);

											String ifsIniFilePath = projectPath + seperator + "ifs.ini".toUpperCase();
											File ifsinifile = new File(ifsIniFilePath);
											if (!ifsinifile.exists()) {
												ifsIniFilePath = project.getProjectPath() + projectName + seperator + "ifs.ini".toUpperCase();
											}

											projectBean.setProjectINIFilePath(ifsIniFilePath);
											
											startXML = startXML == null || startXML.equalsIgnoreCase("") ? MagicMonitorUtilities.readPropertyValueFromIFSINIFile(ifsIniFilePath, "MAGICXPI_GS", "StartProjectFileName") : startXML;
											if(startXML!=null && !startXML.equalsIgnoreCase("")) {
												String startXMLFilePath = projectPath + seperator + startXML;
												String projectpathWithStartXML=projectPath + startXML;
												projectPath = projectPath.replace("\\\\", "/");
												projectPath = projectPath.replace("\\", "/");

												if (isExist(startXMLFilePath) && isExist(ifsIniFilePath) && (projectName != null && !projectName.equalsIgnoreCase("")))	{
													
													projectpathWithStartXML = projectpathWithStartXML.replace("\\\\", "/");
													projectpathWithStartXML = projectpathWithStartXML.replace("\\", "/");
													
													projectHash.put(projectName, counter);
													counter++;
												}
												
												projectBean.setProjectpathWithStartXml(projectpathWithStartXML);
												
											}
											if (projectName != null)
												projectKeys.put(projectName, "");
											
											projectList.add(projectBean);
											
										}
									}
								}
							}
							// refCounter = refCounter > 4 ? 1 : refCounter + 1;
						}
					}
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectList;
	}

	private ApplicationsList deserialize(String FilePath) throws Exception {
		try {
			File file = new File(FilePath);
			JAXBContext jaxbContext = JAXBContext.newInstance(ApplicationsList.class);

			/* Create object from XML */
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (ApplicationsList) jaxbUnmarshaller.unmarshal(file);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private boolean isExist(String path) {
		return new File(path).exists();

	}

	private String recursiveCheckForEndsWith(String projectPath) {

		String seperator = System.getProperty("file.separator");

		if (projectPath.endsWith(seperator)) {
			return recursiveCheckForEndsWith(projectPath.substring(0, projectPath.length() - 1));
		}
		return projectPath;
	}

}