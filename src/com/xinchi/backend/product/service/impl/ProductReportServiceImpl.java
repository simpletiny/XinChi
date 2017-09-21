package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductOrderDAO;
import com.xinchi.backend.product.service.ProductOrderService;
import com.xinchi.bean.ProductOrderDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductReportServiceImpl implements ProductOrderService {

	@Autowired
	private ProductOrderDAO dao;

	@Override
	public List<ProductOrderDto> selectByPage(Page<ProductOrderDto> page) {

		return dao.selectByPage(page);
	}

}