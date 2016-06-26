package com.lucifer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {

	public static final int PAFENO = 1;
	public static final int PAGESIZE = 3;
	
	public static Integer minute_10 =  60*10;
	
	public static Integer hour_1 =  3600;
	
	public static Integer day_30 = 30 * 24 * 3600;
	
	public static Integer day_60 = 60 * 24 * 3600;
	
	public static Integer day_180 = 180 * 24 * 3600;
	
	public static Integer day_365 = 365 * 24 * 3600;
	
	public static Date firstOnlineDate = null;
	
	static{
		try {
			firstOnlineDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-11-9");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
			
			
	
}
