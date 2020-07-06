package com.xinchi.backend.data.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.data.dao.OrderCountDAO;
import com.xinchi.backend.data.service.DataService;
import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.bean.DataFinanceSummaryDto;
import com.xinchi.bean.DataOrderCountDto;
import com.xinchi.bean.KeyValueDto;
import com.xinchi.common.DateUtil;

@Service
@Transactional
public class DataServiceImpl implements DataService {

	@Autowired
	private OrderCountDAO orderCountDao;

	@Override
	public List<DataOrderCountDto> fetchOrderCountData(DataOrderCountDto order_count) {
		if (order_count.getHorizontal().equals("month")) {
			return fetchMonthOrderCount(order_count);
		} else if (order_count.getHorizontal().equals("day")) {
			return fetchDayOrderCount(order_count);
		} else if (order_count.getHorizontal().equals("week")) {
			return fetchWeekOrderCount(order_count);
		}
		return null;

	}

	/**
	 * 按月获取订单数量
	 * 
	 * @param order_count
	 * @return
	 */
	private List<DataOrderCountDto> fetchMonthOrderCount(DataOrderCountDto order_count) {

		String year = order_count.getOption_year();

		String date_from = year + "-01-01";
		String date_to = year + "-12-31";
		boolean isNow = false;
		if (year.equals(DateUtil.getDateStr("yyyy"))) {
			date_to = DateUtil.today();
			isNow = true;
		}

		order_count.setDate_from(date_from);
		order_count.setDate_to(date_to);

		List<DataOrderCountDto> lists = orderCountDao.selectMonthOrderCount(order_count);

		// 涉及到的销售
		Set<String> names = new HashSet<String>();
		for (DataOrderCountDto docd : lists) {
			names.add(docd.getCreate_user());
		}
		List<DataOrderCountDto> result = new ArrayList<DataOrderCountDto>();

		int end_month = isNow ? Calendar.getInstance().get(Calendar.MONTH) + 1 : 12;

		for (String name : names) {
			DataOrderCountDto docd = new DataOrderCountDto();
			docd.setCreate_user(name);
			List<Integer> data_order_cnt = new ArrayList<Integer>();
			List<Integer> data_people_cnt = new ArrayList<Integer>();

			for (int i = 1; i <= end_month; i++) {
				boolean isExists = false;
				for (int j = lists.size() - 1; j >= 0; j--) {
					DataOrderCountDto current = lists.get(j);
					if (current.getCreate_user().equals(name) && current.getMonth() == i) {
						data_order_cnt.add(current.getOrder_cnt());
						data_people_cnt.add(current.getPeople_cnt());
						lists.remove(j);
						isExists = true;
						break;
					}
				}
				if (!isExists) {
					data_order_cnt.add(0);
					data_people_cnt.add(0);
				}
			}
			docd.setData_order_cnt(data_order_cnt);
			docd.setData_people_cnt(data_people_cnt);

			result.add(docd);

		}

		return result;
	}

	/**
	 * 按天获取订单量
	 * 
	 * @param order_count
	 * @return
	 */
	private List<DataOrderCountDto> fetchDayOrderCount(DataOrderCountDto order_count) {

		String month = order_count.getOption_month();
		String date_from = month + "-01";
		String date_to = month + "-31";

		boolean isToday = false;
		if (month.equals(DateUtil.getDateStr("yyyy-MM"))) {
			date_to = DateUtil.today();
			isToday = true;
		}

		order_count.setDate_from(date_from);
		order_count.setDate_to(date_to);

		List<DataOrderCountDto> lists = orderCountDao.selectDayOrderCount(order_count);

		// 涉及到的销售
		Set<String> names = new HashSet<String>();
		for (DataOrderCountDto docd : lists) {
			names.add(docd.getCreate_user());
		}
		List<DataOrderCountDto> result = new ArrayList<DataOrderCountDto>();

		int end_day = isToday ? DateUtil.todayOfMonth() : DateUtil.getMaxDay(month);

		for (String name : names) {
			DataOrderCountDto docd = new DataOrderCountDto();
			docd.setCreate_user(name);
			List<Integer> data_order_cnt = new ArrayList<Integer>();
			List<Integer> data_people_cnt = new ArrayList<Integer>();

			for (int i = 1; i <= end_day; i++) {
				boolean isExists = false;
				for (int j = lists.size() - 1; j >= 0; j--) {
					DataOrderCountDto current = lists.get(j);
					if (current.getCreate_user().equals(name) && current.getDayofm() == i) {
						data_order_cnt.add(current.getOrder_cnt());
						data_people_cnt.add(current.getPeople_cnt());
						lists.remove(j);
						isExists = true;
						break;
					}
				}
				if (!isExists) {
					data_order_cnt.add(0);
					data_people_cnt.add(0);
				}
			}
			docd.setData_order_cnt(data_order_cnt);
			docd.setData_people_cnt(data_people_cnt);
			result.add(docd);
		}

		return result;
	}

