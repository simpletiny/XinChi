package com.xinchi.backend.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.user.dao.UserGroupDAO;
import com.xinchi.backend.user.dao.UserGroupRelatedDAO;
import com.xinchi.backend.user.service.UserGroupService;
import com.xinchi.bean.UserGroupBean;
import com.xinchi.bean.UserGroupRelatedBean;

@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {
	@Autowired
	private UserGroupDAO dao;
	@Autowired
	private UserGroupRelatedDAO relatedDao;

	@Override
	public String insert(UserGroupBean group) {
		UserGroupBean options = new UserGroupBean();
		options.setGroup_name(group.getGroup_name());
		List<UserGroupBean> list = dao.getAllByParam(options);
		if (null != list && list.size() > 0)
			return "EXISTS";

		String[] user_pks = group.getUser_pks().split(",");
		String group_pk = dao.insert(group);
		for (String user_pk : user_pks) {
			UserGroupRelatedBean relate = new UserGroupRelatedBean();
			relate.setUser_pk(user_pk);
			relate.setGroup_pk(group_pk);
			relatedDao.insert(relate);
		}
		return "OK";
	}

	@Override
	public List<UserGroupBean> getAllGroups() {
		return dao.getAllByParam(null);
	}

}