package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.PreciseClientEmployeeBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface PreciseEmployeeService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(PreciseClientEmployeeBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public String update(PreciseClientEmployeeBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String pk);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public PreciseClientEmployeeBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<PreciseClientEmployeeBean> selectByParam(PreciseClientEmployeeBean option);

	public String createPreciseEmployee(PreciseClientEmployeeBean employee);

	public List<PreciseClientEmployeeBean> selectByPage(Page<PreciseClientEmployeeBean> page);

	public String updatePreciseEmployee(PreciseClientEmployeeBean employee);

	public String bindingPreciseEmployees(String json);

	public String deletePreciseEmployee(String employee_pk);
}