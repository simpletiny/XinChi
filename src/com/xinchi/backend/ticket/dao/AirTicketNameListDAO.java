package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.tools.Page;

public interface AirTicketNameListDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(AirTicketNameListBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirTicketNameListBean bean);

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
	public AirTicketNameListBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirTicketNameListBean> selectByParam(AirTicketNameListBean bean);

	public List<AirTicketNameListBean> selectByPage(Page<AirTicketNameListBean> page);

	public List<AirTicketNameListBean> selectByPks(String[] pks);

	public List<PassengerAllotDto> selectByPassengerPks(List<String> passenger_pks);

	public List<PassengerAllotDto> selectPassengerAllotByPassengerPk(String passenger_pk);

	public List<PassengerAllotDto> selectPassengerAllotByOrderPk(String order_pk);
}