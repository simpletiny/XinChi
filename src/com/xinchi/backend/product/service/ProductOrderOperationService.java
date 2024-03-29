package com.xinchi.backend.product.service;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.DropOffBean;
import com.xinchi.bean.PayableOrderBean;
import com.xinchi.bean.ProductOrderOperationBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface ProductOrderOperationService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductOrderOperationBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderOperationBean bean);

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
	public ProductOrderOperationBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderOperationBean> selectByParam(ProductOrderOperationBean bean);

	public List<ProductOrderOperationBean> selectByPage(Page<ProductOrderOperationBean> page);

	public List<ProductOrderOperationBean> selectByTeamNumber(String team_number);

	public void deleteByTeamNumber(String team_number);

	public String createOrderOperation(String json);

	public void deleteOrderSupplier(String order_pk);

	public String deleteOperation(String team_numbers);

	public List<DropOffBean> searchDropOff(DropOffBean drop_off);

	public String finalOperation(String operate_pk, BigDecimal final_supplier_cost, String json);

	public String rollBackOperation(String operate_pk);

	public List<PayableOrderBean> selectPayableOrderByParam(PayableOrderBean option);

	public String modifyOrderOperation(String json);

	public List<DropOffBean> selectDropOffByPage(Page<DropOffBean> page);
}