package com.xinchi.common;

public enum DictionaryEnum  {

	//用户类型     1:个人   2：企业
	PERSON_TYPE("1"),ENTERPRISE_TYPE("2"),ADMIN_TYPE("3"),
	//用户是否已激活
	MAIL_CLOSED(0),MAIL_OPEN(1),
	//用户状态  0:失效   1：正常
	STATE_FAIL("0"),STATE_NORMAL("1"),
	//菜单样式 totally:完全  never:从来没有  half:一半  xt:选填
	MENU_TOTALLY("totally"),MENU_NEVER("never"),MENU_HALF("half"),MENU_XT("xt"),
	OTHER_DATA("99"), 
	//面试通知的状态，  1：已经查看  0：未查看
	INFORM_STATE_SEE("1"),INFORM_STATE_NOSEE("0"),
	//级别：1：1级 		2：2级
	LEVEL_FIRST(1), LEVEL_SECOND(2),
	;
	
	
	private String stringValue;
	private int intValue;
	
	private DictionaryEnum(String stringValue){
		this.stringValue=stringValue;
	}
	private DictionaryEnum(int intValue){
		this.intValue=intValue;
	}
	
	public String getStringValue(){
		return stringValue;
	}
	public int getIntValue(){
		return intValue;
	}
}
