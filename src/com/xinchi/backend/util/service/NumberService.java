package com.xinchi.backend.util.service;

import com.xinchi.common.BaseService;

public interface NumberService extends BaseService {
	public static String source_team_number = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";
	public static String first_team_number = "GT9R";

	public static String source_ticket_order_number = "NBEAO10DCMQ36HJWY7SV48KIULF95ZXRGP2T";
	public static String first_ticket_order_number = "NBEA";

	public static String source_product_order_number = "YB0ZHXCWAMJIGSQOURLTDN7KP231F69485VE";
	public static String first_product_order_number = "YB0Z";

	public String generateTeamNumber();

	public String generatePayOrderNumber(String type, String orderType, String date);

	// 生成产品编号，唯一
	public String generateProductNumber();

	public String generateTicketOrderNumber();

	public String generateProductOrderNumber();
}
