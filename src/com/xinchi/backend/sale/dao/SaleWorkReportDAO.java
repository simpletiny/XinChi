package com.xinchi.backend.sale.dao;

import java.util.List;

import com.xinchi.bean.SaleWorkReportDto;
import com.xinchi.tools.Page;

public interface SaleWorkReportDAO {
	public List<SaleWorkReportDto> selectSwrByPage(Page<SaleWorkReportDto> page);

}
