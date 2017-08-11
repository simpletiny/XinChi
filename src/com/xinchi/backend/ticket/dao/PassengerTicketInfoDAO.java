package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.PassengerTicketInfoBean;


public interface PassengerTicketInfoDAO{
	
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
}