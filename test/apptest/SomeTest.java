package apptest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.xinchi.common.Utils;
import com.xinchi.common.office.SimpletinyWord;

public class SomeTest {

	public static void main(String[] args) throws Exception {

		String a = "D://C.docx";
		String b = "D://C.doc";

		System.out.println(Utils.getFileExt(a));
		System.out.println(Utils.getFileExt(b));

		SimpletinyWord.copy("D://C.docx", "D://test.docx");
		InputStream is = new FileInputStream("D://test.docx");
		XWPFDocument destDoc = new XWPFDocument(is);

		for (XWPFParagraph paragraph : destDoc.getParagraphs()) {
			String text = paragraph.getText();
			if (text.contains("${secellphone}")) {
				for (XWPFRun run : paragraph.getRuns()) {
					String runText = run.getText(0);
					if (runText != null && runText.contains("${secellphone}")) {
						runText = runText.replace("${secellphone}", "1923727183");
						run.setText(runText, 0);
					}
				}
			}
		}

		for (XWPFTable table : destDoc.getTables()) {
			for (XWPFTableRow row : table.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph paragraph : cell.getParagraphs()) {
						String text = paragraph.getText();
						if (text.contains("${secellphone}")) {
							for (XWPFRun run : paragraph.getRuns()) {
								String runText = run.getText(0);
								if (runText != null && runText.contains("${secellphone}")) {
									runText = runText.replace("${secellphone}", "1923727183");
									run.setText(runText, 0);
								}
							}
						}
					}
				}
			}
		}

		is.close();
		OutputStream os = new FileOutputStream("D://test.docx");
		destDoc.write(os);
		os.close();
		destDoc.close();
	}

}
