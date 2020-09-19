package com.xinchi.common.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import com.xinchi.common.DBCommonUtil;

public class SimpletinyWord {

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
