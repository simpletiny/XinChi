package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.TicketNameTempletBean;
import com.xinchi.tools.Page;

public interface TicketNameTempletDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(TicketNameTempletBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(TicketNameTempletBean bean);

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
	public TicketNameTempletBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<TicketNameTempletBean> selectByParam(TicketNameTempletBean bean);

	public List<TicketNameTempletBean> selectByPage(Page<TicketNameTempletBean> page);

}