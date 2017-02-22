package com.xinchi.backend.client.action;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.TravelAgencyService;
import com.xinchi.bean.AgencyFileBean;
import com.xinchi.bean.TravelAgencyBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class TravelAgencyAction extends BaseAction {

	private static final long serialVersionUID = -7171311026157294079L;
	private TravelAgencyBean agency;
	private AgencyFileBean agencyFile;

	@Autowired
	private TravelAgencyService service;

	public String createAgency() throws IOException {
		service.createAgency(agency);
		agencyFile.setAgency_pk(agency.getPk());
		service.saveAgencyFile(agencyFile);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	private List<TravelAgencyBean> agencys;

	public String searchAgencyByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		if (null != agency && null != agency.getIs_exit() && !agency.getIs_exit().equals("")) {
			agency.setIs_exit(agency.getIs_exit().equals("有") ? "Y" : "N");
		}
		if (null != agency && isEmpty(agency.getIs_cancel())) {
			agency.setIs_cancel("N");
		}
		params.put("bo", agency);
		page.setParams(params);
		agencys = service.getAllAgencysByPage(page);
		return SUCCESS;
	}

	private String agency_pk;

	public String searchOneAgency() {
		agency = service.selectByPrimaryKey(agency_pk);
		return SUCCESS;
	}

	private List<AgencyFileBean> agencyFiles;

	public String searchAgencyFiles() {
		agencyFiles = service.searchAgencyFilesByAgencyPk(agency_pk);
		return SUCCESS;
	}

	public String updateAgency() throws IOException {
		service.updateAgency(agency);

		agencyFile.setAgency_pk(agency.getPk());
		service.updateAgencyFile(agencyFile);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	private String supplier_pk;

	private String file_name;

	public String deleteAgencyFile() {
		service.deleteAgencyFile(file_name, agency_pk);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public String cancelAgency() {
		service.cancelAgency(agency_pk);
		resultStr = "OK";
		return SUCCESS;
	}

	private String content;
	private String editType;
	private String inputType;

	public String checkAgencySame() {
		resultStr = service.checkSame(content, editType, inputType, agency_pk);
		return SUCCESS;
	}

	public String getSupplier_pk() {
		return supplier_pk;
	}

	public void setSupplier_pk(String supplier_pk) {
		this.supplier_pk = supplier_pk;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public TravelAgencyBean getAgency() {
		return agency;
	}

	public void setAgency(TravelAgencyBean agency) {
		this.agency = agency;
	}

	public AgencyFileBean getAgencyFile() {
		return agencyFile;
	}

	public void setAgencyFile(AgencyFileBean agencyFile) {
		this.agencyFile = agencyFile;
	}

	public List<TravelAgencyBean> getAgencys() {
		return agencys;
	}

	public void setAgencys(List<TravelAgencyBean> agencys) {
		this.agencys = agencys;
	}

	public String getAgency_pk() {
		return agency_pk;
	}

	public void setAgency_pk(String agency_pk) {
		this.agency_pk = agency_pk;
	}

	public List<AgencyFileBean> getAgencyFiles() {
		return agencyFiles;
	}

	public void setAgencyFiles(List<AgencyFileBean> agencyFiles) {
		this.agencyFiles = agencyFiles;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

}