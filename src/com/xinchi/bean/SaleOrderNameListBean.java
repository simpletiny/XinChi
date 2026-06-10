package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class SaleOrderNameListBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -8213297498122375587L;

	private String team_number;

	private String name;

	private String id;

	private String update_user;

	private String pk;

	private String create_user;

	private String sex;

	private String cellphone_A;

	private String cellphone_B;

	private String id_file;

	private String passport_file;

	private Integer name_index;

	private String chairman;

	private String order_pk;

	private BigDecimal price;

	private String lock_flg;

	private String delete_flg;

	private String id_type;

	private String as_adult;
	private int age;

	public int normalHashCode() {
		int result = 17;
		if (null != cellphone_A)
			result = 31 * result + cellphone_A.hashCode();
		if (null != cellphone_B)
			result = 31 * result + cellphone_B.hashCode();
		if (null != price)
			result = 31 * result + String.valueOf(price.doubleValue()).hashCode();
		if (null != chairman)
			result = 31 * result + chairman.hashCode();
		if (null != name_index)
			result = 31 * result + name_index;
		if (null != id_type)
			result = 31 * result + id_type.hashCode();
		if (null != as_adult)
			result = 31 * result + as_adult.hashCode();
		return result + age;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SaleOrderNameListBean)) {
			return false;
		}
		SaleOrderNameListBean other = (SaleOrderNameListBean) obj;
		return (this.name == other.name || (null != this.name && this.name.equals(other.name)))
				&& (this.id == other.id || (null != this.id && this.id.equals(other.id)))
				&& (this.sex == other.sex || (null != this.sex && this.sex.equals(other.sex)));
	}

	@Override
	public int hashCode() {
		int result = 17;
		if (null != name)
			result = 31 * result + name.hashCode();
		if (null != id)
			result = 31 * result + id.hashCode();
		if (null != sex)
			result = 31 * result + sex.hashCode();
		return result;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCellphone_A() {
		return cellphone_A;
	}

	public void setCellphone_A(String cellphone_A) {
		this.cellphone_A = cellphone_A;
	}

	public String getCellphone_B() {
		return cellphone_B;
	}

	public void setCellphone_B(String cellphone_B) {
		this.cellphone_B = cellphone_B;
	}

	public String getId_file() {
		return id_file;
	}

	public void setId_file(String id_file) {
		this.id_file = id_file;
	}

	public String getPassport_file() {
		return passport_file;
	}

	public void setPassport_file(String passport_file) {
		this.passport_file = passport_file;
	}

	public Integer getName_index() {
		return name_index;
	}

	public void setName_index(Integer name_index) {
		this.name_index = name_index;
	}

	public String getChairman() {
		return chairman;
	}

	public void setChairman(String chairman) {
		this.chairman = chairman;
	}

	public String getOrder_pk() {
		return order_pk;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getLock_flg() {
		return lock_flg;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public void setLock_flg(String lock_flg) {
		this.lock_flg = lock_flg;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}

	public String getId_type() {
		return id_type;
	}

	public int getAge() {
		return age;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAs_adult() {
		return as_adult;
	}

	public void setAs_adult(String as_adult) {
		this.as_adult = as_adult;
	}

}
