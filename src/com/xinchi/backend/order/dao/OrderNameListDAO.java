package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.SaleOrderNameListBean;

public interface OrderNameListDAO {

	public void insert(SaleOrderNameListBean bean);

	public List<SaleOrderNameListBean> selectByTeamNumber(String team_number);

	public void deleteByTeamNumber(String team_number);

	public List<SaleOrderNameListBean> selectByOrderPk(String order_pk);

	public void deleteByOrderPk(String pk);

	public void update(SaleOrderNameListBean name);
}
