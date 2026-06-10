package com.xinchi.common.office;

import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class SimpletinyExcel {
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getCellValueByCell(Cell cell) {

		// 判断是否为null或空串
		if (cell == null || cell.toString().trim().equals("")) {
			return "";
		}
		String cellValue = "";
		String cellType = cell.getCellType().name();

		switch (cellType) {
		case "NUMERIC":
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = df.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
			} else {
				cellValue = String.valueOf(cell.getNumericCellValue());
			}

			break;
		case "STRING":
			cellValue = cell.getStringCellValue();
			break;
		default:
			cellValue = "";
			break;
		}

		return cellValue.trim();
	}
}
