package com.magicsoftware.monitor.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.magicsoftware.ibolt.commons.logging.LogLevel;
import com.magicsoftware.ibolt.commons.logging.LogModules;
import com.magicsoftware.ibolt.commons.logging.Logger;
import com.magicsoftware.monitor.model.Project;
import com.magicsoftware.monitor.model.ProjectsFolder;
import com.magicsoftware.monitor.model.ProjectsGroup;

@XmlRootElement(name = "ApplicationsList")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationsList {
	@XmlElement(name = "ProjectsGroup")
	private List<ProjectsGroup> projectsGroup;

	public List<ProjectsGroup> getProjectsGroup() throws Exception {

		String seperator = System.getProperty("file.separator");
		if (projectsGroup != null) {
			for (ProjectsGroup projectGroup : projectsGroup) {

				List<ProjectsFolder> projectsFolder = projectGroup.getProjectsFolder();

				if (projectsFolder != null) {
					List<Project> projects = new ArrayList<Project>();
					for (ProjectsFolder projectFolder : projectsFolder) {
						String projectFolderPath = projectFolder.getFolderPath() != null ? projectFolder.getFolderPath() : null;

						if (projectFolderPath == null || projectFolderPath.equalsIgnoreCase(""))
							projectFolderPath = ".." + seperator + ".." + seperator + "projects" + seperator;

						if (projectFolderPath != null) {
							/* Check folder accessibility */
							if (!checkFolderAccessibility(projectFolderPath)) {
								Logger.logMessage(LogLevel.LEVEL_ERROR, LogModules.RTVIEW_PROJECT, "Access denied for " + projectFolderPath);
								throw new Exception("Access denied for " + projectFolderPath);
							}

							String[] projectList = traverse(projectFolderPath);
							if (projectList != null) {

								for (String projectName : projectList) {

									Project project = new Project();
									project.setProjectName(projectName);
									project.setProjectPath(projectFolderPath + seperator + projectName);
									String ifsIniFilePath = project.getProjectPath() + "ifs.ini".toUpperCase();
									File ifsinifile = new File(ifsIniFilePath);
									if (!ifsinifile.exists()) {
										ifsIniFilePath = project.getProjectPath() + projectName + seperator + "ifs.ini".toUpperCase();
									}
									project.setStartFile(MagicMonitorUtilities.readPropertyValueFromIFSINIFile(ifsIniFilePath, "MAGICXPI_GS", "StartProjectFileName"));
									projects.add(project);

								}
							}

						}

						if (projectFolderPath == null || projectFolderPath.equalsIgnoreCase(""))
							Logger.logMessage(LogLevel.LEVEL_ERROR, LogModules.RTVIEW_PROJECT, "Folder path is empty");

					}
					projectGroup.setProject(projects);
				}

			}
		} else {
			Logger.logMessage(LogLevel.LEVEL_ERROR, LogModules.RTVIEW_PROJECT, "Invalid ApplicationList.xml");
		}
		return projectsGroup;
	}

	public void setProjectsGroup(List<ProjectsGroup> projectsGroup) {
		this.projectsGroup = projectsGroup;
	}

	String[] traverse(String directory) {
		try {
			if (directory != null) {
				File dir = new File(directory);
				if (dir.isDirectory()) {
					String[] children;
					return children = dir.list();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean checkFolderAccessibility(String folderPath) {
		try {
			String seperator = System.getProperty("file.separator");
			File folder = new File(folderPath);
			if (!folder.exists())
				Logger.logMessage(LogLevel.LEVEL_ERROR, LogModules.RTVIEW_PROJECT, "Invalid folder path");
			else {
				File file = new File(folderPath + seperator + Integer.toString((new Random()).nextInt())+"_Test.txt");
				file.createNewFile();
				file.delete();
				file = null;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}