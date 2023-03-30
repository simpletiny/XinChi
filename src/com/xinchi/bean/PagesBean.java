package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

/**
 * 网站所有页面表
 * 
 * @author simpletiny
 *
 */
public class PagesBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -3059821154961628599L;

	private String page_url;

	private Integer level;

	private String father_pk;

	private Integer order_index;

	private String page_title;

	private String page_class;

	public String getPage_url() {
		return page_url;
	}

	public Integer getLevel() {
		return level;
	}

	public String getFather_pk() {
		return father_pk;
	}

	public Integer getOrder_index() {
		return order_index;
	}

	public String getPage_title() {
		return page_title;
	}

	public String getPage_class() {
		return page_class;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setFather_pk(String father_pk) {
		this.father_pk = father_pk;
	}

	public void setOrder_index(Integer order_index) {
		this.order_index = order_index;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}

	public void setPage_class(String page_class) {
		this.page_class = page_class;
	}

}
