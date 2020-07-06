package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductNeedDto;
import com.xinchi.tools.Page;

public interface ProductNeedDAO {

	public List<ProductNeedDto> selectByPage(Page<ProductNeedDto> page);
}