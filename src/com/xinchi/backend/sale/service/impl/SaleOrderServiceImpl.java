package com.xinchi.backend.sale.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.backend.sale.service.SaleOrderService;
import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderSupplierBean;

@Service
@Transactional
public class SaleOrderServiceImpl implements SaleOrderService {

	@Autowired
	private SaleOrderDAO dao;

	@Override
	public void insert(SaleOrderBean bo) {
		dao.insert(bo);
	}

	@Override
	public void saveNameList(List<SaleOrderNameListBean> arrName) {
		dao.saveNameList(arrName);
	}

	@Override
	public void saveOrderSupplier(List<SaleOrderSupplierBean> arrSupplier) {
		dao.saveOrderSupplier(arrSupplier);
	}

	@Override
	public List<SaleOrderBean> searchOrders(SaleOrderBean bo) {
		
		return dao.selectAllByParam(bo);
	}

}