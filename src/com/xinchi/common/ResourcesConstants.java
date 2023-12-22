package com.xinchi.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinchi.bean.TaskBean;

public class ResourcesConstants {
	public static String SIMPLETINY = "SIMPLETINY";

	public static String SIMPLETINY_RECEIVE = "收入";
	public static String SIMPLETINY_PAY = "支出";

	public static String PLACE_HOLDER = "&&^^@@@";

	public static String DELIMITER = "@@";
	// 用户注册状态
	// 申请中
	public static String USER_STATUS_APPLY = "A";
	// 已通过正常使用
	public static String USER_STATUS_NORMAL = "N";
	// 拒绝
	public static String USER_STATUS_REJECT = "R";

	public static String LOGIN_SESSION_KEY = "user";

	public static String REBOOT_TIMER_KEY = "reboot";

	// 定时任务列表
	public static List<TaskBean> ARRAY_TASK = new ArrayList<TaskBean>();

	// 公开用户id
	public static String USER_PUBLIC = "public";

	// default
	public static String DEFAULT = "default";

	// 管理员员工号
	public static String USER_ADMIN_NUMBER = "N00000";

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
	// 每隔30分钟
	public static String TASK_EVERY30MINS = "7";

	/* 用户角色 */
	public static String USER_ROLE_ADMIN = "ADMIN";
	public static String USER_ROLE_MANAGER = "MANAGER";
	public static String USER_ROLE_SALES = "SALES";
	public static String USER_ROLE_PRODUCT = "PRODUCT";
	public static String USER_ROLE_FINANCE = "FINANCE";
	public static String USER_ROLE_ACCOUNTING = "ACCOUNTING";
	public static String USER_ROLE_CASHIER = "CASHIER";
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
	// 98清尾
	public static String RECEIVED_TYPE_TAIL98 = "TAIL98";
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
	// 返佣
	public static String RECEIVED_TYPE_FLY = "FLY";

	// ************************* 供应商往来账种类 *************************
	// 返款收入
	public static String PAID_TYPE_BACK = "BACK";
	// 支付
	public static String PAID_TYPE_PAID = "PAID";
	// 冲账
	public static String PAID_TYPE_STRIKE = "STRIKE";

	// 单纯收入
	public static String PAID_TYPE_RECEIVE = "RECEIVE";

	// 单纯支出
	public static String PAID_TYPE_PAY = "PAY";

	// 冲账出
	public static String PAID_TYPE_STRIKE_OUT = "STRIKEOUT";
	// 冲账入
	public static String PAID_TYPE_STRIKE_IN = "STRIKEIN";

	// 押金冲账
	public static String PAID_TYPE_DEPOSIT_IN = "DSTRIKEIN";

	// 扣款
	public static String PAID_TYPE_DEDUCT = "DEDUCT";

	// ************************* 支出状态 *********************************
	// 待确认
	public static String PAID_STATUS_ING = "I";
	// 被驳回
	public static String PAID_STATUS_NO = "N";
	// 已同意
	public static String PAID_STATUS_YES = "Y";
	// 已入账
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
	public static String COUNT_TYPE_PRODUCT = "PRODUCT_NUMBER";

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
	// FLY
	public static String PAY_TYPE_FLY = "F";
	// 航司押金
	public static String PAY_TYPE_DEPOSIT_AIR = "A";

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

	// ************************* 决算订单状态 *********************************
	// 决算中
	public static String FINAL_ORDER_STATUS_ING = "I";
	// 被驳回
	public static String FINAL_ORDER_STATUS_REJECT = "N";
	// 正常
	public static String FINAL_ORDER_STATUS_NORMAL = "Y";
	// 取消
	public static String FINAL_ORDER_STATUS_CANCEL = "C";
	// 投诉
	public static String FINAL_ORDER_STATUS_COMPLAIN = "O";
	// 变更
	public static String FINAL_ORDER_STATUS_CHANGE = "H";

	public static String CLIENT_RELATION_LEVEL_01 = "新增级";

	// 订单决算类型
	public static String FINAL_TYPE_NO_CHANGE = "1";
	public static String FINAL_TYPE_CHANGE = "2";
	public static String FINAL_TYPE_COMPLAIN = "3";
	public static String FINAL_TYPE_CANCEL = "4";

