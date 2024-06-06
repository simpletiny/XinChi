package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ProductOrderNameBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -6513128166307804748L;

	private String product_order_number;

	private String team_number;

	private String name_pk;

	private String operate_status;

	private String ticked;

	private String ticked_time;

	private String ticked_user;

	private java.math.BigDecimal ticket_cost;

	private String status;

	private String name;

	private String id;

	private String sex;

	private String cellphone_A;

	private String cellphone_B;

	private String id_file;

	private String passport_file;

	private Integer name_index;

	private String chairman;

	private String order_pk;

	private String lock_flg;

	private String delete_flg;

	private String id_type;

	private String as_adult;

	private String departure_date;

	private int days;

	private String product_pk;

	private int age;

	// option
	private String product_manager_number;
	private String product_model;
	private String product_name;

	private List<String> operate_statuses;

	public String getProduct_order_number() {
		return product_order_number;
	}

	public String getTeam_number() {
		return team_number;
	}

	public String getName_pk() {
		return name_pk;
	}

	public String getOperate_status() {
		return operate_status;
	}

	public String getTicked() {
		return ticked;
	}

	public String getTicked_time() {
		return ticked_time;
	}

	public String getTicked_user() {
		return ticked_user;
	}

	public java.math.BigDecimal getTicket_cost() {
		return ticket_cost;
	}

	public String getStatus() {
		return status;
	}

	public void setProduct_order_number(String product_order_number) {
		this.product_order_number = product_order_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setName_pk(String name_pk) {
		this.name_pk = name_pk;
	}

	public void setOperate_status(String operate_status) {
		this.operate_status = operate_status;
	}

	public void setTicked(String ticked) {
		this.ticked = ticked;
	}

	public void setTicked_time(String ticked_time) {
		this.ticked_time = ticked_time;
	}

	public void setTicked_user(String ticked_user) {
		this.ticked_user = ticked_user;
	}

	public void setTicket_cost(java.math.BigDecimal ticket_cost) {
		this.ticket_cost = ticket_cost;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getSex() {
		return sex;
	}

	public String getCellphone_A() {
		return cellphone_A;
	}

	public String getCellphone_B() {
		return cellphone_B;
	}

	public String getId_file() {
		return id_file;
	}

	public String getPassport_file() {
		return passport_file;
	}

	public Integer getName_index() {
		return name_index;
	}

	public String getChairman() {
		return chairman;
	}

	public String getOrder_pk() {
		return order_pk;
	}

	public String getLock_flg() {
		return lock_flg;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public String getId_type() {
		return id_type;
	}

	public String getAs_adult() {
		return as_adult;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setCellphone_A(String cellphone_A) {
		this.cellphone_A = cellphone_A;
	}

	public void setCellphone_B(String cellphone_B) {
		this.cellphone_B = cellphone_B;
	}

	public void setId_file(String id_file) {
		this.id_file = id_file;
	}

	public void setPassport_file(String passport_file) {
		this.passport_file = passport_file;
	}

	public void setName_index(Integer name_index) {
		this.name_index = name_index;
	}

	public void setChairman(String chairman) {
		this.chairman = chairman;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public void setLock_flg(String lock_flg) {
		this.lock_flg = lock_flg;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public void setAs_adult(String as_adult) {
		this.as_adult = as_adult;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_model() {
		return product_model;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public List<String> getOperate_statuses() {
		return operate_statuses;
	}

	public void setOperate_statuses(List<String> operate_statuses) {
		this.operate_statuses = operate_statuses;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
