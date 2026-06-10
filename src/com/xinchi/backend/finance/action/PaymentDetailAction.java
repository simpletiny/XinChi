package com.xinchi.backend.finance.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.finance.service.ReceivedMatchService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.InnerTransferBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.ReceivedMatchBean;
import com.xinchi.common.BaseAction;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class PaymentDetailAction extends BaseAction {
	private static final long serialVersionUID = 3408762081387739186L;
	@Autowired
	private PaymentDetailService service;
	private PaymentDetailBean detail;

	public String createDetail() {
		resultStr = service.insert(detail);
		return SUCCESS;
	}

	private InnerTransferBean innerTransfer;

	/**
	 * 创建内转详情
	 * 
	 * @return
	 */
	public String createInnerDetail() {
		service.saveInnerDetail(innerTransfer);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	List<PaymentDetailBean> details;

	public String searchDetail() {
		details = service.getAllDetailsByParam(null);
		return SUCCESS;
	}

	public String searchDetailByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", detail);
		page.setParams(params);

		details = service.getAllDetailsByPage(page);
		return SUCCESS;
	}

	public String searchDetailByPk() {
		detail = service.selectByPk(detailId);
		return SUCCESS;
	}

	private String detailId;

	public String deleteDetail() {
		PaymentDetailBean detail = service.selectByPk(detailId);
		if (null != detail) {
			String inner_flg = detail.getInner_flg();
			if (inner_flg.equals("Y")) {
				List<PaymentDetailBean> details = service.selectByInnerPk(detail.getInner_pk());
				if (null != details && details.size() > 0) {
					for (PaymentDetailBean d : details) {
						resultStr = service.deleteDetail(d.getPk());
					}
				}
			} else if (inner_flg.equals("N")) {
				resultStr = service.deleteDetail(detailId);
			}

		} else {
			resultStr = "no exists";
		}
		return SUCCESS;
	}

	public String updateDetail() {
		resultStr = service.updateDetail(detail);
		return SUCCESS;
	}

	// 提交过来的file的名字
	private String fileName;

	public String importDetailExcel() {
		String fileFolder = PropertiesUtil.getProperty("tempUploadFolder");
		File file = new File(fileFolder + File.separator + fileName);
		resultStr = service.importDetailExcel(file);
		file.delete();
		resultStr = OK;
		return SUCCESS;
	}

	private String json;

	@Autowired
	private ReceivedMatchService rms;
	@Autowired
	private ReceivedService receivedService;

	public String matchReceived() {
		resultStr = service.matchReceived(json);
		return SUCCESS;
	}

	private ClientReceivedDetailBean received_detail;

	public String searchReceivedDetailByPaymentDetailPk() {
		List<ReceivedMatchBean> matchs = rms.selectByDetailPk(detailId);

		if (null != matchs && matchs.size() > 0) {
			String received_pk = matchs.get(0).getReceived_pk();
			ClientReceivedDetailBean receivedDetail = receivedService.selectByPk(received_pk);

			received_detail = receivedService.selectReceivedDetailByRelatedPk(receivedDetail.getRelated_pk());
		}

		return SUCCESS;
	}

	public String matchOtherReceived() {
		detail.setMatch_flg("O");
		resultStr = service.update(detail);
		return SUCCESS;
	}

	// 取消匹配
	public String cancelMatchReceived() {
		resultStr = service.cancelMatchReceived(detailId);
		return SUCCESS;
	}

	private String file_name;
	private String account;

	public String batUploadReceived() {
		String fileFolder = PropertiesUtil.getProperty("tempUploadFolder");
		File file = new File(fileFolder + File.separator + file_name);
		details = service.batUploadReceived(file);
		return SUCCESS;
	}

	public String batSaveReceived() {
		resultStr = service.batSaveReceived(account, json);
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

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public ClientReceivedDetailBean getReceived_detail() {
		return received_detail;
	}

	public void setReceived_detail(ClientReceivedDetailBean received_detail) {
		this.received_detail = received_detail;
	}

	public String getFile_name() {
		return file_name;
	}

	public String getAccount() {
		return account;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
