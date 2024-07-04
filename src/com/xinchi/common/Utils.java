package com.xinchi.common;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import com.xinchi.tools.PropertiesUtil;

public class Utils {

	public static String fullFill(int source, String sign, int size) {
		String s = String.valueOf(source);
		if (s.length() >= size) {
			return s;
		} else {
			String before = "";
			for (int i = 0; i < size - s.length(); i++) {
				before += sign;
			}
			return before + s;
		}
	}

	public static String getFileExt(String fileName) {
		String ext = "";
		if (null == fileName || fileName.equals(""))
			return "";
		ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		return ext;
	}

	public static String deleteFileFromDisk(String fileType, String subFolder, String fileName) {
		String baseFolder = PropertiesUtil.getProperty(FileFolder.valueOf(fileType).value());
		if (!isEmpty(subFolder)) {
			baseFolder = baseFolder + File.separator + subFolder;
		}
		File file = new File(baseFolder + File.separator + fileName);
		file.delete();
		return "success";
	}

	/* 身份证验证 start */
	// 身份证号正则表达式
	private static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])\\d{3}[0-9Xx]$";

	// 加权因子
	private static final int[] WEIGHTS = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

	// 校验码
	private static final char[] CHECK_CODE = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

	public static boolean validateIDCard(String idCard) {
		if (idCard == null || !Pattern.matches(ID_CARD_REGEX, idCard)) {
			return false;
		}

		// 校验日期部分
		String birthDateStr = idCard.substring(6, 14);
		if (!validateBirthDate(birthDateStr)) {
			return false;
		}

		// 计算校验码
		return calculateCheckCode(idCard);
	}

	private static boolean validateBirthDate(String birthDateStr) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
			return !birthDate.isAfter(LocalDate.now());
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	private static boolean calculateCheckCode(String idCard) {
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum += Character.getNumericValue(idCard.charAt(i)) * WEIGHTS[i];
		}
		int mod = sum % 11;
		char expectedCheckCode = CHECK_CODE[mod];
		char actualCheckCode = Character.toUpperCase(idCard.charAt(17));
		return expectedCheckCode == actualCheckCode;
	}

	/* 身份证验证 end */
}
