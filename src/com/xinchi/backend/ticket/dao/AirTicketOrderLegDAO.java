package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirTicketOrderLegBean;

public interface AirTicketOrderLegDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(AirTicketOrderLegBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirTicketOrderLegBean bean);

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
	public AirTicketOrderLegBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirTicketOrderLegBean> selectByParam(AirTicketOrderLegBean bean);

	public List<AirTicketOrderLegBean> selectByOrderPk(String order_pk);

	public List<AirTicketOrderLegBean> selectAirLegByOrderPks(List<String> order_pks);

}