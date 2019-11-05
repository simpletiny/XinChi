package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.AirTicketOrderLegBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface AirTicketOrderService extends BaseService{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(AirTicketOrderBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(AirTicketOrderBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public AirTicketOrderBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<AirTicketOrderBean> selectByParam(AirTicketOrderBean bean);

	public List<AirTicketOrderBean> selectByPage(Page<AirTicketOrderBean> page);

	public List<AirTicketOrderBean> selectByPks(List<String> airTicketOrderPks);

	public AirTicketOrderBean selectBySaleOrderPk(String id);

	public String createOrder(String team_number, String json);

	public List<AirTicketOrderLegBean> selectAirTicketOrderLegByOrderPk(String order_pk);

	public String lockAirTicketOrder(List<String> airTicketOrderPks);
}