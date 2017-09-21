package com.xinchi.backend.finance.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
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

	private String json;

	@Autowired
	private ReceivedMatchService rms;
	@Autowired
	private ReceivedService receivedService;

	public String matchReceived() {
		JSONObject obj = JSONObject.fromObject(json);
		String detail_id = obj.getString("detailId");
		JSONArray arr = obj.getJSONArray("arr");
		for (int i = 0; i < arr.size(); i++) {
			JSONObject receiveds = JSONObject.fromObject(arr.get(i));
			String related_pk = receiveds.getString("related_pk");

			List<ClientReceivedDetailBean> receivedDetails = receivedService.selectByRelatedPks(related_pk);
			for (ClientReceivedDetailBean detail : receivedDetails) {
				detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ENTER);
				detail.setConfirm_time(DateUtil.getMinStr());
				receivedService.update(detail);

				ReceivedMatchBean rmb = new ReceivedMatchBean();
				rmb.setDetail_pk(detail_id);
				rmb.setReceived_pk(detail.getPk());
				rms.insert(rmb);
			}
		}
		PaymentDetailBean thisDetail = pds.selectByPk(detail_id);
		thisDetail.setMatch_flg("Y");
		resultStr = pds.update(thisDetail);
		return SUCCESS;
	}

	public String matchOtherReceived() {

		PaymentDetailBean thisDetail = pds.selectByPk(detailId);
		// O for other received
		thisDetail.setMatch_flg("O");

		resultStr = pds.update(thisDetail);
		return SUCCESS;
	}

	// 取消匹配
	public String cancelMatchReceived() {
		// 更新收入详情
		detail = pds.selectByPk(detailId);
		detail.setMatch_flg("N");
		pds.update(detail);

		List<ReceivedMatchBean> rmbs = rms.selectByDetailPk(detailId);
		// 更新收入详表
		for (ReceivedMatchBean rmb : rmbs) {
			ClientReceivedDetailBean crdb = receivedService.selectByPk(rmb.getReceived_pk());
			crdb.setConfirm_time(null);
			crdb.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
			receivedService.update(crdb);
			// 删除匹配关联
			rms.delete(rmb.getPk());
		}

		resultStr = SUCCESS;
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
}
