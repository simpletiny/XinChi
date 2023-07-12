package com.xinchi.backend.supplier.dao;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.tools.Page;

public interface SupplierDepositDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(SupplierDepositBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(SupplierDepositBean bean);

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
	public SupplierDepositBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<SupplierDepositBean> selectByParam(SupplierDepositBean bean);

	public List<SupplierDepositBean> selectByPage(Page page);

	public BigDecimal selectSumBalanceByType(String type);

	public SupplierDepositBean selectByDepositNumber(String deposit_number);

	public List<SupplierDepositBean> selectDepositWithoutNumber();

	public List<SupplierDepositBean> selectDepositSummary(SupplierDepositBean bean);

	public SupplierDepositBean selectSumDeposit();
}