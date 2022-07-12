package com.magicsoftware.monitor.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ProjectsGroup {

	private String groupName;
	private List<ProjectsFolder> projectsFolder;
	private List<Project> project;

	@XmlElement(name = "ProjectsFolder")
	public List<ProjectsFolder> getProjectsFolder() {
		return projectsFolder;
	}

	public void setProjectsFolder(List<ProjectsFolder> projectsFolder) {
		this.projectsFolder = projectsFolder;
	}

	@XmlElement(name = "Project")
	public List<Project> getProject() {
		return project;
	}

	public void setProject(List<Project> project) {
		this.project = project;
	}

	@XmlAttribute(name = "GroupName")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	String[] traverse(String directory) throws FileNotFoundException {
		File dir = new File(directory);
		if (dir.isDirectory()) {
			String[] children;
			return children = dir.list();
		}
		return null;

	}

}