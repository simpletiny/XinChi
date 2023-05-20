package com.xinchi.backend.util.service;

import com.xinchi.common.BaseService;

public interface NumberService extends BaseService {
	public final static String SOURCE_TEAM_NUMBER = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";
	public final static String SOURCE_TICKET_ORDER_NUMBER = "NBEAO10DCMQ36HJWY7SV48KIULF95ZXRGP2T";
	public final static String SOURCE_PRODUCT_ORDER_NUMBER = "YB0ZHXCWAMJIGSQOURLTDN7KP231F69485VE";
	public final static String SOURCE_DEPOSIT_NUMBER = "VWEJL7Q9DNIYHC03R5XAMK1SOF8U6Z2B4PGT";

	public final static String NUMBER_TYPE_TEAM = "N";
	public final static String NUMBER_TYPE_PRODUCT_ORDER = "P";
	public final static String NUMBER_TYPE_TEAM_AIR_TICKET_ORDER = "A";
	public final static String NUMBER_TYPE_TEAM_DEPOSIT = "D";

	public String generateTeamNumber();

	public String generatePayOrderNumber(String type, String orderType, String date);

	// 生成产品编号，唯一
	public String generateProductNumber();

	public String generateTicketOrderNumber();

	public String generateProductOrderNumber();

	// 生成押金代码
	public String generateDepositNumber();
}
