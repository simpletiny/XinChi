package com.xinchi.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class SimpletinyDataUtil {

	public static List<String> getAccounts() throws Exception {
		String basePath = new Object() {
			public String getPath() {
				return this.getClass().getClassLoader().getResource("").getPath();
			}
		}.getPath();

		InputStreamReader config = new InputStreamReader(
				new FileInputStream(basePath + File.separator + "hot" + File.separator + "accountSumConfig.txt"),
				"UTF-8");
		BufferedReader br = new BufferedReader(config);
		String line;
		String r = "";
		while ((line = br.readLine()) != null) {
			r += line;
		}
		br.close();

		List<String> accounts = Arrays.asList(r.split(","));
		return accounts;
	}
}
