package com.xinchi.backend.client.service;

import java.io.IOException;
import java.util.List;

import com.xinchi.bean.AgencyFileBean;
import com.xinchi.bean.TravelAgencyBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "旅游公司")
public interface TravelAgencyService {
	@LogDescription(des = "创建旅游公司")
	public void createAgency(TravelAgencyBean agency);

	@LogDescription(ignore = true)
	public void saveAgencyFile(AgencyFileBean agencyFile) throws IOException;

	@LogDescription(des = "搜索旅游公司")
	public List<TravelAgencyBean> getAllAgencysByPage(Page page);

	@LogDescription(des = "查看旅游公司详情")
	public TravelAgencyBean selectByPrimaryKey(String agency_pk);

	@LogDescription(des = "上传旅游公司相关文件")
	public List<AgencyFileBean> searchAgencyFilesByAgencyPk(String agency_pk);

	@LogDescription(des = "更新旅游公司")
	public void updateAgency(TravelAgencyBean agency);

	@LogDescription(des = "更新旅游公司相关文件")
	public void updateAgencyFile(AgencyFileBean agencyFile) throws IOException;

	@LogDescription(des = "删除旅游公司相关文件")
	public void deleteAgencyFile(String file_name, String agency_pk);

	@LogDescription(des = "停用旅游公司")
	public void cancelAgency(String agency_pk);

	@LogDescription(ignore = true)
	public String checkSame(String content, String editType, String inputType, String agency_pk);

}
