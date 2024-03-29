package com.xinchi.backend.payable.dao;

import java.util.List;

import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.tools.Page;

public interface PaidDAO {
	public List<SupplierPaidDetailBean> selectByParam(SupplierPaidDetailBean options);

	public void update(SupplierPaidDetailBean detail);

	public void insertWithPk(SupplierPaidDetailBean detail);

	public void insert(SupplierPaidDetailBean detail);

	public List<SupplierPaidDetailBean> getAllByPage(Page page);

	public SupplierPaidDetailBean selectByRelatedPk(String related_pk);

	public SupplierPaidDetailBean selectByPk(String pk);

	public List<SupplierPaidDetailBean> selectSupplierPaidDetailByRelatedPk(String related_pk);

	public void deleteByPk(String pk);

}
