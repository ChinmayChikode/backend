package com.magicsoftware.monitor.serviceimpl;


import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.magicsoftware.monitor.model.ActivityLog;
import com.magicsoftware.monitor.model.ActivityLogColor;
import com.magicsoftware.monitor.model.ActivityMsgFilterMetadata;
import com.magicsoftware.monitor.model.BamDetails;
import com.magicsoftware.monitor.model.QueryFilters;
import com.magicsoftware.monitor.query.CustomActivityLogMapper;
//import com.magicsoftware.monitor.query.CustomerUserBlobMapper;
import com.magicsoftware.monitor.query.QueryFactory;
import com.magicsoftware.monitor.repository.ActivityLogRepo;
import com.magicsoftware.monitor.service.ActivityLogService;
import com.magicsoftware.monitor.util.MagicMonitorUtilities;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class ActivityLogServiceImpl implements ActivityLogService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	ActivityLogRepo activityLogRepo;
	
	@Value("${userblob.file.name}")
	private String userblobFile;

	@Override
	public List<ActivityLogColor> getMonitorActivityLog(QueryFilters queryFilters) throws Exception {

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
		

		List<ActivityLogColor> activityLogList = jdbcTemplate.query(queryFilters.getActivityLogQuery().toString(),
				new CustomActivityLogMapper());

		int totalRecordsCount = jdbcTemplate.queryForObject(queryFilters.getActivityLogCountQuery().toString(),
				new Object[] {}, Integer.class);
		//for each // 
//		for (ActivityLogColor activityLogColor : activityLogList) {
//		
//		}
		for (int i = 0; i < activityLogList.size(); i++) {

			if(SpaceServiceImpl.monitorOfflineMetadata != null && SpaceServiceImpl.monitorOfflineMetadata.getStepList() != null) {
				for (int j = 0; j < SpaceServiceImpl.monitorOfflineMetadata.getStepList().size(); j++) {
					if (activityLogList.get(i).getFsstep() == Integer
							.valueOf(SpaceServiceImpl.monitorOfflineMetadata.getStepList().get(j).getStepId())) {
						activityLogList.get(i)
								.setStepName(SpaceServiceImpl.monitorOfflineMetadata.getStepList().get(j).getStepName());
					}
				}	
			}
			
			activityLogList.get(i)
					.setMessageType(MagicMonitorUtilities.getMessageType(activityLogList.get(i).getMessagetypeid()));

			activityLogList.get(i).setDisplayCreatedTime(
					MagicMonitorUtilities.formatDate(activityLogList.get(i).getCreateTimeStamp()));
			
			activityLogList.get(i).setTotalNumberOfRecords(Long.valueOf(totalRecordsCount));
			
//			if(activityLogList.get(i).getBlobexists() > 0) {
//				System.out.println(activityLogList.get(i).getUserblob());
//			}
			
			
			for (int j = 0; j < activityMsgFilterMetadata.size(); j++) {
				if (activityLogList.get(i).getMessagetypeid() == activityMsgFilterMetadata.get(j).getMessageId()) {
					activityLogList.get(i).setColor(activityMsgFilterMetadata.get(j).getColor());
				}
			}
			if(SpaceServiceImpl.monitorOfflineMetadata != null && SpaceServiceImpl.monitorOfflineMetadata.getBpList() != null) {
			
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

			}
			
			if (activityLogList.get(i).getServerid() != null) {
				activityLogList.get(i).setServerName("Server_" + activityLogList.get(i).getServerid());
			}

		}
		//System.out.println("data from activity log table \n"+ activityLogList);

		 

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
		
		queryFiltersDTO = QueryFactory.deleteActLogQueryBuilder(queryFiltersDTO);

		System.out.println("Delete Query : " + queryFiltersDTO.getActivityLogDeleteQuery());

		int totalRecordsDeleted = jdbcTemplate.update(queryFiltersDTO.getActivityLogDeleteQuery().toString());

		System.out.println("Total Records Deleted : " + totalRecordsDeleted);

		return totalRecordsDeleted;
	}

public BamDetails findByProjectkey(String projectKey) {
		
		BamDetails bamDetails = new BamDetails();
		List<ActivityLog> bamData= activityLogRepo.findAllSearch(projectKey);
		
		for (int i = 0; i < bamData.size(); i++) {
			
			bamData.get(i).setDisplayCreatedTime(
					MagicMonitorUtilities.formatDate(bamData.get(i).getCreateTimeStamp()));
			
					System.out.println("Created time stamp : " + bamData.get(i).getCreateTimeStamp() );
			}
		
		bamDetails.setBamData(bamData);
		bamDetails.setCategory(MagicMonitorUtilities.getBamCategory());
		bamDetails.setPriority(MagicMonitorUtilities.getBamPriority());
		
		
//		ArrayList<ActivityLog> bamData = activityLogRepo.findByProjectkey(projectKey);
//		
//		return (ArrayList<ActivityLog>) bamData.stream()  
//		        .filter(p ->p.getCategory() != null)   // filtering price  
//		       
//		        .collect(Collectors.toList()); 
		return bamDetails;
	}

