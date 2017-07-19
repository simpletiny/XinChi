package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductReportDto;
import com.xinchi.tools.Page;

public interface ProductReportDAO {

	public List<ProductReportDto> selectByPage(Page<ProductReportDto> page);
}