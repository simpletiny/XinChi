package com.xinchi.backend.supplier.service;

import java.io.IOException;
import java.util.List;

import com.xinchi.bean.DepositTicketPaidBean;
import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface SupplierDepositService extends BaseService {

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

	public String createDeposit(SupplierDepositBean deposit);

	public String deleteSupplierDeposit(String deposit_pk);

	public String receiveSupplierDeposit(SupplierDepositBean deposit, String json);

	public List<SupplierDepositBean> batUploadDeposit(String file_name, String deposit_type) throws IOException;

	public String batSaveDeposit(String json);

	public List<SupplierDepositBean> selectByVoucherNumber(String voucher_number);

	public List<DepositTicketPaidBean> selectDepositTicketPaidsByDepositPk(String deposit_pk);

	public List<SupplierDepositBean> selectDepositWithoutNumber();

	public List<SupplierDepositBean> selectDepositSummary(SupplierDepositBean bean);

	public SupplierDepositBean selectSumDeposit();
}