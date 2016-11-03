package com.xinchi.backend.sale.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sale.dao.FinalOrderDAO;
import com.xinchi.backend.sale.service.FinalOrderService;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;

@Service
@Transactional
public class FinalOrderServiceImpl implements FinalOrderService {
	@Autowired
	private FinalOrderDAO dao;

	@Override
	public void insert(FinalOrderBean order) {
		dao.insert(order);

	}

	@Override
	public void saveOrderSupplier(List<FinalOrderSupplierBean> arrSupplier) {
		dao.saveOrderSupplier(arrSupplier);
	}

	@Override
	public List<FinalOrderBean> searchOrders(FinalOrderBean order) {
		return dao.selectAllByParam(order);
	}

}
