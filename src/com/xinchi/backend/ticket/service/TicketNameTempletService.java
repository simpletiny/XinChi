package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.TicketNameTempletBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface TicketNameTempletService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(TicketNameTempletBean bean);

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

	public String createTicketNameTemplet(TicketNameTempletBean templet);

	public String deleteTicketNameTemplet(String templet_pk);

	public String updateTicketNameTemplet(TicketNameTempletBean templet);

}