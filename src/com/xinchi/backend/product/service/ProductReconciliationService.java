package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.ProductReconciliationBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface ProductReconciliationService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductReconciliationBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductReconciliationBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public String delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public ProductReconciliationBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductReconciliationBean> selectByParam(ProductReconciliationBean bean);

	public String addReconciliation(ProductReconciliationBean reconciliation);

	public String confirmProductAccounting(String product_manager_number, String belong_month);

	public List<ProductReconciliationBean> selectByPage(Page<ProductReconciliationBean> page);

	public List<ProductReconciliationBean> selectSumReconciliation(AirServiceFeeDto bean);
}