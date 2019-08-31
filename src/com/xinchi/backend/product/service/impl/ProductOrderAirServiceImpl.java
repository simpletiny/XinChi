package com.xinchi.backend.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductOrderAirBaseDAO;
import com.xinchi.backend.product.service.ProductOrderAirService;
import com.xinchi.bean.ProductOrderAirBaseBean;

@Service
@Transactional
public class ProductOrderAirServiceImpl implements ProductOrderAirService {

	@Autowired
	private ProductOrderAirBaseDAO baseDao;

	@Override
	public ProductOrderAirBaseBean selectByTeamNumber(String team_number) {

		return baseDao.selectByTeamNumber(team_number);
	}

}