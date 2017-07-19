package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.BudgetNonStandardOrderBean;


public interface BudgetNonStandardOrderDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(BudgetNonStandardOrderBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(BudgetNonStandardOrderBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public BudgetNonStandardOrderBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<BudgetNonStandardOrderBean> selectByParam(BudgetNonStandardOrderBean bean);
}