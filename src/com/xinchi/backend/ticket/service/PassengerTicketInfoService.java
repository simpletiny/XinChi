package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.common.BaseService;

public interface PassengerTicketInfoService extends BaseService{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(PassengerTicketInfoBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(PassengerTicketInfoBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public PassengerTicketInfoBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<PassengerTicketInfoBean> selectByParam(PassengerTicketInfoBean bean);

	public String insertList(List<PassengerTicketInfoBean> ptis);

	public String checkSameAirLeg(List<String> passenger_pks);

	public String allotTicket(String json);
}