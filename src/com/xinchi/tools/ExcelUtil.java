package com.xinchi.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * excel操作公用类
 * 
 * @author niushixing 2015-1-15 14:41:09
 *
 */
public class ExcelUtil {
	/**
	 * 通过模板中的占位符写数据
	 * 
	 * @param data
	 * @param templet
	 * @param os
	 * @throws Exception
	 */
	public static void writeDataByHolder(Map<String, Object> data, String templet, OutputStream os) throws Exception {
		Workbook book = Workbook.getWorkbook(new File(templet));
		WritableWorkbook result = Workbook.createWorkbook(os, book);
		WritableSheet sheet = result.getSheet(0);
		if (null != data) {
			Iterator<String> iter = data.keySet().iterator();
			while (iter.hasNext()) {
				String holder = iter.next();
				Cell cell = sheet.findCell(holder);
				Label label = new Label(cell.getColumn(), cell.getRow(), data.get(holder).toString());
				sheet.addCell(label);
			}
		}
		result.write();
		result.close();
		book.close();

	}

	/**
	 * 查找占位符位置
	 * 
	 * @return
	 */
	private Map<String, Integer[]> findLocation() {
		Map<String, Integer[]> mapLocation = new HashMap<String, Integer[]>();

		return mapLocation;
	}

	/**
	 * 没有模板的情况下按行写数据
	 * 
	 * @param data
	 * @param os
	 * @throws Exception
	 */
	public static void writeDataRowByRow(Map<Integer, List<Object>> data, OutputStream os) throws Exception {
		WritableWorkbook result = Workbook.createWorkbook(os);
		WritableSheet sheet = result.createSheet("sheet1", 0);
		if (data != null) {
			Iterator<Integer> iter = data.keySet().iterator();
			while (iter.hasNext()) {
				int row = iter.next();
				List<Object> rowList = data.get(row);
				for (int i = 0; i < rowList.size(); i++) {
					String content = rowList.get(i).toString();
					Label label = new Label(i, row, content);
					sheet.addCell(label);
				}
			}
		}
		result.write();
		result.close();
	}

	/**
	 * 通过模板按行写数据
	 * 
	 * @param data
	 *            map<行号，数据List>
	 * @param templet
	 *            模板路径
	 * @param dest
	 * @throws Exception
	 */
	public static void writeDataRowByRow(Map<Integer, List<Object>> data, String templet, OutputStream os)
			throws Exception {
		Workbook book = Workbook.getWorkbook(new File(templet));
		WritableWorkbook result = Workbook.createWorkbook(os, book);
		WritableSheet sheet = result.getSheet(0);
		if (data != null) {
			Iterator<Integer> iter = data.keySet().iterator();
			while (iter.hasNext()) {
				int row = iter.next();
				List<Object> rowList = data.get(row);
				for (int i = 0; i < rowList.size(); i++) {
					String content = rowList.get(i).toString();
					Label label = new Label(i, row, content);
					sheet.addCell(label);
				}
			}
		}
		result.write();
		book.close();
		result.close();
	}

	public static void main(String[] arg) throws Exception {
		// Map<Integer, List<Object>> data = new HashMap<Integer,
		// List<Object>>();
		// for (int i = 0; i < 10; i++) {
		// List<Object> rowList = new ArrayList<Object>();
		// for (int j = 0; j < 10; j++) {
		// rowList.add(i * j + "");
		// }
		// data.put(i + 1, rowList);
		// }
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("${test}", "你好");
		ExcelUtil.writeDataByHolder(map, "D://test//test.xls", new FileOutputStream(new File("D://test//niu.xls")));
	}
}
