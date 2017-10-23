package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientBean;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "客户财务主体")
public interface ClientService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	@LogDescription(des = "新增财务主体")
	public void insert(com.xinchi.bean.ClientBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	@LogDescription(des = "修改财务主体")
	public void update(com.xinchi.bean.ClientBean bo);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	@LogDescription(des = "删除财务主体")
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	@LogDescription(des = "查看财务主体详情")
	public com.xinchi.bean.ClientBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public List<com.xinchi.bean.ClientBean> getAllCompaniesByParam(com.xinchi.bean.ClientBean bo);

	@LogDescription(des = "新增财务主体")
	public String createCompany(ClientBean client);

	@LogDescription(des = "修改财务主体")
	public String updateCompany(ClientBean client);

	@LogDescription(des = "财务主体搜索")
	public List<ClientBean> getAllCompaniesByPage(Page<ClientBean> page);

	@LogDescription(des = "删除财务主体")
	public String deleteClientEmployee(List<String> company_pks);

	@LogDescription(des = "恢复财务主体")
	public String recoveryClientEmployee(List<String> company_pks);

	public String changeClientSales(List<String> company_pks, String sale_pk);
}