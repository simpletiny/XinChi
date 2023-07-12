package com.xinchi.backend.receivable.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.receivable.dao.AirReceivedDAO;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivedDetailDto;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ReceivedServiceImpl implements ReceivedService {

	@Autowired
	private ReceivedDAO dao;

	@Autowired
	private ReceivableService receivableService;

	@Override
	public void insert(ClientReceivedDetailBean detail) {

		dao.insert(detail);
	}

	@Override
	public void insertWithPk(ClientReceivedDetailBean detail) {

		dao.insertWithPk(detail);
	}

	@Override
	public List<ClientReceivedDetailBean> getAllReceivedsByPage(Page<ClientReceivedDetailBean> page) {
		return dao.getAllByPage(page);
	}

	@Autowired
	private PayApprovalDAO payApprovalDao;

	@Override
	public String rollBackReceived(String received_pks) {
		String[] pks = received_pks.split(",");
		for (String pk : pks) {
			ClientReceivedDetailBean detail = dao.selectByPk(pk);
			if (detail.getType().equals(ResourcesConstants.RECEIVED_TYPE_SUM)
					|| detail.getType().equals(ResourcesConstants.RECEIVED_TYPE_STRIKE_OUT)
					|| detail.getType().equals(ResourcesConstants.RECEIVED_TYPE_STRIKE_IN)) {
				String related_pk = detail.getRelated_pk();
				List<ClientReceivedDetailBean> related_detail = dao.selectByRelatedPks(related_pk);
				if (related_detail != null && related_detail.size() > 0) {
					for (ClientReceivedDetailBean d : related_detail) {
						doRollBack(d);
					}
				}
			} else if (detail.getType().equals(ResourcesConstants.RECEIVED_TYPE_PAY)) {
				PayApprovalBean pa = payApprovalDao.selectByBackPk(detail.getRelated_pk());
				payApprovalDao.delete(pa.getPk());
				doRollBack(detail);
			} else if (detail.getType().equals(ResourcesConstants.RECEIVED_TYPE_FLY)) {
				PayApprovalBean pa = payApprovalDao.selectByBackPk(detail.getRelated_pk());
				payApprovalDao.delete(pa.getPk());
				dao.deleteByPk(detail.getPk());
				// doRollBack(detail);
			} else {
				doRollBack(detail);
			}
		}

		return "OK";
	}

	private void doRollBack(ClientReceivedDetailBean detail) {
		detail.setReceived(BigDecimal.ZERO.subtract(detail.getReceived()));
		receivableService.updateReceivableReceived(detail);
		dao.deleteByPk(detail.getPk());
	}

	@Override
	public List<ClientReceivedDetailBean> selectByRelatedPks(String related_pks) {
		return dao.selectByRelatedPks(related_pks);
	}

	@Override
	public void update(ClientReceivedDetailBean detail) {
		dao.update(detail);
	}

	@Override
	public ClientReceivedDetailBean selectByPk(String received_pk) {
		return dao.selectByPk(received_pk);
	}

	@Override
	public ClientReceivedDetailBean selectReceivedDetailByRelatedPk(String related_pk) {
		return dao.selectReceivedDetailByRelatedPk(related_pk);
	}

	@Override
	public List<ClientReceivedDetailBean> selectByParam(ClientReceivedDetailBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String applySum(ClientReceivedDetailBean detail, String allot_json) {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_SUM);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			String client_employee_pk = obj.getString("client_employee_pk");
			detail.setTeam_number(t);
			detail.setClient_employee_pk(client_employee_pk);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setReceived(new BigDecimal(r));
			}

			dao.insert(detail);
			receivableService.updateReceivableReceived(detail);
		}
		// 保存收入凭证
		String subFolder = detail.getReceived_time().substring(0, 4) + File.separator
				+ detail.getReceived_time().substring(5, 7);
		FileUtil.saveFile(detail.getVoucher_file(), FileFolder.CLIENT_RECEIVED_VOUCHER.value(), subFolder);

		return SUCCESS;
	}

	@Override
	public String applyRidTail(ClientReceivedDetailBean detail) {

		ReceivableBean receivable = receivableService.selectByTeamNumber(detail.getTeam_number());
		detail.setType(ResourcesConstants.RECEIVED_TYPE_TAIL);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
		detail.setReceived_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
		detail.setClient_employee_pk(receivable.getClient_employee_pk());

		dao.insert(detail);
		receivableService.updateReceivableReceived(detail);

		return SUCCESS;
	}

	@Override
	public String applyCollect(ClientReceivedDetailBean detail) {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_COLLECT);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		dao.insert(detail);
		receivableService.updateReceivableReceived(detail);

		// 保存收入凭证
		String subFolder = detail.getReceived_time().substring(0, 4) + File.separator
				+ detail.getReceived_time().substring(5, 7);
		FileUtil.saveFile(detail.getVoucher_file(), FileFolder.CLIENT_RECEIVED_VOUCHER.value(), subFolder);

		return SUCCESS;
	}

	@Autowired
	private OrderReportDAO orderReportDao;

	@Override
	public String applyTail98(ClientReceivedDetailBean detail) {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_TAIL98);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
		detail.setReceived_time(DateUtil.today());
		detail.setCollecter("98清尾");
		dao.insert(detail);
		receivableService.updateReceivableReceived(detail);

		// 更新单团核算信息
		// 生成team_report基础数据
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(detail.getTeam_number());
		tr.setDiscount_flg("Y");
		ClientReceivedDetailBean option = new ClientReceivedDetailBean();
		option.setTeam_number(detail.getTeam_number());
		List<ClientReceivedDetailBean> res = dao.selectByParam(option);
		BigDecimal discount_received = BigDecimal.ZERO;
		for (ClientReceivedDetailBean re : res) {
			if (!re.getType().equals(ResourcesConstants.RECEIVED_TYPE_TAIL98)
					&& !re.getType().equals(ResourcesConstants.RECEIVED_TYPE_FLY)) {
				discount_received = discount_received.add(re.getReceived());
			}
		}
		tr.setDiscount_receivable(discount_received);

		orderReportDao.updateTeamReport(tr);

		return SUCCESS;
	}

	@Override
	public String applyReceive(ClientReceivedDetailBean detail) {

		detail.setType(ResourcesConstants.RECEIVED_TYPE_RECEIVED);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
		String pk = DBCommonUtil.genPk();
		detail.setPk(pk);
		detail.setRelated_pk(pk);
		detail.setSum_received(detail.getReceived());
		detail.setAllot_received(detail.getAllot_received());

		dao.insertWithPk(detail);
		// 保存收入凭证
		String subFolder = detail.getReceived_time().substring(0, 4) + File.separator
				+ detail.getReceived_time().substring(5, 7);
		FileUtil.saveFile(detail.getVoucher_file(), FileFolder.CLIENT_RECEIVED_VOUCHER.value(), subFolder);
		receivableService.updateReceivableReceived(detail);

		return SUCCESS;
	}

	@Autowired
	private OrderDAO orderDao;

	@Override
	public String applyIfMorePay(ClientReceivedDetailBean detail, String allot_json) {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_PAY);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);

		detail.setReceived_time(DateUtil.getMinStr());
		detail.setAllot_received(detail.getAllot_received().negate());

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			String client_employee_pk = obj.getString("client_employee_pk");
			detail.setClient_employee_pk(client_employee_pk);
			detail.setTeam_number(t);
			detail.setPk(pks[i]);

			OrderDto order = orderDao.selectByTeamNumber(t);
			detail.setClient_employee_pk(order.getClient_employee_pk());

			if (!SimpletinyString.isEmpty(r)) {
				detail.setReceived(new BigDecimal(r).negate());
			}

			dao.insertWithPk(detail);
			receivableService.updateReceivableReceived(detail);
		}
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		// 生成支付审批数据
		PayApprovalBean pa = new PayApprovalBean();
		pa.setReceiver(detail.getReceiver());
		pa.setMoney(detail.getAllot_received().negate());
		pa.setItem(ResourcesConstants.PAY_TYPE_MORE_BACK);
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		pa.setReceiver_card_number(detail.getReceiver_card_number());
		pa.setReceiver_bank(detail.getReceiver_bank());

		pa.setRelated_pk(related_pk);

		pa.setComment(detail.getComment());
		pa.setApply_user(sessionBean.getUser_number());
		pa.setBack_pk(related_pk);
		pa.setApply_time(DateUtil.getTimeMillis());
		pa.setLimit_time(detail.getLimit_time());

		payApprovalDao.insert(pa);

		// 更新单团核算数据
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(detail.getTeam_number());
		if (null != tr && tr.getDiscount_flg().equals("Y")) {
			tr.setDiscount_receivable(tr.getDiscount_receivable().add(detail.getReceived()));
			orderReportDao.updateTeamReport(tr);
		}
		return SUCCESS;
	}

	@Override
	public String applyStrike(ClientReceivedDetailBean detail, String strike_out_json, String strike_in_json) {

		JSONArray out_array = JSONArray.fromObject(strike_out_json);

		String related_pk = DBCommonUtil.genPk();

		for (int i = 0; i < out_array.size(); i++) {
			ClientReceivedDetailBean current = new ClientReceivedDetailBean();
			current.setType(ResourcesConstants.RECEIVED_TYPE_STRIKE_OUT);
			current.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
			current.setReceived_time(DateUtil.getMinStr());
			current.setRelated_pk(related_pk);

			JSONObject obj = JSONObject.fromObject(out_array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			String client_employee_pk = obj.getString("client_employee_pk");
			current.setClient_employee_pk(client_employee_pk);
			current.setTeam_number(t);

			if (!SimpletinyString.isEmpty(r)) {
				current.setReceived(new BigDecimal(r).negate());
			}
			current.setComment(detail.getComment());
			dao.insert(current);
			receivableService.updateReceivableReceived(current);
		}
		JSONArray in_array = JSONArray.fromObject(strike_in_json);

		for (int i = 0; i < in_array.size(); i++) {
			ClientReceivedDetailBean current = new ClientReceivedDetailBean();
			current.setType(ResourcesConstants.RECEIVED_TYPE_STRIKE_IN);
			current.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
			current.setReceived_time(DateUtil.getMinStr());
			current.setRelated_pk(related_pk);

			JSONObject obj = JSONObject.fromObject(in_array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			String client_employee_pk = obj.getString("client_employee_pk");
			current.setClient_employee_pk(client_employee_pk);
			current.setTeam_number(t);

			if (!SimpletinyString.isEmpty(r)) {
				current.setReceived(new BigDecimal(r));
			}

			current.setComment(detail.getComment());

			dao.insert(current);
			receivableService.updateReceivableReceived(current);
		}

		return SUCCESS;
	}

	@Override
	public String applyFly(ClientReceivedDetailBean detail) {
		// 如果金额不相符
		OrderDto order = orderDao.selectByTeamNumber(detail.getTeam_number());
		if (order.getFy().compareTo(detail.getReceived()) != 0) {
			return "nomatch";
		}

		detail.setType(ResourcesConstants.RECEIVED_TYPE_FLY);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);

		detail.setReceived_time(DateUtil.getMinStr());
		detail.setReceived(detail.getReceived().negate());

		dao.insert(detail);
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		// 生成支付审批数据
		PayApprovalBean pa = new PayApprovalBean();
		pa.setReceiver(detail.getReceiver());
		pa.setMoney(detail.getReceived().negate());
		pa.setItem(ResourcesConstants.PAY_TYPE_FLY);
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		pa.setReceiver_card_number(detail.getReceiver_card_number());
		pa.setReceiver_bank(detail.getReceiver_bank());

		pa.setRelated_pk(related_pk);

		pa.setComment(detail.getTeam_number() + "订单fly");
		pa.setApply_user(sessionBean.getUser_number());
		pa.setBack_pk(related_pk);
		pa.setApply_time(DateUtil.getTimeMillis());
		pa.setLimit_time(detail.getLimit_time());

		payApprovalDao.insert(pa);

		return SUCCESS;
	}

	@Autowired
	private ReceivableDAO receivableDao;

	@Override
	public String checkIs98(String team_number) {
		OrderDto order = orderDao.selectByTeamNumber(team_number);
		// 98清尾不需要确认订单
		// if (order.getConfirm_flg().equals("N"))
		// return "not";

		// 非标订单不享受打折
		if (order.getStandard_flg().equals("N"))
			return "cant";

		if (DateUtil.compare(order.getConfirm_date(), "2021-09-01") != 2) {
			ClientReceivedDetailBean option = new ClientReceivedDetailBean();
			option.setTeam_number(team_number);
			List<ClientReceivedDetailBean> res = dao.selectByParam(option);
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(team_number);
			BigDecimal discount_receivable = (receivable.getFinal_flg().equals("Y")
					? receivable.getFinal_receivable().multiply(new BigDecimal(0.98))
					: receivable.getBudget_receivable().multiply(new BigDecimal(0.98)));
			// 98折应收精确到元，向下取整。如198.4就应该是198
			discount_receivable = discount_receivable.setScale(0, BigDecimal.ROUND_DOWN);

			BigDecimal discount_received = BigDecimal.ZERO;

			// boolean isConfirm = true;
			for (ClientReceivedDetailBean re : res) {
				if (!re.getType().equals(ResourcesConstants.RECEIVED_TYPE_TAIL98)) {

					if (re.getType().equals(ResourcesConstants.RECEIVED_TYPE_PAY)) {
						discount_received = discount_received.add(re.getReceived());
					} else if (!re.getType().equals(ResourcesConstants.RECEIVED_TYPE_FLY)) {
						// 修改为当天汇款享受98折
						// if (order.getConfirm_date().equals(re.getReceived_time().substring(0, 10))) {
						// discount_received = discount_received.add(re.getReceived());
						// }
						if (null != order.getConfirm_date()
								&& DateUtil.compare(order.getConfirm_date(), "2022-07-18") == 2) {
							discount_received = discount_received.add(re.getReceived());
						} else {
							// 如果订单未确认，则用订单创建时间计算
							String limitDate = DateUtil
									.addDate(order.getConfirm_flg().equals("Y") ? order.getConfirm_date()
											: DateUtil.fromUnixTime(order.getCreate_time(), DateUtil.YYYY_MM_DD), 1);
							if (DateUtil.compare(limitDate, re.getReceived_time().substring(0, 10)) < 2) {
								discount_received = discount_received.add(re.getReceived());
							}
						}
					}

					// 取消会计匹配判定
					// if (!re.getStatus().equals("E"))
					// isConfirm = false;
				}
			}

			if (discount_received.compareTo(discount_receivable) >= 0) {
				// if (!isConfirm) {
				// return "noconfirm";
				// } else {
				// return SUCCESS;
				// }

				return SUCCESS;

			} else {
				return "bad";
			}

		}
		return SUCCESS;
	}

	@Override
	public BigDecimal selectSumReceivedByTeamNumber(String team_number) {

		return dao.selectSumReceivedByTeamNumber(team_number);
	}

	@Override
	public String rejectRecived(String related_pks) {
		String[] rps = related_pks.split(ResourcesConstants.DELIMITER);

		for (String related_pk : rps) {
			List<ClientReceivedDetailBean> receiveds = dao.selectByRelatedPks(related_pk);

			for (ClientReceivedDetailBean crd : receiveds) {
				crd.setStatus("N");
				dao.update(crd);

				ClientReceivedDetailBean option = new ClientReceivedDetailBean();
				option.setTeam_number(crd.getTeam_number());
				option.setType(ResourcesConstants.RECEIVED_TYPE_TAIL98);
				List<ClientReceivedDetailBean> tails = dao.selectByParam(option);
				for (ClientReceivedDetailBean tail : tails) {
					tail.setStatus("N");
					dao.update(tail);
				}
			}
		}

		return SUCCESS;
	}

	@Autowired
	private PaidDAO paidDao;

	@Autowired
	private AirTicketPaidDetailDAO airTicketPaidDetailDao;

	@Autowired
	private AirReceivedDAO airReceivedDao;

	@Override
	public List<ReceivedDetailDto> searchAllAboutReceivedByRelatedPks(String related_pk, String from_where) {
		List<ReceivedDetailDto> results = new ArrayList<ReceivedDetailDto>();
		switch (from_where) {
		case "C":
			List<ClientReceivedDetailBean> c_details = dao.selectByRelatedPks(related_pk);
			for (ClientReceivedDetailBean detail : c_details) {
				ReceivedDetailDto result = new ReceivedDetailDto();
				result.copyFromClientReceived(detail);
				results.add(result);
			}
			break;
		case "D":
			List<SupplierPaidDetailBean> d_details = paidDao.selectSupplierPaidDetailByRelatedPk(related_pk);
			for (SupplierPaidDetailBean detail : d_details) {
				ReceivedDetailDto result = new ReceivedDetailDto();
				result.copyFromSupplierReceived(detail);
				results.add(result);
			}
			break;
		case "A":
			List<AirTicketPaidDetailBean> a_details = airTicketPaidDetailDao.selectByRelatedPk(related_pk);
			for (AirTicketPaidDetailBean detail : a_details) {
				ReceivedDetailDto result = new ReceivedDetailDto();
				result.copyFromAirSupplierReceived(detail);
				results.add(result);
			}
			break;
		case "AR":
			List<AirReceivedDetailBean> ar_details = airReceivedDao.selectByRelatedPk(related_pk);
			for (AirReceivedDetailBean detail : ar_details) {
				ReceivedDetailDto result = new ReceivedDetailDto();
				result.copyFromAirReceived(detail);
				results.add(result);
			}
			break;
		default:
			break;
		}

		return results;
	}

	@Override
	public List<ClientReceivedDetailBean> selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}
}