@Override
public List<ActivityLog> getBamDetails(String projectKey){
	
	List<ActivityLog> bamData =  activityLogRepo.findAllSearch(projectKey);
	
	for (int i = 0; i < bamData.size(); i++) {
	
	bamData.get(i).setDisplayCreatedTime(
			MagicMonitorUtilities.formatDate(bamData.get(i).getCreateTimeStamp()));
	
			//System.out.println("Created time stamp : " + bamData.get(i).getCreateTimeStamp() );
	}
	
//		String receivedToDateFromRTViewUser = item.getCreateTimeStamp();
//		SimpleDateFormat sdf=new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
//		Date parsedDateObject = sdf.parse(receivedToDateFromRTViewUser);
//
//		formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
//		strDate = formatter.format(date);  
		
		
//		String timestamp = item.getCreateTimeStamp();
//		LocalDateTime date1 =
//	            Instant.ofEpochMilli(Long.parseLong(timestamp)).atZone(ZoneId.systemDefault()).toLocalDateTime();
// 	    DateTimeFormatter format1 = DateTimeFormatter.ofPattern("HH:mm:ss");  
//        String formatDateTime1 = date1.format(format1);
    
	
    
    return bamData;
}


//	@Override
//	public List<ActivityLog> getFilteredBamDetails(@NotNull String projectkey,@NotNull String userkey1,@NotNull String userkey2,@NotNull String fromDate,@NotNull String toDate ){
//
//		List<ActivityLog> output = new ArrayList<>();
//		output = activityLogRepo.findFilteredSearch(projectkey,userkey1,userkey2,fromDate,toDate);
//		//findFilteredSearchOnAll
//		return output;
//	}


//	@Override
//	public byte[] findBlob(@NotNull String projectkey, @NotNull int msgid) {
//		byte[] blob = activityLogRepo.findBlob(projectkey, msgid);
//		System.out.println("received blob: "+ blob);
//		return blob;
//		 
//	}
	@Override
	public String getBlob(@NotNull String projectKey,@NotNull int msgid) {
		
		//byte[] blob = activityLogRepo.displayBamBlob(projectKey,msgid);
		byte[] blob = null;
		String format = null;
		List<Object[]> list = activityLogRepo.displayBamBlob(projectKey,msgid);
		
		for(Object[] obj:list) {
			blob = (byte[]) obj[0];
		    format = (String) obj[1];
		}
		
		System.out.println("format: "+format);
				
		try (FileOutputStream fos = new FileOutputStream(new File(userblobFile+msgid+"."+format))){
			
            fos.write(blob);
            
            System.out.println("Done\n");
            return format;
		 }
         catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

		
		
	}
	

	@Override
	public List<ActivityLog> getFilteredBamDetailsOnUserkey(@NotNull String projKey, @NotNull String userkey1,
			@NotNull String userkey2) {
		List<ActivityLog> output = new ArrayList<>();
		System.out.println("Recieved values "+projKey+userkey1+userkey2);
		output = activityLogRepo.findFilteredSearchOnUserkey(projKey,userkey1,userkey2);
		
		for (int i = 0; i < output.size(); i++) {
			
			output.get(i).setDisplayCreatedTime(
					MagicMonitorUtilities.formatDate(output.get(i).getCreateTimeStamp()));
			
				//	System.out.println("Created time stamp : " + output.get(i).getCreateTimeStamp() );
			}

		return output;
	}


	@Override
	public List<ActivityLog> getFilteredBamDetailsOnDate(@NotNull String projKey, @NotNull String lblFromDateValue,
			@NotNull String lblToDateValue) throws ParseException {

		List<ActivityLog> output = new ArrayList<>();
		//findFilteredSearchOnAll
		
		String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		Date lblFromDateValue2 = simpleDateFormat.parse(lblFromDateValue);
		
		Date lblToDateValue2 = simpleDateFormat.parse(lblToDateValue);
		
		output = activityLogRepo.findFilteredSearchOnDateTime(projKey,lblFromDateValue2,lblToDateValue2);
		
		for (int i = 0; i < output.size(); i++) {
			
			output.get(i).setDisplayCreatedTime(
					MagicMonitorUtilities.formatDate(output.get(i).getCreateTimeStamp()));
			
					//System.out.println("Created time stamp : " + output.get(i).getCreateTimeStamp() );
			}

		return output;
	}
	
	@Override
	public List<ActivityLog> getFilteredBamDetailsOnAll(@NotNull String projKey, @NotNull String lblFromDateValue,
			@NotNull String lblToDateValue, @NotNull String userkey1, @NotNull String userkey2) throws ParseException {
		List<ActivityLog> output = new ArrayList<>();
		
		
//		lblFromDateValue = "2021-03-13 10:53:15.607";
//		lblToDateValue = "2021-03-20 10:53:15.607";
		
		String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		Date lblFromDateValue2 = simpleDateFormat.parse(lblFromDateValue);
		
		Date lblToDateValue2 = simpleDateFormat.parse(lblToDateValue);
		
//		System.out.println(lblFromDateValue2);
//		
//		System.out.println(lblToDateValue2);
		
		output = activityLogRepo.findFilteredSearchOnAll(projKey,userkey1,userkey2,lblFromDateValue2,lblToDateValue2);
		
		for (int i = 0; i < output.size(); i++) {
			
			output.get(i).setDisplayCreatedTime(
					MagicMonitorUtilities.formatDate(output.get(i).getCreateTimeStamp()));
			
				//	System.out.println("Created time stamp : " + output.get(i).getCreateTimeStamp() );
			}
		return output;
	}
	


//	@Override
//	public byte[] findBlob(@NotNull String projectkey, @NotNull int msgid) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
