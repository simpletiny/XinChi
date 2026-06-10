package apptest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	private String file_path;

	private String csvSplitBy;

	public CSVReader(String file_path) {
		this.file_path = file_path;
		this.csvSplitBy = ",";
	}

	public List<Detail> getDetails() {
		String line = ""; // 每行数据
		List<Detail> result = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
			while ((line = br.readLine()) != null) { // 逐行读取CSV文件
				String[] data = line.split(csvSplitBy); // 按分隔符分割每行数据
				Detail detail = new Detail();
				detail.setMoney(new BigDecimal(data[3].replaceAll(" ", "")));

				String time = data[1].substring(0, 4) + "-" + data[1].substring(4, 6) + "-" + data[1].substring(6) + " "
						+ data[2];

				detail.setTime(time);

				result.add(detail);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getCsvSplitBy() {
		return csvSplitBy;
	}

	public void setCsvSplitBy(String csvSplitBy) {
		this.csvSplitBy = csvSplitBy;
	}

}
