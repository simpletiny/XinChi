package com.xinchi.backend.user.service;

import java.io.IOException;
import java.util.List;

import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.bean.UserInfoBean;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "用户")
public interface UserService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public String register(UserBaseBean bo, UserInfoBean uib) throws IOException;

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public void update(UserBaseBean bo);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	@LogDescription(ignore = true)
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	@LogDescription(ignore = true)
	public UserBaseBean selectByPrimaryKey(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	@LogDescription(ignore = true)
	public UserBaseBean selectByParam(UserBaseBean bo);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public List<UserBaseBean> getAllByParam(UserBaseBean bo);

	@LogDescription(des = "登录")
	public String login(UserBaseBean ubb);

	@LogDescription(ignore = true)
	public List<UserCommonBean> getAllUserCommonByParam(UserCommonBean bo);

	@LogDescription(des = "新用户审批")
	public String approveUser(String user_pk, String user_roles);

	@LogDescription(ignore = true)
	public List<UserCommonBean> getAllNewUsers();

	@LogDescription(des = "拒绝新用户申请")
	public String rejectUser(String user_pk);

	@LogDescription(ignore = true)
	public List<UserCommonBean> getAllUsersByRole(String uSER_ROLE_SALES);

	@LogDescription(ignore = true)
	public List<UserCommonBean> getAllUsers(UserCommonBean bo);

	@LogDescription(des = "查询新注册用户")
	public List<UserCommonBean> selectByPage(Page page);

	@LogDescription(des = "停用用户")
	public String stopUser(String user_pk);

	@LogDescription(ignore = true)
	public UserCommonBean selectUserCommonByPk(String user_pk);

	@LogDescription(des = "修改用户角色")
	public String updateUserRoles(String user_pk, String user_roles);

	public String checkPassword(UserCommonBean ucb);

	public String changePassword(UserCommonBean ucb);

	public String reuseUser(String user_pk);
}