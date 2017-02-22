package com.xinchi.backend.payable.dao;

import java.util.List;

import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.tools.Page;

public interface PaidDAO {

	public void insertWithPk(SupplierPaidDetailBean detail);

	public void insert(SupplierPaidDetailBean detail);

	public List<SupplierPaidDetailBean> getAllByPage(Page page);

}
