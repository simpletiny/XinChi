package com.xinchi.common;

import java.util.Date;
import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.IncomingCallBean;
import com.xinchi.bean.MobileTouchBean;
import com.xinchi.bean.ProductBean;

@Aspect
@Service
@Transactional
public class AopUpdateClientRelation {

	private static String SUCCESS = "success";

	@Autowired
	private ClientRelationDAO clientRelationDao;

	@Autowired
	private EmployeeDAO employeeDao;

	@Autowired
	private ClientEmployeeUserDAO clientEmployeeUserDao;

	@Autowired
	private ProductDAO productDao;

	// *********** 新建客户 start***********
	@Pointcut("(execution(* com.xinchi.backend.client.service.EmployeeService.createEmployee(..)) && args(employee))")
	public void insertEmployeeCut(ClientEmployeeBean employee) {

	}

	// 成功新建一个客户后
	@AfterReturning(value = "insertEmployeeCut(employee)", returning = "res")
	public void afterInsertEmployee(ClientEmployeeBean employee, String res) {
		if (res.equals(SUCCESS)) {
			ClientRelationBean crb = new ClientRelationBean();
			crb.setClient_employee_name(employee.getName());

			crb.setNick_name(employee.getNick_name());
			crb.setClient_employee_pk(employee.getPk());
			crb.setDelete_flg("N");
			crb.setRelation_level(employee.getRelation_level());
			crb.setBack_level(employee.getBack_level());
			crb.setMarket_level(employee.getMarket_level());
			crb.setMonth_order_count(0);
			crb.setYear_order_count(0);
			crb.setSales(employee.getSales());
			crb.setSales_name(employee.getSales_name());

			clientRelationDao.insert(crb);
		}
	}
	// *********** 新建客户 end***********

	// *********** 更新客户 start***********
	@Pointcut("(execution(* com.xinchi.backend.client.dao.EmployeeDAO.update(..)) && args(employee))")
	public void updateEmployeeCut(ClientEmployeeBean employee) {

	}

	@AfterReturning(value = "updateEmployeeCut(employee)", returning = "res")
	public void afterUpdateEmployee(ClientEmployeeBean employee, Object res) {
		ClientEmployeeBean e = employeeDao.selectByPrimaryKey(employee.getPk());

		ClientRelationBean crb = clientRelationDao.selectByEmployeePk(e.getPk());

		crb.setClient_employee_name(e.getName());
		crb.setNick_name(e.getNick_name());
		crb.setDelete_flg(e.getDelete_flg());
		crb.setRelation_level(e.getRelation_level());
		crb.setBack_level(e.getBack_level());
		crb.setMarket_level(e.getMarket_level());

		List<ClientEmployeeUserBean> ceubs = clientEmployeeUserDao.selectByEmployeePk(e.getPk());
		String sales = "";
		String sales_name = "";
		for (ClientEmployeeUserBean ceub : ceubs) {
			sales += ceub.getUser_pk() + ",";
			sales_name += ceub.getUser_name() + ",";
		}
		if (!sales.equals("")) {
			sales = sales.substring(0, sales.length() - 1);
		}
		if (!sales_name.equals("")) {
			sales_name = sales_name.substring(0, sales_name.length() - 1);
		}

		crb.setSales(sales);
		crb.setSales_name(sales_name);

		clientRelationDao.update(crb);
	}
	// *********** 更新客户 end***********

	// *********** 删除客户 start***********
	@Pointcut("(execution(* com.xinchi.backend.client.dao.EmployeeDAO.delete(..)) && args(pk))")
	public void deleteEmployeeCut(String pk) {

	}

	@AfterReturning(value = "deleteEmployeeCut(pk)", returning = "res")
	public void afterDeleteEmployee(String pk, Object res) {

		ClientRelationBean crb = clientRelationDao.selectByEmployeePk(pk);
		if (null != crb) {
			clientRelationDao.delete(crb.getPk());
		}
	}
	// *********** 删除客户 end***********

	// *********** 登门拜访 start***********
	@Pointcut("(execution(* com.xinchi.backend.client.dao.ClientVisitDAO.insert(..)) && args(visit))")
	public void insertVisitCut(ClientVisitBean visit) {

	}

	@AfterReturning(value = "insertVisitCut(visit)", returning = "res")
	public void afterInsertVisit(ClientVisitBean visit, Object res) {

		ClientRelationBean crb = clientRelationDao.selectByEmployeePk(visit.getClient_employee_pk());

		Date old = DateUtil.castStr2Date(crb.getConnect_date());
		Date now = DateUtil.castStr2Date(visit.getDate());

		if (now.after(old)) {
			crb.setConnect_date(visit.getDate());
			crb.setType("VISIT");
			crb.setExtra_info(visit.getTarget());

			clientRelationDao.update(crb);
		}
	}
	// *********** 登门拜访 end***********

