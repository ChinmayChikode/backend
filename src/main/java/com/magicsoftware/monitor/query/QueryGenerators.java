package com.magicsoftware.monitor.query;

import com.magicsoftware.monitor.model.QueryFilters;

public class QueryGenerators {

	public static String queryGeneratorForActivityLog(QueryFilters queryFiltersDTO) {

		String SQLQueryForActivityLog = null;

		try {
			//SQLQueryForActivityLog = QueryFactory.activityLogQueryBuilder(queryFiltersDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SQLQueryForActivityLog;

	}

}
