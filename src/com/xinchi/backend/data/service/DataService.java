package com.xinchi.backend.data.service;

import java.util.List;

import com.xinchi.bean.DataFinanceSummaryDto;
import com.xinchi.bean.DataOrderCountDto;
import com.xinchi.common.BaseService;

public interface DataService extends BaseService {

	public List<DataOrderCountDto> fetchOrderCountData(DataOrderCountDto order_count);

	public DataFinanceSummaryDto fetchFinanceSummary() throws Exception;

}
