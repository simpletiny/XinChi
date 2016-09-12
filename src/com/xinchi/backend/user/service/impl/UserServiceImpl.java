package com.xinchi.backend.user.service.impl;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.backend.userinfo.dao.UserInfoDAO;
import com.xinchi.backend.userinfo.service.UserInfoService;
import com.xinchi.backend.util.UserUtilService;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.bean.UserInfoBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.common.UserSessionBean;
import com.xinchi.exception.BusinessException;

@Service
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
			if (user.getPassword().equals(ubb.getPassword())) {

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
				XinChiApplicationContext.setSession(ResourcesConstants.LOGIN_SESSION_KEY, sessionBean);
				return "success";
			} else {
				return "input";
			}
		}
	}

	@Override
	@Transactional
	public void register(UserBaseBean bo, UserInfoBean uib) {
		String userNumber = uus.getNextUserNumber();
		bo.setUser_number(userNumber);
		bo.setUser_status(ResourcesConstants.USER_STATUS_APPLY);
		dao.insert(bo);

		uib.setId(bo.getId());
		uib.setUser_right("");
		userInfoService.insert(uib);
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

}