	/**
	 * 按星期获取订单量
	 * 
	 * @param order_count
	 * @return
	 */
	private List<DataOrderCountDto> fetchWeekOrderCount(DataOrderCountDto order_count) {

		String year = order_count.getOption_year();

		String date_from = year + "-01-01";
		String date_to = year + "-12-31";

		order_count.setDate_from(date_from);
		order_count.setDate_to(date_to);

		List<DataOrderCountDto> lists = orderCountDao.selectWeekOrderCount(order_count);

		// 涉及到的销售
		Set<String> names = new HashSet<String>();
		for (DataOrderCountDto docd : lists) {
			names.add(docd.getCreate_user());
		}
		List<DataOrderCountDto> result = new ArrayList<DataOrderCountDto>();

		for (String name : names) {
			DataOrderCountDto docd = new DataOrderCountDto();
			docd.setCreate_user(name);
			List<Integer> data_order_cnt = new ArrayList<Integer>();
			List<Integer> data_people_cnt = new ArrayList<Integer>();

			for (int i = 1; i <= 7; i++) {
				boolean isExists = false;
				for (int j = lists.size() - 1; j >= 0; j--) {
					DataOrderCountDto current = lists.get(j);
					int currentDayOfw = current.getDayofw() == 0 ? 7 : current.getDayofw();
					if (current.getCreate_user().equals(name) && currentDayOfw == i) {
						data_order_cnt.add(current.getOrder_cnt());
						data_people_cnt.add(current.getPeople_cnt());
						lists.remove(j);
						isExists = true;
						break;
					}
				}
				if (!isExists) {
					data_order_cnt.add(0);
					data_people_cnt.add(0);
				}
			}
			docd.setData_order_cnt(data_order_cnt);
			docd.setData_people_cnt(data_people_cnt);

			result.add(docd);

		}

		return result;
	}

	@Autowired
	private CardDAO carddao;

	@Autowired
	private PayableDAO payableDao;

	@Autowired
	private ReceivableDAO receivableDao;

	@Override
	public DataFinanceSummaryDto fetchFinanceSummary() throws Exception {
		DataFinanceSummaryDto dfsd = new DataFinanceSummaryDto();

		// 获取现金
		BigDecimal cash = carddao.selectSumBalance();
		dfsd.setCash(cash);

		// 获取现金明细
		KeyValueDto kv = carddao.selectDetailBalance();
		dfsd.setPositive_cash(new BigDecimal(kv.getKey_key()));
		dfsd.setNegative_cash(new BigDecimal(kv.getValue_value()));

		// 获取应收款总额
		BigDecimal receivable = receivableDao.selectSumReceivable();
		dfsd.setReceivable(receivable);

		// 获取应收款明细(地区应收款)
		List<KeyValueDto> receivables = receivableDao.selectReceivableWithClient();
		dfsd.setAreaReceivable(receivables);

		// 获取应收款明细(销售应收款)
		List<KeyValueDto> sale_receivables = receivableDao.selectReceivableWithSales();
		dfsd.setSalesReceivable(sale_receivables);

		// 获取应付款总额
		BigDecimal payable = payableDao.selectSumPayable();
		dfsd.setPayable(payable);

		// 获取应付款明细
		List<KeyValueDto> payables = payableDao.selectPayableWithArea();
		dfsd.setAreaPayable(payables);

		return dfsd;
	}
}
