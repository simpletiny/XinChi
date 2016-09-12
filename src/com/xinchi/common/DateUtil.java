package com.xinchi.common;

import java.util.Calendar;

/**
 * 日期工具
 * @author Simpletiny
 *
 */
public class DateUtil {
	/**
	 * 
	 * @return
	 */
	public static String getTimeMillis(){
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}
}
