package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.ProductOrderDto;
import com.xinchi.tools.Page;

public interface ProductOrderService {

	public List<ProductOrderDto> selectByPage(Page<ProductOrderDto> page);

}