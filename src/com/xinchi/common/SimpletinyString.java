package com.xinchi.common;

import java.math.BigDecimal;
import java.security.MessageDigest;

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
}
