package com.magicsoftware.monitor.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magicsoftware.monitor.model.ODSData;
import com.magicsoftware.monitor.repository.ODSDataRepo;

@Service
public class ODSDataServiceImpl {

	@Autowired
	ODSDataRepo oDSDataRepo;

	public List<ODSData> findByprojectKey(String projectKey) {
		ArrayList<ODSData> list = oDSDataRepo.findByprojectKey(projectKey);
		Map<String, ODSData> listMap = new HashMap<>();
		list.forEach(action -> listMap.put(action.getProjectKey(), action));
		ArrayList<ODSData> modifiedList = new ArrayList<>();
		listMap.forEach((k, v) -> modifiedList.add(v));
		return modifiedList;
	}

	public List<ODSData> findAll() {
		return oDSDataRepo.findAll();
	}

	public List<ODSData> findByuserKey(String userKey) {
		return oDSDataRepo.findByuserKey(userKey);
	}
}
