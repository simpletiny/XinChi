package com.xinchi.backend.sys.service;

import java.util.List;

import com.xinchi.bean.PagesBean;
import com.xinchi.common.BaseService;

public interface PagesService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(PagesBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(PagesBean bean);

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
	public PagesBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<PagesBean> selectByParam(PagesBean bean);

	public String updatePages(String json);
}