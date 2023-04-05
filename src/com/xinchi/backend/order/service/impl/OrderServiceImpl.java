package com.xinchi.backend.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.FinalNonStandardOrderDAO;
import com.xinchi.backend.order.dao.FinalStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.FinalNonStandardOrderBean;
import com.xinchi.bean.FinalStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO dao;

	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;

	@Autowired
	private FinalStandardOrderDAO fsoDao;

	@Autowired
	private FinalNonStandardOrderDAO fnsoDao;

	@Autowired
	private ReceivableDAO receivableDao;

	@Override
	public List<OrderDto> selectTbcByPage(Page<OrderDto> page) {
		return dao.selectTbcByPage(page);
	}

	@Override
	public List<OrderDto> selectCByPage(Page<OrderDto> page) {

		return dao.selectCByPage(page);
	}

	@Override
	public OrderDto selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}

	@Override
	public List<OrderDto> selectFByPage(Page<OrderDto> page) {
		return dao.selectFByPage(page);
	}

	@Override
	public List<OrderDto> selectTbcByParam(OrderDto option) {
		return dao.selectTbcByParam(option);
	}

	@Override
	public OrderDto searchOrderByPk(String order_pk) {

		return dao.searchOrderByPk(order_pk);
	}

	@Override
	public String finalOrder(OrderDto order) {
		OrderDto budget_order = dao.searchOrderByPk(order.getPk());
		String standard_flg = budget_order.getStandard_flg();

		// 如果是标准订单
		if (standard_flg.equals("Y")) {
			FinalStandardOrderBean final_order = new FinalStandardOrderBean();
			final_order.setTeam_number(budget_order.getTeam_number());

			final_order.setClient_employee_pk(budget_order.getClient_employee_pk());

			final_order.setProduct_pk(budget_order.getProduct_pk());

			final_order.setDeparture_date(budget_order.getDeparture_date());

			final_order.setDays(budget_order.getDays());

			final_order.setAdult_count(budget_order.getAdult_count());

			final_order.setAdult_cost(budget_order.getAdult_cost());

			final_order.setSpecial_count(budget_order.getSpecial_count());

			final_order.setSpecial_cost(budget_order.getSpecial_cost());

			final_order.setFy(budget_order.getFy());

			final_order.setOther_cost(budget_order.getOther_cost());

			final_order.setOther_cost_comment(budget_order.getOther_cost_comment());

			final_order.setComment(budget_order.getComment());

			final_order.setIndependent_flg(budget_order.getIndependent_flg());

			final_order.setConfirm_date(budget_order.getConfirm_date());
			final_order.setConfirm_file(order.getConfirm_file());
			final_order.setVoucher_file(order.getVoucher_file());

			final_order.setAir_ticket_cost(budget_order.getAir_ticket_cost());

			final_order.setProduct_cost(budget_order.getProduct_cost());

			final_order.setPassenger_captain(budget_order.getPassenger_captain());

			// final_order.setOperate_flg(budget_order.getOperate_flg());
			final_order.setTreat_comment(budget_order.getTreat_comment());
			final_order.setReceivable_comment(budget_order.getReceivable_comment());

			// 航段信息
			// final_order.setTicket_number(budget_order.getTicket_number());
			// final_order.setStart_city(budget_order.getStart_city());
			// final_order.setStart_airport(budget_order.getStart_airport());
			// final_order.setEnd_city(budget_order.getEnd_city());
			// final_order.setEnd_airport(budget_order.getEnd_airport());
			// final_order.setOff_time(budget_order.getOff_time());
			// final_order.setLand_time(budget_order.getLand_time());
			// final_order.setNext_day(budget_order.getNext_day());

			final_order.setReceivable(order.getReceivable());
			final_order.setFinal_type(order.getFinal_type());
			final_order.setStatus(ResourcesConstants.FINAL_ORDER_STATUS_ING);
			final_order.setSale(budget_order.getCreate_user_number());

			switch (order.getFinal_type()) {
			case "1":
				break;
			case "2":
				final_order.setRaise_money(order.getRaise_money());
				final_order.setRaise_comment(order.getRaise_comment());
				final_order.setReduce_money(order.getReduce_money());
				final_order.setReduce_comment(order.getReduce_comment());
				break;
			case "3":
				final_order.setRaise_money(order.getRaise_money());
				final_order.setRaise_comment(order.getRaise_comment());
				final_order.setReduce_money(order.getReduce_money());
				final_order.setReduce_comment(order.getReduce_comment());
				final_order.setComplain_money(order.getComplain_money());
				final_order.setComplain_reason(order.getComplain_reason());
				final_order.setComplain_solution(order.getComplain_solution());
				break;
			default:
				break;
			}
			fsoDao.insert(final_order);
			// 更新预算订单
			BudgetStandardOrderBean bsOrder = bsoDao.selectByPrimaryKey(order.getPk());
			bsOrder.setConfirm_flg("F");
			bsoDao.update(bsOrder);

			// 更新应收款
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(budget_order.getTeam_number());
			receivable.setFinal_flg("Y");
			receivable.setFinal_receivable(final_order.getReceivable());
			receivable.setFinal_balance(receivable.getFinal_receivable().subtract(receivable.getReceived()));
			receivableDao.update(receivable);
		}
		// 非标准订单
		else {

			FinalNonStandardOrderBean final_order = new FinalNonStandardOrderBean();
			final_order.setTeam_number(budget_order.getTeam_number());

			final_order.setClient_employee_pk(budget_order.getClient_employee_pk());

			final_order.setProduct_name(budget_order.getProduct_name());

			final_order.setDeparture_date(budget_order.getDeparture_date());

			final_order.setDays(budget_order.getDays());

			final_order.setAdult_count(budget_order.getAdult_count());

			final_order.setAdult_cost(budget_order.getAdult_cost());

			final_order.setProduct_manager(budget_order.getProduct_manager_number());

			final_order.setSpecial_count(budget_order.getSpecial_count());

			final_order.setSpecial_cost(budget_order.getSpecial_cost());

			final_order.setFy(budget_order.getFy());

			final_order.setOther_cost(budget_order.getOther_cost());

			final_order.setOther_cost_comment(budget_order.getOther_cost_comment());

			final_order.setComment(budget_order.getComment());

			final_order.setIndependent_flg(budget_order.getIndependent_flg());

			final_order.setConfirm_date(budget_order.getConfirm_date());
			final_order.setConfirm_file(order.getConfirm_file());
			final_order.setVoucher_file(order.getVoucher_file());

			final_order.setAir_ticket_cost(budget_order.getAir_ticket_cost());

			final_order.setProduct_cost(budget_order.getProduct_cost());

			final_order.setPassenger_captain(budget_order.getPassenger_captain());

			// final_order.setOperate_flg(budget_order.getOperate_flg());
			final_order.setTreat_comment(budget_order.getTreat_comment());
			final_order.setReceivable_comment(budget_order.getReceivable_comment());

			// 航段信息
			// final_order.setTicket_number(budget_order.getTicket_number());
			// final_order.setStart_city(budget_order.getStart_city());
			// final_order.setStart_airport(budget_order.getStart_airport());
			// final_order.setEnd_city(budget_order.getEnd_city());
			// final_order.setEnd_airport(budget_order.getEnd_airport());
			// final_order.setOff_time(budget_order.getOff_time());
			// final_order.setLand_time(budget_order.getLand_time());
			// final_order.setNext_day(budget_order.getNext_day());

			final_order.setReceivable(order.getReceivable());
			final_order.setFinal_type(order.getFinal_type());
			final_order.setStatus(ResourcesConstants.FINAL_ORDER_STATUS_ING);
			final_order.setSale(budget_order.getCreate_user_number());

			switch (order.getFinal_type()) {
			case "1":
				break;
			case "2":
				final_order.setRaise_money(order.getRaise_money());
				final_order.setRaise_comment(order.getRaise_comment());
				final_order.setReduce_money(order.getReduce_money());
				final_order.setReduce_comment(order.getReduce_comment());
				break;
			case "3":
				final_order.setRaise_money(order.getRaise_money());
				final_order.setRaise_comment(order.getRaise_comment());
				final_order.setReduce_money(order.getReduce_money());
				final_order.setReduce_comment(order.getReduce_comment());
				final_order.setComplain_money(order.getComplain_money());
				final_order.setComplain_reason(order.getComplain_reason());
				final_order.setComplain_solution(order.getComplain_solution());
				break;
			default:
				break;
			}
			fnsoDao.insert(final_order);
			// 更新预算订单
			BudgetNonStandardOrderBean bnsOrder = bnsoDao.selectByPrimaryKey(order.getPk());
			bnsOrder.setConfirm_flg("F");
			bnsoDao.update(bnsOrder);

			// 更新应收款
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(budget_order.getTeam_number());
			receivable.setFinal_flg("Y");
			receivable.setFinal_receivable(final_order.getReceivable());
			receivable.setFinal_balance(receivable.getFinal_receivable().subtract(receivable.getReceived()));
			receivableDao.update(receivable);
		}

		// 保存决算单文件
		if (!SimpletinyString.isEmpty(order.getConfirm_file())) {
			FileUtil.saveFile(order.getConfirm_file(), FileFolder.CLIENT_FINAL.value(), budget_order.getTeam_number());
		}
		// 保存凭证文件
		if (!SimpletinyString.isEmpty(order.getVoucher_file())) {
			FileUtil.saveFile(order.getVoucher_file(), FileFolder.CLIENT_FINAL_VOUCHER.value(),
					budget_order.getTeam_number());
		}

		return SUCCESS;
	}

	@Autowired
	private OrderReportDAO orderReportDao;

	@Override
	public String rollBackFinalOrder(String order_pk, String standard_flg) {

		// TODO 打回需要处理应收款

		if (standard_flg.equals("N")) {
			FinalNonStandardOrderBean final_order = fnsoDao.selectByPrimaryKey(order_pk);
			// 判断单团核算单是否已经审核，如果审核则不允许打回。
			TeamReportBean tr = orderReportDao.selectTeamReportByTn(final_order.getTeam_number());
			if (null != tr && tr.getApproved().equals("Y")) {
				return "approved";
			}

			// 更新预算订单状态
			BudgetNonStandardOrderBean budget_order = bnsoDao.selectByTeamNumber(final_order.getTeam_number());
			budget_order.setConfirm_flg("Y");
			bnsoDao.update(budget_order);

			// 更新应付款
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(final_order.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setFinal_receivable(BigDecimal.ZERO);
			receivable.setFinal_balance(BigDecimal.ZERO);
			receivableDao.update(receivable);
			// 删除决算单
			fnsoDao.delete(order_pk);

			// 删除相关文件
			String confirm_file = final_order.getConfirm_file();
			String voucher_file = final_order.getVoucher_file();
			String team_number = final_order.getTeam_number();

			FileUtil.deleteFile(confirm_file, FileFolder.CLIENT_FINAL.value(), team_number);
			FileUtil.deleteFile(voucher_file, FileFolder.CLIENT_FINAL_VOUCHER.value(), team_number);

		} else if (standard_flg.equals("Y")) {
			FinalStandardOrderBean final_order = fsoDao.selectByPrimaryKey(order_pk);

			// 判断单团核算单是否已经审核，如果审核则不允许打回。
			TeamReportBean tr = orderReportDao.selectTeamReportByTn(final_order.getTeam_number());
			if (null != tr && tr.getApproved().equals("Y")) {
				return "approved";
			}

			// 更新预算订单状态
			BudgetStandardOrderBean budget_order = bsoDao.selectByTeamNumber(final_order.getTeam_number());
			budget_order.setConfirm_flg("Y");
			bsoDao.update(budget_order);

			// 更新应付款
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(final_order.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setFinal_receivable(BigDecimal.ZERO);
			receivable.setFinal_balance(BigDecimal.ZERO);
			receivableDao.update(receivable);
			// 删除决算单
			fsoDao.delete(order_pk);

			// 删除相关文件
			String confirm_file = final_order.getConfirm_file();
			String voucher_file = final_order.getVoucher_file();
			String team_number = final_order.getTeam_number();

			FileUtil.deleteFile(confirm_file, FileFolder.CLIENT_FINAL.value(), team_number);
			FileUtil.deleteFile(voucher_file, FileFolder.CLIENT_FINAL_VOUCHER.value(), team_number);
		}

		return SUCCESS;
	}

	@Autowired
	private ReceivedDAO receivedDao;

	@Override
	public String cancelOrder(OrderDto order) {
		OrderDto budget_order = dao.searchOrderByPk(order.getPk());
		String standard_flg = budget_order.getStandard_flg();

		// 如果是标准订单
		if (standard_flg.equals("Y")) {
			FinalStandardOrderBean final_order = new FinalStandardOrderBean();
			final_order.setTeam_number(budget_order.getTeam_number());

			final_order.setClient_employee_pk(budget_order.getClient_employee_pk());

			final_order.setProduct_pk(budget_order.getProduct_pk());

			final_order.setDeparture_date(budget_order.getDeparture_date());

			final_order.setDays(budget_order.getDays());

			final_order.setAdult_count(budget_order.getAdult_count());

			final_order.setAdult_cost(budget_order.getAdult_cost());

			final_order.setSpecial_count(budget_order.getSpecial_count());

			final_order.setSpecial_cost(budget_order.getSpecial_cost());

			final_order.setFy(budget_order.getFy());

			final_order.setOther_cost(budget_order.getOther_cost());

			final_order.setOther_cost_comment(budget_order.getOther_cost_comment());

			final_order.setComment(budget_order.getComment());

			final_order.setIndependent_flg(budget_order.getIndependent_flg());

			final_order.setConfirm_date(budget_order.getConfirm_date());
			final_order.setConfirm_file(order.getConfirm_file());
			final_order.setVoucher_file(order.getVoucher_file());

			final_order.setAir_ticket_cost(budget_order.getAir_ticket_cost());

			final_order.setProduct_cost(budget_order.getProduct_cost());

			final_order.setPassenger_captain(budget_order.getPassenger_captain());

			// final_order.setOperate_flg(budget_order.getOperate_flg());
			final_order.setTreat_comment(budget_order.getTreat_comment());
			final_order.setReceivable_comment(order.getReceivable_comment());

			// 航段信息
			// final_order.setTicket_number(budget_order.getTicket_number());
			// final_order.setStart_city(budget_order.getStart_city());
			// final_order.setStart_airport(budget_order.getStart_airport());
			// final_order.setEnd_city(budget_order.getEnd_city());
			// final_order.setEnd_airport(budget_order.getEnd_airport());
			// final_order.setOff_time(budget_order.getOff_time());
			// final_order.setLand_time(budget_order.getLand_time());
			// final_order.setNext_day(budget_order.getNext_day());

			final_order.setReceivable(order.getReceivable());
			final_order.setFinal_type("4");
			final_order.setStatus(ResourcesConstants.FINAL_ORDER_STATUS_ING);
			final_order.setSale(budget_order.getCreate_user_number());
			fsoDao.insert(final_order);

			// 更新预算订单
			BudgetStandardOrderBean bsOrder = bsoDao.selectByPrimaryKey(order.getPk());
			bsOrder.setConfirm_flg("F");
			bsoDao.update(bsOrder);

		}
		// 非标准订单
		else {
			FinalNonStandardOrderBean final_order = new FinalNonStandardOrderBean();
			final_order.setTeam_number(budget_order.getTeam_number());

			final_order.setClient_employee_pk(budget_order.getClient_employee_pk());

			final_order.setProduct_name(budget_order.getProduct_name());

			final_order.setDeparture_date(budget_order.getDeparture_date());

			final_order.setDays(budget_order.getDays());

			final_order.setAdult_count(budget_order.getAdult_count());

			final_order.setAdult_cost(budget_order.getAdult_cost());

			final_order.setSpecial_count(budget_order.getSpecial_count());

			final_order.setSpecial_cost(budget_order.getSpecial_cost());

			final_order.setFy(budget_order.getFy());

			final_order.setOther_cost(budget_order.getOther_cost());

			final_order.setOther_cost_comment(budget_order.getOther_cost_comment());

			final_order.setComment(budget_order.getComment());

			final_order.setIndependent_flg(budget_order.getIndependent_flg());

			final_order.setConfirm_date(budget_order.getConfirm_date());
			final_order.setConfirm_file(order.getConfirm_file());
			final_order.setVoucher_file(order.getVoucher_file());

			final_order.setAir_ticket_cost(budget_order.getAir_ticket_cost());

			final_order.setProduct_cost(budget_order.getProduct_cost());

			final_order.setPassenger_captain(budget_order.getPassenger_captain());

			// final_order.setOperate_flg(budget_order.getOperate_flg());
			final_order.setTreat_comment(budget_order.getTreat_comment());
			final_order.setReceivable_comment(order.getReceivable_comment());

			// 航段信息
			// final_order.setTicket_number(budget_order.getTicket_number());
			// final_order.setStart_city(budget_order.getStart_city());
			// final_order.setStart_airport(budget_order.getStart_airport());
			// final_order.setEnd_city(budget_order.getEnd_city());
			// final_order.setEnd_airport(budget_order.getEnd_airport());
			// final_order.setOff_time(budget_order.getOff_time());
			// final_order.setLand_time(budget_order.getLand_time());
			// final_order.setNext_day(budget_order.getNext_day());

			final_order.setReceivable(order.getReceivable());
			final_order.setFinal_type("4");
			final_order.setStatus(ResourcesConstants.FINAL_ORDER_STATUS_ING);
			final_order.setSale(budget_order.getCreate_user_number());

			fnsoDao.insert(final_order);
			// 更新预算订单
			BudgetNonStandardOrderBean bnsOrder = bnsoDao.selectByPrimaryKey(order.getPk());
			bnsOrder.setConfirm_flg("F");
			bnsoDao.update(bnsOrder);

		}

		// 保存决算单文件
		if (!SimpletinyString.isEmpty(order.getConfirm_file())) {
			FileUtil.saveFile(order.getConfirm_file(), FileFolder.CLIENT_FINAL.value(), budget_order.getTeam_number());
		}
		// 保存凭证文件
		if (!SimpletinyString.isEmpty(order.getVoucher_file())) {
			FileUtil.saveFile(order.getVoucher_file(), FileFolder.CLIENT_FINAL_VOUCHER.value(),
					budget_order.getTeam_number());
		}

		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(budget_order.getTeam_number());
		receivable.setFinal_flg("Y");
		receivable.setFinal_receivable(order.getReceivable());

		// 如果存在单团核算数据，则删除
		// 不删除单团核算数据，会影响到应收款中的退返和决算打回
		// orderReportDao.deleteByTeamNumber(budget_order.getTeam_number());

		// 如果订单存在98清尾，则打回
		ClientReceivedDetailBean option = new ClientReceivedDetailBean();
		option.setTeam_number(budget_order.getTeam_number());

		List<ClientReceivedDetailBean> res = receivedDao.selectByParam(option);
		// 在订单所有的收入记录中查找98清尾
		for (ClientReceivedDetailBean re : res) {
			if (re.getType().equals(ResourcesConstants.RECEIVED_TYPE_TAIL98)) {
				// 应收款已收减去98清尾的金额
				receivable.setReceived(receivable.getReceived().subtract(re.getReceived()));
				// 删除98清尾
				receivedDao.deleteByPk(re.getPk());
			}
		}

		receivable.setFinal_balance(receivable.getFinal_receivable().subtract(receivable.getReceived()));
		receivableDao.update(receivable);
		return SUCCESS;
	}

	@Override
	public List<SaleScoreDto> searchSaleScoreByPage(Page<SaleScoreDto> page) {
		return dao.searchSaleScore(page);
	}

	@Override
	public List<SaleScoreDto> searchBackMoneyScoreByPage(Page<SaleScoreDto> page) {
		return dao.searchBackMoneyScoreByPage(page);
	}

	@Override
	public List<SaleScoreDto> searchSaleScoreByParam(SaleScoreDto ssd) {
		return dao.searchSaleScoreByParam(ssd);
	}

	@Override
	public List<OrderDto> selectByParam(OrderDto order) {
		return dao.selectByParam(order);
	}

	@Override
	public List<OrderDto> selectConfirmingOrders() {
		OrderDto orderOption = new OrderDto();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			orderOption.setCreate_user(sessionBean.getUser_number());
		}

		return dao.selectConfirmingOrders(orderOption);
	}

	@Override
	public String confirmNameList(String team_number) {
		OrderDto order = dao.selectByTeamNumber(team_number);
		if (order.getStandard_flg().equals("Y")) {
			BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
			bso.setPk(order.getPk());

			int old = Integer.valueOf(order.getName_confirm_status());
			// // 尾款不为0不能进行二次名单确认
			// if (old == 4) {
			//
			// }

			if (old < 5) {
				bso.setName_confirm_status(String.valueOf(old + 1));
				bsoDao.update(bso);
			}
		} else {
			BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
			bnso.setPk(order.getPk());
			int old = Integer.valueOf(order.getName_confirm_status());
			if (old < 5) {
				bnso.setName_confirm_status(String.valueOf(old + 1));
				bnsoDao.update(bnso);
			}
		}
		return SUCCESS;
	}

	@Override
	public List<OrderDto> selectByTeamNumbers(List<String> team_numbers) {

		return dao.selectByTeamNumbers(team_numbers);
	}

	@Override
	public List<OrderDto> selectWithProductByParam(OrderDto order) {
		return dao.selectWithProductByParam(order);
	}

	@Override
	public String rollBackConfirmNameList(String team_number) {
		OrderDto order = dao.selectByTeamNumber(team_number);
		if (order.getStandard_flg().equals("Y")) {
			BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
			bso.setPk(order.getPk());

			int old = Integer.valueOf(order.getName_confirm_status());
			bso.setName_confirm_status(String.valueOf(old - 1));

			bsoDao.update(bso);
		} else {
			BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
			bnso.setPk(order.getPk());
			int old = Integer.valueOf(order.getName_confirm_status());
			bnso.setName_confirm_status(String.valueOf(old - 1));
			bnsoDao.update(bnso);
		}
		return SUCCESS;
	}

	@Override
	public List<SaleScoreDto> search3MonthScoreByUserNumber(String user_number) {

		return dao.search3MonthScoreByUserNumber(user_number);
	}

	@Autowired
	private ReceivedService receivedService;

	@Override
	public List<OrderDto> searchOrderByReceivedRelatedPk(String related_pk) {
		List<ClientReceivedDetailBean> receiveds = receivedService.selectByRelatedPks(related_pk);

		if (null != receiveds && receiveds.size() > 0) {
			List<String> team_numbers = new ArrayList<String>();
			for (ClientReceivedDetailBean crd : receiveds) {
				team_numbers.add(crd.getTeam_number());
			}
			List<OrderDto> orders = dao.selectByTeamNumbers(team_numbers);
			return orders;
		}

		return null;
	}

	@Autowired
	private NumberService numberService;

	@Autowired
	private ProductDAO productDao;

	@Override
	public String createReceivable(String order_pk) {
		OrderDto order = dao.searchOrderByPk(order_pk);
		if (order.getReceivable() == null || order.getReceivable().compareTo(BigDecimal.ZERO) == 0) {
			return "nomoney";
		}

		if (order.getReceivable_first_flg().equals("Y")) {
			return "already";
		}

		// 生成团号和应付款
		String team_number = numberService.generateTeamNumber();
		// 生成应收款
		ReceivableBean receivable = new ReceivableBean();
		receivable.setTeam_number(team_number);
		receivable.setFinal_flg("N");
		receivable.setClient_employee_pk(order.getClient_employee_pk());
		String departureDate = order.getDeparture_date();
		Integer days = order.getDays();
		receivable.setDeparture_date(departureDate);
		if (!SimpletinyString.isEmpty(departureDate) && days != null && days != 0) {
			String returnDate = DateUtil.addDate(departureDate, days - 1);
			receivable.setReturn_date(returnDate);
		}

		if (order.getStandard_flg().equals("Y")) {
			ProductBean product = productDao.selectByPrimaryKey(order.getProduct_pk());
			receivable.setProduct(product.getName());
		} else {
			receivable.setProduct(order.getProduct_name());
		}

		if (order.getAdult_count() != null) {
			int people_count = order.getAdult_count()
					+ (order.getSpecial_count() == null ? 0 : order.getSpecial_count());
			receivable.setPeople_count(people_count);
		}

		receivable.setBudget_receivable(order.getReceivable());

		receivable.setBudget_balance(order.getReceivable());
		receivable.setReceived(BigDecimal.ZERO);
		receivable.setSales(order.getCreate_user_number());
		receivable.setCreate_user(order.getCreate_user_number());

		receivableDao.insert(receivable);

		// 更新订单是否先生成团款标识
		if (order.getStandard_flg().equals("Y")) {
			BudgetStandardOrderBean bso = bsoDao.selectByPrimaryKey(order_pk);
			bso.setTeam_number(team_number);
			bso.setReceivable_first_flg("Y");
			bsoDao.update(bso);
		} else {
			BudgetNonStandardOrderBean bnso = bnsoDao.selectByPrimaryKey(order_pk);
			bnso.setTeam_number(team_number);
			bnso.setReceivable_first_flg("Y");
			bnsoDao.update(bnso);
		}

		// 生成team_report基础数据
		TeamReportBean tr = new TeamReportBean();
		tr.setTeam_number(team_number);
		orderReportDao.insert(tr);

		return SUCCESS;
	}
}
