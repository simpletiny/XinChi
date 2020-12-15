package com.xinchi.backend.payable.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.AccPaidDAO;
import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.payable.service.AirTicketPayableService;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.PassengerTicketInfoDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class AirTicketPayableServiceImpl implements AirTicketPayableService {

	@Autowired
	private AirTicketPayableDAO dao;

	@Override
	public void insert(AirTicketPayableBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketPayableBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketPayableBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketPayableBean> selectByParam(AirTicketPayableBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirTicketPayableBean> selectByPage(Page<AirTicketPayableBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketPayableBean> selectByPks(List<String> pks) {
		return dao.selectByPks(pks);
	}

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public List<AirTicketNameListBean> searchPayablePassengersByPayablePk(String payable_pk) {
		return airTicketNameListDao.selectByPayablePk(payable_pk);
	}

	@Autowired
	private PassengerTicketInfoDAO ptiDao;

	@Override
	public List<PassengerTicketInfoBean> searchTicketInfoByPayablePk(String payable_pk) {
		return ptiDao.selectByPayablePk(payable_pk);
	}

	@Autowired
	private AirTicketPaidDetailDAO airTicketPaidDetailDao;

	@Autowired
	private CardDAO cardDao;

	@Autowired
	private NumberService numberService;

	@Autowired
	private PaymentDetailService paymentDetailService;
	@Autowired
	private AccPaidDAO accPaidDao;

	@Override
	public String payAirTicket(String paidJson, String payableJson, BigDecimal allot_money) {
		UserSessionBean user = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String related_pk = DBCommonUtil.genPk();

		JSONArray array = JSONArray.fromObject(paidJson);
		String concat_voucher_number = "";
		String msg = SUCCESS;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String account = obj.getString("account");
			String time = obj.getString("time");
			String receiver = obj.getString("receiver");
			BigDecimal money = new BigDecimal(obj.getString("money"));

			CardBean card = cardDao.getCardByAccount(account);
			BigDecimal balance = card.getBalance().subtract(money);

			String voucher_file_name = obj.getString("voucherFile");

			String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
					ResourcesConstants.PAY_TYPE_PIAOWU, DateUtil.getDateStr(DateUtil.YYYYMMDD));

			concat_voucher_number += voucher_number + ",";

			PaymentDetailBean detail = new PaymentDetailBean();
			detail.setVoucher_number(voucher_number);
			detail.setAccount(account);
			detail.setTime(time);
			detail.setReceiver(receiver);
			detail.setMoney(money);
			detail.setBalance(balance);
			detail.setType("支出");
			detail.setComment(receiver + ",凭证号：" + voucher_number);
			detail.setVoucher_file_name(voucher_file_name);

			msg = paymentDetailService.insert(detail);

			if (!msg.equals(SUCCESS)) {
				return msg;
			}

			// 生成待支付数据并直接写入为已支付状态
			WaitingForPaidBean waiting = new WaitingForPaidBean();
			waiting.setPay_number(voucher_number);

			waiting.setItem(ResourcesConstants.PAY_TYPE_PIAOWU);
			waiting.setReceiver(receiver);
			waiting.setMoney(money);
			waiting.setApply_user(user.getUser_number());
			waiting.setApproval_user(user.getUser_number());
			waiting.setRelated_pk(related_pk);
			waiting.setStatus(ResourcesConstants.PAY_STATUS_YES);
			waiting.setPay_user(user.getUser_number());

			accPaidDao.insert(waiting);
		}

		concat_voucher_number = concat_voucher_number.substring(0, concat_voucher_number.length() - 1);
		// 生成申请信息，并更新应付款“已付款”内容
		JSONArray payableArray = JSONArray.fromObject(payableJson);
		BigDecimal sumPaid = BigDecimal.ZERO;
		for (int i = 0; i < payableArray.size(); i++) {
			JSONObject obj = JSONObject.fromObject(payableArray.get(i));

			String payable_pk = obj.getString("payable_pk");
			String this_paid = obj.getString("this_paid");
			AirTicketPayableBean airTicketPayable = dao.selectByPrimaryKey(payable_pk);

			String supplier_employee_pk = airTicketPayable.getSupplier_employee_pk();
			String base_pk = airTicketPayable.getPk();
			String PNR = airTicketPayable.getPNR();

			AirTicketPaidDetailBean currentDetail = new AirTicketPaidDetailBean();

			currentDetail.setAllot_money(allot_money);
			currentDetail.setSupplier_employee_pk(supplier_employee_pk);
			currentDetail.setBase_pk(base_pk);
			currentDetail.setPNR(PNR);
			currentDetail.setType(ResourcesConstants.PAID_TYPE_PAID);
			currentDetail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			currentDetail.setRelated_pk(related_pk);

			if (!SimpletinyString.isEmpty(this_paid)) {
				sumPaid = new BigDecimal(this_paid);
			}
			currentDetail.setTime(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
			currentDetail.setMoney(sumPaid);
			currentDetail.setConfirm_time(DateUtil.getTimeMillis());
			currentDetail.setApprove_user(user.getUser_number());
			currentDetail.setVoucher_number(concat_voucher_number);
			airTicketPaidDetailDao.insert(currentDetail);

			airTicketPayable.setPaid(airTicketPayable.getPaid().add(sumPaid));
			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().subtract(sumPaid));
			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().subtract(sumPaid));
			}
			dao.update(airTicketPayable);
		}

		return SUCCESS;
	}

}