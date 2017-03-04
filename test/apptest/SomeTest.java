package apptest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.solr.client.solrj.SolrServerException;

import com.xinchi.common.UserSessionBean;

public class SomeTest {
	public static String source = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";
	public static String solr_url = "http://localhost:8983/solr/receivable";

	public static void main(String[] args) throws SolrServerException, IOException {
		// String path =
		// "C:\\Users\\simpletiny\\Desktop\\交易明细_9028_20161201_20161228.xls";
		// InputStream is = new FileInputStream(path);
		// HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		//
		// HSSFRow hssfRow = hssfSheet.getRow(6);
		// System.out.println((new
		// BigDecimal(SomeTest.getValue(hssfRow.getCell(4)))).compareTo(BigDecimal.ONE));
		// hssfWorkbook.close();

		Vector<UserSessionBean> v = new Vector<UserSessionBean>();
		UserSessionBean user = new UserSessionBean();
		user.setPk("s");
		user.setCellphone("12312");
		user.setNick_name("xxx");

		v.addElement(user);

		System.out.println(v.size());

		UserSessionBean user1 = new UserSessionBean();
		user1.setPk("s");
		user1.setCellphone("12312");
		user1.setNick_name("xxx");

//		v.removeElement(user1);
		System.out.println(v.indexOf(user1));
	}

	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

}
