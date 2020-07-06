package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.common.BaseService;

public interface ProductOrderTeamNumberService extends BaseService {
	public List<ProductOrderTeamNumberBean> selectByOrderNumber(String order_number);
}