	// *********** 主动电联 start***********
	@Pointcut("(execution(* com.xinchi.backend.client.dao.MobileTouchDAO.insert(..)) && args(touch))")
	public void insertMobileTouchCut(MobileTouchBean touch) {

	}

	@AfterReturning(value = "insertMobileTouchCut(touch)", returning = "res")
	public void afterInsertMobileTouch(MobileTouchBean touch, Object res) {

		ClientRelationBean crb = clientRelationDao.selectByEmployeePk(touch.getClient_employee_pk());

		Date old = DateUtil.castStr2Date(crb.getConnect_date());
		Date now = DateUtil.castStr2Date(touch.getDate());

		if (now.after(old)) {
			crb.setConnect_date(touch.getDate());
			crb.setType("TOUCH");
			crb.setExtra_info(touch.getTouch_target());

			clientRelationDao.update(crb);
		}
	}
	// *********** 主动电联 end***********

	// *********** 被动咨询 start***********
	@Pointcut("(execution(* com.xinchi.backend.client.dao.IncomingCallDAO.insert(..)) && args(call))")
	public void insertIncomingCallCut(IncomingCallBean call) {

	}

	@AfterReturning(value = "insertIncomingCallCut(call)", returning = "res")
	public void afterInsertIncomingCall(IncomingCallBean call, Object res) {

		ClientRelationBean crb = clientRelationDao.selectByEmployeePk(call.getClient_employee_pk());

		Date old = DateUtil.castStr2Date(crb.getConnect_date());
		Date now = DateUtil.castStr2Date(call.getDate());

		if (now.after(old)) {
			crb.setConnect_date(call.getDate());
			crb.setType("VISIT");
			crb.setExtra_info(call.getType());

			clientRelationDao.update(crb);
		}
	}
	// *********** 被动咨询 end***********

	// *********** 标准订单确认 start***********
	@Pointcut("(execution(* com.xinchi.backend.order.dao.BudgetStandardOrderDAO.update(..)) && args(order))")
	public void confirmStandardOrderCut(BudgetStandardOrderBean order) {

	}

	@AfterReturning(value = "confirmStandardOrderCut(order)", returning = "res")
	public void afterConfirmStandardOrder(BudgetStandardOrderBean order, Object res) {

		if (null != order.getConfirm_flg() && order.getConfirm_flg().equals("Y")) {
			ClientRelationBean crb = clientRelationDao.selectByEmployeePk(order.getClient_employee_pk());

			Date old = DateUtil.castStr2Date(crb.getConnect_date());
			Date now = DateUtil.castStr2Date(order.getConfirm_date());

			if (now.after(old)) {
				crb.setConnect_date(order.getConfirm_date());
				crb.setType("ORDER");
				ProductBean product = productDao.selectByPrimaryKey(order.getProduct_pk());
				int peopleCnt = (null == order.getSpecial_count() ? 0 : order.getSpecial_count())
						+ order.getAdult_count();
				crb.setExtra_info(product.getName() + ":" + peopleCnt + "人");

				clientRelationDao.update(crb);
			}
		}

	}
	// *********** 标准订单确认end***********

	// *********** 非标订单确认 start***********
	@Pointcut("(execution(* com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO.update(..)) && args(order))")
	public void confirmNonStandardOrderCut(BudgetNonStandardOrderBean order) {

	}

	@AfterReturning(value = "confirmNonStandardOrderCut(order)", returning = "res")
	public void afterConfirmNonStandardOrder(BudgetNonStandardOrderBean order, Object res) {

		if (null != order.getConfirm_flg() && order.getConfirm_flg().equals("Y")) {
			ClientRelationBean crb = clientRelationDao.selectByEmployeePk(order.getClient_employee_pk());

			Date old = DateUtil.castStr2Date(crb.getConnect_date());
			Date now = DateUtil.castStr2Date(order.getConfirm_date());

			if (now.after(old)) {
				crb.setConnect_date(order.getConfirm_date());
				crb.setType("ORDER");
				int peopleCnt = (null == order.getSpecial_count() ? 0 : order.getSpecial_count())
						+ order.getAdult_count();
				crb.setExtra_info(order.getProduct_name() + ":" + peopleCnt + "人");

				clientRelationDao.update(crb);
			}
		}

	}
	// *********** 非标订单确认end***********
}
