package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ClientEmployeeBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String nick_name;
	private String sex;
	private String age;
	private String wechat;

	private String qq;

	private String cellphone;

	private String telephone;
	private String fax;
	private String type;
	private String id;

	private String financial_body_pk;

	private String financial_body_name;

	private String area;

	private String sales;
	private String sales_name;

	private String create_user;

	private String update_user;

	private String pk;

	private String comment;

	private String birth_year;

	private String public_flg;
	
	private String relation_level;
	private String back_level;
	private String market_level;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFinancial_body_pk() {
		return financial_body_pk;
	}

	public void setFinancial_body_pk(String financial_body_pk) {
		this.financial_body_pk = financial_body_pk;
	}

	public String getFinancial_body_name() {
		return financial_body_name;
	}

	public void setFinancial_body_name(String financial_body_name) {
		this.financial_body_name = financial_body_name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBirth_year() {
		return birth_year;
	}

	public void setBirth_year(String birth_year) {
		this.birth_year = birth_year;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getSales_name() {
		return sales_name;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public String getPublic_flg() {
		return public_flg;
	}

	public void setPublic_flg(String public_flg) {
		this.public_flg = public_flg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getRelation_level() {
		return relation_level;
	}

	public void setRelation_level(String relation_level) {
		this.relation_level = relation_level;
	}

	public String getBack_level() {
		return back_level;
	}

	public void setBack_level(String back_level) {
		this.back_level = back_level;
	}

	public String getMarket_level() {
		return market_level;
	}

	public void setMarket_level(String market_level) {
		this.market_level = market_level;
	}

}
