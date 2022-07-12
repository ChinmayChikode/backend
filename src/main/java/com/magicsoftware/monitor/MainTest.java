package com.magicsoftware.monitor;

import java.util.ArrayList;
import java.util.List;

import com.magicsoftware.monitor.model.ProjectBean;
import com.magicsoftware.monitor.util.AllProjectsAdapter;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<ProjectBean> projectList = new ArrayList<>();
		
		AllProjectsAdapter allProjectsAdapter = new AllProjectsAdapter();
		
		projectList = allProjectsAdapter.getAllMagicXpiProjects();
		
		System.out.println(projectList.size());

		System.out.println(projectList.size());
		System.out.println(projectList.size());
		System.out.println(projectList.size());

		
	}

}
