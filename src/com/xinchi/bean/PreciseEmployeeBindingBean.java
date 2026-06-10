package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class PreciseEmployeeBindingBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 6182505458056755161L;

	private String precise_pk;

	private String employee_pk;

	public String getPrecise_pk() {
		return precise_pk;
	}

	public void setPrecise_pk(String precise_pk) {
		this.precise_pk = precise_pk;
	}

	public String getEmployee_pk() {
		return employee_pk;
	}

	public void setEmployee_pk(String employee_pk) {
		this.employee_pk = employee_pk;
	}

}
