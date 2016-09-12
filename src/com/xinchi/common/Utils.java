package com.xinchi.common;

import java.io.File;

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
		ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		return ext;
	}

	public static void main(String[] str) {
//		String xx="D:$workplace$fileUpload$userId";
//		System.out.println( File.separator);
//		String x = File.separator.toString();
//		System.out.println(xx.replaceAll("\\$", new String(x)));
	}
}
