package com.xinchi.backend.user.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.user.service.UserService;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.bean.UserInfoBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.Utils;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction {

	private static final long serialVersionUID = 386891927229151305L;

	private UserBaseBean ubb;
	private UserInfoBean uib;
	private File file;
	// 提交过来的file的名字
	private String fileFileName;

	// 提交过来的file的MIME类型
	private String fileContentType;
	private String password2;
	private String login_name;
	@Autowired
	private UserService userService;

	public String login() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String result = userService.login(ubb);
		if (result.equals(SUCCESS)) {
			request.removeAttribute("login_result");
		} else {
			request.setAttribute("login_result", result);
		}

		return result;
	}

	public String logout() {
		HttpSession map = ServletActionContext.getRequest().getSession();
		map.invalidate();
		return "SUCCESS";
	}

	public String register() throws IOException {
		if (null == file)
			return ERROR;
		if (ubb.getPassword().equals(password2)) {

			String ext = Utils.getFileExt(fileFileName);
			String fileFolder = PropertiesUtil.getProperty("userIdFileFolder");
			File destfile = new File(fileFolder + File.separator + ubb.getId() + "." + ext);
			FileUtils.copyFile(file, destfile);
			file.delete();
			uib.setId_file_name(ubb.getId() + "." + ext);
			userService.register(ubb, uib);
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public String checkLoginName() {
		ubb = new UserBaseBean();
		ubb.setLogin_name(login_name);
		List<UserBaseBean> list = userService.getAllByParam(ubb);
		if (null != list && list.size() > 0) {
			resultStr = "exist";
		} else {
			resultStr = "OK";
		}

		return SUCCESS;
	}

	private List<UserCommonBean> users;

	public String searchNewUsers() {
		users = userService.getAllNewUsers();
		return SUCCESS;
	}

	private UserCommonBean ucb;

	public String searchUsersByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", ucb);
		page.setParams(params);

		users = userService.selectByPage(page);
		return SUCCESS;
	}

	private String user_pk;
	private String user_roles;

	public String searchUserByPk() {
		ucb = userService.selectUserCommonByPk(user_pk);
		return SUCCESS;
	}

	public String approveUser() {
		resultStr = userService.approveUser(user_pk, user_roles);
		return SUCCESS;
	}

	public String updateUserRoles() {
		resultStr = userService.updateUserRoles(user_pk, user_roles);
		return SUCCESS;
	}

	public String searchAllSales() {
		users = userService.getAllUsersByRole(ResourcesConstants.USER_ROLE_SALES);
		return SUCCESS;
	}

	public String searchAllUsers() {
		users = userService.getAllUsers(null);
		return SUCCESS;
	}

	public String searchAllUseUsers() {
		UserCommonBean ucb = new UserCommonBean();
		ucb.setDelete_flg("N");
		users = userService.getAllUsers(ucb);
		return SUCCESS;
	}

	public String rejectUser() {
		resultStr = userService.rejectUser(user_pk);
		return SUCCESS;
	}

	/**
	 * 停用用户
	 * 
	 * @return
	 */
	public String stopUser() {
		resultStr = userService.stopUser(user_pk);
		return SUCCESS;
	}

	@JSON(serialize = false)
	public UserBaseBean getUbb() {
		return ubb;
	}

	public void setUbb(UserBaseBean ubb) {
		this.ubb = ubb;
	}

	@JSON(serialize = false)
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	@JSON(serialize = false)
	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	@JSON(serialize = false)
	public UserInfoBean getUib() {
		return uib;
	}

	public void setUib(UserInfoBean uib) {
		this.uib = uib;
	}

	@JSON(serialize = false)
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@JSON(serialize = false)
	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	@JSON(serialize = false)
	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public List<UserCommonBean> getUsers() {
		return users;
	}

	public void setUsers(List<UserCommonBean> users) {
		this.users = users;
	}

	public String getUser_pk() {
		return user_pk;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public String getUser_roles() {
		return user_roles;
	}

	public void setUser_roles(String user_roles) {
		this.user_roles = user_roles;
	}

	public UserCommonBean getUcb() {
		return ucb;
	}

	public void setUcb(UserCommonBean ucb) {
		this.ucb = ucb;
	}

}