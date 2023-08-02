package com.xinchi.common;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.order.service.BudgetStandardOrderService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.bean.OrderDto;

@Service
@Transactional
public class AutoDeleteTbcOrder {
	@Autowired
	private OrderService service;
	@Autowired
	private BudgetNonStandardOrderService bnsOrderService;
	@Autowired
	private BudgetStandardOrderService bsOrderService;

	public void delete5DaysAgoTbcOrder(String[] param) {

		List<OrderDto> orders = service.selectTbcByParam(null);

		Calendar c = Calendar.getInstance();
		String today = DateUtil.getDateStr(DateUtil.YYYY_MM_DD);
		for (OrderDto order : orders) {
			if (order.getUpdate_time() == null) {
				c.setTimeInMillis(Long.parseLong(order.getCreate_time()));
			} else {
				c.setTimeInMillis(Long.parseLong(order.getUpdate_time()));
			}

			int days = DateUtil.dateDiff(today, DateUtil.sdf1.format(c.getTime()));
			// 2023-07-25从5天改为30天
			if (days >= 30 && order.getReceivable_first_flg().equals("N")) {
				if (order.getStandard_flg().equals("Y")) {
					bsOrderService.delete(order.getPk());
				} else {
					bnsOrderService.delete(order.getPk());
				}
			}
		}

	}
}
