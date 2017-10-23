package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.FinalStandardOrderBean;


public interface FinalStandardOrderDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(FinalStandardOrderBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(FinalStandardOrderBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public FinalStandardOrderBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<FinalStandardOrderBean> selectByParam(FinalStandardOrderBean bean);
}