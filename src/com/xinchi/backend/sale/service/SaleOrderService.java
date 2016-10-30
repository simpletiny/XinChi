package com.xinchi.backend.sale.service;

import java.util.List;

import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderSupplierBean;

public interface SaleOrderService {
	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.SaleOrderBean bo);

	public void saveNameList(List<SaleOrderNameListBean> arrName);

	public void saveOrderSupplier(List<SaleOrderSupplierBean> arrSupplier);

	public List<SaleOrderBean> searchOrders(SaleOrderBean bo);
}
