package com.xinchi.common;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 字符串工具类
 * 
 * @author simpletiny
 * 
 */
public class SimpletinyString {
	/**
	 * 字符串MD5加密100次
	 * 
	 * @param strSource
	 * @return 加密后的密文
	 */
	public static String MD5(String strSource) {
		try {
			for (int x = 0; x < 100; x++) {
				strSource = MD5OneTime(strSource);
			}
			return strSource;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * md5执行一次
	 * 
	 * @param strSource
	 * @return
	 */
	public static String MD5OneTime(String strSource) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {

			byte[] strTemp = strSource.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// 将没个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			strSource = String.valueOf(str);

			return strSource;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isEmpty(String str) {

		return null == str || str.equals("");
	}

	public static int str2Int(String str) {
		return (null == str || str.trim().equals("")) ? 0 : Integer.valueOf(str.trim());
	}

	public static BigDecimal str2Decimal(String str) {
		return (null == str || str.trim().equals("")) ? BigDecimal.ZERO : new BigDecimal((str.trim()));
	}

	public static String addSingleQuote(String str) {
		if (isEmpty(str))
			return "";
		return "'" + str.replaceAll(",", "','") + "'";
	}

	public static String left(String src, int length) {
		if (isEmpty(src))
			return src;
		int len = src.length();
		return len >= length ? src.substring(0, length) : src;
	}

	public static String replaceWhenWithComma(String src, String cha) {
		String b = src.replaceAll(",{0,1}" + cha + ",{0,1}", ",");
		if (b.endsWith(",")) {
			b = b.substring(0, b.length() - 1);
		}

		if (b.startsWith(",")) {
			b = b.substring(1);
		}
		return b;
	}

	public static String removeEmoji(String src) {
		String regex = "[\\x{1F600}-\\x{1F64F}\\x{1F300}-\\x{1F5FF}\\x{1F680}-\\x{1F6FF}\\x{2600}-\\x{26FF}\\x{2700}-\\x{27BF}\\x{1F900}-\\x{1F9FF}\\x{1F1E0}-\\x{1F1FF}\\x{1F191}-\\x{1F251}\\x{1F004}\\x{1F0CF}\\x{1F170}-\\x{1F171}\\x{1F17E}-\\x{1F17F}\\x{1F18E}\\x{3030}\\x{2B50}\\x{2B55}\\x{2934}-\\x{2935}\\x{2B05}-\\x{2B07}\\x{2B1B}-\\x{2B1C}\\x{3297}\\x{3299}\\x{303D}\\x{00A9}\\x{00AE}\\x{2122}\\x{23F3}\\x{24C2}\\x{25B6}\\x{23F8}-\\x{23FA}]";
		String output = src.replaceAll(regex, "");
		return output;
	}

	public static String replaceCharFromLeft(String src, String replacer, Integer... length) {
		int len = 1;
		if (length.length > 0)
			len = length[0];
		if (len > src.length())
			return "";
		String result = "";
		result = src.substring(len);
		result = replacer + result;
		return result;
	}

	public static String replaceCharFromRight(String src, String replacer, Integer... length) {
		int len = 1;
		if (length.length > 0)
			len = length[0];
		if (len > src.length())
			return "";
		String result = "";
		result = src.substring(0, src.length() - len);
		result = result + replacer;
		return result;
	}

	public static boolean isNumeric(String src) {
		return null != src && src.matches("^\\d*\\.?\\d+$");
	}

	public static Map<String, String> analysisId(String id) {
		Map<String, String> result = new HashMap<>();
		String id_type = getIdType(id);
		result.put("idtype", id_type);
		if (!id_type.equals("身份证")) {
			result.put("age", "");
			result.put("birthdate", "");
			result.put("gender", "");
			result.put("passengertype", "");
			return result;
		}
		int age = getAgeFromId(id);
		result.put("age", String.valueOf(age));
		result.put("birthdate", getBirthDateFromId(id));
		result.put("gender", getGenderFromId(id));
		result.put("passengertype", getPassengerTypeFromAge(age));
		return null;
	}

	public static String getPassengerTypeFromAge(int age) {
		if (age >= 0 && age < 2) {
			return "婴儿";
		} else if (age >= 2 && age < 12) {
			return "儿童";
		} else if (age >= 12 && age < 22) {
			return "青少年";
		} else if (age >= 22 && age < 60) {
			return "成人";
		} else if (age >= 60 && age < 70) {
			return "中老年";
		} else if (age >= 70) {
			return "老年";
		} else {
			return "妖怪";
		}
	}

	private static String getIdType(String id) {
		if (id == null) {
			return "FAIL";
		}
		// 检查字符串的第一个字符是否为数字
		char firstChar = id.charAt(0);
		if (!Character.isDigit(firstChar) || id.length() != 18) {
			return "护照";
		} else {
			return "身份证";
		}
	}

	public static String getGenderFromId(String id) {
		char ad = id.charAt(id.length() - 2);
		return Character.getNumericValue(ad) % 2 == 0 ? "女" : "男";
	}

	public static String getBirthDateFromId(String id) {
		String birthDateString = id.substring(6, 14);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
		return birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public static int getAgeFromId(String idCard) {
		// 获取出生日期
		String birthDateString = idCard.substring(6, 14);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
		// 获取当前日期
		LocalDate currentDate = LocalDate.now();

		// 计算年龄
		return Period.between(birthDate, currentDate).getYears();
	}

}
