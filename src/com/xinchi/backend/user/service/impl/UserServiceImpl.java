package com.xinchi.backend.user.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.backend.userinfo.dao.UserInfoDAO;
import com.xinchi.backend.userinfo.service.UserInfoService;
import com.xinchi.backend.util.UserUtilService;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.bean.ReceivableBalanceDto;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.bean.UserInfoBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.exception.BusinessException;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO dao;
	@Autowired
	private UserInfoDAO infoDao;
	@Autowired
	private UserUtilService uus;

	@Autowired
	private UserInfoService userInfoService;

	@Override
	public String login(UserBaseBean ubb) {
		List<UserBaseBean> users = dao.getAllByParam(ubb);
		if (null == users || users.size() == 0) {
			return "none";
		} else {
			UserBaseBean user = users.get(0);
			if (user.getPassword().equals(SimpletinyString.MD5(ubb.getPassword()))) {

				if (user.getDelete_flg().equals("Y"))
					return "stop";
				if (user.getUser_status().equals("A"))
					return "noright";
				UserSessionBean sessionBean = new UserSessionBean();
				try {
					PropertyUtils.copyProperties(sessionBean, ubb);
				} catch (Exception e) {
					throw new BusinessException(e);
				}

				UserInfoBean uib = infoDao.selectByUserId(user.getId());

				sessionBean.setPk(user.getPk());
				sessionBean.setUser_number(user.getUser_number());
				sessionBean.setUser_name(user.getUser_name());
				sessionBean.setNick_name(uib.getNick_name());
				sessionBean.setCellphone(uib.getCellphone());
				sessionBean.setUser_status(user.getUser_status());
				sessionBean.setUser_roles(uib.getUser_role());
				sessionBean.setCredit_limit(uib.getCredit_limit());
				sessionBean.setCredit_balance(uib.getCredit_balance());
				sessionBean.setCurrent_date(DateUtil.today());

				XinChiApplicationContext.setSession(ResourcesConstants.LOGIN_SESSION_KEY, sessionBean);

				if (uib.getUser_role().contains("SALE"))
					return "sale";
				return "success";
			} else {
				return "input";
			}
		}
	}

	@Override
	public String register(UserBaseBean bo, UserInfoBean uib) throws IOException {
		// 判断是否有相同的登录名
		UserBaseBean ubbOption = new UserBaseBean();
		ubbOption.setLogin_name(bo.getLogin_name());
		List<UserBaseBean> arr = dao.getAllByParam(ubbOption);
		if (arr != null && arr.size() > 0)
			return "same_login_name";

		// 判断是否有相同的身份证号
		UserInfoBean oldUif = infoDao.selectByUserId(bo.getId());
		if (oldUif != null)
			return "same_id";

		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty("userIdFileFolder");

		File sourceFile = new File(tempFolder + File.separator + uib.getId_file_name());
		File destfile = new File(fileFolder + File.separator + uib.getId_file_name());
		FileUtils.copyFile(sourceFile, destfile);
		sourceFile.delete();

		String userNumber = uus.getNextUserNumber();
		bo.setPassword(SimpletinyString.MD5(bo.getPassword()));
		bo.setUser_number(userNumber);
		bo.setUser_status(ResourcesConstants.USER_STATUS_APPLY);
		dao.insert(bo);

		uib.setId(bo.getId());
		uib.setUser_right("");
		userInfoService.insert(uib);

		return SUCCESS;
	}

	@Override
	public String approveUser(String user_pk, String user_roles) {
		UserBaseBean ubb = dao.selectByPrimaryKey(user_pk);
		UserInfoBean uib = infoDao.selectByUserId(ubb.getId());
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		ubb.setApprove_time(DateUtil.getTimeMillis());
		ubb.setApprove_user(sessionBean.getUser_number());
		ubb.setUser_status(ResourcesConstants.USER_STATUS_NORMAL);
		uib.setUser_role(user_roles);

		dao.update(ubb);

		infoDao.update(uib);

		return "success";
	}

	@Override
	public String rejectUser(String user_pk) {
		UserBaseBean user = dao.selectByPrimaryKey(user_pk);
		UserInfoBean info = infoDao.selectByUserId(user.getId());
		if (null != info)
			infoDao.delete(info.getPk());
		dao.delete(user_pk);

		return SUCCESS;
	}

	@Override
	public List<UserCommonBean> getAllUsersByRole(String roles) {
		return dao.getAllUsersByRole(roles);
	}

	@Override
	public List<UserCommonBean> getAllUsers(UserCommonBean bo) {
		return dao.getAllUserCommonByParam(bo);
	}

	@Override
	public void update(UserBaseBean bo) {
		dao.update(bo);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public com.xinchi.bean.UserBaseBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<com.xinchi.bean.UserBaseBean> getAllByParam(UserBaseBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public UserBaseBean selectByParam(UserBaseBean bo) {

		return null;
	}

	@Override
	public List<UserCommonBean> getAllUserCommonByParam(UserCommonBean bo) {
		return dao.getAllUserCommonByParam(bo);
	}

	@Override
	public List<UserCommonBean> getAllNewUsers() {
		return dao.getAllNewUsers();

	}

	@Override
	public List<UserCommonBean> selectByPage(Page page) {
		return dao.selectByPage(page);
	}

	@Override
	public String stopUser(String user_pk) {
		UserBaseBean ubb = dao.selectByPrimaryKey(user_pk);
		ubb.setDelete_flg("Y");
		dao.update(ubb);
		return "success";
	}

	@Override
	public UserCommonBean selectUserCommonByPk(String user_pk) {

		return dao.selectUserCommonByPk(user_pk);
	}

	@Override
	public String updateUserRoles(String user_pk, String user_roles) {
		UserBaseBean ubb = dao.selectByPrimaryKey(user_pk);
		UserInfoBean uib = infoDao.selectByUserId(ubb.getId());
		uib.setUser_role(user_roles);
		infoDao.update(uib);
		return "success";
	}

	@Override
	public String checkPassword(UserCommonBean ucb) {
		UserBaseBean user = dao.selectByPrimaryKey(ucb.getPk());
		if (user.getPassword().equals(SimpletinyString.MD5(ucb.getPassword()))) {
			return SUCCESS;
		} else {
			return FAIL;
		}
	}

	@Override
	public String changePassword(UserCommonBean ucb) {
		UserBaseBean user = dao.selectByPrimaryKey(ucb.getPk());
		user.setPassword(SimpletinyString.MD5(ucb.getPassword()));
		dao.update(user);
		return SUCCESS;
	}

	@Override
	public String reuseUser(String user_pk) {
		UserBaseBean ubb = dao.selectByPrimaryKey(user_pk);
		ubb.setDelete_flg("N");
		dao.update(ubb);
		return SUCCESS;
	}

	@Override
	public UserBaseBean selectByUserNumber(String user_number) {
		return dao.getByUserNumber(user_number);
	}

	@Override
	public UserCommonBean selectUserCommonByUserNumber(String user_number) {
		return dao.selectUserCommonByUserNumber(user_number);
	}

	@Autowired
	private ReceivableDAO receivableDao;

	// 修改销售信用额度
	@Override
	public String updateCreditLimit(String user_pks, BigDecimal credit_limit) {
		String[] pks = user_pks.split(",");

		for (String pk : pks) {
			UserBaseBean ubb = dao.selectByPrimaryKey(pk);
			UserInfoBean uib = infoDao.selectByUserId(ubb.getId());
			uib.setCredit_limit(credit_limit);

			// 更新余额，余额=初始金额-应收款
			ReceivableBalanceDto rb = receivableDao.selectUserReceivableBalanceByUserNumber(ubb.getUser_number());
			uib.setCredit_balance(credit_limit.subtract(rb.getAll_balance()));

			infoDao.update(uib);
		}

		return SUCCESS;
	}

	@Autowired
	private BaseDataDAO baseDataDao;

	@Override
	public boolean hasEnoughCreditToConfirm(String receivable_first_flg, String user_number, String team_number,
			BigDecimal money) {
		BaseDataBean bd = baseDataDao.selectByPk(ResourcesConstants.BASE_DATA_PK_SSCREDIT);

		if (bd.getExt1().equals("Y")) {

			UserBaseBean ubb = dao.getByUserNumber(user_number);
			UserInfoBean uib = infoDao.selectByUserId(ubb.getId());

			BigDecimal credit_balance = uib.getCredit_balance();
			BigDecimal more = BigDecimal.ZERO;
			if (receivable_first_flg.equals("Y")) {
				ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(team_number);
				more = money.subtract(receivable.getBudget_receivable());
			} else {
				more = money;
			}

			if (BigDecimal.ZERO.compareTo(credit_balance.subtract(more)) != 1) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}

}