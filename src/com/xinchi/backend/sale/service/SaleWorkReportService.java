package com.xinchi.backend.sale.service;

import java.util.List;

import com.xinchi.bean.SaleWorkReportDto;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "预算单")
public interface SaleWorkReportService {
	public List<SaleWorkReportDto> selectSwrByPage(Page<SaleWorkReportDto> page);
}
