package apptest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;

public class CSVReader {
	public static void main(String[] args) {
		String csvFile = "D://list.csv"; // CSV文件路径
		String line = ""; // 每行数据
		String csvSplitBy = ","; // CSV分隔符

		String cls = "";
		String father_pk = "";
		int index = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			int i = 1;
			int j = 1;
			while ((line = br.readLine()) != null) { // 逐行读取CSV文件
				String[] data = line.split(csvSplitBy); // 按分隔符分割每行数据
				if (data.length != 4)
					continue;
				String result = "";
				String pk = DBCommonUtil.genPk();

				index = j;
				if (!data[0].equals("")) {
					cls = data[0];
					father_pk = pk;
					index = i;
					i++;
					j = 1;
				} else {
					j++;
				}

				result += data[1] + "," + data[3] + "," + father_pk + "," + index + "," + data[2] + "," + cls + "," + pk
						+ "," + "N00000" + "," + DateUtil.getTimeMillis() + ",,";

				System.out.println(result);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
