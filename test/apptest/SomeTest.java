package apptest;

import net.sf.json.JSONArray;

public class SomeTest {

	public static void main(String[] args) throws Exception {
		String a = "[{\"account\":\"新非公票务\",\"comment\":\"成交编号：G1060760273;航段：卢沟湖;航班日期：2021-10-01;杜招商\",\"money\":\"16620\",\"return_date\":\"2021-10-11\",\"supplier_name\":\"祥鹏航空公司\",\"time\":\"2021-06-08 21\\\\:25\\\\:34\",\"isConfirmed\":\"1\"}]";

		JSONArray array = JSONArray.fromObject(a);

	}

}
