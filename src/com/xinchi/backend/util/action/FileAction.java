package com.xinchi.backend.util.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.common.BaseAction;
import com.xinchi.common.FileFolder;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class FileAction extends BaseAction {

	private static final long serialVersionUID = -4686523277751323355L;
	private InputStream fips;

	private String fileName;
	private String fileFileName;
	// 下载的文件类型，通过类型查找文件夹地址
	private String fileType;

	public String getFileStream() throws IOException {
		String baseFolder = PropertiesUtil.getProperty(FileFolder.valueOf(fileType).value());
		File file = new File(baseFolder + File.separator + fileFileName);
		fileName = file.getName();
		fips = new FileInputStream(file);

		return SUCCESS;
	}

	public InputStream getFips() {
		return fips;
	}

	public void setFips(InputStream fips) {
		this.fips = fips;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
}
