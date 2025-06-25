package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.PreciseClientEmployeeBean;
import com.xinchi.bean.PreciseEmployeeBindingBean;
import com.xinchi.tools.Page;

public interface PreciseEmployeeDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(PreciseClientEmployeeBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(PreciseClientEmployeeBean bean);

	/**
	 * 删除
	 * 
	 * @param pk
	 */
	public void delete(String pk);

	/**
	 * 根据主键查找
	 * 
	 * @param pk
	 */
	public PreciseClientEmployeeBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param option
	 */
	public List<PreciseClientEmployeeBean> selectByParam(PreciseClientEmployeeBean option);

	public List<PreciseClientEmployeeBean> selectByPage(Page<PreciseClientEmployeeBean> page);

	public int deleteBindingByEmployeePk(String employee_pk);

	public void insertBinding(PreciseEmployeeBindingBean peb);

	public List<PreciseEmployeeBindingBean> selectBindingsByPrecisePk(String precise_pk);

	public void deleteBindingByPrecisePk(String precise_pk);
}