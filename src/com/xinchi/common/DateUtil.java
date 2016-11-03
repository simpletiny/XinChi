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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = sdf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DATE, days);
			return sdf.format(c.getTime());
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

//	public static void main(String[] args) {
//		System.out.println(getDateStr("HH:mm"));
//	}
}
