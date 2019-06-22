package com.xinchi.common.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import com.xinchi.common.DBCommonUtil;

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
		String[] sp = inFile.split("\\.");
		if (sp.length > 0) {
			// docx
			if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
				XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inFile));
				// 替换段落中的指定文字
				Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
				while (itPara.hasNext()) {
					XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
					List<XWPFRun> runs = paragraph.getRuns();

					for (int i = 0; i < runs.size(); i++) {
						XWPFRun run = runs.get(i);
						String oneparaString = run.getText(run.getTextPosition());
						if (null == oneparaString)
							continue;
						System.out.println(oneparaString);
						for (Map.Entry<String, String> entry : data.entrySet()) {
							oneparaString = oneparaString.replace("${" + entry.getKey() + "}", entry.getValue());
						}
						run.setText(oneparaString, 0);
					}
				}

				// 替换表格中的指定文字
				Iterator<XWPFTable> itTable = document.getTablesIterator();
				while (itTable.hasNext()) {
					XWPFTable table = (XWPFTable) itTable.next();
					int rcount = table.getNumberOfRows();
					for (int i = 0; i < rcount; i++) {
						XWPFTableRow row = table.getRow(i);
						List<XWPFTableCell> cells = row.getTableCells();
						for (XWPFTableCell cell : cells) {
							String cellTextString = cell.getText();
							for (Entry<String, String> e : data.entrySet()) {
								if (cellTextString.contains(e.getKey()))
									cellTextString = cellTextString.replace("${" + e.getKey() + "}", e.getValue());
							}
							cell.removeParagraph(0);
							cell.setText(cellTextString);
						}
					}
				}
				document.write(os);

			} else if (sp[sp.length - 1].equalsIgnoreCase("doc")) {
				HWPFDocument document = new HWPFDocument(new FileInputStream(inFile));
				Range range = document.getRange();
				for (Map.Entry<String, String> entry : data.entrySet()) {
					range.replaceText("${" + entry.getKey() + "}", entry.getValue());
				}
				document.write(os);
				document.close();
			} else {
				// donoting
			}
			close(os);
		}
	}

	/**
	 * word03转化为html
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static String Word2003ToHtml(String file) throws Exception {
		String tempHtmlPath = ServletActionContext.getServletContext().getRealPath("/");
		final String imagepath = tempHtmlPath + "templates" + File.separator + "temp" + File.separator + "viewword"
				+ File.separator + "image";

		String htmlFileName = DBCommonUtil.genPk() + ".html";
		String htmlFilePath = tempHtmlPath + "templates" + File.separator + "temp" + File.separator + "viewword"
				+ File.separator + htmlFileName;

		InputStream is = new FileInputStream(new File(file));
		HWPFDocument wordDocument = new HWPFDocument(is);
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		// 设置图片存放的位置
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
					float heightInches) {
				File imgPath = new File(imagepath);
				if (!imgPath.exists()) {// 图片目录不存在则创建
					imgPath.mkdirs();
				}
				File file = new File(imagepath + File.separator + suggestedName);
				try {
					OutputStream os = new FileOutputStream(file);
					os.write(content);
					os.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "image" + File.separator + suggestedName;
			}
		});

		// 解析word文档
		wordToHtmlConverter.processDocument(wordDocument);
		Document htmlDocument = wordToHtmlConverter.getDocument();

		File htmlFile = new File(htmlFilePath);
		OutputStream os = new FileOutputStream(htmlFile);

		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(os);

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer serializer = factory.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");

		serializer.transform(domSource, streamResult);
		close(os);
		close(is);

		return htmlFileName;
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
