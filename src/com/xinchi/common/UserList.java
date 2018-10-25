package com.xinchi.common;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class UserList {
	private static final UserList userList = new UserList();

	private Vector<UserSessionBean> v = new Vector<UserSessionBean>();

	private UserList() {
	}

	public static UserList getInstance() {
		return userList;
	}

	public UserSessionBean getUser(String user_pk) {
		List<UserSessionBean> users = getUserList();
		for (UserSessionBean user : users) {
			if (user.getPk().equals(user_pk))
				return user;
		}
		return null;
	}

	// 将用户登陆身份证保存到Vector中
	public void addUser(UserSessionBean user) {
		if (user != null) {
			if (IsExist(user.getPk()) >= 0)// 判断是否已经存在
				return;
			v.addElement(user);
		}
	}

	// 删除用户登录ID
	public void RemoveUser(UserSessionBean user) {
		if (user != null) {
			v.removeElementAt(IsExist(user.getPk()));
		}

	}

	// 判断Vector中是否存在已经登录的用户，使用Id判断
	public int IsExist(String user_pk) {
		for (int i = 0; i < v.size(); i++) {
			if (v.get(i).getPk().equals(user_pk)) {
				return i;
			}
		}
		return -1;

	}

	// 返回Vector枚举
	public List<UserSessionBean> getUserList() {
		return Collections.list(v.elements());
	}

	// 返回迭代器
	public Iterator<UserSessionBean> getUserListItera() {
		return v.iterator();
	}

	// 返回在线人数
	public int getUserCount() {
		return v.size();
	}
}
