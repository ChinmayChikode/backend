package com.magicsoftware.monitor.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.magicsoftware.monitor.model.QueryFilters;

public class QueryFactory {

	public static StringBuilder SQLQueryBuilder = new StringBuilder();
	
	public static SimpleDateFormat defaultLocalFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
//			(SimpleDateFormat) DateFormat
//			.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());
	public static SimpleDateFormat formatForDBFilterOracle = new SimpleDateFormat("dd-MM-yy hh:mm:ss.SSS a");
	public static SimpleDateFormat formatForDatabaseFilter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static SimpleDateFormat formatForReceivedDateParsing = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	enum databaseDialects {
		ORG_HIBERNATE_DIALECT_SQLSERVERDIALECT, ORG_HIBERNATE_DIALECT_ORACLEDIALECT, ORG_HIBERNATE_DIALECT_MYSQLDIALECT,
		ORG_HIBERNATE_DIALECT_DB2400DIALECT, ORG_HIBERNATE_DIALECT_DB2DIALECT;
	}

	public static QueryFilters activityLogQueryBuilder(QueryFilters queryFilters) throws Exception {

		String SQLQueryToExecute = "";
		String DatabaseDialect = "";
		String removeDotFromDatabaseDialect = "";

		try {
			DatabaseDialect = "ORG_HIBERNATE_DIALECT_SQLSERVERDIALECT";// functionDesc.getArgStringValue("DatabaseDialect",
																		// functionIcon);
			removeDotFromDatabaseDialect = DatabaseDialect.toUpperCase().replace(".", "_");
			
			queryFilters.setActivityLogQuery(new StringBuilder());
			queryFilters.setActivityLogCountQuery(new StringBuilder());
			//queryFilters.setActivityLogDeleteQuery(new StringBuilder());
			/*
			 * queryFilters.setServerID(functionDesc.getArgStringValue("ServerIDAL",
			 * functionIcon)); queryFilters.setBPID(functionDesc.getArgStringValue("BPIDAL",
			 * functionIcon));
			 * queryFilters.setFlowID(functionDesc.getArgStringValue("FlowIDAL",
			 * functionIcon));
			 * queryFilters.setStepID(functionDesc.getArgStringValue("StepIDAL",
			 * functionIcon)); queryFilters
			 * .setErrorsAndUserMessages(functionDesc.getArgStringValue(
			 * "errorsAndUserMessagesAL", functionIcon));
			 * queryFilters.setLblFilterRootFSIDValue(
			 * functionDesc.getArgStringValue("lblFilterRootFSIDValueAL", functionIcon));
			 * queryFilters.setLblFilterMessageIDValue(
			 * functionDesc.getArgStringValue("lblFilterMessageIDValueAL", functionIcon));
			 * queryFilters.setLblFilterFSIDValue(functionDesc.getArgStringValue(
			 * "lblFilterFSIDValueAL", functionIcon));
			 * queryFilters.setMessageTypeIDString(functionDesc.getArgStringValue(
			 * "messageTypeIDStringAL", functionIcon));
			 * queryFilters.setLblFromDateValue(functionDesc.getArgStringValue(
			 * "lblFromDateValueAL", functionIcon));
			 * queryFilters.setLblToDateValue(functionDesc.getArgStringValue(
			 * "lblToDateValueAL", functionIcon));
			 * queryFilters.setProjKey(functionDesc.getArgStringValue("ProjKey",
			 * functionIcon));
			 * queryFilters.setPrevious(functionDesc.getArgStringValue("PreviousAL",
			 * functionIcon)); queryFilters.setNext(functionDesc.getArgStringValue("NextAL",
			 * functionIcon)); queryFilters.setRunId(functionDesc.getArgStringValue("RunId",
			 * functionIcon));
			 */

			/*
			 * SimpleDateFormat defaultLocalFormat = (SimpleDateFormat)
			 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,
			 * Locale.getDefault());
			 */
			String pattern = defaultLocalFormat.toPattern();
			SimpleDateFormat formatForReceivedDateParsing = new SimpleDateFormat(pattern);

			if ((queryFilters.getLblFromDateValue()) != null
					&& !((queryFilters.getLblFromDateValue()).equalsIgnoreCase(""))) {

				String receivedFromDateFromRTViewUser = queryFilters.getLblFromDateValue();

				Date parsedDateObject = formatForReceivedDateParsing.parse(receivedFromDateFromRTViewUser);
		
				String formattedStringDateForDBFilter = null;

				if ((removeDotFromDatabaseDialect.toUpperCase())
						.equals(String.valueOf(databaseDialects.ORG_HIBERNATE_DIALECT_ORACLEDIALECT))) {
					formattedStringDateForDBFilter = formatForDBFilterOracle.format(parsedDateObject);
				} else {
					formattedStringDateForDBFilter = formatForDatabaseFilter.format(parsedDateObject);
				}

				queryFilters.setLblFromDateValue(formattedStringDateForDBFilter);

			}

			if ((queryFilters.getLblToDateValue()) != null
					&& !((queryFilters.getLblToDateValue()).equalsIgnoreCase(""))) {

				String receivedToDateFromRTViewUser = queryFilters.getLblToDateValue();

				Date parsedDateObject = formatForReceivedDateParsing.parse(receivedToDateFromRTViewUser);
			
				String formattedStringDateForDBFilter = null;
				if ((removeDotFromDatabaseDialect.toUpperCase())
						.equals(String.valueOf(databaseDialects.ORG_HIBERNATE_DIALECT_ORACLEDIALECT))) {
					formattedStringDateForDBFilter = (formatForDBFilterOracle.format(parsedDateObject))
							.replaceAll(".000", ".999");
				} else {
					formattedStringDateForDBFilter = (formatForDatabaseFilter.format(parsedDateObject))
							.replaceAll(".000", ".999");
				}
				queryFilters.setLblToDateValue(formattedStringDateForDBFilter);

			}

			switch (databaseDialects.valueOf(removeDotFromDatabaseDialect)) {

			case ORG_HIBERNATE_DIALECT_SQLSERVERDIALECT:
				
				queryFilters = getActLogSQLQueryForSQLServerDialect(queryFilters);

				break;

			case ORG_HIBERNATE_DIALECT_ORACLEDIALECT:

				queryFilters.setActivityLogQuery(getActLogSQLQueryForOracleDialect(queryFilters));
				
				break;

			case ORG_HIBERNATE_DIALECT_MYSQLDIALECT:

				queryFilters = getActLogSQLQueryForMySQlDialect(queryFilters);
				
				break;

			case ORG_HIBERNATE_DIALECT_DB2400DIALECT:

				queryFilters.setActivityLogQuery(getActLogSQLQueryForDB2400Dialect(queryFilters));

				break;

			case ORG_HIBERNATE_DIALECT_DB2DIALECT:

				queryFilters.setActivityLogQuery(getActLogSQLQueryForDB2Dialect(queryFilters));
				
				break;

			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return queryFilters;
	}

	private static QueryFilters getActLogSQLQueryForSQLServerDialect(QueryFilters queryFilters) throws ParseException {

		queryFilters.getActivityLogQuery().append("SELECT	A.SERVERID\r\n" + "		,A.MSGID\r\n" + "		,A.BPID\r\n"
				+ "		,A.FLOWID\r\n" + "		,A.FSID\r\n" + "		,A.FSSTEP\r\n" + "		,A.MESSAGETYPEID\r\n"
				+ "		,A.MESSAGESTRING\r\n" + "		,A.CREATETIMESTAMP\r\n" + "		,A.OBJECTLEVEL\r\n"
				+ "		,A.PROJECTKEY\r\n" + "		,A.BLOBEXISTS\r\n" + "		,A.ROOTFSID\r\n"
				+ "		,A.FLOWREQUESTID\r\n" + "		,TEMP_MGXPI4_1.TEMP_ROW_NUM \r\n" + "		\r\n"
				+ "		FROM	IFS_ACTLOG AS A WITH (NOLOCK) INNER JOIN\r\n" + "		( \r\n"
				+ "			SELECT		MSGID\r\n"
				+ "						,ROW_NUMBER() OVER(ORDER BY CREATETIMESTAMP DESC,MSGID DESC) AS TEMP_ROW_NUM \r\n"
				+ "			FROM		IFS_ACTLOG WITH (NOLOCK)\r\n" + "			\r\n");
		
		queryFilters.getActivityLogCountQuery().append("SELECT COUNT(MSGID) AS \"COUNT(MSGID)\" FROM ifs_actlog");

		if (!((queryFilters.getProjKey()).equalsIgnoreCase(""))) {
			
			String projectKeyFilter = " WHERE PROJECTKEY='" + (queryFilters.getProjKey()) + "'";
			
			queryFilters.getActivityLogQuery().append(projectKeyFilter);
			queryFilters.getActivityLogCountQuery().append(projectKeyFilter);
		}

		if (!((queryFilters.getLblFromDateValue()).trim().equalsIgnoreCase("0")
				|| (queryFilters.getLblFromDateValue()).trim().equalsIgnoreCase(""))) {
			
			String projectTimestampFilter = " AND (CREATETIMESTAMP <= '" + queryFilters.getLblToDateValue()
			+ "' AND CREATETIMESTAMP >= '" + queryFilters.getLblFromDateValue() + "')";
			
			queryFilters.getActivityLogQuery().append(projectTimestampFilter);
			queryFilters.getActivityLogCountQuery().append(projectTimestampFilter);
		}

		queryFilters.getActivityLogQuery().append(getActivityLogAllIDsFiltered(queryFilters));
		queryFilters.getActivityLogCountQuery().append(getActivityLogAllIDsFiltered(queryFilters));

		queryFilters.getActivityLogQuery().append(") AS TEMP_MGXPI4_1 ON A.MSGID = TEMP_MGXPI4_1.MSGID");

		if (!((Integer.valueOf(queryFilters.getPrevious()) == 0)) && !((Integer.valueOf(queryFilters.getNext()) == 0))) {
			queryFilters.getActivityLogQuery().append(" WHERE TEMP_MGXPI4_1.TEMP_ROW_NUM BETWEEN " + (queryFilters.getPrevious()) + " AND "
					+ (queryFilters.getNext()) + "");
		}

		return queryFilters;

	}

	@SuppressWarnings("deprecation")
	private static StringBuilder getActLogSQLQueryForOracleDialect(QueryFilters filtersDTO) {

		/*
		 * SQLQueryBuilder
		 * .append("SELECT SERVERID,MSGID,BPID,FLOWID,FSID,FSSTEP,MESSAGETYPEID,MESSAGESTRING,USERCODE,CREATEDATE,CREATETIME,OBJECTLEVEL,CATEGORY,USERKEY1,USERKEY2,VERSIONKEY,STATUSCODE,SEVERITY,EXTENSION,PROJECTKEY,BLOBEXISTS,ROOTFSID,FLOWREQUESTID,FILELOCATION,RUNID,DateTimeObject,TEMP_ROW_NUM FROM ( SELECT SERVERID,MSGID,BPID,FLOWID,FSID,FSSTEP,MESSAGETYPEID,MESSAGESTRING,USERBLOB,USERCODE,CREATEDATE,CREATETIME,OBJECTLEVEL,CATEGORY,USERKEY1,USERKEY2,VERSIONKEY,STATUSCODE,SEVERITY,EXTENSION,PROJECTKEY,BLOBEXISTS,ROOTFSID,FLOWREQUESTID,FILELOCATION,RUNID,REPLACE(TO_CHAR(CREATEDATE,'YYYY-MM-DD'),'-','') || CAST(CREATETIME AS VARCHAR2(50)) AS DateTimeObject,ROW_NUMBER() OVER(ORDER BY CREATEDATE DESC,CREATETIME DESC,MSGID DESC) AS TEMP_ROW_NUM FROM IFS_ACTLOG"
		 * );
		 */

		SQLQueryBuilder.append("SELECT \r\n" + "		A.SERVERID,\r\n" + "		A.MSGID,\r\n" + "		A.BPID,\r\n"
				+ "		A.FLOWID,\r\n" + "		A.FSID,\r\n" + "		A.FSSTEP,\r\n" + "		A.MESSAGETYPEID,\r\n"
				+ "		A.MESSAGESTRING,\r\n" + "		A.CREATETIMESTAMP,\r\n" + "		A.OBJECTLEVEL,\r\n"
				+ "		A.PROJECTKEY,\r\n" + "		A.BLOBEXISTS,\r\n" + "		A.ROOTFSID,\r\n"
				+ "		A.FLOWREQUESTID,\r\n" + "		TEMP_MGXPI4_1.TEMP_ROW_NUM\r\n" + "FROM IFS_ACTLOG A\r\n"
				+ "  INNER JOIN (\r\n" + "    SELECT \r\n" + "      MSGID, \r\n"
				+ "      ROW_NUMBER() OVER (ORDER BY CREATETIMESTAMP DESC,MSGID DESC) TEMP_ROW_NUM\r\n"
				+ "    FROM IFS_ACTLOG");

		if (!((filtersDTO.getProjKey()).equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" WHERE PROJECTKEY='" + (filtersDTO.getProjKey()) + "'");
		}

		/*
		 * if (!((filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase("0") ||
		 * (filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase(""))) {
		 * SQLQueryBuilder.
		 * append(" AND (REPLACE(TO_CHAR(CREATEDATE,'YYYY-MM-DD'),'-','') || CAST(CREATETIME AS VARCHAR2(50))) >= '"
		 * + (filtersDTO.getLblFromDateValue()) + "'"); }
		 * 
		 * if (!((filtersDTO.getLblToDateValue()).trim().equalsIgnoreCase("0") ||
		 * (filtersDTO.getLblToDateValue()).trim().equalsIgnoreCase(""))) {
		 * SQLQueryBuilder.
		 * append(" AND (REPLACE(TO_CHAR(CREATEDATE,'YYYY-MM-DD'),'-','') || CAST(CREATETIME AS VARCHAR2(50))) <= '"
		 * + (filtersDTO.getLblToDateValue()) + "'"); }
		 */

		if (!((filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase("0")
				|| (filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase(""))) {
			// SQLQueryBuilder.append(" AND (CREATETIMESTAMP <= '"+
			// filtersDTO.getLblToDateValue() +"' AND CREATETIMESTAMP >= '"+
			// filtersDTO.getLblFromDateValue() +"')");
			SQLQueryBuilder.append(" AND (CREATETIMESTAMP <= TO_TIMESTAMP('" + filtersDTO.getLblToDateValue() + "'"
					+ ",'DD-MM-YY HH12:MI:SS.FF3 AM')" + "AND CREATETIMESTAMP >= TO_TIMESTAMP('"
					+ filtersDTO.getLblFromDateValue() + "'" + ",'DD-MM-YY HH12:MI:SS.FF3 AM'))");
		}

		SQLQueryBuilder.append(getActivityLogAllIDsFiltered(filtersDTO));

		SQLQueryBuilder.append(") TEMP_MGXPI4_1 ON A.MSGID = TEMP_MGXPI4_1.MSGID");

		/*if (!((filtersDTO.getPrevious().trim().equalsIgnoreCase("0"))
				|| (filtersDTO.getPrevious().trim().equalsIgnoreCase("")))) {
			SQLQueryBuilder.append(" WHERE TEMP_MGXPI4_1.TEMP_ROW_NUM BETWEEN " + (filtersDTO.getPrevious()) + " AND "
					+ (filtersDTO.getNext()) + "");
		}*/

		return SQLQueryBuilder;

	}

	private static QueryFilters getActLogSQLQueryForMySQlDialect(QueryFilters queryFilters) {

		/*
		 * SQLQueryBuilder
		 * .append("SELECT SERVERID,CAST(MSGID AS DECIMAL(12,0)) as MSGID,BPID,FLOWID,FSID,FSSTEP,MESSAGETYPEID,MESSAGESTRING,USERBLOB,USERCODE,CREATEDATE,CREATETIME,OBJECTLEVEL,CATEGORY,USERKEY1,USERKEY2,VERSIONKEY,STATUSCODE,SEVERITY,EXTENSION,PROJECTKEY,BLOBEXISTS,ROOTFSID,FLOWREQUESTID,FILELOCATION,RUNID,TEMP_MGXPI4_1.DateTimeObject FROM ( SELECT SERVERID,MSGID,BPID,FLOWID,FSID,FSSTEP,MESSAGETYPEID,MESSAGESTRING,USERBLOB,USERCODE,CREATEDATE,CREATETIME,OBJECTLEVEL,CATEGORY,USERKEY1,USERKEY2,VERSIONKEY,STATUSCODE,SEVERITY,EXTENSION,PROJECTKEY,BLOBEXISTS,ROOTFSID,FLOWREQUESTID,FILELOCATION,RUNID,CONCAT(REPLACE(CONVERT(CREATEDATE,CHAR(10)),'-',''),CONVERT(CREATETIME,CHAR)) AS DateTimeObject FROM IFS_ACTLOG"
		 * );
		 */
		/*
		 * SQLQueryBuilder.append("SELECT \r\n" + "	A.SERVERID,\r\n" +
		 * "	A.MSGID,\r\n" + "	A.BPID,\r\n" + "	A.FLOWID,\r\n" + "	A.FSID,\r\n"
		 * + "	A.FSSTEP,\r\n" + "	A.MESSAGETYPEID,\r\n" + "	A.MESSAGESTRING,\r\n" +
		 * "	A.CREATETIMESTAMP AS \"Date & Time\",\r\n" + "	A.OBJECTLEVEL,\r\n" +
		 * "	A.PROJECTKEY,\r\n" + "	A.BLOBEXISTS,\r\n" + "	A.ROOTFSID,\r\n" +
		 * "	A.FLOWREQUESTID\r\n" + "	FROM MGXPI4_1.IFS_ACTLOG A INNER JOIN (\r\n"
		 * + "   SELECT \r\n" + "   MSGID\r\n" + "   FROM MGXPI4_1.IFS_ACTLOG");
		 */

		// SQLQueryBuilder.append("SELECT
		// A.SERVERID,A.MSGID,A.BPID,A.FLOWID,A.FSID,A.FSSTEP,A.MESSAGETYPEID,A.MESSAGESTRING,A.CREATETIMESTAMP,A.OBJECTLEVEL,A.PROJECTKEY,A.BLOBEXISTS,A.ROOTFSID,A.FLOWREQUESTID
		// FROM IFS_ACTLOG A INNER JOIN (SELECT MSGID FROM IFS_ACTLOG");
		queryFilters.getActivityLogQuery().append(
				"SELECT A.SERVERID,A.MSGID,A.BPID,A.FLOWID,A.FSID,A.FSSTEP,A.MESSAGETYPEID,A.MESSAGESTRING,A.CREATETIMESTAMP,A.OBJECTLEVEL,A.PROJECTKEY,A.BLOBEXISTS,A.ROOTFSID,A.FLOWREQUESTID FROM IFS_ACTLOG A ");

		queryFilters.getActivityLogCountQuery().append("SELECT COUNT(MSGID) AS \"COUNT(MSGID)\" FROM ifs_actlog");	
		
		if (!((queryFilters.getProjKey()).equalsIgnoreCase(""))) {
			
			String projectKeyFilter = " WHERE PROJECTKEY='" + (queryFilters.getProjKey()) + "'";
			
			queryFilters.getActivityLogQuery().append(projectKeyFilter);
			queryFilters.getActivityLogCountQuery().append(projectKeyFilter);
		}

		/*
		 * if (!((filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase("0") ||
		 * (filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase(""))) {
		 * SQLQueryBuilder.
		 * append(" AND (CONCAT(REPLACE(CONVERT(CREATEDATE,CHAR(10)),'-',''),CONVERT(CREATETIME,CHAR))) >= '"
		 * + (filtersDTO.getLblFromDateValue()) + "'"); }
		 * 
		 * if (!((filtersDTO.getLblToDateValue()).trim().equalsIgnoreCase("0") ||
		 * (filtersDTO.getLblToDateValue()).trim().equalsIgnoreCase(""))) {
		 * SQLQueryBuilder.
		 * append(" AND (CONCAT(REPLACE(CONVERT(CREATEDATE,CHAR(10)),'-',''),CONVERT(CREATETIME,CHAR))) <='"
		 * + (filtersDTO.getLblToDateValue()) + "'"); }
		 */

		if (!((queryFilters.getLblFromDateValue()).trim().equalsIgnoreCase("0")
				|| (queryFilters.getLblFromDateValue()).trim().equalsIgnoreCase(""))) {
			
			String projectTimestampFilter = " AND (CREATETIMESTAMP <= '" + queryFilters.getLblToDateValue()
			+ "' AND CREATETIMESTAMP >= '" + queryFilters.getLblFromDateValue() + "')";
			
			queryFilters.getActivityLogQuery().append(projectTimestampFilter);
			queryFilters.getActivityLogCountQuery().append(projectTimestampFilter);
			
		}

		queryFilters.getActivityLogQuery().append(getActivityLogAllIDsFiltered(queryFilters));
		queryFilters.getActivityLogCountQuery().append(getActivityLogAllIDsFiltered(queryFilters));

		// SQLQueryBuilder.append(" ) TEMP_MGXPI4_1 ON A.MSGID = TEMP_MGXPI4_1.MSGID
		// ORDER BY CREATETIMESTAMP DESC,MSGID DESC");

		queryFilters.getActivityLogQuery().append(" ORDER BY CREATETIMESTAMP DESC,MSGID DESC");

		/*
		 * if (!((filtersDTO.getPrevious().trim().equalsIgnoreCase("0")) ||
		 * (filtersDTO.getPrevious().trim().equalsIgnoreCase("")))) {
		 * SQLQueryBuilder.append(" LIMIT " + (filtersDTO.getNext()) + " OFFSET " +
		 * (Integer.parseInt(filtersDTO.getPrevious()) - 1) + " "); }
		 */
		if (((queryFilters.getPrevious()!=0)
				|| (queryFilters.getPrevious()!=null))) {
			int pageCount = (queryFilters.getNext() - queryFilters.getPrevious()) + 1;
			queryFilters.getActivityLogQuery().append(
					" LIMIT " + pageCount + " OFFSET " + (queryFilters.getPrevious() - 1) + " ");
		}

		return queryFilters;

	}

	private static StringBuilder getActLogSQLQueryForDB2400Dialect(QueryFilters filtersDTO) {

		SQLQueryBuilder.append(
				"SELECT A.SERVERID,A.MSGID,A.BPID,A.FLOWID,A.FSID,A.FSSTEP,A.MESSAGETYPEID,A.MESSAGESTRING,A.CREATETIMESTAMP,A.OBJECTLEVEL,A.PROJECTKEY,A.BLOBEXISTS,A.ROOTFSID,A.FLOWREQUESTID,TEMP_MGXPI4_1.TEMP_ROW_NUM FROM IFS_ACTLOG A INNER JOIN ( SELECT MSGID,ROW_NUMBER() OVER(ORDER BY CREATETIMESTAMP DESC,MSGID DESC) AS TEMP_ROW_NUM FROM IFS_ACTLOG");

		if (!((filtersDTO.getProjKey()).equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" WHERE PROJECTKEY='" + (filtersDTO.getProjKey()) + "'");
		}

		if (!((filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase("0")
				|| (filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND (CREATETIMESTAMP <= '" + filtersDTO.getLblToDateValue()
					+ "' AND CREATETIMESTAMP >= '" + filtersDTO.getLblFromDateValue() + "')");
		}

		SQLQueryBuilder.append(getActivityLogAllIDsFiltered(filtersDTO));

		SQLQueryBuilder.append(") AS TEMP_MGXPI4_1 ON A.MSGID = TEMP_MGXPI4_1.MSGID");

		/*if (!((filtersDTO.getPrevious().trim().equalsIgnoreCase("0"))
				|| (filtersDTO.getPrevious().trim().equalsIgnoreCase("")))) {
			SQLQueryBuilder.append(" WHERE TEMP_MGXPI4_1.TEMP_ROW_NUM BETWEEN " + (filtersDTO.getPrevious()) + " AND "
					+ (filtersDTO.getNext()) + "");
		}*/

		return SQLQueryBuilder;

	}

	private static StringBuilder getActLogSQLQueryForDB2Dialect(QueryFilters filtersDTO) {

		SQLQueryBuilder.append(
				"SELECT A.SERVERID,A.MSGID,A.BPID,A.FLOWID,A.FSID,A.FSSTEP,A.MESSAGETYPEID,A.MESSAGESTRING,A.CREATETIMESTAMP,A.OBJECTLEVEL,A.PROJECTKEY,A.BLOBEXISTS,A.ROOTFSID,A.FLOWREQUESTID,TEMP_MGXPI4_1.TEMP_ROW_NUM FROM MGXPI4_1.IFS_ACTLOG A INNER JOIN ( SELECT MSGID,ROW_NUMBER() OVER(ORDER BY CREATETIMESTAMP DESC,MSGID DESC) AS TEMP_ROW_NUM FROM MGXPI4_1.IFS_ACTLOG");

		if (!((filtersDTO.getProjKey()).equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" WHERE PROJECTKEY='" + (filtersDTO.getProjKey()) + "'");
		}

		if (!((filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase("0")
				|| (filtersDTO.getLblFromDateValue()).trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND (CREATETIMESTAMP <= '" + filtersDTO.getLblToDateValue()
					+ "' AND CREATETIMESTAMP >= '" + filtersDTO.getLblFromDateValue() + "')");
		}

		SQLQueryBuilder.append(getActivityLogAllIDsFiltered(filtersDTO));

		SQLQueryBuilder.append(") AS TEMP_MGXPI4_1 ON A.MSGID = TEMP_MGXPI4_1.MSGID");

		/*if (!((filtersDTO.getPrevious().trim().equalsIgnoreCase("0"))
				|| (filtersDTO.getPrevious().trim().equalsIgnoreCase("")))) {
			SQLQueryBuilder.append(" WHERE TEMP_MGXPI4_1.TEMP_ROW_NUM BETWEEN " + (filtersDTO.getPrevious()) + " AND "
					+ (filtersDTO.getNext()) + "");
		}*/

		return SQLQueryBuilder;

	}

	private static StringBuilder getActivityLogAllIDsFiltered(QueryFilters filtersDTO) {

		StringBuilder SQLQueryBuilder = new StringBuilder();

		if (filtersDTO.getErrorsAndUserMessages().trim().equalsIgnoreCase("Errors")) {
			SQLQueryBuilder.append(" AND MESSAGETYPEID IN(9,15)");
		} else if (filtersDTO.getErrorsAndUserMessages().trim().equalsIgnoreCase("Component Logging")) {
			SQLQueryBuilder.append(" AND MESSAGETYPEID IN(9,15,14,50,56)");
		}

		if (!(filtersDTO.getServerID().trim().equalsIgnoreCase("*")
				|| filtersDTO.getServerID().trim().equalsIgnoreCase("ALL")
				|| filtersDTO.getServerID().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND SERVERID = " + filtersDTO.getServerID() + "");
		}

		if (!(filtersDTO.getBPID().trim().equalsIgnoreCase("*") || filtersDTO.getBPID().trim().equalsIgnoreCase("ALL")
				|| filtersDTO.getBPID().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND BPID = " + filtersDTO.getBPID() + "");
		}

		if (!(filtersDTO.getFlowID().trim().equalsIgnoreCase("*")
				|| filtersDTO.getFlowID().trim().equalsIgnoreCase("ALL")
				|| filtersDTO.getFlowID().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND FLOWID = " + filtersDTO.getFlowID() + "");
		}

		if (!(filtersDTO.getStepID().trim().equalsIgnoreCase("*")
				|| filtersDTO.getStepID().trim().equalsIgnoreCase("ALL")
				|| filtersDTO.getStepID().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND FSSTEP = " + filtersDTO.getStepID() + "");
		}

		if (!(filtersDTO.getLblFilterRootFSIDValue().trim().equalsIgnoreCase("0")
				|| filtersDTO.getLblFilterRootFSIDValue().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND ROOTFSID = " + filtersDTO.getLblFilterRootFSIDValue() + "");
		}

		if (!(filtersDTO.getLblFilterFlowReqIDValue().trim().equalsIgnoreCase("0")
				|| filtersDTO.getLblFilterFlowReqIDValue().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND FLOWREQUESTID = " + filtersDTO.getLblFilterFlowReqIDValue() + "");
		}

		if (!(filtersDTO.getLblFilterFSIDValue().trim().equalsIgnoreCase("0")
				|| filtersDTO.getLblFilterFSIDValue().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND FSID = " + filtersDTO.getLblFilterFSIDValue() + "");
		}

		/*
		 * if (!(filtersDTO.getRunId().trim().equalsIgnoreCase("0") ||
		 * filtersDTO.getRunId().trim().equalsIgnoreCase(""))) {
		 * SQLQueryBuilder.append(" AND RUNID = '" + filtersDTO.getRunId() + "'"); }
		 */

		if (!(filtersDTO.getMessageTypeIDString().trim().equalsIgnoreCase(""))) {
			SQLQueryBuilder.append(" AND MESSAGETYPEID NOT IN (" + filtersDTO.getMessageTypeIDString() + ")");
		}

		return SQLQueryBuilder;

	}
	
	public static QueryFilters deleteActLogQueryBuilder(QueryFilters queryFilters) {
		
		String SQLQueryToExecute = "";
		String DatabaseDialect = "";
		String removeDotFromDatabaseDialect = "";
		
		queryFilters.setActivityLogDeleteQuery(new StringBuilder());

		try {
			/*
			 * DatabaseDialect = functionDesc.getArgStringValue("DatabaseDialect",
			 * functionIcon); removeDotFromDatabaseDialect =
			 * DatabaseDialect.toUpperCase().replace(".", "_");
			 */
			
			DatabaseDialect = "ORG_HIBERNATE_DIALECT_SQLSERVERDIALECT";// functionDesc.getArgStringValue("DatabaseDialect",
			// functionIcon);
			removeDotFromDatabaseDialect = DatabaseDialect.toUpperCase().replace(".", "_");

			//String projectKey = functionDesc.getArgStringValue("ProjKey", functionIcon);
			//String selectedFromDateByUser = functionDesc.getArgStringValue("CreateDate", functionIcon);

			Date dateTimeStamp = null;
		    try {
		    	dateTimeStamp = formatForReceivedDateParsing.parse(queryFilters.getActLogDeletionDate());
		    }
		    catch (ParseException e) {
		      e.printStackTrace();
		    }
			
			switch (databaseDialects.valueOf(removeDotFromDatabaseDialect)) {

			case ORG_HIBERNATE_DIALECT_SQLSERVERDIALECT:

				queryFilters.getActivityLogDeleteQuery().append("DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + (formatForDatabaseFilter.format(dateTimeStamp)).replaceAll(".000", ".999") + "'");
				
				//SQLQueryToExecute = "DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + (formatForDatabaseFilter.format(dateTimeStamp)).replaceAll(".000", ".999") + "'";

				break;

			case ORG_HIBERNATE_DIALECT_ORACLEDIALECT:

				queryFilters.getActivityLogDeleteQuery().append("DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < TO_TIMESTAMP('" + (formatForDBFilterOracle.format(dateTimeStamp)).replaceAll(".000", ".999") + "','DD-MM-YY HH12:MI:SS.FF3 AM')");
				
				//SQLQueryToExecute = "DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < TO_TIMESTAMP('" + (formatForDBFilterOracle.format(dateTimeStamp)).replaceAll(".000", ".999") + "','DD-MM-YY HH12:MI:SS.FF3 AM')";

				break;

			case ORG_HIBERNATE_DIALECT_MYSQLDIALECT:

				queryFilters.getActivityLogDeleteQuery().append("DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + (formatForDatabaseFilter.format(dateTimeStamp)).replaceAll(".000", ".999") + "'");
				
				//SQLQueryToExecute = "DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + (formatForDatabaseFilter.format(dateTimeStamp)).replaceAll(".000", ".999") + "'";
				
				break;

			case ORG_HIBERNATE_DIALECT_DB2400DIALECT:

				queryFilters.getActivityLogDeleteQuery().append("DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + formatForDatabaseFilter.format(dateTimeStamp) + "'");
				
				//SQLQueryToExecute = "DELETE FROM IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + formatForDatabaseFilter.format(dateTimeStamp) + "'";
				
				break;

			case ORG_HIBERNATE_DIALECT_DB2DIALECT:

				queryFilters.getActivityLogDeleteQuery().append("DELETE FROM MGXPI4_1.IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + formatForDatabaseFilter.format(dateTimeStamp) + "'");
				//SQLQueryToExecute = "DELETE FROM MGXPI4_1.IFS_ACTLOG WHERE PROJECTKEY='" + queryFilters.getProjKey() + "' AND CREATETIMESTAMP < '" + formatForDatabaseFilter.format(dateTimeStamp) + "'";

				break;

			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return queryFilters;
	}

}
