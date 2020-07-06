package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.SeasonTicketBaseBean;
import com.xinchi.tools.Page;

public interface SeasonTicketBaseDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(SeasonTicketBaseBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(SeasonTicketBaseBean bean);

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
	public SeasonTicketBaseBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<SeasonTicketBaseBean> selectByParam(SeasonTicketBaseBean bean);

	public List<SeasonTicketBaseBean> selectByPage(Page<SeasonTicketBaseBean> page);
}