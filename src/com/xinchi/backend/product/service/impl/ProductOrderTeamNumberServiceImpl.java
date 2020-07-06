package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinchi.backend.product.dao.ProductOrderTeamNumberDAO;
import com.xinchi.backend.product.service.ProductOrderTeamNumberService;
import com.xinchi.bean.ProductOrderTeamNumberBean;

@Service
public class ProductOrderTeamNumberServiceImpl implements ProductOrderTeamNumberService {

	@Autowired
	private ProductOrderTeamNumberDAO dao;

	@Override
	public List<ProductOrderTeamNumberBean> selectByOrderNumber(String order_number) {

		return dao.selectByOrderNumber(order_number);

	}
}