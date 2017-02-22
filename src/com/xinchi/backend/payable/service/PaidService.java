package com.xinchi.backend.payable.service;

import java.util.List;

import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.tools.Page;

public interface PaidService {

	public void insertWithPk(SupplierPaidDetailBean detail);

	public void insert(SupplierPaidDetailBean detail);

	public List<SupplierPaidDetailBean> getAllPaidsByPage(Page page);

}
