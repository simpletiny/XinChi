package com.xinchi.backend.client.service.impl;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.AgencyFileDAO;
import com.xinchi.backend.client.dao.TravelAgencyDAO;
import com.xinchi.backend.client.service.TravelAgencyService;
import com.xinchi.bean.AgencyFileBean;
import com.xinchi.bean.TravelAgencyBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class TravelAgencyServiceImpl implements TravelAgencyService {

	@Autowired
	private TravelAgencyDAO dao;
	@Autowired
	private AgencyFileDAO agencyFileDao;

	@Override
	public void createAgency(TravelAgencyBean agency) {
		dao.insert(agency);
	}

	@Override
	public void saveAgencyFile(AgencyFileBean agencyFile) throws IOException {
		if (!isEmpty(agencyFile.getLicence_name())) {
			AgencyFileBean afb = new AgencyFileBean();
			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_name(agencyFile.getLicence_name());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_LICENCE);
			saveFile(afb);
		}
		if (!isEmpty(agencyFile.getPermit_name())) {
			AgencyFileBean afb = new AgencyFileBean();

			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_name(agencyFile.getPermit_name());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_PERMIT);
			saveFile(afb);
		}
		if (!isEmpty(agencyFile.getLiability_insurance_name())) {
			AgencyFileBean afb = new AgencyFileBean();

			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_name(agencyFile.getLiability_insurance_name());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_INSURANCE);
			saveFile(afb);
		}
		if (!isEmpty(agencyFile.getCorporate_name())) {
			AgencyFileBean afb = new AgencyFileBean();

			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_name(agencyFile.getCorporate_name());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_CORPORATE);
			saveFile(afb);
		}
		if (!isEmpty(agencyFile.getChief_name())) {
			AgencyFileBean afb = new AgencyFileBean();

			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_name(agencyFile.getChief_name());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_CHIEF);
			saveFile(afb);
		}
		if (!isEmpty(agencyFile.getOther_name())) {
			String[] names = agencyFile.getOther_name().split(";;;");
			for (String each : names) {
				if (isEmpty(each))
					continue;
				AgencyFileBean afb = new AgencyFileBean();

				afb.setAgency_pk(agencyFile.getAgency_pk());
				afb.setFile_name(each);
				afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_OTHER);
				saveFile(afb);
			}

		}
	}

	@Override
	public List<TravelAgencyBean> getAllAgencysByPage(Page page) {
		return dao.getAllByPage(page);
	}

	@Override
	public TravelAgencyBean selectByPrimaryKey(String agency_pk) {
		return dao.selectByPk(agency_pk);
	}

	@Override
	public List<AgencyFileBean> searchAgencyFilesByAgencyPk(String agency_pk) {
		return agencyFileDao.searchAgencyFilesByAgencyPk(agency_pk);
	}

	@Override
	public void updateAgency(TravelAgencyBean agency) {
		dao.update(agency);

	}

	@Override
	public void updateAgencyFile(AgencyFileBean agencyFile) throws IOException {
		List<AgencyFileBean> check = null;
		if (!isEmpty(agencyFile.getLicence_name())) {
			AgencyFileBean afb = new AgencyFileBean();
			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_LICENCE);
			check = agencyFileDao.selectByParam(afb);
			afb.setFile_name(agencyFile.getLicence_name());

			if (null != check && check.size() == 1) {
				AgencyFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(afb.getFile_name())) {
					agencyFileDao.delete(checkFile.getPk());
					saveFile(afb);
				}
			} else {
				saveFile(afb);
			}
		}
		if (!isEmpty(agencyFile.getPermit_name())) {
			AgencyFileBean afb = new AgencyFileBean();
			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_PERMIT);
			check = agencyFileDao.selectByParam(afb);
			afb.setFile_name(agencyFile.getPermit_name());

			if (null != check && check.size() == 1) {
				AgencyFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(afb.getFile_name())) {
					agencyFileDao.delete(checkFile.getPk());
					saveFile(afb);
				}
			} else {
				saveFile(afb);
			}
		}
		if (!isEmpty(agencyFile.getLiability_insurance_name())) {
			AgencyFileBean afb = new AgencyFileBean();
			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_INSURANCE);
			check = agencyFileDao.selectByParam(afb);
			afb.setFile_name(agencyFile.getLiability_insurance_name());

			if (null != check && check.size() == 1) {
				AgencyFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(afb.getFile_name())) {
					agencyFileDao.delete(checkFile.getPk());
					saveFile(afb);
				}
			} else {
				saveFile(afb);
			}
		}
		if (!isEmpty(agencyFile.getCorporate_name())) {
			AgencyFileBean afb = new AgencyFileBean();
			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_CORPORATE);
			check = agencyFileDao.selectByParam(afb);
			afb.setFile_name(agencyFile.getCorporate_name());

			if (null != check && check.size() == 1) {
				AgencyFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(afb.getFile_name())) {
					agencyFileDao.delete(checkFile.getPk());
					saveFile(afb);
				}
			} else {
				saveFile(afb);
			}
		}
		if (!isEmpty(agencyFile.getChief_name())) {
			AgencyFileBean afb = new AgencyFileBean();
			afb.setAgency_pk(agencyFile.getAgency_pk());
			afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_CHIEF);
			check = agencyFileDao.selectByParam(afb);
			afb.setFile_name(agencyFile.getChief_name());

			if (null != check && check.size() == 1) {
				AgencyFileBean checkFile = check.get(0);
				if (!checkFile.getFile_name().equals(afb.getFile_name())) {
					agencyFileDao.delete(checkFile.getPk());
					saveFile(afb);
				}
			} else {
				saveFile(afb);
			}
		}
		if (!isEmpty(agencyFile.getOther_name())) {
			String[] names = agencyFile.getOther_name().split(";;;");
			for (String each : names) {
				if (isEmpty(each))
					continue;
				AgencyFileBean afb = new AgencyFileBean();
				afb.setAgency_pk(agencyFile.getAgency_pk());
				afb.setFile_type(ResourcesConstants.AGENCY_FILE_TYPE_OTHER);
				afb.setFile_name(each);
				check = agencyFileDao.selectByParam(afb);

				if (null == check || check.size() == 0) {
					saveFile(afb);
				}
			}

		}
	}

	private void saveFile(AgencyFileBean afb) throws IOException {
		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty("agencyFileFolder");
		File sourceFile = new File(tempFolder + File.separator + afb.getFile_name());
		File destfile = new File(fileFolder + File.separator + afb.getAgency_pk() + File.separator + afb.getFile_name());
		FileUtils.copyFile(sourceFile, destfile);
		sourceFile.delete();
		agencyFileDao.insert(afb);
	}

	@Override
	public void deleteAgencyFile(String file_name, String agency_pk) {
		AgencyFileBean afb = new AgencyFileBean();
		afb.setAgency_pk(agency_pk);
		afb.setFile_name(file_name);
		deleteFile(afb);
		agencyFileDao.deleteFileByParam(afb);
	}

	private void deleteFile(AgencyFileBean sfb) {
		String fileFolder = PropertiesUtil.getProperty("agencyFileFolder");
		File destfile = new File(fileFolder + File.separator + sfb.getAgency_pk() + File.separator + sfb.getFile_name());
		destfile.delete();
	}

	@Override
	public void cancelAgency(String agency_pk) {
		TravelAgencyBean agency = selectByPrimaryKey(agency_pk);
		agency.setIs_cancel("Y");
		updateAgency(agency);
	}

	@Override
	public String checkSame(String content, String editType, String inputType, String agency_pk) {
		List<TravelAgencyBean> agencys = new ArrayList<TravelAgencyBean>();
		if (inputType.equals("NAME")) {
			agencys = dao.selectSameName(content);
		} else if (inputType.equals("CODE")) {
			agencys = dao.selectSameCode(content);
		}

		if (editType.equals("new")) {

			if (agencys != null && agencys.size() > 0) {
				return "EXIST";
			} else {
				return "NO";
			}
		} else if (editType.equals("edit")) {
			TravelAgencyBean agency = selectByPrimaryKey(agency_pk);
			if (agencys == null || agencys.size() == 0) {
				return "NO";
			} else if (agencys != null && agencys.size() > 0) {
				TravelAgencyBean old = agencys.get(0);
				if (old.getPk().equals(agency.getPk())) {
					return "NO";
				} else {
					return "EXIST";
				}
			}
		}
		return "NO";
	}
}