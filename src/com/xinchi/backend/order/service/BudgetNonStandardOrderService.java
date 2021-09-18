package com.xinchi.backend.order.service;

import java.util.List;

import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.common.BaseService;

public interface BudgetNonStandardOrderService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String createOrder(BudgetNonStandardOrderBean bean, String json);

	/**
	 * 修改
	 * 
	 * @param bean
	 * @param json
	 */
	public String update(BudgetNonStandardOrderBean bean, String json);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public String delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public BudgetNonStandardOrderBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<BudgetNonStandardOrderBean> selectByParam(BudgetNonStandardOrderBean bean);

	public String updateComment(BudgetNonStandardOrderBean bean);

	public BudgetNonStandardOrderBean selectByTeamNumber(String team_number);

	public String updateConfirmedNonStandardOrder(BudgetNonStandardOrderBean bnsOrder, String json);

	public String rollBackCOrder(String order_pk);
}