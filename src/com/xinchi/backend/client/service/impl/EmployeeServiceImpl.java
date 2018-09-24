package com.xinchi.backend.client.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.AccurateSaleDAO;
import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.backend.client.dao.ClientUserDAO;
import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.dao.JobHoppingLogDAO;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.bean.ClientUserBean;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.JobHoppingLogBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.RelationLevelDto;
import com.xinchi.bean.SqlBean;
import com.xinchi.common.ToolsUtil;
import com.xinchi.tools.Page;

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

		List<ClientEmployeeBean> exists = dao.getAllByParam(employee);

		if (exists != null && exists.size() > 0)
			return "exist";

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
		List<ClientEmployeeBean> exists = dao.getAllByParam(employee);

		if (exists != null && exists.size() > 0) {
			if (exists.get(0).getPk().equals(employee.getPk())) {
				dao.update(employee);
				return "success";
			} else {
				return "exist";
			}
		} else {
			dao.update(employee);
			return "success";
		}
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
		dao.publicClientEmployee(employee_pks);
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

}