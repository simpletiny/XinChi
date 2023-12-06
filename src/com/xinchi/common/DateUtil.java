package com.xinchi.common;

import static com.xinchi.common.SimpletinyString.isEmpty;

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
	public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");

	public static SimpleDateFormat sdf3 = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
	public static SimpleDateFormat sdf4 = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

	/**
	 * 
	 * @return
	 */
	public static String getTimeMillis() {
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}

	public static String getTimeMillis(String date) {
		try {
			Date d = sdf1.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return String.valueOf(c.getTimeInMillis());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

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

	public static String lastMonth(String... format) {
		SimpleDateFormat sdf = sdf2;
		if (format.length > 0) {
			sdf = new SimpleDateFormat(format[0]);
		}
		Calendar c = Calendar.getInstance();

		// 减去一个月
		c.add(Calendar.MONTH, -1);
		return sdf.format(c.getTime());
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

	public static String today() {
		Calendar c = Calendar.getInstance();

		return sdf1.format(c.getTime());
	}

	public static String yesterday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		return sdf1.format(c.getTime());
	}

	public static String getMinStr() {
		Calendar c = Calendar.getInstance();
		return sdf3.format(c.getTime());
	}

	public static String getTimeStr() {
		Calendar c = Calendar.getInstance();
		return sdf4.format(c.getTime());
	}

	public static int dateDiff(String date1, String... date2) {
		if (null == date1)
			return 0;
		try {
			Date d1 = sdf1.parse(date1);
			Date d2 = null;
			if (date2.length > 0) {
				d2 = sdf1.parse(date2[0]);
			} else {
				d2 = new Date();
			}
			long diff = Math.abs(d1.getTime() - d2.getTime());
			int days = (int) (diff / (1000 * 60 * 60 * 24));
			return days;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static Date castStr2Date(String source, String... format) {
		SimpleDateFormat sdf = sdf1;
		if (format.length > 0) {
			sdf = new SimpleDateFormat(format[0]);
		}
		if (isEmpty(source)) {
			source = "1988-03-22";
		}
		try {

			return sdf.parse(source);
		} catch (ParseException e) {

			return null;
		}

	}

	public static String castDate2Str(Date date, String... format) {
		if (format.length > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(format[0]);
			return sdf.format(date);
		} else {
			return sdf1.format(date);
		}
	}

	/**
	 * 获取世界调和时间
	 * 
	 * @param date
	 * @return
	 */
	public static String getUTC(String... date) {
		Calendar cal = Calendar.getInstance();

		if (date.length > 0) {
			Date d1 = castStr2Date(date[0]);
			cal.setTime(d1);
		}

		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return sdf.format(cal.getTime());
	}

	public static String getLastDay(String month, String... format) {
		Date d = null;
		if (format.length > 0) {
			d = castStr2Date(month, format[0]);
		} else {
			d = castStr2Date(month, "yyyy-MM");
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

		return sdf1.format(cal.getTime());
	}

	public static String addOneMin(String source) {
		Date d = castStr2Date(source, YYYY_MM_DD_HH_MM);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE, 1);

		return sdf3.format(c.getTime());
	}

	public static String addMin(String date, int num) {
		try {
			Date d = sdf4.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.MINUTE, num);
			return sdf4.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getThisWeekFirstDay() {
		Calendar cal = Calendar.getInstance();

		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		// 所在周开始日期
		return sdf1.format(cal.getTime());
	}

	public static String getThisWeekLastDay() {
		Calendar cal = Calendar.getInstance();

		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}

		cal.add(Calendar.DAY_OF_WEEK, d + 6);
		// 所在周结束日期
		return sdf1.format(cal.getTime());

	}

	/**
	 * 
	 * @param date1
	 * @param date2
	 *            为空时为今天
	 * @return 0 date1 = date2,1 date1>date2, 2 date1<date2
	 */
	public static int compare(String date1, String... date2) {
		if (isEmpty(date1))
			return 0;

		try {
			Date d1 = sdf1.parse(date1);
			Date d2 = null;
			if (date2.length > 0) {
				d2 = sdf1.parse(date2[0]);
			} else {
				d2 = sdf1.parse(today());
			}
			if (d1.equals(d2)) {
				return 0;
			} else if (d1.after(d2)) {
				return 1;
			} else if (d1.before(d2)) {
				return 2;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int todayOfMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	public static int dayOfWeek(String date) {
		try {
			Date d = sdf1.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);

			int res = c.get(Calendar.DAY_OF_WEEK);
			return (res + 6) % 7;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean isWeekend(String date) {
		int res = dayOfWeek(date);
		return res == 0 || res == 6;
	}

	public static int workDays() {
		int max = DateUtil.todayOfMonth();
		int min = 0;
		int count = 0;
		Calendar c = Calendar.getInstance();
		while (max > min) {
			c.set(Calendar.DAY_OF_MONTH, max);
			if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
				count++;
			}
			max--;
		}

		return count;
	}

	/**
	 * 获取当月有多少天
	 * 
	 * @return
	 */
	public static int getMaxDay(String month, String... format) {
		Date d = null;
		if (format.length > 0) {
			d = castStr2Date(month, format[0]);
		} else {
			d = castStr2Date(month, "yyyy-MM");
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(d);

		return cal.getActualMaximum(Calendar.DATE);
	}

	public static void main(String[] args) {

		System.out.println(yesterday());
	}

	public static String fromUnixTime(String time, String... format) {
		if (isEmpty(time))
			return "";
		String date = "";
		if (format.length > 0) {
			SimpleDateFormat ft = new SimpleDateFormat(format[0]);
			date = ft.format(new Date(Long.parseLong(time)));

		} else {
			date = sdf4.format(new Date(Long.parseLong(time)));
		}
		return date;
	}

}
