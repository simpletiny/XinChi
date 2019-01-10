package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ClientEmployeeBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String nick_name;
	private String sex;
	private String age;
	private String wechat;
	private String wechat1;
	
	private String qq;

	private String cellphone;
	private String cellphone1;
	
	private String telephone;
	private String fax;
	private String type;
	private String id;

	private String financial_body_pk;
	private List<String> public_flgs;
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
	private List<String> delete_flgs;
	private String relation_level;
	private String back_level;
	private String market_level;
	private String dimission_flg;
	private String review_flg;
	private String quit_flg;

	private String head_photo;
	
	private String useful_time;
	private int year_order_count;
	private int last_order_period;
	private List<ClientEmployeeUserBean> employee_users;
	//
	private String new_client_pk;
	private String hopping_date;

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

	public List<String> getDelete_flgs() {
		return delete_flgs;
	}

	public void setDelete_flgs(List<String> delete_flgs) {
		this.delete_flgs = delete_flgs;
	}

	public String getNew_client_pk() {
		return new_client_pk;
	}

	public void setNew_client_pk(String new_client_pk) {
		this.new_client_pk = new_client_pk;
	}

	public String getHopping_date() {
		return hopping_date;
	}

	public void setHopping_date(String hopping_date) {
		this.hopping_date = hopping_date;
	}

	public String getDimission_flg() {
		return dimission_flg;
	}

	public void setDimission_flg(String dimission_flg) {
		this.dimission_flg = dimission_flg;
	}

	public List<String> getPublic_flgs() {
		return public_flgs;
	}

	public void setPublic_flgs(List<String> public_flgs) {
		this.public_flgs = public_flgs;
	}

	public List<ClientEmployeeUserBean> getEmployee_users() {
		return employee_users;
	}

	public void setEmployee_users(List<ClientEmployeeUserBean> employee_users) {
		this.employee_users = employee_users;
	}

	public String getReview_flg() {
		return review_flg;
	}

	public void setReview_flg(String review_flg) {
		this.review_flg = review_flg;
	}

	public String getQuit_flg() {
		return quit_flg;
	}

	public void setQuit_flg(String quit_flg) {
		this.quit_flg = quit_flg;
	}

	public String getUseful_time() {
		return useful_time;
	}

	public void setUseful_time(String useful_time) {
		this.useful_time = useful_time;
	}

	public int getYear_order_count() {
		return year_order_count;
	}

	public int getLast_order_period() {
		return last_order_period;
	}

	public void setYear_order_count(int year_order_count) {
		this.year_order_count = year_order_count;
	}

	public void setLast_order_period(int last_order_period) {
		this.last_order_period = last_order_period;
	}

	public String getWechat1() {
		return wechat1;
	}

	public String getCellphone1() {
		return cellphone1;
	}

	public void setWechat1(String wechat1) {
		this.wechat1 = wechat1;
	}

	public void setCellphone1(String cellphone1) {
		this.cellphone1 = cellphone1;
	}

	public String getHead_photo() {
		return head_photo;
	}

	public void setHead_photo(String head_photo) {
		this.head_photo = head_photo;
	}

}
