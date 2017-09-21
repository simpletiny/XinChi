package com.xinchi.common.office;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

public class SimpletinyWord {
	/**
	 * word操作替换占位符（限doc类型）
	 * 
	 * @param inFile
	 * @param data
	 * @param os
	 * @throws Exception
	 */
	public static void writeWord(String inFile, Map<String, String> data, OutputStream os) throws Exception {
		InputStream is = new FileInputStream(inFile);
		HWPFDocument document = new HWPFDocument(is);

		replaceByHolder(document, data);
		document.write(os);
		close(os);
		close(is);
	}

	private static void replaceByHolder(HWPFDocument doc, Map<String, String> params) {
		Range range = doc.getRange();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			range.replaceText("${" + entry.getKey() + "}", entry.getValue());
		}

	}

	private static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
