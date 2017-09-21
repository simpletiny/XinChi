package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderDto;
import com.xinchi.tools.Page;

public interface ProductOrderDAO {

	public List<ProductOrderDto> selectByPage(Page<ProductOrderDto> page);
}