package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.ProductReportDto;
import com.xinchi.tools.Page;

public interface ProductReportService {

	public List<ProductReportDto> selectByPage(Page<ProductReportDto> page);

}