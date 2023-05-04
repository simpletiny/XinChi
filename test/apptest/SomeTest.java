package apptest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SomeTest {

	public static void main(String[] args) {
		Map<String, BigDecimal> map = new HashMap<>();
		map.put("one", BigDecimal.ZERO);

		map.put("one", (map.get("one") == null ? BigDecimal.ZERO : map.get("one")).add(new BigDecimal("100")));
		map.put("one", (map.get("one") == null ? BigDecimal.ZERO : map.get("one")).add(new BigDecimal("100")));

		System.out.println(map.get("one"));

	}

	public static Map<String, List<String[]>> groupDataByFirstField(List<String[]> data) {
		Map<String, List<String[]>> groupedData = new HashMap<>();

		for (String[] record : data) {
			String key = record[0];

			if (groupedData.containsKey(key)) {
				groupedData.get(key).add(record);
			} else {
				List<String[]> group = new ArrayList<>();
				group.add(record);
				groupedData.put(key, group);
			}
		}

		return groupedData;
	}

}
