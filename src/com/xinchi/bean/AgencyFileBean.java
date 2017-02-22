package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class AgencyFileBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -6633649329891914026L;

	private String agency_pk;

	private String file_name;

	private String file_type;

	private String update_user;

	private String create_user;

	private String pk;

	private String licence_name;
	private String permit_name;
	private String liability_insurance_name;
	private String corporate_name;
	private String chief_name;
	private String other_name;

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getLicence_name() {
		return licence_name;
	}

	public void setLicence_name(String licence_name) {
		this.licence_name = licence_name;
	}

	public String getPermit_name() {
		return permit_name;
	}

	public void setPermit_name(String permit_name) {
		this.permit_name = permit_name;
	}

	public String getLiability_insurance_name() {
		return liability_insurance_name;
	}

	public void setLiability_insurance_name(String liability_insurance_name) {
		this.liability_insurance_name = liability_insurance_name;
	}

	public String getCorporate_name() {
		return corporate_name;
	}

	public void setCorporate_name(String corporate_name) {
		this.corporate_name = corporate_name;
	}

	public String getChief_name() {
		return chief_name;
	}

	public void setChief_name(String chief_name) {
		this.chief_name = chief_name;
	}

	public String getOther_name() {
		return other_name;
	}

	public void setOther_name(String other_name) {
		this.other_name = other_name;
	}

	public String getAgency_pk() {
		return agency_pk;
	}

	public void setAgency_pk(String agency_pk) {
		this.agency_pk = agency_pk;
	}

}
