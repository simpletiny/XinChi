package com.xinchi.backend.data.dao;

import java.util.List;

import com.xinchi.bean.DataOrderCountDto;

public interface OrderCountDAO {

	public List<DataOrderCountDto> selectMonthOrderCount(DataOrderCountDto order_count);

	public List<DataOrderCountDto> selectDayOrderCount(DataOrderCountDto order_count);

	public List<DataOrderCountDto> selectWeekOrderCount(DataOrderCountDto order_count);

}
