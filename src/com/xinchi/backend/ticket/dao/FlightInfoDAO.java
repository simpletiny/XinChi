package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.FlightInfoBean;

public interface FlightInfoDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(FlightInfoBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(FlightInfoBean bean);

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
	public FlightInfoBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<FlightInfoBean> selectByParam(FlightInfoBean bean);

	public void deleteByFlightPk(String flight_pk);
}