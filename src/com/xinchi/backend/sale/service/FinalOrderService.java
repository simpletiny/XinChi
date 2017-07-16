package com.xinchi.backend.sale.service;

import java.util.List;

import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "决算单")
public interface FinalOrderService {

	@LogDescription(des = "新建决算单")
	public void insert(FinalOrderBean order);

	@LogDescription(ignore = true)
	public void saveOrderSupplier(List<FinalOrderSupplierBean> arrSupplier);

	@LogDescription(ignore = true)
	public List<FinalOrderBean> searchOrders(FinalOrderBean order);

	@LogDescription(des = "查看决算单")
	public FinalOrderBean searchFinalOrderByPk(String order_pk);

	@LogDescription(ignore = true)
	public List<FinalOrderSupplierBean> searchFinalSupplier(String team_number);

	@LogDescription(des = "搜索决算单")
	public List<FinalOrderBean> searchOrdersByPage(Page<FinalOrderBean> page);
	
	@LogDescription(ignore = true)
	public FinalOrderBean getFinalOrderByTeamNo(String team_number);

	@LogDescription(ignore = true)
	public List<FinalOrderSupplierBean> searchFinalSupplierByParam(FinalOrderSupplierBean bo);

	@LogDescription(ignore = true)
	public void deleteFinalOrderSupplier(String team_number);

	@LogDescription(ignore = true)
	public void deleteFinalOrderByPk(String order_pk);
}
