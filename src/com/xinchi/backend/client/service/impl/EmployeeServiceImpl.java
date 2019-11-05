package com.xinchi.backend.client.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.AccurateSaleDAO;
import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.backend.client.dao.ClientUserDAO;
import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.dao.JobHoppingLogDAO;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.bean.ClientUserBean;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.JobHoppingLogBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.RelationLevelDto;
import com.xinchi.bean.SqlBean;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.ToolsUtil;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeDAO dao;

	@Override
	public void insert(ClientEmployeeBean bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public String update(ClientEmployeeBean bo) {
		dao.update(bo);
		return SUCCESS;
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ClientEmployeeBean selectByPrimaryKey(String pk) {
		return dao.selectByPrimaryKey(pk);
	}

	@Override
	public List<ClientEmployeeBean> getAllClientEmployeeByParam(ClientEmployeeBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public String createEmployee(ClientEmployeeBean employee) {

		ClientEmployeeBean option1 = new ClientEmployeeBean();
		option1.setCellphone(employee.getCellphone());
		List<ClientEmployeeBean> exists1 = dao.getAllByParam(option1);
		if (exists1 != null && exists1.size() > 0)
			return "existcellphone";

		if (!SimpletinyString.isEmpty(employee.getCellphone1())) {
			ClientEmployeeBean option2 = new ClientEmployeeBean();
			option2.setCellphone1(employee.getCellphone1());
			List<ClientEmployeeBean> exists2 = dao.getAllByParam(option2);
			if (exists2 != null && exists2.size() > 0)
				return "existcellphone";
		}

		ClientEmployeeBean option3 = new ClientEmployeeBean();
		option3.setWechat(employee.getWechat());
		List<ClientEmployeeBean> exists3 = dao.getAllByParam(option3);
		if (exists3 != null && exists3.size() > 0)
			return "existwechat";

		if (!SimpletinyString.isEmpty(employee.getWechat1())) {
			ClientEmployeeBean option4 = new ClientEmployeeBean();
			option4.setWechat1(employee.getWechat1());
			List<ClientEmployeeBean> exists4 = dao.getAllByParam(option4);
			if (exists4 != null && exists4.size() > 0)
				return "existwechat";
		}
		if (SimpletinyString.isEmpty(employee.getHead_photo())) {
			employee.setHead_photo(null);
		} else {
			saveFile(employee.getHead_photo());
		}

		employee.setRelation_level("新增级");
		dao.insert(employee);
		// 记录客户和销售对应关系
		ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();
		ceub.setEmployee_pk(employee.getPk());
		ceub.setUser_pk(employee.getSales());
		employeeUserDao.insert(ceub);

		return "success";
	}

	@Override
	public String updateEmployee(ClientEmployeeBean employee) {
		ClientEmployeeBean option1 = new ClientEmployeeBean();
		option1.setCellphone(employee.getCellphone());
		List<ClientEmployeeBean> exists1 = dao.getAllByParam(option1);
		if (exists1 != null && exists1.size() > 0 && !exists1.get(0).getPk().equals(employee.getPk())
				&& !employee.getPublic_flg().equals("Y"))
			return "existcellphone";

		if (!SimpletinyString.isEmpty(employee.getCellphone1())) {
			ClientEmployeeBean option2 = new ClientEmployeeBean();
			option2.setCellphone1(employee.getCellphone1());
			List<ClientEmployeeBean> exists2 = dao.getAllByParam(option2);
			if (exists2 != null && exists2.size() > 0 && !exists2.get(0).getPk().equals(employee.getPk())
					&& !employee.getPublic_flg().equals("Y"))
				return "existcellphone";
		}
		ClientEmployeeBean option3 = new ClientEmployeeBean();
		option3.setWechat(employee.getWechat());
		List<ClientEmployeeBean> exists3 = dao.getAllByParam(option3);
		if (exists3 != null && exists3.size() > 0 && !exists3.get(0).getPk().equals(employee.getPk())
				&& !employee.getPublic_flg().equals("Y"))
			return "existwechat";

		if (!SimpletinyString.isEmpty(employee.getWechat1())) {
			ClientEmployeeBean option4 = new ClientEmployeeBean();
			option4.setWechat1(employee.getWechat1());
			List<ClientEmployeeBean> exists4 = dao.getAllByParam(option4);
			if (exists4 != null && exists4.size() > 0 && !exists4.get(0).getPk().equals(employee.getPk())
					&& !employee.getPublic_flg().equals("Y"))
				return "existwechat";
		}
		// 如果是公开维护修改所属销售
		if (employee.getPublic_flg().equals("Y")) {
			UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
					.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

			employee.setPublic_flg("N");
			employee.setRelation_level(ResourcesConstants.CLIENT_RELATION_LEVEL_01);
			// 删除之前存在的对应关系
			employeeUserDao.deleteByEmployeePk(employee.getPk());

			// 保存新的对应关系
			ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();

			ceub.setEmployee_pk(employee.getPk());
			ceub.setUser_pk(sessionBean.getPk());
			employeeUserDao.insert(ceub);
		}

		ClientEmployeeBean oldCeb = dao.selectByPrimaryKey(employee.getPk());

		if (!oldCeb.getHead_photo().equals(employee.getHead_photo())) {
			deleteOldHead(oldCeb.getHead_photo());
			saveFile(employee.getHead_photo());
		}

		dao.update(employee);
		return SUCCESS;

	}

	@Override
	public List<ClientEmployeeBean> getAllClientEmployeeByPage(Page<ClientEmployeeBean> page) {
		return dao.getAllByPage(page);
	}

	@Override
	public List<String> getBodyPksByEmployeePks(String[] employee_pks) {

		return dao.getBodyPksByEmployeePks(employee_pks);
	}

	@Override
	public String deleteClientEmployee(List<String> employee_pks) {
		dao.deleteClientEmployeeByPks(employee_pks);
		return "success";
	}

	@Override
	public String recoveryClientEmployee(List<String> employee_pks) {
		dao.recoveryClientEmployeeByPks(employee_pks);
		return "success";
	}

	@Override
	public RelationLevelDto selectRelationCntBySales(String sales_pk) {
		return dao.selectRelationCntBySales(sales_pk);
	}

	@Override
	public String publicClientEmployee(List<String> employee_pks) {
		for (String employee_pk : employee_pks) {
			ClientEmployeeBean employee = dao.selectByPrimaryKey(employee_pk);
			employee.setPublic_flg("Y");
			// 删除之前存在的对应关系
			employeeUserDao.deleteByEmployeePk(employee_pk);

			// 保存新的对应关系
			ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();

			ceub.setEmployee_pk(employee_pk);
			ceub.setUser_pk(ResourcesConstants.USER_PUBLIC);
			employeeUserDao.insert(ceub);

			dao.update(employee);
		}
		return "success";
	}

	@Autowired
	private ClientVisitDAO clientVisitDao;

	@Autowired
	private AccurateSaleDAO accurateSaleDao;

	@Autowired
	private OrderDAO orderDao;

	@Override
	public String deleteClientEmployeeReally(String employee_pk) {
		// 检测当前客户员工是否存在有效拜访
		ClientVisitBean option1 = new ClientVisitBean();
		option1.setClient_employee_pk(employee_pk);
		List<ClientVisitBean> results1 = clientVisitDao.selectByParam(option1);
		if (null != results1 && results1.size() > 0) {
			return "exist_visit";
		}
		// 检测当前客户员工是否存在有效沟通
		AccurateSaleBean option2 = new AccurateSaleBean();
		option2.setClient_employee_pk(employee_pk);
		List<AccurateSaleBean> results2 = accurateSaleDao.selectByParam(option2);
		if (null != results2 && results2.size() > 0) {
			return "exist_accurate";
		}
		// 检测是否存在订单
		OrderDto option3 = new OrderDto();
		option3.setClient_employee_pk(employee_pk);
		List<OrderDto> results3 = orderDao.selectByParam(option3);
		if (null != results3 && results3.size() > 0) {
			return "exist_order";
		}
		ClientEmployeeBean ceb = dao.selectByPrimaryKey(employee_pk);
		deleteOldHead(ceb.getHead_photo());
		// 删除头像
		dao.delete(employee_pk);
		return SUCCESS;
	}

	@Autowired
	private CommonDAO commonDao;

	@Override
	public String combineClientEmployee(List<String> employee_pks) {
		if (null == employee_pks || employee_pks.size() != 2)
			return FAIL;
		String main_pk = employee_pks.get(0);
		String assist_pk = employee_pks.get(1);
		ClientEmployeeBean main = dao.selectByPrimaryKey(main_pk);
		ClientEmployeeBean assist = dao.selectByPrimaryKey(assist_pk);

		// 合并员工，并更新
		String[] exceptions = { "name", "telephone", "financial_body_pk", "financial_body_name", "delete_flgs" };
		ToolsUtil.combineObject(main, assist, Arrays.asList(exceptions));
		dao.update(main);

		// 更新涉及到client_employee_pk的表
		String[] tables = { "accurate_sale", "client_visit", "client_received_detail", "receivable", "budget_order",
				"budget_standard_order", "final_order", "final_standard_order", "budget_non_standard_order",
				"final_non_standard_order" };

		SqlBean sqlbean = new SqlBean();
		for (String table : tables) {
			String sql = "update " + table;
			sql += " set client_employee_pk = '" + main_pk + "'";
			sql += " where client_employee_pk = '" + assist_pk + "'";
			sql += ";";

			sqlbean.setSql(sql);
			commonDao.exeBySql(sqlbean);
		}
		return SUCCESS;
	}

	@Autowired
	private JobHoppingLogDAO jhlDao;

	@Override
	public String jobHopping(ClientEmployeeBean employee) {
		ClientEmployeeBean old = dao.selectByPrimaryKey(employee.getPk());

		JobHoppingLogBean jhlBean = new JobHoppingLogBean();
		jhlBean.setClient_employee_pk(employee.getPk());
		jhlBean.setPre_client_pk(old.getFinancial_body_pk());
		jhlBean.setNow_client_pk(employee.getNew_client_pk());
		jhlBean.setHop_date(employee.getHopping_date());
		jhlDao.insert(jhlBean);

		old.setFinancial_body_pk(employee.getNew_client_pk());
		dao.update(old);

		return SUCCESS;
	}

	@Autowired
	private ClientEmployeeUserDAO employeeUserDao;

	@Autowired
	private ClientUserDAO clientUserDao;

	@Override
	public String changeEmployeeSales(List<String> employee_pks, List<String> sale_pks) {
		String main_user = sale_pks.get(0);

		for (String employee_pk : employee_pks) {
			ClientEmployeeBean employee = dao.selectByPrimaryKey(employee_pk);

			// 删除之前存在的对应关系
			employeeUserDao.deleteByEmployeePk(employee_pk);

			if (main_user.equals("public")) {
				employee.setPublic_flg("Y");
				// 保存新的对应关系
				ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();

				ceub.setEmployee_pk(employee_pk);
				ceub.setUser_pk(ResourcesConstants.USER_PUBLIC);
				employeeUserDao.insert(ceub);
			} else {
				employee.setPublic_flg("N");
				// 保存新的对应关系
				for (String sale_pk : sale_pks) {
					ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();

					ceub.setEmployee_pk(employee_pk);
					ceub.setUser_pk(sale_pk);
					employeeUserDao.insert(ceub);
				}
			}
			dao.update(employee);
			if (main_user.equals("public"))
				return SUCCESS;

			// 调整客户财务主体相关
			List<ClientUserBean> cubs = clientUserDao.selectByClientPk(employee.getFinancial_body_pk());
			if (null != cubs) {
				for (ClientUserBean cub : cubs) {
					sale_pks.remove(cub.getUser_pk());
				}
			}

			for (String sale_pk : sale_pks) {
				ClientUserBean cub = new ClientUserBean();
				cub.setClient_pk(employee.getFinancial_body_pk());
				cub.setUser_pk(sale_pk);

				clientUserDao.insert(cub);
			}
		}
		return SUCCESS;
	}

	@Override
	public RelationLevelDto selectRelationCntAdmin() {
		return dao.selectRelationCntAdmin();
	}

	@Autowired
	private ClientDAO clientDao;

	@Override
	public String reviewEmployee(ClientEmployeeBean employee) {
		// 更新客户资料
		dao.update(employee);

		// 客户所属的销售
		List<ClientEmployeeUserBean> ceubs = employeeUserDao.selectByEmployeePk(employee.getPk());

		// 财务主体所属的销售
		List<ClientUserBean> cubs = clientUserDao.selectByClientPk(employee.getFinancial_body_pk());

		// 如果财务主体是公开状态
		if (cubs.size() == 0 || cubs.get(0).getUser_pk().equals(ResourcesConstants.USER_PUBLIC)) {

			// 如果客户也是公开状态则什么也不做
			if (ceubs.get(0).getUser_pk().equals(ResourcesConstants.USER_PUBLIC)) {
				// do nothing
			} else {

				ClientBean client = clientDao.selectByPrimaryKey(employee.getFinancial_body_pk());
				client.setPublic_flg("N");
				clientDao.update(client);

				// 删除之前的对应关系
				clientUserDao.deleteByClientPk(employee.getFinancial_body_pk());

				// 保存新的对应关系
				for (ClientEmployeeUserBean ceub : ceubs) {
					ClientUserBean cub = new ClientUserBean();
					cub.setClient_pk(employee.getFinancial_body_pk());
					cub.setUser_pk(ceub.getUser_pk());
					clientUserDao.insert(cub);
				}
			}

		} else {
			for (int i = ceubs.size() - 1; i >= 0; i--) {
				for (int j = 0; j < cubs.size(); j++) {
					if (ceubs.get(i).getUser_pk().equals(cubs.get(j).getUser_pk())) {
						ceubs.remove(i);
						break;
					}
				}
			}
			// 添加新的对应关系
			for (ClientEmployeeUserBean ceub : ceubs) {
				ClientUserBean cub = new ClientUserBean();
				cub.setClient_pk(employee.getFinancial_body_pk());
				cub.setUser_pk(ceub.getUser_pk());
				clientUserDao.insert(cub);
			}
		}

		return SUCCESS;
	}

	@Override
	public List<ClientEmployeeBean> selectSameTelEmployee(ClientEmployeeBean e) {

		return dao.selectSameTelEmployee(e);
	}

	private void saveFile(String fileName) {
		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty("clientEmployeeHeadFolder");

		File sourceFile = new File(tempFolder + File.separator + fileName);
		File destfile = new File(fileFolder + File.separator + fileName);
		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 压缩头像
		String minFolder = PropertiesUtil.getProperty("clientEmployeeMinHeadFolder");
		File minDestFile = new File(minFolder + File.separator + fileName);
		FileUtil.reduceImg(sourceFile, minDestFile, 100, 100, new Float(0.3));

		sourceFile.delete();
	}

	private void deleteOldHead(String fileName) {
		String fileFolder = PropertiesUtil.getProperty("clientEmployeeHeadFolder");
		String minFolder = PropertiesUtil.getProperty("clientEmployeeMinHeadFolder");

		File fullImg = new File(fileFolder + File.separator + fileName);
		File minImg = new File(minFolder + File.separator + fileName);

		fullImg.delete();
		minImg.delete();
	}

	@Override
	public String updateMarketLevel(List<String> employee_pks, String market_level) {
		List<ClientEmployeeBean> employees = dao.selectByPks(employee_pks);
		for (ClientEmployeeBean employee : employees) {
			employee.setMarket_level(market_level);
			dao.update(employee);
		}
		return SUCCESS;
	}
}