package com.xinchi.backend.client.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.dao.ClientUserDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientUserBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO dao;

	@Autowired
	private ClientUserDAO clientUserDao;

	@Override
	public String createCompany(ClientBean client) {
		ClientBean options = new ClientBean();
		options.setSales(client.getSales());
		options.setClient_name(client.getClient_name());

		List<ClientBean> exists = dao.getAllByParam(options);
		if (exists != null && exists.size() > 0)
			return "exist";

		dao.insert(client);
		// 记录客户和销售对应关系
		ClientUserBean cub = new ClientUserBean();
		cub.setClient_pk(client.getPk());
		cub.setUser_pk(client.getSales());
		clientUserDao.insert(cub);

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

	@Override
	public String changeClientSales(List<String> company_pks, List<String> sale_pks) {
		String main_user = sale_pks.get(0);

		for (String company_pk : company_pks) {
			ClientBean client = dao.selectByPrimaryKey(company_pk);

			// 删除之前存在的对应关系
			clientUserDao.deleteByClientPk(company_pk);

			if (main_user.equals("public")) {
				client.setPublic_flg("Y");
				// 保存新的对应关系
				ClientUserBean cub = new ClientUserBean();

				cub.setClient_pk(company_pk);
				cub.setUser_pk(ResourcesConstants.USER_PUBLIC);
				clientUserDao.insert(cub);
			} else {
				client.setPublic_flg("N");
				// 保存新的对应关系
				for (String sale_pk : sale_pks) {
					ClientUserBean cub = new ClientUserBean();

					cub.setClient_pk(company_pk);
					cub.setUser_pk(sale_pk);
					clientUserDao.insert(cub);
				}
			}

			ClientEmployeeBean employeeOption = new ClientEmployeeBean();
			employeeOption.setFinancial_body_pk(company_pk);

			List<ClientEmployeeBean> PreEmployees = employeeDao.getAllByParam(employeeOption);

			if (null != PreEmployees) {
				for (ClientEmployeeBean employee : PreEmployees) {
					employee.setSales(main_user);

					if (main_user.equals("public")) {
						employee.setPublic_flg("Y");
					} else {
						employee.setPublic_flg("N");
					}
					employeeDao.update(employee);
				}
			}
			dao.update(client);
		}
		return SUCCESS;
	}

	@Autowired
	private OrderDAO orderDao;

	@Override
	public String deleteClientReally(String client_pk) {
		ClientEmployeeBean employeeOption = new ClientEmployeeBean();
		employeeOption.setFinancial_body_pk(client_pk);

		List<ClientEmployeeBean> employees = employeeDao.getAllByParam(employeeOption);
		OrderDto orderOption = new OrderDto();

		// 查询每个客户下是否存在订单
		for (ClientEmployeeBean employee : employees) {
			orderOption.setClient_employee_pk(employee.getPk());
			List<OrderDto> orders = orderDao.selectByParam(orderOption);

			if (null != orders && orders.size() > 0) {
				return "has_order";
			}
		}
		// 删除当前财务主体下的客户
		for (ClientEmployeeBean employee : employees) {
			deleteOldHead(employee.getHead_photo());
			employeeDao.delete(employee.getPk());
			
		}

		// 删除财务主体
		dao.delete(client_pk);
		return SUCCESS;
	}

	@Override
	public String pureUpdate(ClientBean client) {
		dao.update(client);
		return SUCCESS;
	}

	@Override
	public List<ClientBean> selectCompaniesByPageAdmin(Page<ClientBean> page) {

		return dao.selectCompaniesByPageAdmin(page);
	}
	private void deleteOldHead(String fileName) {
		String fileFolder = PropertiesUtil.getProperty("clientEmployeeHeadFolder");
		String minFolder = PropertiesUtil.getProperty("clientEmployeeMinHeadFolder");

		File fullImg = new File(fileFolder + File.separator + fileName);
		File minImg = new File(minFolder + File.separator + fileName);

		fullImg.delete();
		minImg.delete();
	}
}