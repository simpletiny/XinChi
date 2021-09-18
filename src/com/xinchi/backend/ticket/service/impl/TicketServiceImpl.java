package com.xinchi.backend.ticket.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.ticket.dao.AirTicketChangeLogDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.service.TicketService;
import com.xinchi.bean.AirTicketChangeLogBean;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

	@Autowired
	private AirTicketChangeLogDAO changeLogDao;

	@Autowired
	private AirTicketNameListDAO nameListDao;

	@Autowired
	private AirTicketPayableDAO payableDao;

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;

	@Override
	public String changFlight(String json) {
		JSONObject obj = JSONObject.fromObject(json);

		String passenger_pks = obj.getString("passenger_pks");
		String change_reason = obj.getString("change_reason");
		BigDecimal cost = new BigDecimal(SimpletinyString.isEmpty(obj.getString("cost")) ? "0" : obj.getString("cost"));

		String comment = obj.getString("comment");

		String team_number = obj.getString("team_number");

		// 增加票务航变记录
		AirTicketChangeLogBean atcl = new AirTicketChangeLogBean();
		atcl.setChange_cost(cost);
		atcl.setChange_reason(change_reason);
		atcl.setComment(comment);

		String change_pk = changeLogDao.insert(atcl);

		String[] pks = passenger_pks.split(",");
		List<AirTicketNameListBean> names = nameListDao.selectByPks(pks);

		String captain = names.get(0).getName();
		String first_ticket_date = names.get(0).getFirst_ticket_date();
		// 更新票务名单状态
		for (AirTicketNameListBean name : names) {

			name.setStatus("C");
			name.setChange_pk(change_pk);

			nameListDao.update(name);
		}

		JSONArray arr = obj.getJSONArray("allot");

		BigDecimal sumMoney = BigDecimal.ZERO;
		for (int i = 0; i < arr.size(); i++) {
			JSONObject current = arr.getJSONObject(i);

			// 新增一条应付款记录
			String supplier_employee_pk = current.getString("ticket_source_pk");
			BigDecimal money = new BigDecimal(
					SimpletinyString.isEmpty(current.getString("money")) ? "0" : current.getString("money"));

			if (money.compareTo(BigDecimal.ZERO) != 0) {
				AirTicketPayableBean atp = new AirTicketPayableBean();

				atp.setSupplier_employee_pk(supplier_employee_pk);
				atp.setPaid(BigDecimal.ZERO);
				atp.setPayable_type(ResourcesConstants.Ticket_PAYABLE_TYPE_CHANGE);
				atp.setBudget_payable(money);
				atp.setBudget_balance(money);
				atp.setFinal_flg("N");

				atp.setComment(first_ticket_date + captain + pks.length + "人航变");

				payableDao.insert(atp);

				sumMoney = sumMoney.add(money);
			}

		}

		// 更新订单票务费用
		OrderDto saleOrder = orderDao.selectByTeamNumber(team_number);
		if (saleOrder.getStandard_flg().equals("Y")) {
			BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
			bso.setPk(saleOrder.getPk());
			bso.setAir_ticket_cost(saleOrder.getAir_ticket_cost().add(sumMoney));
			bsoDao.update(bso);
		} else {
			BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
			bnso.setPk(saleOrder.getPk());
			bnso.setAir_ticket_cost(saleOrder.getAir_ticket_cost().add(sumMoney));
			bnsoDao.update(bnso);
		}
		return SUCCESS;
	}

	@Override
	public AirTicketChangeLogBean searchFlightChangeLogByPassengerPk(String passenger_pk) {
		AirTicketNameListBean name = nameListDao.selectByPrimaryKey(passenger_pk);

		AirTicketChangeLogBean log = changeLogDao.selectByPrimaryKey(name.getChange_pk());
		return log;
	}

	@Override
	public List<AirTicketChangeLogBean> searchTicketChangeByPage(Page page) {
		return changeLogDao.selectByPage(page);
	}

}