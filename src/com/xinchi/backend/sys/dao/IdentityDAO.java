package com.xinchi.backend.sys.dao;

import java.util.List;

import com.xinchi.bean.IdentityBean;

public interface IdentityDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(IdentityBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(IdentityBean bean);

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
	public IdentityBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<IdentityBean> selectByParam(IdentityBean bean);

	public IdentityBean selectById(String id);
}