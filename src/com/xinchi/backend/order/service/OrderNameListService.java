package com.xinchi.backend.order.service;

import java.util.List;

import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.BaseService;

public interface OrderNameListService extends BaseService {

	public String saveNameList(List<SaleOrderNameListBean> names);

	public List<SaleOrderNameListBean> selectByTeamNumber(String team_number);

	public List<SaleOrderNameListBean> selectByOrderPk(String order_pk);

}
