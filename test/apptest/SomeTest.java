package apptest;

import java.io.IOException;

import com.xinchi.common.DateUtil;

public class SomeTest {

	public static void main(String[] args) throws IOException {
		String firstWeekDay = DateUtil.addDate(DateUtil.today(), -7);
		String lastWeekDay = DateUtil.addDate(DateUtil.today(), -1);
		String sql17 = "update client_employee set market_level='核心' where pk in(SELECT client_employee_pk FROM view_connect_info where connect_date>='"
				+ firstWeekDay + "' and connect_date<='" + lastWeekDay
				+ "' group by client_employee_pk having count(client_employee_pk) >=2);";

		System.out.println(sql17);
	}
}
