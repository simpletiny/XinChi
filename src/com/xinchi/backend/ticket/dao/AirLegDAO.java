package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirLegBean;
import com.xinchi.tools.Page;

public interface AirLegDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(AirLegBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirLegBean bean);

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
	public AirLegBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirLegBean> selectByParam(AirLegBean bean);

	public List<AirLegBean> selectByPage(Page<AirLegBean> page);

}