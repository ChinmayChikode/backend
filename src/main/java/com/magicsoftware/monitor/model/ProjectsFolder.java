package com.magicsoftware.monitor.model;

import javax.xml.bind.annotation.XmlAttribute;

public class ProjectsFolder {
	private String folderPath;
	private String autoRun;

	@XmlAttribute(name = "FolderPath")
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	@XmlAttribute(name = "AutoRun")
	public String getAutoRun() {
		return autoRun;
	}

	public void setAutoRun(String autoRun) {
		this.autoRun = "N";

	}

}
