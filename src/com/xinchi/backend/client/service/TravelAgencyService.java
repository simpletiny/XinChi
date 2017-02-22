package com.xinchi.backend.client.service;

import java.io.IOException;
import java.util.List;

import com.xinchi.bean.AgencyFileBean;
import com.xinchi.bean.TravelAgencyBean;
import com.xinchi.tools.Page;

public interface TravelAgencyService {

	public void createAgency(TravelAgencyBean agency);

	public void saveAgencyFile(AgencyFileBean agencyFile) throws IOException;

	public List<TravelAgencyBean> getAllAgencysByPage(Page page);

	public TravelAgencyBean selectByPrimaryKey(String agency_pk);

	public List<AgencyFileBean> searchAgencyFilesByAgencyPk(String agency_pk);

	public void updateAgency(TravelAgencyBean agency);

	public void updateAgencyFile(AgencyFileBean agencyFile) throws IOException;

	public void deleteAgencyFile(String file_name, String agency_pk);

	public void cancelAgency(String agency_pk);

	public String checkSame(String content, String editType, String inputType, String agency_pk);

}
