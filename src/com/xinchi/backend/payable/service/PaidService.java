package com.xinchi.backend.payable.service;

import java.util.List;

import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "支出申请")
public interface PaidService extends BaseService {

	@LogDescription(des = "支出申请")
	public void insertWithPk(SupplierPaidDetailBean detail);

	@LogDescription(des = "支出申请")
	public void insert(SupplierPaidDetailBean detail);

	@LogDescription(des = "搜索支出详表")
	public List<SupplierPaidDetailBean> getAllPaidsByPage(Page page);

	@LogDescription(ignore = true)
	public String update(SupplierPaidDetailBean detail);

	@LogDescription(ignore = true)
	public List<SupplierPaidDetailBean> selectByRelatedPk(String related_pk);

	public String rollBackPayApply(String related_pk);

	public SupplierPaidDetailBean selectPaidDetailByRelatedPk(String related_pk);
}
