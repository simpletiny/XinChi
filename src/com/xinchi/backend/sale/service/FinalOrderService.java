package com.xinchi.backend.sale.service;

import java.util.List;

import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;
import com.xinchi.tools.Page;

public interface FinalOrderService {

	public void insert(FinalOrderBean order);

	public void saveOrderSupplier(List<FinalOrderSupplierBean> arrSupplier);

	public List<FinalOrderBean> searchOrders(FinalOrderBean order);

	public FinalOrderBean searchFinalOrderByPk(String order_pk);

	public List<FinalOrderSupplierBean> searchFinalSupplier(String team_number);

	public List<FinalOrderBean> searchOrdersByPage(Page<FinalOrderBean> page);

	public FinalOrderBean getFinalOrderByTeamNo(String team_number);

	public List<FinalOrderSupplierBean> searchFinalSupplierByParam(
			FinalOrderSupplierBean bo);

	public void deleteFinalOrderSupplier(String team_number);

	public void deleteFinalOrderByPk(String order_pk);
}
