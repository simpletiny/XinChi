package apptest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import generator.util.JdbcUtils;

public class SomeTest {

	public static List<Map<String, String>> execSql(String sql) {

		List<Map<String, String>> result = new ArrayList<>();

		Connection conn = null;
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnect();
			state = conn.prepareStatement(sql);
			rs = state.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map<String, String> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					Object columnValue = rs.getObject(i);
					row.put(columnName, columnValue.toString());
				}
				result.add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (state != null)
					state.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		int a = Integer.MAX_VALUE;
		System.out.println(a * 33 + a * 33);

	}

	public static Detail random() {
		Detail detail = new Detail();
		int a = (int) (Math.random() * 100000);
		detail.setFirst(a);
		int b = 0;
		int i = 0;
		while (a != b) {
			b = (int) (Math.random() * 100000);
			i++;
		}
		detail.setLast(b);
		detail.setDays(i);
		return detail;
	}

	public void readAndRight(String[] args) {
		CSVReader c = new CSVReader("D://out.csv");

		List<Detail> d = c.getDetails();

		Connection conn = null;
		PreparedStatement state = null;

		conn = JdbcUtils.getConnect();
		List<Detail> nos = new ArrayList<>();

		try {
			int progress = 0;
			for (Detail detail : d) {
				int count = 0;
				progress++;
				String sql = "select * from payment_detail where account = ? and type=? and time like '"
						+ detail.getTime().substring(0, 15) + "%' and money=" + detail.getMoney();
				state = conn.prepareStatement(sql);
				state.setString(1, "机票专用2022");
				state.setString(2, "支出");
				ResultSet rs = state.executeQuery();
				while (rs.next()) {
					count++;
				}
				rs.close();
				if (count != 1) {
					nos.add(detail);
					System.out.println(detail.getTime() + "," + detail.getMoney());
				}

				System.out.println(progress);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (state != null)
					state.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String filename = "D://output.txt"; // 文件名

		try {
			// 创建 FileWriter 对象，并指定要写入的文件名
			FileWriter fileWriter = new FileWriter(filename);

			// 创建 BufferedWriter 对象，用于写入文本内容
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (Detail detail : nos) {
				String date = detail.getTime().substring(0, 10).replaceAll("-", "");
				String time = detail.getTime().substring(11);
				bufferedWriter.write(date + time + "," + detail.getMoney());
				bufferedWriter.newLine();
			}

			// 关闭资源
			bufferedWriter.close();
			fileWriter.close();

			System.out.println("文件写入成功。");
		} catch (IOException e) {
			System.out.println("写入文件时发生错误：" + e.getMessage());
		}
	}
}
