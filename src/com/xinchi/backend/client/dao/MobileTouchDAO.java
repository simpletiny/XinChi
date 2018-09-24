package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.MobileTouchBean;

public interface MobileTouchDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(MobileTouchBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(MobileTouchBean bean);

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
	public MobileTouchBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<MobileTouchBean> selectByParam(MobileTouchBean bean);
}