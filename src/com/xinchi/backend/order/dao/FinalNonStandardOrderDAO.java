package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.FinalNonStandardOrderBean;


public interface FinalNonStandardOrderDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(FinalNonStandardOrderBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(FinalNonStandardOrderBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public FinalNonStandardOrderBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<FinalNonStandardOrderBean> selectByParam(FinalNonStandardOrderBean bean);
}