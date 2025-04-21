package com.xinchi.backend.user.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.backend.sys.dao.PagesDAO;
import com.xinchi.backend.user.dao.AssistantManagerDAO;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.backend.userinfo.dao.UserInfoDAO;
import com.xinchi.backend.userinfo.service.UserInfoService;
import com.xinchi.backend.util.UserUtilService;
import com.xinchi.bean.AssistantManagerBean;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.bean.PagesBean;
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

	@Autowired
	private PagesDAO pagesDao;

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

				// 存储用户基本信息
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

				// 存储用户导航信息
				List<PagesBean> navigation = new ArrayList<>();

				if (uib.getUser_role().contains(ResourcesConstants.USER_ROLE_ADMIN)) {
					PagesBean admin_option = new PagesBean();
					admin_option.setLevel(1);
					navigation = pagesDao.selectByParam(admin_option);
				} else {
					PagesBean other_option = new PagesBean();
					other_option.setRoles(Arrays.asList(uib.getUser_role().split(",")));
					other_option.setLevel(1);
					navigation = pagesDao.selectByRoles(other_option);
					navigation = list2list(navigation);
				}

				XinChiApplicationContext.setSession(ResourcesConstants.NAVIGATION_SESSION_KEY, navigation);

				String navigation_html = create_navigatioin_html(navigation, uib.getUser_role());
				XinChiApplicationContext.setSession(ResourcesConstants.NAVIGATION_HTML_SESSION_KEY, navigation_html);
				if (uib.getUser_role().contains("SALE"))
					return "sale";
				return "success";
			} else {
				return "input";
			}
		}
	}

	private List<PagesBean> list2list(List<PagesBean> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (PagesBean page : list) {
			if (page.getLevel() == 1) {
				if (map.get("P" + page.getPk()) == null) {
					map.put("P" + page.getPk(), page);
				}
			} else {
				if (map.get(page.getFather_pk()) == null) {
					List<PagesBean> children = new ArrayList<PagesBean>();
					children.add(page);
					map.put(page.getFather_pk(), children);
				} else {
					((List<PagesBean>) map.get(page.getFather_pk())).add(page);
				}
			}
		}
		List<PagesBean> result = new ArrayList<PagesBean>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().startsWith("P")) {
				PagesBean page = (PagesBean) entry.getValue();
				page.setChild_pages((List<PagesBean>) map.get(page.getPk()));
				Collections.sort(page.getChild_pages(), new Comparator<PagesBean>() {
					@Override
					public int compare(PagesBean o1, PagesBean o2) {
						// 比较两个 PagesBean 对象的 orderIndex 字段
						return Integer.compare(o1.getOrder_index(), o2.getOrder_index());
					}
				});
				result.add(page);
			}
		}
		return result;
	}

	private String create_navigatioin_html(List<PagesBean> navigation, String roles) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/templates/";
		String html = "";
		StringBuilder sb = new StringBuilder();
		if (navigation != null) {
			for (PagesBean parent : navigation) {
				// 假设父页面为 Map 类型
				String parentTitle = parent.getPage_title();
				String parentUrl = basePath;
				if (roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
					parentUrl += parent.getChild_pages().get(0).getPage_url();
				} else {
					parentUrl += parent.getPage_url();
				}

				String parentClass = parent.getPage_class() != null ? parent.getPage_class() : "default";
				List<PagesBean> childPages = parent.getChild_pages();

				sb.append("<li class=\"").append(parentClass).append("\">");
				sb.append("<a href=\"").append(parentUrl != null ? parentUrl : "#").append("\">");
				sb.append("<i class=\"fa fa-users1 fa-lg fa-fw\"></i>");
				sb.append(parentTitle);
				sb.append("</a>");

				// 如果存在子页面则生成子菜单（使用 <ol>）
				if (childPages != null && !childPages.isEmpty()) {
					sb.append("<ol style=\"display:none;\">");
					for (PagesBean child : childPages) {
						String childTitle = child.getPage_title();
						String childUrl = basePath + child.getPage_url();

						sb.append("<li>");
						sb.append("<a href=\"").append(childUrl).append("?page_title=").append(childTitle)
								.append("\">");
						sb.append("<i class=\"fa fa-angle-right fa-lg fa-fw\"></i>");
						sb.append(childTitle);
						sb.append("</a>");
						sb.append("</li>");
					}
					sb.append("</ol>");
				}
				sb.append("</li>");
			}
		}
		// 将生成的导航 HTML 保存到 session 中
		html = sb.toString();
		return html;
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
		List<String> arr_roles = Arrays.asList(roles.split(","));

		return dao.getAllUsersByRole(arr_roles);
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

	@Autowired
	private AssistantManagerDAO assistantManagerDao;

	@Override
	public String updateUserRoles(String user_pk, String user_roles, String product_managers) {
		UserBaseBean ubb = dao.selectByPrimaryKey(user_pk);
		UserInfoBean uib = infoDao.selectByUserId(ubb.getId());

		// 如果之前包含产品助理角色，现在不包含，则删除对应经理数据
		if (uib.getUser_role().contains(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT)
				&& !user_roles.contains(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT)) {
			assistantManagerDao.deleteByAssistantNumber(ubb.getUser_number());
		}
		// 有选择的产品经理才进行更新
		if (!SimpletinyString.isEmpty(product_managers)) {
			// 如果之前就包含产品助理角色，则删除对应经理数据
			if (uib.getUser_role().contains(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT)) {
				assistantManagerDao.deleteByAssistantNumber(ubb.getUser_number());
			}
			// 如果现在包含产品助理角色，则写入对应的经理数据
			if (user_roles.contains(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT)) {
				for (String manager_number : product_managers.split(",")) {
					AssistantManagerBean amb = new AssistantManagerBean();
					amb.setAssistant_number(ubb.getUser_number());
					amb.setManager_number(manager_number);
					amb.setAssistant_type(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT);
					assistantManagerDao.insert(amb);
				}
			}
		}

		uib.setUser_role(user_roles);
		infoDao.update(uib);
		return SUCCESS;
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

			BigDecimal balance = BigDecimal.ZERO;
			if (null != rb) {
				balance = rb.getAll_balance();
			}

			uib.setCredit_balance(credit_limit.subtract(balance));

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