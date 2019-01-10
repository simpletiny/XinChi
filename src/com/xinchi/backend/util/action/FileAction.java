package com.xinchi.backend.util.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.FileFolder;
import static com.xinchi.common.SimpletinyString.isEmpty;
import com.xinchi.common.Utils;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class FileAction extends BaseAction {

	private static final long serialVersionUID = -4686523277751323355L;
	private InputStream fips;
	private String subFolder;
	private String fileName;
	private String fileFileName;
	// 下载的文件类型，通过类型查找文件夹地址
	private String fileType;

	public String getFileStream() throws IOException {
		String baseFolder = PropertiesUtil.getProperty(FileFolder.valueOf(fileType).value());
		if (!isEmpty(subFolder)) {
			baseFolder = baseFolder + File.separator + subFolder;
		}
		File file = new File(baseFolder + File.separator + fileFileName);
		fileName = file.getName();
		fips = new FileInputStream(file);
		// fips.close();
		return SUCCESS;
	}

	private File file;

	public String fileUpload() throws IOException {
		String ext = Utils.getFileExt(fileFileName);
		String fileFolder = PropertiesUtil.getProperty("tempUploadFolder");
		File destfile = new File(fileFolder + File.separator + DBCommonUtil.genPk() + "." + ext);
		FileUtils.copyFile(file, destfile);
		fileName = destfile.getName();
		fips = new FileInputStream(destfile);
		// fips.close();
		return SUCCESS;
	}
	public String blobUpload() throws IOException {
		InputStream in = new FileInputStream(file);
		
		String ext = Utils.getFileExt(fileFileName);
		String fileFolder = PropertiesUtil.getProperty("tempUploadFolder");
		File destfile = new File(fileFolder + File.separator + DBCommonUtil.genPk() + "." + ext);
		FileUtils.copyFile(file, destfile);
		fileName = destfile.getName();
		fips = new FileInputStream(destfile);
		// fips.close();
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getSubFolder() {
		return subFolder;
	}

	public void setSubFolder(String subFolder) {
		this.subFolder = subFolder;
	}
}