	// 文件下载类型

	// 出团通知
	public static String FILE_TYPE_OUT_NOTICE = "A";
	// 出团通知
	public static String FILE_TYPE_CLIENT_CONFIRM = "B";
	// 地接确认
	public static String FILE_TYPE_SUPPLIER_CONFIRM = "C";

	public static String FILE_REPLACE_HOLDER_NONE = "无";

	public static String FILE_REPLACE_HOLDER_NO_CLIENT = "未审核客户";

	// ************************* 基础数据 类型*********************************
	// 产品线
	public static String BASE_DATA_TYPE_LINE = "LINE";
	// 呆账罚息设置
	public static String BASE_DATA_TYPE_BAD_CONFIG = "BAD";
	// 单团核算相关
	public static String BASE_DATA_TYPE_TEAM_CONFIG = "TEAM";
	// 销售信用额度启用配置
	public static String BASE_DATA_TYPE_SALE_CREDIT = "SCREDIT";

	/** 基础数据固定pk **/
	// 单团核算pk
	public static String BASE_DATA_PK_TEAM = "pk_team";
	// 呆账清除pk
	public static String BASE_DATA_PK_CLEAN_BAD = "pk_clean_bad";
	// 销售信用
	public static String BASE_DATA_PK_SSCREDIT = "pk_scredit";
	// 产品每周上架次数限制，p for product,cnt for count
	public static String BASE_DATA_PK_LIMIT_PRODUCT_URGENT_COUNT = "pk_p_urgent_cnt_limit";

	// 未确认
	public static String NAME_CONFIRM_STATUS_NO = "1";
	// 产品待确认中
	public static String NAME_CONFIRM_STATUS_PRODUCTING = "2";
	// 产品已确认
	public static String NAME_CONFIRM_STATUS_PRODUCTYES = "3";
	// 机票待确认中
	public static String NAME_CONFIRM_STATUS_TICKETING = "4";
	// 最终确认
	public static String NAME_CONFIRM_STATUS_YES = "5";

	/** 销售订单操作状态 start **/
	// 产品端
	// 未操作
	public static String ORDER_OPERATE_STATUS_NO = "N";
	// 票务状态（老数据）
	public static String ORDER_OPERATE_STATUS_AIR = "A";
	// 产品订单状态
	public static String ORDER_OPERATE_STATUS_ORDERED = "P";
	// 操作中/已操作
	public static String ORDER_OPERATE_STATUS_YES = "I";
	// 票务端
	// 未操作
	public static String AIR_OPERATE_STATUS_NO = "N";
	// 已生成订单
	public static String AIR_OPERATE_STATUS_ORDERD = "P";
	// 出票中
	public static String AIR_OPERATE_STATUS_ING = "I";
	// 已完成出票
	public static String AIR_OPERATE_STATUS_YES = "Y";

	/** 销售订单操作状态 end **/

	/** 押金相关 start **/
	// 押金类型
	// 航司押金
	public static String DEPOSIT_TYPE_AIR = "A";
	/** 押金相关 end **/

	// ************************* 票务相关*********************************
	/** 票务应付款类型 start **/
	// 机票款
	public static String TICKET_PAYABLE_TYPE_COST = "A";
	// 手续费
	public static String TICKET_PAYABLE_TYPE_CHARGES = "B";
	// 航变
	public static String TICKET_PAYABLE_TYPE_CHANGE = "C";
	/** 票务应付款类型 end **/
	// ************************* 会计相关*********************************
	/** 收入匹配申报来源 start **/
	// 客户收入
	public static String RECEIVED_FROM_WHERE_CLIENT = "C";
	// 地接返款
	public static String RECEIVED_FROM_WHERE_SUPPLIER = "D";
	// 机票返款
	public static String RECEIVED_FROM_WHERE_AIR_TICKET = "A";
	// 机票收入
	public static String RECEIVED_FROM_WHERE_AIR_RECEIVED = "AR";

	public static String PAY_REJECT_DEFAULT_REASON = "账目不符";

	/** 收入匹配申报来源 end **/

	// ************************* 虚拟账户*********************************
	// 单机票虚拟产品经理 员工号
	public static String UNREAL_USER_NUMBER_ONLY_TICKET = "OT";

}
