package com.xinchi.backend.util.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.Utils;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class KindEditorAction extends BaseAction {
	private static final long serialVersionUID = -3838216231649467600L;

	private InputStream fips;

	private int error;
	private String url;
	private String message;

	public String fileUpload() throws IOException, FileUploadException {
		HttpServletRequest request = ServletActionContext.getRequest();
		// 文件保存目录路径
		String savePath = PropertiesUtil.getProperty(FileFolder.valueOf("SYSTEM_GUIDE_FILE").value());
		String saveUrl = request.getContextPath() + "/";
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		// 最大文件大小
		long maxSize = 4194304;

		if (!ServletFileUpload.isMultipartContent(request)) {
			error = 1;
			message = "请选择文件。";
			return SUCCESS;
		}
		// 检查目录
		File uploadDir = new File(savePath);
		if (!uploadDir.isDirectory()) {
			error = 1;
			message = "上传目录不存在。";
			return SUCCESS;
		}
		// 检查目录写权限
		if (!uploadDir.canWrite()) {
			error = 1;
			message = "上传目录没有写权限。";
			return SUCCESS;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			error = 1;
			message = "目录名不正确。";
			return SUCCESS;
		}
		// 创建文件夹
		savePath += File.separator + dirName + File.separator;
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}

		request.getInputStream();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List<?> items = upload.parseRequest(request);
		Iterator<?> itr = items.iterator();

		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			long fileSize = item.getSize();
			if (!item.isFormField()) {
				// 检查文件大小
				if (fileSize > maxSize) {
					error = 1;
					message = "上传文件大小超过限制。";
					return SUCCESS;
				}

				// 检查扩展名
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {
					error = 1;
					message = "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。";
					return SUCCESS;
				}

				String newFileName = DBCommonUtil.genPk() + "." + fileExt;
				try {
					File uploadedFile = new File(savePath, newFileName);
					item.write(uploadedFile);
				} catch (Exception e) {
					error = 1;
					message = "上传文件失败。";
					return SUCCESS;
				}

				error = 0;
				url = saveUrl + "file/getFileStream?fileType=SYSTEM_GUIDE_FILE&subFolder=" + dirName + "&fileFileName=" + newFileName;
				message = "success";
			}
		}
		return SUCCESS;
	}

	private String fileType;
	private String subFolder;
	private String fileFileName;

	// 删除文件
	public String keFileDelete() {
		resultStr = Utils.deleteFileFromDisk(fileType, subFolder, fileFileName);
		return SUCCESS;
	}

	public InputStream getFips() {
		return fips;
	}

	public void setFips(InputStream fips) {
		this.fips = fips;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSubFolder() {
		return subFolder;
	}

	public void setSubFolder(String subFolder) {
		this.subFolder = subFolder;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

}
