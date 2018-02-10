package com.xinchi.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.xinchi.tools.PropertiesUtil;

public class FileUtil {

	public static void saveFile(String fileName, String destFolderStr, String subFolder) {

		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty(destFolderStr);
		File sourceFile = new File(tempFolder + File.separator + fileName);
		File destfile = new File(fileFolder + File.separator + subFolder + File.separator + fileName);
		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sourceFile.delete();
	}
}
