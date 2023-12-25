package com.xinchi.backend.accounting.dao;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.PayApprovalBean;
import com.xinchi.tools.Page;

public interface PayApprovalDAO {

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

	public PayApprovalBean selectByBackPk(String pk);

	public BigDecimal selectSumBalance();

	public void suspensePayApplyByPks(List<String> pks);

	public BigDecimal selectSumSuspense();
}