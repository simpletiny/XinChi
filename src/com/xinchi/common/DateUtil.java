package com.xinchi.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 * 
 * @author Simpletiny
 * 
 */
public class DateUtil {
	public static String YYYYMMDD = "yyyyMMdd";
	public static String YYYY_MM_DD = "yyyy-MM-dd";

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 * @return
	 */
	public static String getTimeMillis() {
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 计算加几天后的日期（date 格式yyyy-MM-dd)
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String addDate(String date, int days) {
		try {
			Date d = sdf1.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DATE, days);
			return sdf1.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 按格式取得日期字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateStr(String format) {
		Calendar c = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(c.getTime());
	}

	public static int dateDiff(String date1, String... date2) {
		try {
			Date d1 = sdf1.parse(date1);
			Date d2 = null;
			if (date2.length > 0) {
				d2 = sdf1.parse(date2[0]);
			} else {
				d2 = new Date();
			}
			long diff = Math.abs(d1.getTime() - d2.getTime());
			int days = (int) diff / (1000 * 60 * 60 * 24);
			return days;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static Date castStr2Date(String source, String... format) {
		 SimpleDateFormat sdf = sdf1;
		if(format.length>0){
			sdf = new SimpleDateFormat(format[0]);
		}
		
		try {
			return sdf.parse(source);
		} catch (ParseException e) {

			return null;
		}


	}

	// public static void main(String[] args) {
	// System.out.println(castStr2Date("2016-05-16 12:23:33","yyyy-MM-dd HH:mm:ss"));
	// }
}
