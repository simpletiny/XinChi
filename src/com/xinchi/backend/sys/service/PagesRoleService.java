package com.xinchi.backend.sys.service;

import java.util.List;

import com.xinchi.bean.PagesRoleBean;
import com.xinchi.common.BaseService;

public interface PagesRoleService extends BaseService {

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

	public String updateRolePages(String json);

	public List<PagesRoleBean> searchRolePages(String role);
}