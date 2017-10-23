package com.xinchi.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinchi.bean.TaskBean;

public class ResourcesConstants {

	// 用户注册状态
	// 申请中
	public static String USER_STATUS_APPLY = "A";
	// 已通过正常使用
	public static String USER_STATUS_NORMAL = "N";
	// 拒绝
	public static String USER_STATUS_REJECT = "R";

	public static String LOGIN_SESSION_KEY = "user";

	// 定时任务列表
	public static List<TaskBean> ARRAY_TASK = new ArrayList<TaskBean>();

	// 用户编号和用户姓名map
	public static Map<Object, Object> MAP_USER_NO = new HashMap<Object, Object>();
	public static Map<Object, Object> MAP_USER_NAME = new HashMap<Object, Object>();

	// 执行一次
	public static String TASK_ONETIME = "1";
	// 每天执行
	public static String TASK_EVERYDAY = "2";
	// 每个星期执行
	public static String TASK_EVERYWEEK = "3";
	// 每个月执行
	public static String TASK_EVERYMONTH = "4";
	// 每年执行
	public static String TASK_EVERYYEAR = "5";
	// never used每个世纪
	public static String TASK_EVERYCENTURY = "6";

	/* 用户角色 */
	public static String USER_ROLE_ADMIN = "ADMIN";
	public static String USER_ROLE_MANAGER = "MANAGER";
	public static String USER_ROLE_SALES = "SALES";
	public static String USER_ROLE_PRODUCT = "PRODUCT";
	public static String USER_ROLE_FINANCE = "FINANCE";
	public static String USER_ROLE_TICKET = "TICKET";

	// 团队状态
	public static String TEAM_STATUS_BEFORE = "未出团";
	public static String TEAM_STATUS_AFTER = "已出团";
	public static String TEAM_STATUS_RETURN = "已回团";
	public static String TEAM_STATUS_NOT_RETURN = "未回团";

	// ************************* 收入种类 ********************************
	// 抹零
	public static String RECEIVED_TYPE_TAIL = "TAIL";
	// 代收
	public static String RECEIVED_TYPE_COLLECT = "COLLECT";
	// 合账
	public static String RECEIVED_TYPE_SUM = "SUM";
	// 冲账出
	public static String RECEIVED_TYPE_STRIKE_OUT = "STRIKEOUT";
	// 冲账入
	public static String RECEIVED_TYPE_STRIKE_IN = "STRIKEIN";
	// 收入
	public static String RECEIVED_TYPE_RECEIVED = "RECEIVED";
	// 多付返款支出
	public static String RECEIVED_TYPE_PAY = "PAY";

	// ************************* 供应商往来账种类 *************************
	// 返款收入
	public static String PAID_TYPE_BACK = "BACK";
	// 支付
	public static String PAID_TYPE_PAID = "PAID";
	// 冲账
	public static String PAID_TYPE_STRIKE = "STRIKE";

	// 冲账出
	public static String PAID_TYPE_STRIKE_OUT = "STRIKEOUT";
	// 冲账入
	public static String PAID_TYPE_STRIKE_IN = "STRIKEIN";

	// 扣款
	public static String PAID_TYPE_DEDUCT = "DEDUCT";

	// ************************* 支出状态 *********************************
	// 待确认
	public static String PAID_STATUS_ING = "I";
	// 被驳回
	public static String PAID_STATUS_NO = "N";
	// 已同意
	public static String PAID_STATUS_YES = "Y";
	// 已支付
	public static String PAID_STATUS_PAID = "P";

	// 收入状态
	// 待确认
	public static String RECEIVED_STATUS_ING = "I";
	// 被驳回
	public static String RECEIVED_STATUS_NO = "N";
	//
	public static String RECEIVED_STATUS_YES = "Y";
	// 已入账 enter
	public static String RECEIVED_STATUS_ENTER = "E";
	/**
	 * 供应商文件类型
	 */
	// 营业执照
	public static String SUPPLIER_FILE_TYPE_LICENCE = "LICENCE";
	// 许可证
	public static String SUPPLIER_FILE_TYPE_PERMIT = "PERMIT";
	// 责任险
	public static String SUPPLIER_FILE_TYPE_INSURANCE = "INSURANCE";
	// 法人身份证
	public static String SUPPLIER_FILE_TYPE_CORPORATE = "CORPORATE";
	// 负责人身份证
	public static String SUPPLIER_FILE_TYPE_CHIEF = "CHIEF";
	// 其他文件
	public static String SUPPLIER_FILE_TYPE_OTHER = "OTHER";

