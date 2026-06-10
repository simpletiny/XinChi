package com.xinchi.backend.sys.service;

import java.util.List;

import com.xinchi.bean.DishonestLogBean;
import com.xinchi.common.BaseService;

public interface DishonestLogService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(DishonestLogBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(DishonestLogBean bean);

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
	public DishonestLogBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<DishonestLogBean> selectByParam(DishonestLogBean bean);

	public List<DishonestLogBean> selectByPersonId(String id);
}