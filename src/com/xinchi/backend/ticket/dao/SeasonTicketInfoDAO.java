package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.SeasonTicketInfoBean;

public interface SeasonTicketInfoDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(SeasonTicketInfoBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(SeasonTicketInfoBean bean);

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
	public SeasonTicketInfoBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<SeasonTicketInfoBean> selectByParam(SeasonTicketInfoBean bean);

	public void deleteByBasePk(String base_pk);
}