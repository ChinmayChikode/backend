package com.magicsoftware.monitor.model;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;

import com.magicsoftware.monitor.util.MagicMonitorUtilities;

public class Project {

	private String projectPath;
	private String projectName;
	private String startFile;

	@XmlAttribute(name = "ProjectPath")
	public String getProjectPath() {
		String seperator = System.getProperty("file.separator");
		return projectPath + seperator;
	}

	public void setProjectPath(String projectPath) {

		if (projectPath == null || projectPath.equalsIgnoreCase("")) {

		} else {
			String seperator = System.getProperty("file.separator");
			String ifsIniFilePath = projectPath + seperator + "ifs.ini".toUpperCase();
			File ifsinifile = new File(ifsIniFilePath);
			if (!ifsinifile.exists()) {
				ifsIniFilePath = projectPath + seperator + projectName + seperator + "ifs.ini".toUpperCase();
			}

			String startXMLExists = projectPath + seperator + MagicMonitorUtilities.readPropertyValueFromIFSINIFile(ifsIniFilePath, "MAGICXPI_GS", "StartProjectFileName");
			File startxmlfile = new File(startXMLExists);
			if (startxmlfile.exists()) {
				this.projectPath = projectPath + seperator;
			} else {
				this.projectPath = projectPath + seperator + projectName + seperator;
			}
		}
	}

	@XmlAttribute(name = "ProjectName")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		if (projectName == null || projectName.equalsIgnoreCase("")) {
		} else {
			this.projectName = projectName;
		}
	}

	@XmlAttribute(name = "StartFile")
	public String getStartFile() {
		return startFile;
	}

	public void setStartFile(String startFile) {
		this.startFile = startFile;
	}
}