	/**
	 * 旅行社文件类型
	 */
	// 营业执照
	public static String AGENCY_FILE_TYPE_LICENCE = "LICENCE";
	// 许可证
	public static String AGENCY_FILE_TYPE_PERMIT = "PERMIT";
	// 责任险
	public static String AGENCY_FILE_TYPE_INSURANCE = "INSURANCE";
	// 法人身份证
	public static String AGENCY_FILE_TYPE_CORPORATE = "CORPORATE";
	// 负责人身份证
	public static String AGENCY_FILE_TYPE_CHIEF = "CHIEF";
	// 其他文件
	public static String AGENCY_FILE_TYPE_OTHER = "OTHER";

	/* 银行简称 */
	// 建设银行
	public static String BANK_CCB = "CCB";
	// 农业银行
	public static String BANK_ABC = "ABC";
	// 工商银行
	public static String BANK_ICBC = "ICBC";
	// 中国银行
	public static String BANK_BOC = "BOC";
	// 招商银行
	public static String BANK_CMB = "CMB";
	// 交通银行
	public static String BANK_BCM = "BCM";
	// 邮储银行
	public static String BANK_PSBC = "PSBC";
	// 哈尔滨银行
	public static String BANK_HRB = "HRB";
	// 网商银行
	public static String BANK_MY = "MY";

	// 客户.供应商等停用状态
	public static String STOP_STATUS_NORMAL = "正常";
	public static String STOP_STATUS_STOP = "已停用";

	// 财务主体关联旅游公司状态
	public static String RELATE_STATUS_YES = "已关联";
	public static String RELATE_STATUS_NO = "未关联";

	/**
	 * 计数种类
	 */
	// 支付单号计数
	public static String COUNT_TYPE_PAY_ORDER = "PAY_ORDER_NUMBER";
	public static String COUNT_TYPE_PRODUCT_ORDER = "PRODUCT_NUMBER";

	// ************************* 支付种类 *************************
	// 地接款
	public static String PAY_TYPE_DIJIE = "D";
	// 销售费用
	public static String PAY_TYPE_XIAOSHOU = "X";
	// 办公费用
	public static String PAY_TYPE_BANGONG = "B";
	// 产品费用
	public static String PAY_TYPE_CHANPIN = "C";
	// 票务费用
	public static String PAY_TYPE_PIAOWU = "P";
	// 交通费用
	public static String PAY_TYPE_JIAOTONG = "J";
	// 工资费用
	public static String PAY_TYPE_GONGZI = "G";
	// 其他费用
	public static String PAY_TYPE_QITA = "Q";
	// 多付退还
	public static String PAY_TYPE_MORE_BACK = "M";

	public static String PAY_STATUS_ING = "I";
	public static String PAY_STATUS_YES = "Y";
	public static String PAY_STATUS_NO = "N";

	// 产品机票包票人
	public static String PRODUCT_AIR_TICKET_CHARGE_PRODUCT = "PRODUCT";
	public static String PRODUCT_AIR_TICKET_CHARGE_SALE = "SALE";
	public static String PRODUCT_AIR_TICKET_CHARGE_NONE = "NONE";

	// 供应商种类
	// 地接
	public static String SUPPLIER_TYPE_DIJIE = "D";
	// 机票供应商
	public static String SUPPLIER_TYPE_AIR_TICKET = "A";

	// 客户财务主体更换销售日志记录种类
	// 合并
	public static String CLIENT_CHANGE_SALE_TYPE_COMBINE = "COMBINE";
	// 转移
	public static String CLIENT_CHANGE_SALE_TYPE_TRANSFER = "TRANSFER";
}
