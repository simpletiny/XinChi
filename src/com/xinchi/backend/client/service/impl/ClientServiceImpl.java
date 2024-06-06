package com.xinchi.backend.client.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.backend.client.dao.ClientInoutImgDAO;
import com.xinchi.backend.client.dao.ClientUserDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientCountDto;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.bean.ClientInoutImgBean;
import com.xinchi.bean.ClientUserBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO dao;

	@Autowired
	private ClientUserDAO clientUserDao;

	@Autowired
	private ClientInoutImgDAO clientInoutImgDao;

	@Override
	public String createCompany(ClientBean client, String json) {
		ClientBean options = new ClientBean();
		// options.setSales(client.getSales());
		options.setClient_name(client.getClient_name());

		List<ClientBean> exists = dao.getAllByParam(options);
		// 改为验证公开状态下，和自己名下的重复
		// if (exists != null && exists.size() > 0)
		// return "exist";

		if (exists != null && exists.size() > 0) {
			for (ClientBean exist : exists) {
				if (exist.getSales().equals(ResourcesConstants.USER_PUBLIC)
						|| exist.getSales().equals(client.getSales())) {
					return "exist";
				}
			}
		}
		String client_pk = dao.insert(client);
		// 记录客户和销售对应关系
		ClientUserBean cub = new ClientUserBean();
		cub.setClient_pk(client.getPk());
		cub.setUser_pk(client.getSales());
		clientUserDao.insert(cub);

		// 记录内环境外环境图片

		if (!SimpletinyString.isEmpty(json)) {
			JSONObject imgs = JSONObject.fromObject(json);
			JSONArray outImgs = imgs.getJSONArray("outImgs");
			if (null != outImgs && outImgs.size() > 0) {
				for (int i = 0; i < outImgs.size(); i++) {
					String outImg = outImgs.getString(i);
					ClientInoutImgBean inout = new ClientInoutImgBean();
					inout.setClient_pk(client_pk);
					inout.setImg_name(outImg);
					inout.setImg_type(ClientInoutImgBean.IMG_TYPE_OUT);
					clientInoutImgDao.insert(inout);

					// 保存文件
					FileUtil.saveFile(outImg, FileFolder.CLIENT_INOUT_IMG.value(), client_pk);
				}
			}
			JSONArray inImgs = imgs.getJSONArray("inImgs");
			if (null != inImgs && inImgs.size() > 0) {
				for (int i = 0; i < inImgs.size(); i++) {
					String inImg = inImgs.getString(i);
					ClientInoutImgBean inout = new ClientInoutImgBean();
					inout.setClient_pk(client_pk);
					inout.setImg_name(inImg);
					inout.setImg_type(ClientInoutImgBean.IMG_TYPE_IN);
					clientInoutImgDao.insert(inout);

					// 保存文件
					FileUtil.saveFile(inImg, FileFolder.CLIENT_INOUT_IMG.value(), client_pk);
				}
			}

		}

		return SUCCESS;
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
	public String publicCompany(String pk) {
		ClientBean client = dao.selectByPrimaryKey(pk);

		// 删除之前存在的对应关系
		clientUserDao.deleteByClientPk(pk);

		// 公开财务主体名下的客户资料
		List<ClientEmployeeBean> employees = employeeDao.selectByClientPk(pk);
		for (ClientEmployeeBean employee : employees) {
			employee.setPublic_flg("Y");
			// 这个ID是个需要解决的问题，要么改成SimpletinyPUblic,要么系统分发到其他公司时，这是个问题。
			employee.setFinancial_body_pk("cn12cn13fHd3eXNxc3x6fw");
			// 删除之前存在的对应关系
			employeeUserDao.deleteByEmployeePk(employee.getPk());

			// 保存新的对应关系
			ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();
			ceub.setEmployee_pk(employee.getPk());
			ceub.setUser_pk(ResourcesConstants.USER_PUBLIC);
			employeeUserDao.insert(ceub);

			employeeDao.update(employee);
		}
		client.setPublic_flg("Y");
		// 保存新的对应关系
		ClientUserBean cub = new ClientUserBean();
		cub.setClient_pk(pk);
		cub.setUser_pk(ResourcesConstants.USER_PUBLIC);
		clientUserDao.insert(cub);
		return SUCCESS;
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
	public String updateCompany(ClientBean client, String json) {
		ClientBean options = new ClientBean();
		UserSessionBean user = SimpletinyUser.user();
		// options.setSales(client.getSales());
		options.setClient_name(client.getClient_name());

		List<ClientBean> exists = dao.getAllByParam(options);

		// 改为验证公开状态下，和自己名下的重复
		if (exists != null && exists.size() > 0) {
			for (ClientBean exist : exists) {
				if ((exist.getSales().contains(ResourcesConstants.USER_PUBLIC)
						|| exist.getSales().contains(user.getPk())) && !exist.getPk().equals(client.getPk())) {
					return "exist";
				}
			}
		}

		dao.update(client);

		List<ClientInoutImgBean> inouts = clientInoutImgDao.selectByClientPk(client.getPk());
		// 记录内环境外环境图片
		if (!SimpletinyString.isEmpty(json)) {
			JSONObject imgs = JSONObject.fromObject(json);
			JSONArray outImgs = imgs.getJSONArray("outImgs");
			if (null != outImgs && outImgs.size() > 0) {
				for (int i = 0; i < outImgs.size(); i++) {
					String outImg = outImgs.getString(i);
					boolean is_change = true;
					for (int j = inouts.size() - 1; j >= 0; j--) {
						if (inouts.get(j).getImg_name().equals(outImg)) {
							inouts.remove(j);
							is_change = false;
							break;
						}
					}
					if (is_change) {
						ClientInoutImgBean inout = new ClientInoutImgBean();
						inout.setClient_pk(client.getPk());
						inout.setImg_name(outImg);
						inout.setImg_type(ClientInoutImgBean.IMG_TYPE_OUT);
						clientInoutImgDao.insert(inout);
						// 保存文件
						FileUtil.saveFile(outImg, FileFolder.CLIENT_INOUT_IMG.value(), client.getPk());
					}
				}
			}
			JSONArray inImgs = imgs.getJSONArray("inImgs");
			if (null != inImgs && inImgs.size() > 0) {
				for (int i = 0; i < inImgs.size(); i++) {
					String inImg = inImgs.getString(i);
					boolean is_change = true;
					for (int j = inouts.size() - 1; j >= 0; j--) {
						if (inouts.get(j).getImg_name().equals(inImg)) {
							inouts.remove(j);
							is_change = false;
							break;
						}
					}
					if (is_change) {
						ClientInoutImgBean inout = new ClientInoutImgBean();
						inout.setClient_pk(client.getPk());
						inout.setImg_name(inImg);
						inout.setImg_type(ClientInoutImgBean.IMG_TYPE_IN);
						clientInoutImgDao.insert(inout);

						// 保存文件
						FileUtil.saveFile(inImg, FileFolder.CLIENT_INOUT_IMG.value(), client.getPk());
					}
				}
			}
		}

		// 删除不存在的图片

		for (int j = inouts.size() - 1; j >= 0; j--) {
			ClientInoutImgBean inout = inouts.get(j);
			String img_name = inout.getImg_name();
			FileUtil.deleteFile(img_name, FileFolder.CLIENT_INOUT_IMG.value(), client.getPk());

			clientInoutImgDao.delete(inout.getPk());
		}
		return SUCCESS;
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
	private ClientEmployeeUserDAO employeeUserDao;

	@Override
	public String changeClientSales(List<String> company_pks, List<String> sale_pks) {
		String main_user = sale_pks.get(0);

		for (String company_pk : company_pks) {
			ClientBean client = dao.selectByPrimaryKey(company_pk);

			// 删除之前存在的对应关系
			clientUserDao.deleteByClientPk(company_pk);

			if (main_user.equals("public")) {
				List<ClientEmployeeBean> employees = employeeDao.selectByClientPk(company_pk);
				for (ClientEmployeeBean employee : employees) {
					employee.setPublic_flg("Y");
					// 这个ID是个需要解决的问题，要么改成SimpletinyPUblic,要么系统分发到其他公司时，这是个问题。
					employee.setFinancial_body_pk("cn12cn13fHd3eXNxc3x6fw");
					// 删除之前存在的对应关系
					employeeUserDao.deleteByEmployeePk(employee.getPk());

					// 保存新的对应关系
					ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();
					ceub.setEmployee_pk(employee.getPk());
					ceub.setUser_pk(ResourcesConstants.USER_PUBLIC);
					employeeUserDao.insert(ceub);

					employeeDao.update(employee);
				}
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

	@Override
	public ClientCountDto selectCountByParam(ClientBean client) {

		return dao.selectCountByParam(client);
	}
}