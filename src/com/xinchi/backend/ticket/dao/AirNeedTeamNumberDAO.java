package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirNeedTeamNumberBean;

public interface AirNeedTeamNumberDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(AirNeedTeamNumberBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirNeedTeamNumberBean bean);

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
	public AirNeedTeamNumberBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirNeedTeamNumberBean> selectByParam(AirNeedTeamNumberBean bean);

	public List<AirNeedTeamNumberBean> selectByNeedPk(String need_pk);

}