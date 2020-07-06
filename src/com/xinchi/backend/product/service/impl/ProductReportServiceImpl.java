package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductReportDAO;
import com.xinchi.backend.product.service.ProductReportService;
import com.xinchi.bean.ProductReportDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductReportServiceImpl implements ProductReportService {

	@Autowired
	private ProductReportDAO dao;

	@Override
	public List<ProductReportDto> selectByPage(Page<ProductReportDto> page) {

		return dao.selectByPage(page);
	}

}