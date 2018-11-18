package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.RelationLevelDto;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "客户员工")
public interface EmployeeService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.ClientEmployeeBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public String update(com.xinchi.bean.ClientEmployeeBean bo);

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
	public com.xinchi.bean.ClientEmployeeBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public List<com.xinchi.bean.ClientEmployeeBean> getAllClientEmployeeByParam(com.xinchi.bean.ClientEmployeeBean bo);

	@LogDescription(des = "新建客户员工")
	public String createEmployee(ClientEmployeeBean client);

	@LogDescription(des = "修改客户员工")
	public String updateEmployee(ClientEmployeeBean client);

	@LogDescription(des = "客户员工搜索")
	public List<ClientEmployeeBean> getAllClientEmployeeByPage(Page<ClientEmployeeBean> page);

	@LogDescription(ignore = true)
	public List<String> getBodyPksByEmployeePks(String[] employee_pks);

	@LogDescription(des = "停用客户员工")
	public String deleteClientEmployee(List<String> employee_pks);

	@LogDescription(des = "恢复客户员工")
	public String recoveryClientEmployee(List<String> employee_pks);

	public RelationLevelDto selectRelationCntBySales(String sales_pk);

	public String publicClientEmployee(List<String> employee_pks);

	public String deleteClientEmployeeReally(String employee_pk);

	public String combineClientEmployee(List<String> employee_pks);

	public String jobHopping(ClientEmployeeBean employee);

	public String changeEmployeeSales(List<String> employee_pks, List<String> sale_pks);

	public RelationLevelDto selectRelationCntAdmin();

	public String reviewEmployee(ClientEmployeeBean employee);

}