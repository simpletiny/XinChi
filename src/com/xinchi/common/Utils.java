package com.xinchi.common;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.File;

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
}
