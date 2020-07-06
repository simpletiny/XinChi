package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class KeyValueDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -3592572935530479714L;
	private String key_key;
	private String value_value;

	public String getKey_key() {
		return key_key;
	}

	public String getValue_value() {
		return value_value;
	}

	public void setKey_key(String key_key) {
		this.key_key = key_key;
	}

	public void setValue_value(String value_value) {
		this.value_value = value_value;
	}

}
