package com.xinchi.backend.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.data.service.DataService;
import com.xinchi.bean.DataFinanceSummaryDto;
import com.xinchi.bean.DataOrderCountDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DataAction extends BaseAction {
	private static final long serialVersionUID = -7091347932051875839L;

	private DataOrderCountDto order_count;

	private List<DataOrderCountDto> order_datas;

	private List<String> xAxis;
	@Autowired
	private DataService service;

	// 获取订单量数据
	public String fetchOrderCountData() {
		order_datas = service.fetchOrderCountData(order_count);
		xAxis = new ArrayList<String>();
		if (order_count.getHorizontal().equals("month")) {
			xAxis = Arrays
					.asList(new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" });

		} else if (order_count.getHorizontal().equals("day")) {
			int last = DateUtil.getMaxDay(order_count.getOption_month());
			for (int i = 0; i < last; i++) {
				xAxis.add((i + 1) + "日");
			}
		} else if (order_count.getHorizontal().equals("week")) {
			xAxis = Arrays.asList(new String[] { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日", });
		}

		return SUCCESS;
	}

	private DataFinanceSummaryDto dfsd;

	/**
	 * 获取财务汇总数据
	 * 
	 * @return
	 */
	public String fetchFinanceSummary() throws Exception {
		dfsd = service.fetchFinanceSummary();
		return SUCCESS;
	}

	public DataOrderCountDto getOrder_count() {
		return order_count;
	}

	public List<DataOrderCountDto> getOrder_datas() {
		return order_datas;
	}

	public void setOrder_count(DataOrderCountDto order_count) {
		this.order_count = order_count;
	}

	public void setOrder_datas(List<DataOrderCountDto> order_datas) {
		this.order_datas = order_datas;
	}

	public List<String> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}

	public DataFinanceSummaryDto getDfsd() {
		return dfsd;
	}

	public void setDfsd(DataFinanceSummaryDto dfsd) {
		this.dfsd = dfsd;
	}
}
