package com.magicsoftware.monitor.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	
	public static Date addDatePart(int datePart, int adjestValue) {
		Calendar cal = Calendar.getInstance();
		cal.add(datePart, adjestValue);
		return cal.getTime();
	}
	
	public static Date addDatePart(Date date, int datePart, int adjestValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(datePart, adjestValue);
		return cal.getTime();
	}
	
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

}
