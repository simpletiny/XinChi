package com.xinchi.backend.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xinchi.bean.BudgetStandardOrderBean;

@Repository
public interface BudgetStandardOrderDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(BudgetStandardOrderBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(BudgetStandardOrderBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public BudgetStandardOrderBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<BudgetStandardOrderBean> selectByParam(BudgetStandardOrderBean bean);

	public BudgetStandardOrderBean selectByTeamNumber(String team_number);

	public void insertWithPk(BudgetStandardOrderBean bean);
}