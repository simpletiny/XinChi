package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ClientChangeSaleLogBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 5864774831216694788L;

	private String pre_sale_pk;

	private String client_pk;

	private String change_user;

	private String type;

	private String combine_client_pk;

	private String pk;

	private String create_user;

	private String update_user;

	public String getPre_sale_pk() {
		return pre_sale_pk;
	}

	public void setPre_sale_pk(String pre_sale_pk) {
		this.pre_sale_pk = pre_sale_pk;
	}

	public String getClient_pk() {
		return client_pk;
	}

	public void setClient_pk(String client_pk) {
		this.client_pk = client_pk;
	}

	public String getChange_user() {
		return change_user;
	}

	public void setChange_user(String change_user) {
		this.change_user = change_user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCombine_client_pk() {
		return combine_client_pk;
	}

	public void setCombine_client_pk(String combine_client_pk) {
		this.combine_client_pk = combine_client_pk;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
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

}
