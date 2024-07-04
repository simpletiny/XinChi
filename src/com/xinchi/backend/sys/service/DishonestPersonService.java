package com.xinchi.backend.sys.service;

import java.util.List;

import com.xinchi.bean.DishonestPersonBean;
import com.xinchi.common.BaseService;

public interface DishonestPersonService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(DishonestPersonBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(DishonestPersonBean bean);

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
	public DishonestPersonBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<DishonestPersonBean> selectByParam(DishonestPersonBean bean);

	public DishonestPersonBean selectByPersonId(String id);
}