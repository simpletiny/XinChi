package com.xinchi.backend.finance.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.backend.finance.dao.ReceivedMatchDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.receivable.dao.AirReceivedDAO;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.InnerTransferBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.ReceivedMatchBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class PaymentDetailServiceImpl implements PaymentDetailService {
	@Autowired
	private PaymentDetailDAO dao;

	@Autowired
	private CardDAO cardDao;

	@Override
	public String insert(PaymentDetailBean detail) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		detail.setRecord_user(sessionBean.getUser_number());
		detail.setRecord_time(DateUtil.getTimeMillis());

		// check same time in same account
		PaymentDetailBean time = new PaymentDetailBean();
		time.setTime(detail.getTime());
		time.setAccount(detail.getAccount());

		List<PaymentDetailBean> sameDetail = dao.selectAllDetailsByParam(time);

		if (null != sameDetail && sameDetail.size() > 0
				&& (null == detail.getInner_flg() || detail.getInner_flg().equals("N"))) {
			return "time";
		}

		List<PaymentDetailBean> afterDetails = dao.selectAfterByParam(detail);
		BigDecimal wrong = detail.getMoney();
		if (detail.getType().equals("支出")) {
			wrong = wrong.multiply(BigDecimal.valueOf(-1));
		}
		for (PaymentDetailBean d : afterDetails) {
			d.setBalance(d.getBalance().add(wrong));
		}
		dao.updateDetails(afterDetails);

		PaymentDetailBean beforeDetail = dao.selectPreDetail(detail);

		CardBean card = cardDao.getCardByAccount(detail.getAccount());

		if (null == beforeDetail) {
			detail.setBalance(card.getInit_money());
		} else {
			// 判断如果之前这一笔是内转，就要考虑汇兑应该是正确的承接关系
			if (beforeDetail.getInner_flg().equals("Y")) {
				List<PaymentDetailBean> inners = dao.selectByInnerPk(beforeDetail.getInner_pk());
				for (PaymentDetailBean pdb : inners) {
					if (pdb.getAccount().equals(detail.getAccount()) && pdb.getExchange_flg().equals("Y")) {
						beforeDetail = pdb;
					}
				}
			}
			detail.setBalance(beforeDetail.getBalance().add(wrong));
		}

		card.setBalance(card.getBalance().add(wrong));
		cardDao.update(card);

		// 保存凭证文件
		if (!SimpletinyString.isEmpty(detail.getVoucher_file_name())) {
			String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
			String fileFolder = PropertiesUtil.getProperty("voucherFileFolder");
			File sourceFile = new File(tempFolder + File.separator + detail.getVoucher_file_name());
			File destfile = new File(
					fileFolder + File.separator + card.getPk() + File.separator + detail.getVoucher_file_name());
			try {
				FileUtils.copyFile(sourceFile, destfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sourceFile.delete();
		}
		dao.insert(detail);
		return "success";
	}

	@Override
	public List<PaymentDetailBean> getAllDetailsByParam(PaymentDetailBean bean) {

		return dao.selectAllDetailsByParam(bean);
	}

	@Override
	public List<PaymentDetailBean> getAllDetailsByPage(Page<PaymentDetailBean> page) {
		return dao.selectAllDetailsByPage(page);
	}

	@Override
	public void saveInnerDetail(InnerTransferBean innerTransfer) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String inner_pk = DBCommonUtil.genPk();

		// 支出明细
		PaymentDetailBean payDetail = new PaymentDetailBean();
		payDetail.setAccount(innerTransfer.getFrom_account());
		payDetail.setTime(innerTransfer.getFrom_time());
		payDetail.setType("支出");
		payDetail.setMoney(innerTransfer.getFrom_money());
		payDetail.setRecord_user(sessionBean.getUser_number());
		payDetail.setRecord_time(DateUtil.getTimeMillis());
		payDetail.setComment(innerTransfer.getComment());
		payDetail.setInner_flg("Y");
		payDetail.setFinance_flg("Y");
		payDetail.setInner_pk(inner_pk);

		// 收入明细
		PaymentDetailBean receiveDetail = new PaymentDetailBean();

		receiveDetail.setAccount(innerTransfer.getTo_account());
		receiveDetail.setTime(innerTransfer.getTo_time());
		receiveDetail.setType("收入");
		receiveDetail.setMoney(innerTransfer.getTo_money());
		receiveDetail.setRecord_user(sessionBean.getUser_number());
		receiveDetail.setRecord_time(DateUtil.getTimeMillis());
		receiveDetail.setComment(innerTransfer.getComment());
		receiveDetail.setInner_flg("Y");
		receiveDetail.setFinance_flg("Y");
		receiveDetail.setInner_pk(inner_pk);

		insert(payDetail);
		insert(receiveDetail);

		// 汇兑明细
		if (null != innerTransfer.getExchange_account()) {
			PaymentDetailBean exchangeDetail = new PaymentDetailBean();

			if (innerTransfer.getExchange_account().equals("out")) {
				exchangeDetail.setAccount(innerTransfer.getFrom_account());
				exchangeDetail.setTime(innerTransfer.getFrom_time());
			} else {
				exchangeDetail.setAccount(innerTransfer.getTo_account());
				exchangeDetail.setTime(innerTransfer.getTo_time());
			}

			exchangeDetail.setType("支出");
			exchangeDetail.setMoney(innerTransfer.getExchange_money());
			exchangeDetail.setRecord_user(sessionBean.getUser_number());
			exchangeDetail.setRecord_time(DateUtil.getTimeMillis());
			exchangeDetail.setComment("汇兑：" + innerTransfer.getComment());
			exchangeDetail.setInner_flg("Y");
			exchangeDetail.setInner_pk(inner_pk);
			exchangeDetail.setExchange_flg("Y");
			exchangeDetail.setFinance_flg("Y");

			insert(exchangeDetail);
		}
	}

	@Override
	public String deleteDetail(String detailId) {
		PaymentDetailBean detail = dao.selectById(detailId);
		if (detail.getMatch_flg().equals("Y")) {
			return "matched";
		}
		/*
		 * if (detail.getFinance_flg().equals("N")) return "forbidden";
		 */

		List<PaymentDetailBean> afterDetails = dao.selectAfterByParam(detail);
		BigDecimal wrong = detail.getMoney();
		if (detail.getType().equals("收入")) {
			wrong = wrong.multiply(BigDecimal.valueOf(-1));
		}
		for (PaymentDetailBean d : afterDetails) {
			d.setBalance(d.getBalance().add(wrong));
		}
		CardBean card = cardDao.getCardByAccount(detail.getAccount());
		if (null != card) {
			card.setBalance(card.getBalance().add(wrong));
			cardDao.update(card);
		}
		dao.updateDetails(afterDetails);
		dao.delete(detailId);

		// 删除凭证文件，如果有的话
		if (!SimpletinyString.isEmpty(detail.getVoucher_file_name())) {
			String fileFolder = PropertiesUtil.getProperty("voucherFileFolder");
			File destfile = new File(
					fileFolder + File.separator + card.getPk() + File.separator + detail.getVoucher_file_name());
			if (destfile.exists()) {
				destfile.delete();
			}
		}

		return "success";
	}

	@Override
	public PaymentDetailBean selectByPk(String detailId) {
		return dao.selectById(detailId);
	}

	@Override
	public String updateDetail(PaymentDetailBean newDetail) {
		// check same time
		PaymentDetailBean time = new PaymentDetailBean();
		time.setTime(newDetail.getTime());
		time.setAccount(newDetail.getAccount());

		List<PaymentDetailBean> sameDetail = dao.selectAllDetailsByParam(time);

		if (null != sameDetail && sameDetail.size() > 0) {
			for (PaymentDetailBean detail : sameDetail) {
				if (!detail.getPk().equals(newDetail.getPk())) {
					return "time";
				}
			}
		}

		PaymentDetailBean oldDetail = dao.selectById(newDetail.getPk());
		List<PaymentDetailBean> oldAfterDetails = dao.selectAfterByParam(oldDetail);
		BigDecimal wrong = oldDetail.getMoney();
		if (oldDetail.getType().equals("收入")) {
			wrong = wrong.multiply(BigDecimal.valueOf(-1));
		}
		for (PaymentDetailBean d : oldAfterDetails) {
			d.setBalance(d.getBalance().add(wrong));
		}
		dao.updateDetails(oldAfterDetails);

		List<PaymentDetailBean> newAfterDetails = dao.selectAfterByParam(newDetail);
		BigDecimal insert = newDetail.getMoney();
		if (newDetail.getType().equals("支出")) {
			insert = insert.multiply(BigDecimal.valueOf(-1));
		}

		for (PaymentDetailBean d : newAfterDetails) {
			d.setBalance(d.getBalance().add(insert));
		}
		dao.updateDetails(newAfterDetails);

		// 获取上一笔明细
		PaymentDetailBean preDetail = dao.selectPreDetail(newDetail);
		CardBean newCard = cardDao.getCardByAccount(newDetail.getAccount());
		if (null == preDetail) {
			newDetail.setBalance(newCard.getInit_money().add(insert));
		} else {
			newDetail.setBalance(preDetail.getBalance().add(insert));
		}

		dao.updateDetail(newDetail);

		// 更新账户
		CardBean oldCard = cardDao.getCardByAccount(oldDetail.getAccount());
		if (null != oldCard) {
			oldCard.setBalance(oldCard.getBalance().add(wrong));
			cardDao.update(oldCard);
		}

		if (null != newCard) {
			newCard.setBalance(newCard.getBalance().add(insert));
			cardDao.update(newCard);
		}
		return "success";
	}

	@Override
	public String importDetailExcel(File file) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		try {
			InputStream is = new FileInputStream(file);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			String card_number = getValue(hssfSheet.getRow(1).getCell(1));
			CardBean card = cardDao.getCardByNumber(card_number);
			String bank_nickname = card.getBank_nickname();
			if (bank_nickname.equals(ResourcesConstants.BANK_ABC)) {
				details = readABC(hssfSheet);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_BCM)) {
				details = readBCM(hssfSheet);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_BOC)) {
				details = readBOC(hssfSheet, card);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_CCB)) {
				details = readCCB(hssfSheet, card);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_CMB)) {
				details = readCMB(hssfSheet);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_HRB)) {
				details = readHRB(hssfSheet);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_ICBC)) {
				details = readICBC(hssfSheet);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_MY)) {
				details = readMY(hssfSheet);
			} else if (bank_nickname.equals(ResourcesConstants.BANK_PSBC)) {
				details = readPSBC(hssfSheet);
			}
			hssfWorkbook.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dao.insertDetails(details);
		return null;
	}

	// 读取交行文件
	private List<PaymentDetailBean> readBCM(HSSFSheet hssfSheet) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		return details;
	}

	// 读取招行文件
	private List<PaymentDetailBean> readCMB(HSSFSheet hssfSheet) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		return details;
	}

	// 网商
	private List<PaymentDetailBean> readMY(HSSFSheet hssfSheet) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		return details;
	}

	// 哈尔滨银行
	private List<PaymentDetailBean> readHRB(HSSFSheet hssfSheet) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		return details;
	}

	// 工商银行
	private List<PaymentDetailBean> readICBC(HSSFSheet hssfSheet) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		return details;
	}

	// 中行
	private List<PaymentDetailBean> readBOC(HSSFSheet sheet, CardBean card) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String record_user = sessionBean.getUser_number();
		String record_time = DateUtil.getTimeMillis();
		for (int i = 9; i < sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			PaymentDetailBean detail = new PaymentDetailBean();
			detail.setAccount(card.getAccount());
			String date = getValue(row.getCell(10));
			String time = getValue(row.getCell(11));
			detail.setTime(combineTime(date, time));

			if (getValue(row.getCell(0)).equals("往账")) {
				detail.setType("支出");
			} else {
				detail.setType("收入");
			}

			detail.setMoney(new BigDecimal(getValue(row.getCell(13)).replaceAll(",", "")).abs());

			detail.setBalance(new BigDecimal(getValue(row.getCell(14)).replaceAll(",", "")));
			detail.setRecord_user(record_user);
			detail.setRecord_time(record_time);
			detail.setInner_flg("N");
			String comment = "";
			comment += "摘要:" + getValue(row.getCell(23));
			comment += "业务类型:" + getValue(row.getCell(1));
			comment += "付款人名称：" + getValue(row.getCell(5));
			comment += "付款人账号：" + getValue(row.getCell(4));
			comment += "付款人开户行：" + getValue(row.getCell(3));
			comment += "用途：" + getValue(row.getCell(24));
			comment += "备注：" + getValue(row.getCell(26));
			detail.setComment(comment);
			details.add(detail);
		}
		return details;
	}

	// 邮储银行
	private List<PaymentDetailBean> readPSBC(HSSFSheet hssfSheet) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		return details;
	}

	// 农行
	private List<PaymentDetailBean> readABC(HSSFSheet hssfSheet) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		return details;
	}

	// 建行
	private List<PaymentDetailBean> readCCB(HSSFSheet sheet, CardBean card) {
		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String record_user = sessionBean.getUser_number();
		String record_time = DateUtil.getTimeMillis();
		for (int i = 6; i < sheet.getLastRowNum() - 1; i++) {
			HSSFRow row = sheet.getRow(i);
			PaymentDetailBean detail = new PaymentDetailBean();
			detail.setAccount(card.getAccount());
			String date = getValue(row.getCell(1));
			String time = getValue(row.getCell(2));
			detail.setTime(combineTime(date, time));

			if (getValue(row.getCell(4)).equals("0.0")) {
				detail.setType("收入");
				detail.setMoney(new BigDecimal(getValue(row.getCell(5))));
			} else {
				detail.setType("支出");
				detail.setMoney(new BigDecimal(getValue(row.getCell(4))));
			}
			detail.setBalance(new BigDecimal(getValue(row.getCell(6))));
			detail.setRecord_user(record_user);
			detail.setRecord_time(record_time);
			detail.setInner_flg("N");
			String comment = "";
			comment += "摘要:" + getValue(row.getCell(10));
			comment += "对方户名：" + getValue(row.getCell(8));
			comment += "对方账户：" + getValue(row.getCell(7));
			comment += "交易地点：" + getValue(row.getCell(3));
			detail.setComment(comment);
			details.add(detail);
		}
		return details;
	}

	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == CellType.NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	private String combineTime(String date, String time) {
		String result = "";
		result += date.substring(0, 4);
		result += "-";
		result += date.substring(4, 6);
		result += "-";
		result += date.substring(6, 8);
		result += " " + time;
		return result;
	}

	@Override
	public List<PaymentDetailBean> selectByVoucherNumber(String voucher_number) {

		return dao.selectByVoucherNumber(voucher_number);
	}

	@Override
	public String update(PaymentDetailBean detail) {
		dao.updateDetail(detail);
		return SUCCESS;
	}

	@Override
	public List<PaymentDetailBean> selectByInnerPk(String inner_pk) {

		return dao.selectByInnerPk(inner_pk);
	}

	@Autowired
	private ReceivedDAO receivedDao;

	@Autowired
	private PaidDAO paidDao;

	@Autowired
	private AirTicketPaidDetailDAO airTicketPaidDetailDao;

	@Autowired
	private ReceivedMatchDAO receivedMatchDao;

	@Autowired
	private AirReceivedDAO airReceivedDao;

	@Override
	public String matchReceived(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		String detail_id = obj.getString("detailId");
		JSONArray arr = obj.getJSONArray("arr");
		UserSessionBean user = SimpletinyUser.user();
		for (int i = 0; i < arr.size(); i++) {
			JSONObject receiveds = JSONObject.fromObject(arr.get(i));
			String related_pk = receiveds.getString("related_pk");
			String from_where = receiveds.getString("from_where");
			if (from_where.equals(ResourcesConstants.RECEIVED_FROM_WHERE_CLIENT)) {

				List<ClientReceivedDetailBean> receivedDetails = receivedDao.selectByRelatedPks(related_pk);
				for (ClientReceivedDetailBean detail : receivedDetails) {
					detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ENTER);
					detail.setConfirm_time(DateUtil.getMinStr());
					receivedDao.update(detail);

					saveReceivedMatch(detail_id, from_where, detail.getPk());
				}
			} else if (from_where.equals(ResourcesConstants.RECEIVED_FROM_WHERE_SUPPLIER)) {
				List<SupplierPaidDetailBean> receivedDetails = paidDao.selectSupplierPaidDetailByRelatedPk(related_pk);
				for (SupplierPaidDetailBean detail : receivedDetails) {
					detail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
					detail.setConfirm_time(DateUtil.getMinStr());
					paidDao.update(detail);

					saveReceivedMatch(detail_id, from_where, detail.getPk());
				}
			} else if (from_where.equals(ResourcesConstants.RECEIVED_FROM_WHERE_AIR_TICKET)) {
				List<AirTicketPaidDetailBean> receivedDetails = airTicketPaidDetailDao.selectByRelatedPk(related_pk);
				for (AirTicketPaidDetailBean detail : receivedDetails) {
					detail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
					detail.setConfirm_time(DateUtil.getMinStr());
					detail.setApprove_user(user.getUser_number());
					airTicketPaidDetailDao.update(detail);

					saveReceivedMatch(detail_id, from_where, detail.getPk());
				}
			} else if (from_where.equals(ResourcesConstants.RECEIVED_FROM_WHERE_AIR_RECEIVED)) {
				List<AirReceivedDetailBean> receivedDetails = airReceivedDao.selectByRelatedPk(related_pk);
				for (AirReceivedDetailBean detail : receivedDetails) {
					detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ENTER);
					detail.setConfirm_time(DateUtil.getMinStr());
					detail.setConfirm_user(user.getUser_number());
					airReceivedDao.update(detail);
					saveReceivedMatch(detail_id, from_where, detail.getPk());
				}
			}
		}

		PaymentDetailBean thisDetail = dao.selectById(detail_id);
		thisDetail.setMatch_flg("Y");
		dao.updateDetail(thisDetail);

		return SUCCESS;
	}

	private void saveReceivedMatch(String detail_pk, String from_where, String received_pk) {
		ReceivedMatchBean rmb = new ReceivedMatchBean();
		rmb.setFrom_where(from_where);
		rmb.setDetail_pk(detail_pk);
		rmb.setReceived_pk(received_pk);
		receivedMatchDao.insert(rmb);
	}

	@Override
	public String cancelMatchReceived(String detailId) {
		// 更新收入详情
		PaymentDetailBean detail = dao.selectById(detailId);
		detail.setMatch_flg("N");
		dao.updateDetail(detail);

		List<ReceivedMatchBean> rmbs = receivedMatchDao.selectByDetailPk(detailId);
		// 更新收入详表
		for (ReceivedMatchBean rmb : rmbs) {
			if (rmb.getFrom_where().equals(ResourcesConstants.RECEIVED_FROM_WHERE_CLIENT)) {
				ClientReceivedDetailBean receivedDetail = receivedDao.selectByPk(rmb.getReceived_pk());
				receivedDetail.setConfirm_time("");
				receivedDetail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
				receivedDao.update(receivedDetail);
				// 删除匹配关联
				receivedMatchDao.delete(rmb.getPk());
			} else if (rmb.getFrom_where().equals(ResourcesConstants.RECEIVED_FROM_WHERE_SUPPLIER)) {
				SupplierPaidDetailBean receivedDetail = paidDao.selectByPk(rmb.getReceived_pk());
				receivedDetail.setConfirm_time("");
				receivedDetail.setStatus(ResourcesConstants.PAID_STATUS_ING);
				paidDao.update(receivedDetail);
				// 删除匹配关联
				receivedMatchDao.delete(rmb.getPk());

			} else if (rmb.getFrom_where().equals(ResourcesConstants.RECEIVED_FROM_WHERE_AIR_TICKET)) {
				AirTicketPaidDetailBean receivedDetail = airTicketPaidDetailDao
						.selectByPrimaryKey(rmb.getReceived_pk());
				receivedDetail.setConfirm_time("");
				receivedDetail.setApprove_user("");
				receivedDetail.setStatus(ResourcesConstants.PAID_STATUS_ING);
				airTicketPaidDetailDao.update(receivedDetail);
				// 删除匹配关联
				receivedMatchDao.delete(rmb.getPk());
			} else if (rmb.getFrom_where().equals(ResourcesConstants.RECEIVED_FROM_WHERE_AIR_RECEIVED)) {
				AirReceivedDetailBean receivedDetail = airReceivedDao.selectByPk(rmb.getReceived_pk());
				receivedDetail.setConfirm_time("");
				receivedDetail.setConfirm_user("");
				receivedDetail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
				airReceivedDao.update(receivedDetail);
				// 删除匹配关联
				receivedMatchDao.delete(rmb.getPk());
			}
		}
		return SUCCESS;
	}

	@Override
	public List<PaymentDetailBean> batUploadReceived(File file) {

		List<PaymentDetailBean> details = new ArrayList<PaymentDetailBean>();

		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line;

			boolean first_line = true;
			while ((line = br.readLine()) != null) {
				String[] c = line.trim().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				if (c.length != 6) {
					continue;
				} else {

					String time = c[0].replaceAll("\"", "");
					if (first_line) {
						first_line = false;
						continue;
					}

					String type = c[1].replaceAll("\"", "");
					String giver = c[2].replaceAll("\"", "");
					String goods = c[3].replaceAll("\"", "");
					String name = c[4].replaceAll("\"", "");
					String money = c[5].replaceAll("\"", "").replaceAll("¥", "").replaceAll(",", "");

					String comment = type + "；付款人：" + giver + "；备注：" + goods + "；";

					PaymentDetailBean pd = new PaymentDetailBean();

					pd.setComment(StringUtils.substring(comment, 0, 200));
					pd.setMoney(new BigDecimal(money));
					pd.setType(name);

					Date r_time = DateUtil.castStr2Date(time, "yyyy/M/d H:m");
					if (r_time == null)
						continue;
					pd.setTime(DateUtil.castDate2Str(r_time, "yyyy-MM-dd HH:mm"));

					details.add(pd);
				}
			}
			br.close();
		} catch (Exception e) {
			file.delete();
			System.err.format("IOException: %s%n", e);
		}

		file.delete();
		return details;
	}

	@Override
	public String batSaveReceived(String account, String json) {

		JSONArray arr = JSONArray.fromObject(json);
		if (null != arr && arr.size() > 0) {
			List<PaymentDetailBean> pds = new ArrayList<PaymentDetailBean>();
			Set<String> times = new HashSet<String>();
			for (int i = 0; i < arr.size(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				String time = obj.getString("time");
				String type = obj.getString("type");
				String comment = obj.getString("comment");
				String money = obj.getString("money");

				PaymentDetailBean pd = new PaymentDetailBean();
				pd.setAccount(account);
				pd.setTime(time);

				pd.setType(type);
				pd.setComment(SimpletinyString.removeEmoji(comment));
				pd.setMoney(new BigDecimal(money));
				pds.add(pd);

				times.add(time);
			}

			if (times.size() < pds.size()) {
				return "repeat";
			}

			for (PaymentDetailBean pd : pds) {
				PaymentDetailBean option = new PaymentDetailBean();
				option.setAccount(pd.getAccount());
				option.setTime(pd.getTime());
				List<PaymentDetailBean> sameDetail = dao.selectAllDetailsByParam(option);
				if (null != sameDetail && sameDetail.size() > 0) {
					return "time&" + pd.getTime();
				}
			}

			for (PaymentDetailBean pd : pds) {
				insert(pd);
			}
		}

		return SUCCESS;
	}
}
