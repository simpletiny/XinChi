package com.xinchi.sys.xinchitask.dao;

import java.util.List;

import com.xinchi.bean.TaskBean;

public interface XinChiTaskDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(TaskBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(TaskBean bo);

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
	public TaskBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	public List<TaskBean> getAllByParam(TaskBean bo);
}