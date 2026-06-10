package com.xinchi.backend.client.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.dao.PreciseEmployeeDAO;
import com.xinchi.backend.client.service.PreciseEmployeeService;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.PreciseClientEmployeeBean;
import com.xinchi.bean.PreciseEmployeeBindingBean;
import com.xinchi.common.APIUtil;
import com.xinchi.common.FileUtil;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class PreciseEmployeeServiceImpl implements PreciseEmployeeService {

	@Autowired
	private PreciseEmployeeDAO dao;

	@Override
	public String insert(PreciseClientEmployeeBean bean) {
		dao.insert(bean);

		return SUCCESS;
	}

	@Override
	public String update(PreciseClientEmployeeBean bean) {
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public PreciseClientEmployeeBean selectByPrimaryKey(String pk) {
		return dao.selectByPrimaryKey(pk);
	}

	@Override
	public List<PreciseClientEmployeeBean> selectByParam(PreciseClientEmployeeBean option) {
		return dao.selectByParam(option);
	}

	@Override
	public String createPreciseEmployee(PreciseClientEmployeeBean employee) {
		// 实名认证，如果没有填写身份证号，则不验证。
		if (!SimpletinyString.isEmpty(employee.getId())) {
			// 检测同名和同ID
			PreciseClientEmployeeBean option1 = new PreciseClientEmployeeBean();
			option1.setCellphone(employee.getName());
			option1.setId(employee.getId());
			List<PreciseClientEmployeeBean> exists1 = dao.selectByParam(option1);
			if (exists1 != null && exists1.size() > 0) {
				return "existsamenameid";
			}
			String id_check_str = APIUtil.checkIDFromAPI(employee.getName(), employee.getId());
			if (!id_check_str.equals(SUCCESS)) {
				return id_check_str;
			}
			employee.setIs_verified("Y");
		}

		// 检测相同手机号
		PreciseClientEmployeeBean option2 = new PreciseClientEmployeeBean();
		option2.setCellphone(employee.getCellphone());
		List<PreciseClientEmployeeBean> exists2 = dao.selectByParam(option2);
		if (exists2 != null && exists2.size() > 0) {
			return "existcellphone";
		}
		// 保存客户照片
		if (SimpletinyString.isEmpty(employee.getSelf_photo())) {
			employee.setSelf_photo(null);
		} else {
			FileUtil.saveFile(employee.getSelf_photo(), "preciseEmployeeSelfPhotoFolder");
		}

		// 保存客户其他照片
		if (SimpletinyString.isEmpty(employee.getOther_photo())) {
			employee.setOther_photo(null);
		} else {
			FileUtil.saveFile(employee.getOther_photo(), "preciseEmployeeOtherPhotoFolder");
		}
		// 保存客户头像
		if (SimpletinyString.isEmpty(employee.getHead_photo())) {
			employee.setHead_photo(null);
		} else {
			saveHeadFile(employee.getHead_photo());
		}
		dao.insert(employee);
		return SUCCESS;
	}

	@Override
	public String updatePreciseEmployee(PreciseClientEmployeeBean employee) {
		PreciseClientEmployeeBean old_employee = dao.selectByPrimaryKey(employee.getPk());

		// 实名认证，如果没有填写身份证号，则不验证。
		if (!SimpletinyString.isEmpty(employee.getId())) {
			if (old_employee.getId().equals(employee.getId()) || old_employee.getName().equals(employee.getName())) {
				// 检测同名和同ID
				PreciseClientEmployeeBean option1 = new PreciseClientEmployeeBean();
				option1.setCellphone(employee.getName());
				option1.setId(employee.getId());
				List<PreciseClientEmployeeBean> exists1 = dao.selectByParam(option1);
				if (exists1 != null && exists1.size() > 0) {
					for (PreciseClientEmployeeBean pce : exists1) {
						if (!pce.getPk().equals(employee.getPk())) {
							return "existsamenameid";
						}
					}
				}

				String id_check_str = APIUtil.checkIDFromAPI(employee.getName(), employee.getId());
				if (!id_check_str.equals(SUCCESS)) {
					return id_check_str;
				}
				employee.setIs_verified("Y");
			}
		} else {
			employee.setIs_verified("N");
		}

		// 检测相同手机号
		PreciseClientEmployeeBean option2 = new PreciseClientEmployeeBean();
		option2.setCellphone(employee.getCellphone());
		List<PreciseClientEmployeeBean> exists2 = dao.selectByParam(option2);
		if (exists2 != null && exists2.size() > 0) {
			for (PreciseClientEmployeeBean pce : exists2) {
				if (!pce.getPk().equals(employee.getPk())) {
					return "existcellphone";
				}
			}
		}
		// 保存客户照片
		if (SimpletinyString.isEmpty(employee.getSelf_photo())) {
			if (!SimpletinyString.isEmpty(old_employee.getSelf_photo())) {
				FileUtil.deleteFile(old_employee.getSelf_photo(), "preciseEmployeeSelfPhotoFolder");
			}
			employee.setSelf_photo(null);
		} else {
			if (SimpletinyString.isEmpty(old_employee.getSelf_photo())) {
				FileUtil.saveFile(employee.getSelf_photo(), "preciseEmployeeSelfPhotoFolder");
			} else {
				if (!old_employee.getSelf_photo().equals(employee.getSelf_photo())) {
					FileUtil.saveFile(employee.getSelf_photo(), "preciseEmployeeSelfPhotoFolder");
					FileUtil.deleteFile(old_employee.getSelf_photo(), "preciseEmployeeSelfPhotoFolder");
				}
			}
		}
		// 保存客户其他照片
		if (SimpletinyString.isEmpty(employee.getOther_photo())) {
			if (!SimpletinyString.isEmpty(old_employee.getOther_photo())) {
				FileUtil.deleteFile(old_employee.getOther_photo(), "preciseEmployeeOtherPhotoFolder");
			}
			employee.setOther_photo(null);
		} else {
			if (SimpletinyString.isEmpty(old_employee.getOther_photo())) {
				FileUtil.saveFile(employee.getOther_photo(), "preciseEmployeeOtherPhotoFolder");
			} else {
				if (!old_employee.getOther_photo().equals(employee.getOther_photo())) {
					FileUtil.saveFile(employee.getOther_photo(), "preciseEmployeeOtherPhotoFolder");
					FileUtil.deleteFile(old_employee.getOther_photo(), "preciseEmployeeOtherPhotoFolder");
				}
			}
		}
		// 保存客户头像
		if (SimpletinyString.isEmpty(employee.getHead_photo()) || employee.getHead_photo().equals("img")) {
			if (!SimpletinyString.isEmpty(old_employee.getHead_photo()) && !old_employee.getHead_photo().equals("img")) {
				FileUtil.deleteFile(old_employee.getHead_photo(), "preciseEmployeeHeadFolder");
				FileUtil.deleteFile(old_employee.getHead_photo(), "preciseEmployeeMinHeadFolder");
			}
			employee.setHead_photo("img");
		} else {
			if (SimpletinyString.isEmpty(old_employee.getHead_photo()) || old_employee.getHead_photo().equals("img")) {
				saveHeadFile(employee.getHead_photo());
			} else {
				if (!old_employee.getHead_photo().equals(employee.getHead_photo())) {
					saveHeadFile(employee.getHead_photo());
					FileUtil.deleteFile(old_employee.getHead_photo(), "preciseEmployeeHeadFolder");
					FileUtil.deleteFile(old_employee.getHead_photo(), "preciseEmployeeMinHeadFolder");
				}
			}
		}
		dao.update(employee);
		return SUCCESS;
	}

	private void saveHeadFile(String fileName) {
		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty("preciseEmployeeHeadFolder");

		File sourceFile = new File(tempFolder + File.separator + fileName);
		File destfile = new File(fileFolder + File.separator + fileName);
		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 压缩头像
		String minFolder = PropertiesUtil.getProperty("preciseEmployeeMinHeadFolder");
		File minDestFile = new File(minFolder + File.separator + fileName);
		FileUtil.reduceImg(sourceFile, minDestFile, 100, 100, new Float(0.3));

		sourceFile.delete();
	}

	@Override
	public List<PreciseClientEmployeeBean> selectByPage(Page<PreciseClientEmployeeBean> page) {

		return dao.selectByPage(page);
	}

	@Autowired
	private EmployeeDAO employeeDAO;

	@Override
	public String bindingPreciseEmployees(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		String precise_pk = obj.getString("precise_pk");
		JSONArray employee_pks = obj.getJSONArray("employee_pks");
		for (int i = 0; i < employee_pks.size(); i++) {
			String employee_pk = employee_pks.getString(i);
			dao.deleteBindingByEmployeePk(employee_pk);
			PreciseEmployeeBindingBean peb = new PreciseEmployeeBindingBean();
			peb.setPrecise_pk(precise_pk);
			peb.setEmployee_pk(employee_pk);
			dao.insertBinding(peb);
			// 更新客户资料关联状态
			ClientEmployeeBean employee = new ClientEmployeeBean();
			employee.setPk(employee_pk);
			employee.setBinding_status("Y");
			employeeDAO.update(employee);
		}

		// 更新精准客户状态
		List<PreciseEmployeeBindingBean> pebs = dao.selectBindingsByPrecisePk(precise_pk);
		PreciseClientEmployeeBean pce = new PreciseClientEmployeeBean();
		pce.setPk(precise_pk);
		pce.setBinding_status("Y");
		pce.setBinding_count(pebs.size());
		dao.update(pce);
		return SUCCESS;
	}

	@Override
	public String deletePreciseEmployee(String employee_pk) {
		// 删除客户相关文件
		PreciseClientEmployeeBean old = dao.selectByPrimaryKey(employee_pk);
		// 删除头像
		if (!SimpletinyString.isEmpty(old.getHead_photo()) && !old.getHead_photo().equals("img")) {
			FileUtil.deleteFile(old.getHead_photo(), "preciseEmployeeHeadFolder");
			FileUtil.deleteFile(old.getHead_photo(), "preciseEmployeeMinHeadFolder");
		}
		// 删除本人照片
		if (!SimpletinyString.isEmpty(old.getSelf_photo())) {
			FileUtil.deleteFile(old.getSelf_photo(), "preciseEmployeeSelfPhotoFolder");
		}
		// 删除其他照片
		if (!SimpletinyString.isEmpty(old.getOther_photo())) {
			FileUtil.deleteFile(old.getOther_photo(), "preciseEmployeeOtherPhotoFolder");
		}
		// 修改相关客户资料的关联状态
		List<PreciseEmployeeBindingBean> pebs = dao.selectBindingsByPrecisePk(employee_pk);
		for(PreciseEmployeeBindingBean peb:pebs) {
			ClientEmployeeBean employee = new ClientEmployeeBean();
			employee.setPk(peb.getEmployee_pk());
			employee.setBinding_status("N");
			employeeDAO.update(employee);
		}
		// 删除关联关系
		dao.deleteBindingByPrecisePk(employee_pk);
		// 删除精准客户
		dao.delete(employee_pk);
		return SUCCESS;
	}

}