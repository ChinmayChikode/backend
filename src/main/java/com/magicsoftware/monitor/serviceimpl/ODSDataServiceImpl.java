package com.magicsoftware.monitor.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.magicsoftware.monitor.model.ODSData;
import com.magicsoftware.monitor.model.OdsDetails;
import com.magicsoftware.monitor.repository.ODSDataRepo;
import com.magicsoftware.monitor.util.MagicMonitorUtilities;

@Service

//public class ODSDataServiceImpl implements OdsService{
public class ODSDataServiceImpl{

	@Autowired
	ODSDataRepo oDSDataRepo;
	
	@Value("${userblob.file.name}")
	private String userblobFile;

	public OdsDetails findByprojectKey(String projectKey) {
		OdsDetails odsDetails = new OdsDetails();
		ArrayList<ODSData> list = oDSDataRepo.findByprojectKey(projectKey); 	
		
//		return list.stream()  
//		        .filter(p ->p.getUserString() != null)   // filtering price  
//		       
//		        .collect(Collectors.toList()); }

		ArrayList<ODSData> sortedList = new ArrayList<>();
//		int listSize = sortedList.size();
		//nested comparison
		loop: for(ODSData item1: list)
			{
			
				if(!sortedList.isEmpty()) {
//				if( listSize != 0) {
					for(ODSData item2: sortedList)
					{
					    if(item1.getUserKey() .equals(item2.getUserKey()))
					    	continue loop;
					    
					}
				   sortedList.add(item1);
				}
				else
					sortedList.add(item1);						
			}
	
			odsDetails.setOdsData(sortedList);
			odsDetails.setUserKeyType(MagicMonitorUtilities.getOdsType());
			
			return odsDetails;
	}
	
	
		public List<ODSData> findAll() {
		return oDSDataRepo.findAll();
	}

		public List<ODSData> findByuserKey(String userKey) {
			
			List<ODSData> odsChildData = oDSDataRepo.findByuserKey(userKey);
			
			for(ODSData item:odsChildData) {
				
			//For time attribute of child table
				if(item.getUserTime() != null) {
					
				 String timestamp1 = item.getUserTime();
				
				LocalDateTime date1 =
			            Instant.ofEpochMilli(Long.parseLong(timestamp1)).atZone(ZoneId.systemDefault()).toLocalDateTime();
		 	    DateTimeFormatter format1 = DateTimeFormatter.ofPattern("HH:mm:ss");  
		        String formatDateTime1 = date1.format(format1);
		        
		        item.setUserTime(formatDateTime1);
				}
		     
		      //For created attribute of child table
				if(item.getCreateTime() != null) {
				String timestamp2 = item.getCreateTime();
					
					LocalDateTime date2 =
				            Instant.ofEpochMilli(Long.parseLong(timestamp2)).atZone(ZoneId.systemDefault()).toLocalDateTime();
			 	    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("HH:mm:ss");  
			        String formatDateTime2 = date2.format(format2);
			        
			        item.setCreateTime(formatDateTime2);
			        
				}
				
				if(item.getModifyTime() != null) {
				String timestamp4 = item.getModifyTime();
					
					LocalDateTime date4 =
				            Instant.ofEpochMilli(Long.parseLong(timestamp4)).atZone(ZoneId.systemDefault()).toLocalDateTime();
			 	    DateTimeFormatter format4 = DateTimeFormatter.ofPattern("HH:mm:ss");  
			        String formatDateTime4 = date4.format(format4);
			        
			        item.setModifyTime(formatDateTime4);
				}
			}
			return odsChildData;
			
			//return oDSDataRepo.findByuserKey(userKey);
		}

		public String getBlob(@NotNull String projectKey,@NotNull double usernumber) {
			
			System.out.println("Project key :"+projectKey );
			System.out.println("user number :"+ usernumber);
			
			byte[] blob = null;
			blob = oDSDataRepo.displayBamBlob(projectKey,usernumber);
			
			System.out.println("BLOB :"+ blob);
			
			String format = "txt";
			int odsFileName = (int) usernumber;
			System.out.println("user number :"+ odsFileName);
					
			try (FileOutputStream fos = new FileOutputStream(new File(userblobFile + odsFileName +"."+format))){
				
	            fos.write(blob);
	            
	            System.out.println("Done\n");
	            return format;
			 }
	         catch (Exception ex) {
	            ex.printStackTrace();
	            return null;
	        }
		}
}