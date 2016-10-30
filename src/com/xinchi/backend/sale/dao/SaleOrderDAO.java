package com.xinchi.backend.sale.dao;

import java.util.List;

import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderSupplierBean;

public interface SaleOrderDAO {

	public void insert(SaleOrderBean bo);

	public void saveNameList(List<SaleOrderNameListBean> arrName);

	public void saveOrderSupplier(List<SaleOrderSupplierBean> arrSupplier);

	public List<SaleOrderBean> selectAllByParam(SaleOrderBean bo);

}
