package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class OrderSupplierSaleOrderBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 5180393585502778487L;

	private String team_number;

	private String receivable_comment;

	private String contact_way;

	private String hotel_comment;

	private String treat_comment;

	private String pick_send_info;

	private String base_pk;

	private String final_payable_comment;
	private String final_comment;

	// DTO
	private String sale_number;
	private String sale_name;
	private int adult_count;
	private int special_count;

	private List<OrderSupplierSaleOrderNameInfoBean> infos;

	public String getTeam_number() {
		return team_number;
	}

	public String getReceivable_comment() {
		return receivable_comment;
	}

	public String getContact_way() {
		return contact_way;
	}

	public String getHotel_comment() {
		return hotel_comment;
	}

	public String getTreat_comment() {
		return treat_comment;
	}

	public String getPick_send_info() {
		return pick_send_info;
	}

	public String getBase_pk() {
		return base_pk;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setReceivable_comment(String receivable_comment) {
		this.receivable_comment = receivable_comment;
	}

	public void setContact_way(String contact_way) {
		this.contact_way = contact_way;
	}

	public void setHotel_comment(String hotel_comment) {
		this.hotel_comment = hotel_comment;
	}

	public void setTreat_comment(String treat_comment) {
		this.treat_comment = treat_comment;
	}

	public void setPick_send_info(String pick_send_info) {
		this.pick_send_info = pick_send_info;
	}

	public void setBase_pk(String base_pk) {
		this.base_pk = base_pk;
	}

	public List<OrderSupplierSaleOrderNameInfoBean> getInfos() {
		return infos;
	}

	public void setInfos(List<OrderSupplierSaleOrderNameInfoBean> infos) {
		this.infos = infos;
	}

	public String getSale_number() {
		return sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public int getAdult_count() {
		return adult_count;
	}

	public int getSpecial_count() {
		return special_count;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public void setAdult_count(int adult_count) {
		this.adult_count = adult_count;
	}

	public void setSpecial_count(int special_count) {
		this.special_count = special_count;
	}

	public String getFinal_payable_comment() {
		return final_payable_comment;
	}

	public String getFinal_comment() {
		return final_comment;
	}

	public void setFinal_payable_comment(String final_payable_comment) {
		this.final_payable_comment = final_payable_comment;
	}

	public void setFinal_comment(String final_comment) {
		this.final_comment = final_comment;
	}

}
