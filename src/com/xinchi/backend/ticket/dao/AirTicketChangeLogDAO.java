package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirTicketChangeLogBean;
import com.xinchi.tools.Page;

public interface AirTicketChangeLogDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(AirTicketChangeLogBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirTicketChangeLogBean bean);

	/**
	 * 删除
	 * 
	 * @param pk
	 */
	public void delete(String pk);

	/**
	 * 根据主键查找
	 * 
	 * @param pk
	 */
	public AirTicketChangeLogBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirTicketChangeLogBean> selectByParam(AirTicketChangeLogBean bean);

	public List<AirTicketChangeLogBean> selectByPage(Page page);

}