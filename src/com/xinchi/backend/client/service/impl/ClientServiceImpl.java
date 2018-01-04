package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientChangeSaleLogDAO;
import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientChangeSaleLogBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO dao;
	@Autowired
	private UserDAO userDao;

	@Override
	public String createCompany(ClientBean client) {
		ClientBean options = new ClientBean();
		options.setSales(client.getSales());
		options.setClient_name(client.getClient_name());

		List<ClientBean> exists = dao.getAllByParam(options);
		if (exists != null && exists.size() > 0)
			return "exist";

		dao.insert(client);
		return "success";
	}

	@Override
	public void insert(com.xinchi.bean.ClientBean bo) {
		dao.insert(bo);
	}

	@Override
	public void update(ClientBean client) {
		dao.update(client);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public com.xinchi.bean.ClientBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<com.xinchi.bean.ClientBean> getAllCompaniesByParam(com.xinchi.bean.ClientBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public String updateCompany(ClientBean client) {
		ClientBean options = new ClientBean();
		options.setSales(client.getSales());
		options.setClient_name(client.getClient_name());

		List<ClientBean> exists = dao.getAllByParam(options);

		if (exists != null && exists.size() > 0) {
			if (exists.get(0).getPk().equals(client.getPk())) {
				dao.update(client);
				return "success";
			} else {
				return "exist";
			}
		} else {
			dao.update(client);
			return "success";
		}
	}

	@Override
	public List<ClientBean> getAllCompaniesByPage(Page<ClientBean> page) {
		return dao.getAllCompaniesByPage(page);
	}

	@Override
	public String deleteClient(List<String> company_pks) {
		dao.deleteCompanyByPks(company_pks);
		return "success";
	}

	@Override
	public String recoveryClientEmployee(List<String> company_pks) {
		dao.recoveryCompanyByPks(company_pks);
		return "success";
	}

	@Autowired
	private EmployeeDAO employeeDao;

	@Autowired
	private ClientChangeSaleLogDAO clientChangeSaleLogDao;

	@Override
	public String changeClientSales(List<String> company_pks, String sale_pk) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String current_user = sessionBean.getUser_number();

		for (String company_pk : company_pks) {
			ClientBean client = dao.selectByPrimaryKey(company_pk);
			// 如果本身就属于此销售，则略过
			if (client.getSales().equals(sale_pk))
				continue;

			ClientBean options = new ClientBean();
			options.setSales(sale_pk);
			options.setClient_name(client.getClient_name());

			String pre_sale_pk = client.getSales();
			List<ClientBean> exists = dao.getAllByParam(options);
			// 记录更换日志
			ClientChangeSaleLogBean changeLog = new ClientChangeSaleLogBean();

			// 如果新销售存在同名财务主体
			if (exists != null && exists.size() > 0) {
				// 原财务主体停用
				client.setDelete_flg("Y");
				changeLog.setType(ResourcesConstants.CLIENT_CHANGE_SALE_TYPE_COMBINE);
				changeLog.setCombine_client_pk(exists.get(0).getPk());
			} else {
				client.setSales(sale_pk);
				if (sale_pk.equals("public")) {
					client.setPublic_flg("Y");
				} else {
					client.setPublic_flg("N");
				}

				changeLog.setType(ResourcesConstants.CLIENT_CHANGE_SALE_TYPE_TRANSFER);
			}

			ClientEmployeeBean employeeOption = new ClientEmployeeBean();
			employeeOption.setFinancial_body_pk(company_pk);

			List<ClientEmployeeBean> PreEmployees = employeeDao.getAllByParam(employeeOption);

			if (null != PreEmployees) {
				for (ClientEmployeeBean employee : PreEmployees) {
					employee.setSales(sale_pk);

					if (sale_pk.equals("public")) {
						employee.setPublic_flg("Y");
					} else {
						employee.setPublic_flg("N");
					}

					employeeDao.update(employee);
				}
			}

			changeLog.setPre_sale_pk(pre_sale_pk);
			changeLog.setClient_pk(company_pk);
			changeLog.setChange_user(current_user);
			dao.update(client);
			clientChangeSaleLogDao.insert(changeLog);
		}
		return SUCCESS;
	}

	@Autowired
	private SaleOrderDAO saleOrderDao;

	@Override
	public String deleteClientReally(String client_pk) {
		ClientEmployeeBean employeeOption = new ClientEmployeeBean();
		employeeOption.setFinancial_body_pk(client_pk);

		List<ClientEmployeeBean> employees = employeeDao.getAllByParam(employeeOption);
		BudgetOrderBean orderOption = new BudgetOrderBean();

		// 查询每个客户下是否存在订单
		for (ClientEmployeeBean employee : employees) {
			orderOption.setClient_employee_pk(employee.getPk());
			List<BudgetOrderBean> orders = saleOrderDao.selectAllByParam(orderOption);

			if (null != orders && orders.size() > 0) {
				return "has_order";
			}
		}
		// 删除当前财务主体下的客户
		for (ClientEmployeeBean employee : employees) {
			employeeDao.delete(employee.getPk());
		}

		// 删除财务主体
		dao.delete(client_pk);
		return SUCCESS;
	}
}