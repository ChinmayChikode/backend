package com.magicsoftware.monitor.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.magicsoftware.monitor.model.ActivityLog;
import com.magicsoftware.monitor.model.ActivityMsgFilterMetadata;
import com.magicsoftware.monitor.model.QueryFilters;
import com.magicsoftware.monitor.query.CustomActivityLogMapper;
import com.magicsoftware.monitor.query.QueryFactory;
import com.magicsoftware.monitor.service.ActivityLogService;
import com.magicsoftware.monitor.util.MagicMonitorUtilities;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityLogServiceImpl implements ActivityLogService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ActivityLog> getMonitorActivityLog(QueryFilters queryFilters) throws Exception {

		queryFilters.setActMsgFiltersMetadata(new ArrayList<ActivityMsgFilterMetadata>());
		List<ActivityMsgFilterMetadata> activityMsgFilterMetadata = MagicMonitorUtilities
				.readActMsgFiltersMetadata(queryFilters);
		String messageTypeIdString = "";
		for (int i = 0; i < activityMsgFilterMetadata.size(); i++) {
			if (!activityMsgFilterMetadata.get(i).isDisplay()) {
				messageTypeIdString = messageTypeIdString + activityMsgFilterMetadata.get(i).getMessageId() + ",";
			}
		}

		if (!messageTypeIdString.equalsIgnoreCase("")) {
			queryFilters.setMessageTypeIDString(messageTypeIdString.substring(0, messageTypeIdString.length() - 1));
		}

		queryFilters = QueryFactory.activityLogQueryBuilder(queryFilters);

		System.out.println("Query : " + queryFilters.getActivityLogQuery());

		log.info("Activity Log Query : {}" + queryFilters.getActivityLogQuery().toString());

		List<ActivityLog> activityLogList = jdbcTemplate.query(queryFilters.getActivityLogQuery().toString(),
				new CustomActivityLogMapper());

		int totalRecordsCount = jdbcTemplate.queryForObject(queryFilters.getActivityLogCountQuery().toString(),
				new Object[] {}, Integer.class);

		for (int i = 0; i < activityLogList.size(); i++) {

			for (int j = 0; j < SpaceServiceImpl.monitorOfflineMetadata.getStepList().size(); j++) {
				if (activityLogList.get(i).getFsstep() == Integer
						.valueOf(SpaceServiceImpl.monitorOfflineMetadata.getStepList().get(j).getStepId())) {
					activityLogList.get(i)
							.setStepName(SpaceServiceImpl.monitorOfflineMetadata.getStepList().get(j).getStepName());
				}
			}
			activityLogList.get(i)
					.setMessageType(MagicMonitorUtilities.getMessageType(activityLogList.get(i).getMessagetypeid()));

			activityLogList.get(i).setDisplayCreatedTime(
					MagicMonitorUtilities.formatDate(activityLogList.get(i).getCreateTimeStamp()));

			activityLogList.get(i).setTotalNumberOfRecords(Long.valueOf(totalRecordsCount));

			for (int j = 0; j < activityMsgFilterMetadata.size(); j++) {
				if (activityLogList.get(i).getMessagetypeid() == activityMsgFilterMetadata.get(j).getMessageId()) {
					activityLogList.get(i).setColor(activityMsgFilterMetadata.get(j).getColor());
				}
			}

			BPLoop: for (int j = 0; j < SpaceServiceImpl.monitorOfflineMetadata.getBpList().size(); j++) {
				if (SpaceServiceImpl.monitorOfflineMetadata.getBpList().get(j).getBpId() != null
						&& activityLogList.get(i).getBpid() == Integer
								.valueOf(SpaceServiceImpl.monitorOfflineMetadata.getBpList().get(j).getBpId())) {
					activityLogList.get(i)
							.setBpName(SpaceServiceImpl.monitorOfflineMetadata.getBpList().get(j).getBpName());
					FlowLoop: for (int k = 0; k < SpaceServiceImpl.monitorOfflineMetadata.getFlowList().size(); k++) {
						if (SpaceServiceImpl.monitorOfflineMetadata.getFlowList().get(k).getFlowId() != null
								&& activityLogList.get(i).getFlowid() == Integer.valueOf(
										SpaceServiceImpl.monitorOfflineMetadata.getFlowList().get(k).getFlowId())) {
							activityLogList.get(i).setFlowName(
									SpaceServiceImpl.monitorOfflineMetadata.getFlowList().get(k).getFlowName());
							continue BPLoop;
						}
					}
				}
			}

			if (activityLogList.get(i).getServerid() != null) {
				activityLogList.get(i).setServerName("Server_" + activityLogList.get(i).getServerid());
			}

		}

		return activityLogList;
	}

	@Override
	public int getMonitorActivityLogCount(QueryFilters queryFiltersDTO) throws Exception {

		queryFiltersDTO = QueryFactory.activityLogQueryBuilder(queryFiltersDTO);

		System.out.println("Query : " + queryFiltersDTO.getActivityLogCountQuery());

		int totalRecordsCount = jdbcTemplate.queryForObject(queryFiltersDTO.getActivityLogCountQuery().toString(),
				new Object[] {}, Integer.class);

		return totalRecordsCount;
	}

	@Override
	public int deleteActivityLog(QueryFilters queryFiltersDTO) {
		// TODO Auto-generated method stub
		queryFiltersDTO = QueryFactory.deleteActLogQueryBuilder(queryFiltersDTO);

		System.out.println("Delete Query : " + queryFiltersDTO.getActivityLogDeleteQuery());

		int totalRecordsDeleted = jdbcTemplate.update(queryFiltersDTO.getActivityLogDeleteQuery().toString());

		System.out.println("Total Records Deleted : " + totalRecordsDeleted);

		return totalRecordsDeleted;
	}

}
