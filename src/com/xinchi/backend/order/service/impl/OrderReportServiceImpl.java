package com.xinchi.backend.order.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.order.service.OrderReportService;
import com.xinchi.backend.payable.dao.PayableOrderDAO;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.bean.PayableOrderBean;
import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.tools.Page;

@Service
@Transactional
public class OrderReportServiceImpl implements OrderReportService {

	@Autowired
	private OrderReportDAO dao;

	@Override
	public List<OrderReportDto> selectOrderReportByPage(Page<OrderReportDto> page) {

		return dao.selectOrderReportByPage(page);
	}

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Autowired
	private AirTicketOrderDAO airTicketOrderDao;

	@Autowired
	private ReceivedService receivedService;

	@Override
	public String apporveTeamReport(String team_number) {
		// 检测是否可以确认单团。
		// 检测票务订单是否已经决算。
		List<AirTicketNameListBean> names = airTicketNameListDao.selectByTeamNumber(team_number);
		Set<String> order_numbers = new HashSet<>();
		for (AirTicketNameListBean name : names) {
			order_numbers.add(name.getOrder_number());
		}

		for (String order_number : order_numbers) {
			AirTicketOrderBean air_order = airTicketOrderDao.selectByOrderNumber(order_number);
			if (air_order.getFinal_flg().equals("N"))
				return "airnofinal";
		}

		// 检测是否还有未确认的收支
		// 收入
		List<ClientReceivedDetailBean> receiveds = receivedService.selectByTeamNumber(team_number);
		List<String> receivedTyps = Arrays.asList(ResourcesConstants.RECEIVED_TYPE_RECEIVED, ResourcesConstants.RECEIVED_TYPE_PAY,
				ResourcesConstants.RECEIVED_TYPE_SUM);
		for (ClientReceivedDetailBean received : receiveds) {
			if (receivedTyps.contains(received.getType()) && !received.getStatus().equals(ResourcesConstants.RECEIVED_STATUS_ENTER)) {
				return "existreceived";
			}
		}

		TeamReportBean tr = dao.selectTeamReportByTn(team_number);
		tr.setApproved("Y");
		dao.updateTeamReport(tr);
		return SUCCESS;
	}

	@Override
	public String rollBackTeamReport(String team_number) {
		TeamReportBean tr = dao.selectTeamReportByTn(team_number);
		tr.setApproved("N");
		dao.updateTeamReport(tr);
		return SUCCESS;
	}

	@Autowired
	private OrderDAO orderDao;

	@Override
	public String fillAriTicketCost(String team_number, BigDecimal air_ticket_cost) {
		OrderDto order = orderDao.selectByTeamNumber(team_number);

		SaleOrderBean sale_order = new SaleOrderBean();
		sale_order.setPk(order.getPk());
		sale_order.setAir_ticket_cost(air_ticket_cost);
		orderDao.update(sale_order);
		return SUCCESS;
	}

	@Override
	public OrderReportDto searchSumReport(OrderReportDto option) {

		return dao.selectSumReport(option);
	}

	@Override
	public TeamReportBean selectTeamReportByTeamNumber(String team_number) {
		return dao.selectTeamReportByTn(team_number);
	}

	@Override
	public String updateTeamReport(TeamReportBean bean) {
		dao.updateTeamReport(bean);

		return SUCCESS;
	}

	@Autowired
	private PayableOrderDAO payableOrderDao;

	@Override
	public String checkOrderReportCanBeApproved(String team_number) {
		OrderDto order = orderDao.selectByTeamNumber(team_number);

		// 检验销售订单是否决算
		if (!order.getConfirm_flg().equals("F")) {
			return "notfinalorder";
		}

		if (!order.getIndependent_flg().equals("A")) {
			// 检测产品是否操作
			if (order.getOperate_flg().startsWith("N")) {
				return "noproductinfo";
			}

			// 检测所有产品操作是否已经决算。
			List<PayableOrderBean> pos = payableOrderDao.selectByTeamNumber(team_number);
			for (PayableOrderBean po : pos) {
				if (po.getFinal_flg().equals("N")) {
					return "notfinaloperation";
				}
			}
		}

		// if (order.getAir_ticket_cost() == null) {
		// return "noairinfo";
		// }

		return SUCCESS;
	}
}
