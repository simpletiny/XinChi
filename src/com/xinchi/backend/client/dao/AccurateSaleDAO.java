package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.tools.Page;


public interface AccurateSaleDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(AccurateSaleBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(AccurateSaleBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public AccurateSaleBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<AccurateSaleBean> getAllByParam(AccurateSaleBean bean);

	public List<AccurateSaleBean> selectByPage(Page page);
}