package com.xinchi.backend.util.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.ticket.service.AirTicketNameListService;
import com.xinchi.backend.ticket.service.TicketNameTempletService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.TicketNameTempletBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.Utils;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class TicketFileAction extends BaseAction {

	private static final long serialVersionUID = -4686523277751323355L;
	private InputStream fips;
	private String subFolder;
	private String fileName;
	private String fileFileName;
	// A 出团通知
	private String fileType;

	private String templet_pk;
	private String[] name_pks;

	@Autowired
	private TicketNameTempletService ticketNameTempletService;

	@Autowired
	private AirTicketNameListService airTicketNameListService;

	public String downloadTicketFile() throws Exception {

		Map<String, List<String>> data = new HashMap<String, List<String>>();
		String _fileName = "";

		String tempDownloadFolder = PropertiesUtil.getProperty("tempDownloadFolder");
		// 复制模板的地址
		String dest_file_path = tempDownloadFolder + File.separator + DBCommonUtil.genPk();

		// 模板文件夹
		String templetFolder = "";
		// 模板文件
		String templet_file_path = "";
		if (fileType.equals(ResourcesConstants.FILE_TICKET_NAME)) {
			// 模板文件夹
			templetFolder = PropertiesUtil.getProperty(FileFolder.TICKET_NAME_TEMPLET.value());
			TicketNameTempletBean templet = ticketNameTempletService.selectByPrimaryKey(templet_pk);
			if (null == templet)
				return INPUT;
			templet_file_path = templetFolder + File.separator + templet.getFile_name();
			List<AirTicketNameListBean> names = airTicketNameListService.selectByPks(name_pks);
			data = getNameData(names);
			_fileName = names.get(0).getName() + "等" + names.size() + "人名单";

			templet.setUsed_count(templet.getUsed_count() + 1);
			templet.setLast_used_time(DateUtil.getTimeStr());

			ticketNameTempletService.update(templet);
		}

		_fileName += "." + Utils.getFileExt(templet_file_path);
		dest_file_path += "." + Utils.getFileExt(templet_file_path);

		Files.copy(Paths.get(templet_file_path), Paths.get(dest_file_path), StandardCopyOption.REPLACE_EXISTING);

		replacePlaceholdersWithContent(dest_file_path, data);

		fileName = new String(_fileName.getBytes(), "ISO8859-1");
		fips = new FileInputStream(dest_file_path);
		return SUCCESS;
	}

	private Map<String, List<String>> getNameData(List<AirTicketNameListBean> names) {
		Map<String, List<String>> data = new HashMap<>();
		List<String> name = new ArrayList<>();
		List<String> id = new ArrayList<>();
		List<String> gender = new ArrayList<>();
		List<String> birthdate = new ArrayList<>();
		List<String> idtype = new ArrayList<>();
		List<String> passengertype = new ArrayList<>();
		List<String> age = new ArrayList<>();
		List<String> cellphone = new ArrayList<>();
		for (AirTicketNameListBean n : names) {
			name.add(n.getName());
			id.add(n.getId());
			cellphone.add(n.getCellphone_A());
			Map<String, String> id_result = SimpletinyString.analysisId(n.getId());
			idtype.add(id_result.get("idtype"));
			gender.add(id_result.get("gender"));
			age.add(id_result.get("age"));
			birthdate.add(id_result.get("birthdate"));
			passengertype.add(id_result.get("passengertype"));
		}

		data.put("${name}", name);
		data.put("${id}", id);
		data.put("${gender}", gender);
		data.put("${birthdate}", birthdate);
		data.put("${cellphone}", cellphone);
		data.put("${idtype}", idtype);
		data.put("${passengertype}", passengertype);
		return data;
	}

	private void replacePlaceholdersWithContent(String filePath, Map<String, List<String>> data) throws Exception {
		FileInputStream fis = new FileInputStream(new File(filePath));
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0); // 假设占位符在第一个工作表

		// 遍历每行每列寻找占位符
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.STRING) {
					String cellValue = cell.getStringCellValue();
					// 检查单元格内容是否为我们的占位符之一
					for (String placeholder : data.keySet()) {
						if (cellValue.contains(placeholder)) {
							// 找到占位符，获取对应的内容列表
							List<String> contents = data.get(placeholder);
							int rowIndex = row.getRowNum();
							// 填充内容
							for (String content : contents) {
								Row contentRow = sheet.getRow(rowIndex) == null ? sheet.createRow(rowIndex)
										: sheet.getRow(rowIndex);
								Cell contentCell = contentRow.createCell(cell.getColumnIndex());
								contentCell.setCellValue(content);
								rowIndex++;
							}
							break;
						}
					}
				}
			}
		}
		// 保存更改
		fis.close();
		FileOutputStream fos = new FileOutputStream(new File(filePath));
		workbook.write(fos);
		fos.close();
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

	public String getSubFolder() {
		return subFolder;
	}

	public void setSubFolder(String subFolder) {
		this.subFolder = subFolder;
	}

	public String getTemplet_pk() {
		return templet_pk;
	}

	public void setTemplet_pk(String templet_pk) {
		this.templet_pk = templet_pk;
	}

	public String[] getName_pks() {
		return name_pks;
	}

	public void setName_pks(String[] name_pks) {
		this.name_pks = name_pks;
	}

}
