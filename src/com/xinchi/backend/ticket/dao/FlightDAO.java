package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.FlightBean;
import com.xinchi.tools.Page;

public interface FlightDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(FlightBean bean);

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

	public List<FlightBean> selectByPage(Page<FlightBean> page);

	public FlightBean selectByProductPk(String product_pk);
}