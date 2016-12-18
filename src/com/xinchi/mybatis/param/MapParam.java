package com.xinchi.mybatis.param;

import java.util.HashMap;

import com.xinchi.common.SupperBO;

public class MapParam extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 作为Key的字段对应MapParam的Key
	 */
	public static final String KEY_FIELD = "mapKeyField";
	/**
	 * 作为Value的字段对应MapParam的Key
	 */
	public static final String VALUE_FIELD = "mapValueField";

	public MapParam() {

	}

	private SupperBO bo;

	/**
	 * 指定keyField和valueField
	 * 
	 * @param keyField
	 *            Map中key对应的字段
	 * @param valueField
	 *            Map中value对应的字段
	 */
	public MapParam(String keyField, String valueField) {
		this.put(KEY_FIELD, keyField);
		this.put(VALUE_FIELD, valueField);
	}

	public SupperBO getBo() {
		return bo;
	}

	public void setBo(SupperBO bo) {
		this.bo = bo;
	}

}
