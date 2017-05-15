package com.xinchi.backend.finance.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.bean.InnerTransferBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.Utils;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class PaymentDetailAction extends BaseAction {
	private static final long serialVersionUID = 3408762081387739186L;
	@Autowired
	private PaymentDetailService pds;
	private PaymentDetailBean detail;

	public String createDetail() {
		resultStr = pds.insert(detail);
		return SUCCESS;
	}

	private InnerTransferBean innerTransfer;

	/**
	 * 创建内转详情
	 * 
	 * @return
	 */
	public String createInnerDetail() {
		pds.saveInnerDetail(innerTransfer);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	List<PaymentDetailBean> details;

	public String searchDetail() {
		details = pds.getAllDetailsByParam(null);
		return SUCCESS;
	}

	public String searchDetailByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", detail);
		page.setParams(params);

		details = pds.getAllDetailsByPage(page);
		return SUCCESS;
	}

	public String searchDetailByPk() {
		detail = pds.selectByPk(detailId);
		return SUCCESS;
	}

	private String detailId;

	public String deleteDetail() {
		resultStr = pds.deleteDetail(detailId);
		return SUCCESS;
	}

	public String updateDetail() {
		resultStr = pds.updateDetail(detail);
		return SUCCESS;
	}

	// 提交过来的file的名字
	private String fileName;

	public String importDetailExcel() {
		String fileFolder = PropertiesUtil.getProperty("tempUploadFolder");
		File file = new File(fileFolder + File.separator + fileName);
		resultStr = pds.importDetailExcel(file);
		file.delete();
		resultStr = OK;
		return SUCCESS;
	}

	public PaymentDetailBean getDetail() {
		return detail;
	}

	public void setDetail(PaymentDetailBean detail) {
		this.detail = detail;
	}

	public List<PaymentDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<PaymentDetailBean> details) {
		this.details = details;
	}

	public InnerTransferBean getInnerTransfer() {
		return innerTransfer;
	}

	public void setInnerTransfer(InnerTransferBean innerTransfer) {
		this.innerTransfer = innerTransfer;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
