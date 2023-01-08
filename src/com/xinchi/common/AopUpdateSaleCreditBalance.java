package com.xinchi.common;

import java.math.BigDecimal;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.userinfo.dao.UserInfoDAO;
import com.xinchi.bean.ReceivableBalanceDto;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserInfoBean;

@Aspect
@Service
@Transactional
public class AopUpdateSaleCreditBalance {

	@Autowired
	private UserDAO userDao;

	@Autowired
	private UserInfoDAO userInfoDao;

	// 成功插入一条应收款后
	@AfterReturning(pointcut = "(execution(* com.xinchi.backend.receivable.dao.ReceivableDAO.insert(..)) && args(receivable))", returning = "res")
	public void afterInsertReceivable(ReceivableBean receivable, String res) {
		if (res.length() == 22) {
			String user_number = receivable.getSales();
			BigDecimal balance = receivable.getBudget_balance();

			UserBaseBean ub = userDao.getByUserNumber(user_number);

			UserInfoBean uib = userInfoDao.selectByUserId(ub.getId());

			uib.setCredit_balance(uib.getCredit_balance().subtract(balance));
			userInfoDao.update(uib);

			// 更新session存储
			UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
					.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

			sessionBean.setCredit_limit(uib.getCredit_limit());
			sessionBean.setCredit_balance(uib.getCredit_balance());

		}
	}

	@Autowired
	private ReceivableDAO receivableDao;

	// 成功删除一条应收款后
	@Before("(execution(* com.xinchi.backend.receivable.dao.ReceivableDAO.deleteByTeamNumber(..)) && args(team_number))")
	public void afterDeleteReceivable(String team_number) {
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(team_number);
		String user_number = receivable.getSales();
		UserBaseBean ub = userDao.getByUserNumber(user_number);

		UserInfoBean uib = userInfoDao.selectByUserId(ub.getId());

		BigDecimal money = receivable.getFinal_flg().equals("Y") ? receivable.getFinal_receivable()
				: receivable.getBudget_receivable();

		uib.setCredit_balance(uib.getCredit_balance().add(money));
		userInfoDao.update(uib);

		// 更新session存储
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		sessionBean.setCredit_limit(uib.getCredit_limit());
		sessionBean.setCredit_balance(uib.getCredit_balance());

	}

	// 在成功更新一条应收款后
	@AfterReturning(pointcut = "(execution(* com.xinchi.backend.receivable.dao.ReceivableDAO.update(..)) && args(receivable))")
	public void afterUpdateReceivable(ReceivableBean receivable) {

		String user_number = receivable.getSales();
		ReceivableBalanceDto rbd = receivableDao.selectUserReceivableBalanceByUserNumber(user_number);
		BigDecimal balance = rbd.getAll_balance();

		UserBaseBean ub = userDao.getByUserNumber(user_number);

		UserInfoBean uib = userInfoDao.selectByUserId(ub.getId());

		uib.setCredit_balance(uib.getCredit_limit().subtract(balance));
		userInfoDao.update(uib);

		// 更新session存储
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		sessionBean.setCredit_limit(uib.getCredit_limit());
		sessionBean.setCredit_balance(uib.getCredit_balance());

	}

	// 更新应收款之后
	// @AfterReturning(pointcut = "(execution(*
	// com.xinchi.backend.receivable.dao.ReceivedDAO.insert(..)) or execution(*
	// com.xinchi.backend.receivable.dao.ReceivedDAO.insertWithPk(..))) &&
	// args(detail) ")
	// public void afterInsertReceived(ClientReceivedDetailBean detail) {
	// // TODO 这个有可能管理员或者其他高权限的为销售支付
	// String user_number = detail.getCreate_user();
	// UserBaseBean ub = userDao.getByUserNumber(user_number);
	// UserInfoBean uib = userInfoDao.selectByUserId(ub.getId());
	//
	// uib.setCredit_balance(uib.getCredit_balance().add(detail.getReceived()));
	// userInfoDao.update(uib);
	// // 更新session存储
	// UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
	// .getSession(ResourcesConstants.LOGIN_SESSION_KEY);
	//
	// sessionBean.setCredit_limit(uib.getCredit_limit());
	// sessionBean.setCredit_balance(uib.getCredit_balance());
	// }
	//
	// @Autowired
	// private ReceivedDAO receivedDao;
	//
	// @Before("execution(*
	// com.xinchi.backend.receivable.dao.ReceivedDAO.deleteByPk(..)) && args(pk)")
	// public void beforeUpdateReceivable(String pk) {
	// ClientReceivedDetailBean detail = receivedDao.selectByPk(pk);
	//
	// String user_number = detail.getCreate_user();
	// UserBaseBean ub = userDao.getByUserNumber(user_number);
	// UserInfoBean uib = userInfoDao.selectByUserId(ub.getId());
	//
	// uib.setCredit_balance(uib.getCredit_balance().subtract(detail.getReceived()));
	// userInfoDao.update(uib);
	// // 更新session存储
	// UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
	// .getSession(ResourcesConstants.LOGIN_SESSION_KEY);
	//
	// sessionBean.setCredit_limit(uib.getCredit_limit());
	// sessionBean.setCredit_balance(uib.getCredit_balance());
	// }
}
