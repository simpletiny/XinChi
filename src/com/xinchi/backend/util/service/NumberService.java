package com.xinchi.backend.util.service;

public interface NumberService {
	public static String source = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";
	public static String first = "GT9R";

	public String generateTeamNumber();

	public String generatePayOrderNumber(String type, String orderType, String date);

	// 生成产品编号，唯一
	public String generateProductNumber();
}
