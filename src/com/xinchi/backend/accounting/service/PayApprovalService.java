package com.xinchi.backend.accounting.service;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.PayApprovalBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface PayApprovalService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(PayApprovalBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(PayApprovalBean bean);

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
	public PayApprovalBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<PayApprovalBean> selectByParam(PayApprovalBean bean);

	public List<PayApprovalBean> selectByPage(Page<PayApprovalBean> page);

	public BigDecimal selectSumBalance();

	public PayApprovalBean selectByBackPk(String back_pk);
}