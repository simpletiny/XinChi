package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class TodoListBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -4507488412675039097L;

	private String content;

	private String limit_time;

	private String pk;

	private String create_user;

	private String update_user;

	private String done_flg;

	private String type;

	private String ext1;

	private String ext2;

	private String ext3;

	private String ext4;

	private String ext5;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(String limit_time) {
		this.limit_time = limit_time;
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

	public String getDone_flg() {
		return done_flg;
	}

	public void setDone_flg(String done_flg) {
		this.done_flg = done_flg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

}
