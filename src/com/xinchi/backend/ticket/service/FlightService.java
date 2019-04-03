package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.FlightBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface FlightService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(FlightBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(FlightBean bean);

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
	public FlightBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<FlightBean> selectByParam(FlightBean bean);

	public String createFlight(FlightBean flight, String json);

	public List<FlightBean> selectByPage(Page<FlightBean> page);
	
	public FlightBean selectByProductPk(String product_pk);
}