package com.xinchi.backend.supplier.service.impl;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.supplier.dao.SupplierDAO;
import com.xinchi.backend.supplier.dao.SupplierFileDAO;
import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierFileBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private SupplierDAO dao;

	@Override
	@Transactional
	public String createSupplier(SupplierBean supplier) {
		dao.insert(supplier);
		return "success";
	}

	@Override
	public void insert(com.xinchi.bean.SupplierBean bo) {
		dao.insert(bo);
	}

	@Override
	@Transactional
	public void update(com.xinchi.bean.SupplierBean bo) {
		dao.update(bo);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public com.xinchi.bean.SupplierBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<com.xinchi.bean.SupplierBean> getAllCompaniesByParam(com.xinchi.bean.SupplierBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public String updateSupplier(SupplierBean supplier) {
		dao.update(supplier);
		return "success";
	}

	@Override
	public List<SupplierBean> getAllCompaniesByPage(Page<SupplierBean> page) {
		return dao.getAllByPage(page);
	}

	@Autowired
	private SupplierFileDAO supplierFileDAO;

	@Override
	@Transactional
	public void saveSupplierFile(SupplierFileBean supplierFile) throws IOException {
		if (!isEmpty(supplierFile.getLicence_name())) {
			SupplierFileBean sfb = new SupplierFileBean();
			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_name(supplierFile.getLicence_name());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_LICENCE);
			saveFile(sfb);
		}
		if (!isEmpty(supplierFile.getPermit_name())) {
			SupplierFileBean sfb = new SupplierFileBean();

			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_name(supplierFile.getPermit_name());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_PERMIT);
			saveFile(sfb);
		}
		if (!isEmpty(supplierFile.getLiability_insurance_name())) {
			SupplierFileBean sfb = new SupplierFileBean();

			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_name(supplierFile.getLiability_insurance_name());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_INSURANCE);
			saveFile(sfb);
		}
		if (!isEmpty(supplierFile.getCorporate_name())) {
			SupplierFileBean sfb = new SupplierFileBean();

			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_name(supplierFile.getCorporate_name());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_CORPORATE);
			saveFile(sfb);
		}
		if (!isEmpty(supplierFile.getChief_name())) {
			SupplierFileBean sfb = new SupplierFileBean();

			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_name(supplierFile.getChief_name());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_CHIEF);
			saveFile(sfb);
		}
		if (!isEmpty(supplierFile.getOther_name())) {
			String[] names = supplierFile.getOther_name().split(";;;");
			for (String each : names) {
				if (isEmpty(each))
					continue;
				SupplierFileBean sfb = new SupplierFileBean();

				sfb.setSupplier_pk(supplierFile.getSupplier_pk());
				sfb.setFile_name(each);
				sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_OTHER);
				saveFile(sfb);
			}

		}
	}

	@Override
	public List<SupplierFileBean> searchSupplierFilesBySupplierPk(String supplier_pk) {
		return supplierFileDAO.selectSupplierFilesBySupplierPk(supplier_pk);
	}

	private void saveFile(SupplierFileBean sfb) throws IOException {
		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty("supplierFileFolder");
		File sourceFile = new File(tempFolder + File.separator + sfb.getFile_name());
		File destfile = new File(
				fileFolder + File.separator + sfb.getSupplier_pk() + File.separator + sfb.getFile_name());
		FileUtils.copyFile(sourceFile, destfile);
		sourceFile.delete();
		supplierFileDAO.insert(sfb);
	}

	private void deleteFile(SupplierFileBean sfb) {
		String fileFolder = PropertiesUtil.getProperty("supplierFileFolder");
		File destfile = new File(
				fileFolder + File.separator + sfb.getSupplier_pk() + File.separator + sfb.getFile_name());
		destfile.delete();
	}

	@Override
	@Transactional
	public void updateSupplierFile(SupplierFileBean supplierFile) throws IOException {

		List<SupplierFileBean> check = null;
		if (!isEmpty(supplierFile.getLicence_name())) {
			SupplierFileBean sfb = new SupplierFileBean();
			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_LICENCE);
			check = supplierFileDAO.selectByParam(sfb);
			sfb.setFile_name(supplierFile.getLicence_name());

			if (null != check && check.size() == 1) {
				SupplierFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(sfb.getFile_name())) {
					supplierFileDAO.delete(checkFile.getPk());
					saveFile(sfb);
				}
			} else {
				saveFile(sfb);
			}
		}
		if (!isEmpty(supplierFile.getPermit_name())) {
			SupplierFileBean sfb = new SupplierFileBean();
			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_PERMIT);
			check = supplierFileDAO.selectByParam(sfb);
			sfb.setFile_name(supplierFile.getPermit_name());

			if (null != check && check.size() == 1) {
				SupplierFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(sfb.getFile_name())) {
					supplierFileDAO.delete(checkFile.getPk());
					saveFile(sfb);
				}
			} else {
				saveFile(sfb);
			}
		}
		if (!isEmpty(supplierFile.getLiability_insurance_name())) {
			SupplierFileBean sfb = new SupplierFileBean();
			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_INSURANCE);
			check = supplierFileDAO.selectByParam(sfb);
			sfb.setFile_name(supplierFile.getLiability_insurance_name());

			if (null != check && check.size() == 1) {
				SupplierFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(sfb.getFile_name())) {
					supplierFileDAO.delete(checkFile.getPk());
					saveFile(sfb);
				}
			} else {
				saveFile(sfb);
			}
		}
		if (!isEmpty(supplierFile.getCorporate_name())) {
			SupplierFileBean sfb = new SupplierFileBean();
			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_CORPORATE);
			check = supplierFileDAO.selectByParam(sfb);
			sfb.setFile_name(supplierFile.getCorporate_name());

			if (null != check && check.size() == 1) {
				SupplierFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(sfb.getFile_name())) {
					supplierFileDAO.delete(checkFile.getPk());
					saveFile(sfb);
				}
			} else {
				saveFile(sfb);
			}
		}
		if (!isEmpty(supplierFile.getChief_name())) {
			SupplierFileBean sfb = new SupplierFileBean();
			sfb.setSupplier_pk(supplierFile.getSupplier_pk());
			sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_CHIEF);
			check = supplierFileDAO.selectByParam(sfb);
			sfb.setFile_name(supplierFile.getChief_name());

			if (null != check && check.size() == 1) {
				SupplierFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(sfb.getFile_name())) {
					supplierFileDAO.delete(checkFile.getPk());
					saveFile(sfb);
				}
			} else {
				saveFile(sfb);
			}
		}
		if (!isEmpty(supplierFile.getOther_name())) {
			String[] names = supplierFile.getOther_name().split(";;;");
			for (String each : names) {
				if (isEmpty(each))
					continue;
				SupplierFileBean sfb = new SupplierFileBean();
				sfb.setSupplier_pk(supplierFile.getSupplier_pk());
				sfb.setFile_type(ResourcesConstants.SUPPLIER_FILE_TYPE_OTHER);
				sfb.setFile_name(each);
				check = supplierFileDAO.selectByParam(sfb);

				if (null == check || check.size() == 0) {
					saveFile(sfb);
				}
			}

		}
	}

	@Override
	public void deleteSupplierFile(String file_name, String supplier_pk) {
		SupplierFileBean sfb = new SupplierFileBean();
		sfb.setSupplier_pk(supplier_pk);
		sfb.setFile_name(file_name);
		deleteFile(sfb);
		supplierFileDAO.deleteFileByParam(sfb);
	}

	@Override
	public String blockSupplier(String supplier_pk) {
		SupplierBean s = dao.selectByPrimaryKey(supplier_pk);
		s.setIs_cooperate("N");
		dao.update(s);
		return SUCCESS;
	}
}