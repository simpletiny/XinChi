package com.xinchi.backend.sys.dao;

import java.util.List;

import com.xinchi.bean.PagesRoleBean;

public interface PagesRoleDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(PagesRoleBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(PagesRoleBean bean);

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
	public PagesRoleBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<PagesRoleBean> selectByParam(PagesRoleBean bean);
}