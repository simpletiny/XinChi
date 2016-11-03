package com.xinchi.backend.sale.service;

import java.util.List;

import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;

public interface FinalOrderService {

	public void insert(FinalOrderBean order);

	public void saveOrderSupplier(List<FinalOrderSupplierBean> arrSupplier);

	public List<FinalOrderBean> searchOrders(FinalOrderBean order);

}
