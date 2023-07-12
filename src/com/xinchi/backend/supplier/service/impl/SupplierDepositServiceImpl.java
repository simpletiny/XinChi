package com.xinchi.backend.supplier.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.receivable.dao.AirReceivedDAO;
import com.xinchi.backend.supplier.dao.DepositTicketPaidDAO;
import com.xinchi.backend.supplier.dao.SupplierDAO;
import com.xinchi.backend.supplier.dao.SupplierDepositDAO;
import com.xinchi.backend.supplier.service.SupplierDepositService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.DepositTicketPaidBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.office.SimpletinyExcel;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class SupplierDepositServiceImpl implements SupplierDepositService {

	@Autowired
	private SupplierDepositDAO dao;

	@Override
	public void insert(SupplierDepositBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(SupplierDepositBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public SupplierDepositBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<SupplierDepositBean> selectByParam(SupplierDepositBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<SupplierDepositBean> selectByPage(Page page) {

		return dao.selectByPage(page);
	}

	@Autowired
	private NumberService numberService;

	@Autowired
	private CardDAO cardDao;

	@Autowired
	private SupplierDAO supplierDao;

	@Autowired
	private PaymentDetailService paymentDetailService;

	@Override
	public String createDeposit(SupplierDepositBean deposit) {
		SupplierBean supplier = supplierDao.selectByPrimaryKey(deposit.getSupplier_pk());

		// 生成银行流水账
		String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				ResourcesConstants.PAY_TYPE_DEPOSIT_AIR, DateUtil.getDateStr(DateUtil.YYYYMMDD));

		// 生成银行流水数据
		String msg = SUCCESS;
		String account = deposit.getAccount();
		CardBean card = cardDao.getCardByAccount(account);

		BigDecimal balance = card.getBalance().subtract(deposit.getMoney());

		String voucher_file_name = deposit.getVoucher_file_name();

		PaymentDetailBean payment = new PaymentDetailBean();
		payment.setVoucher_number(voucher_number);
		payment.setAccount(account);
		payment.setTime(deposit.getTime());
		payment.setMoney(deposit.getMoney());
		payment.setBalance(balance);
		payment.setType("支出");
		payment.setComment(supplier.getSupplier_short_name() + "机票押金,凭证号：" + voucher_number);
		payment.setVoucher_file_name(voucher_file_name);

		msg = paymentDetailService.insert(payment);
		if (!msg.equals(SUCCESS)) {
			return msg;
		}

		// 保存航司押金记录
		String deposit_number = numberService.generateDepositNumber();
		deposit.setDeposit_number(deposit_number);
		deposit.setVoucher_number(voucher_number);
		deposit.setReceived(BigDecimal.ZERO);
		deposit.setBalance(deposit.getMoney());
		dao.insert(deposit);

		return SUCCESS;
	}

	@Override
	public String deleteSupplierDeposit(String deposit_pk) {
		String result = SUCCESS;
		SupplierDepositBean deposit = dao.selectByPrimaryKey(deposit_pk);
		// 删除银行流水
		List<PaymentDetailBean> details = paymentDetailService.selectByVoucherNumber(deposit.getVoucher_number());
		if (null != details && details.size() > 0) {
			PaymentDetailBean detail = details.get(0);
			result = paymentDetailService.deleteDetail(detail.getPk());
		}

		if (result.equals(SUCCESS)) {
			// 删除押金账
			SupplierDepositBean option = new SupplierDepositBean();
			option.setVoucher_number(deposit.getVoucher_number());

			List<SupplierDepositBean> deposits = dao.selectByParam(option);

			for (SupplierDepositBean a : deposits) {
				dao.delete(a.getPk());
			}
		}

		return result;
	}

	@Autowired
	private AirReceivedDAO airReceivedDao;

	@Override
	public String receiveSupplierDeposit(SupplierDepositBean deposit, String json) {
		JSONArray arr = JSONArray.fromObject(json);
		String related_pk = DBCommonUtil.genPk();
		String received_type = arr.size() > 1 ? ResourcesConstants.RECEIVED_TYPE_SUM
				: ResourcesConstants.RECEIVED_TYPE_RECEIVED;
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			String deposit_pk = obj.getString("deposit_pk");
			SupplierDepositBean currentDeposit = dao.selectByPrimaryKey(deposit_pk);
			BigDecimal received = new BigDecimal(
					SimpletinyString.isEmpty(obj.getString("money")) ? "0" : obj.getString("money"));

			String comment = obj.getString("comment");
			// 生成退还记录
			AirReceivedDetailBean detail = new AirReceivedDetailBean();
			detail.setBusiness_number(currentDeposit.getDeposit_number());
			detail.setReceived(received);
			detail.setSum_received(deposit.getMoney());
			detail.setReceived_type(received_type);
			detail.setReceived_time(deposit.getTime());
			detail.setCard_account(deposit.getAccount());
			detail.setRelated_pk(related_pk);
			detail.setSupplier_pk(currentDeposit.getSupplier_pk());

			detail.setVoucher_file(deposit.getVoucher_file_name());

			detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
			detail.setComment(comment);
			airReceivedDao.insert(detail);

			currentDeposit.setReceived(currentDeposit.getReceived() == null ? BigDecimal.ZERO
					: currentDeposit.getReceived().add(received));
			currentDeposit.setBalance(currentDeposit.getBalance().subtract(received));

			String return_way = currentDeposit.getReturn_way();
			if (SimpletinyString.isEmpty(return_way)) {
				currentDeposit.setReturn_way("T");
			} else {
				if (currentDeposit.getReturn_way().indexOf("T") < 0) {
					currentDeposit.setReturn_way(return_way + ",T");
				}
			}

			if (currentDeposit.getBalance().compareTo(BigDecimal.ZERO) == 0) {
				currentDeposit.setStatus("Y");
			}
			// 更新押金账
			dao.update(currentDeposit);
			// // 保存押金账和银行流水押金退回收入之间的对应关系
			// DepositPaymentBean dp = new DepositPaymentBean();
			// dp.setDeposit_pk(deposit_pk);
			// dp.setPayment_voucher_number(voucher_number);
			// depositPaymentDao.insert(dp);
		}

		// 保存凭证文件
		String[] split_str = deposit.getTime().split("-");
		if (split_str.length > 1) {
			String sub_folder = split_str[0] + File.separator + split_str[1];
			FileUtil.saveFile(deposit.getVoucher_file_name(), FileFolder.SUPPLIER_RECEIVED_VOUCHER.value(), sub_folder);
		}

		return SUCCESS;
	}

	@Override
	public List<SupplierDepositBean> batUploadDeposit(String file_name, String deposit_type) throws IOException {
		List<SupplierDepositBean> deposits = new ArrayList<SupplierDepositBean>();

		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String tem_file = tempFolder + File.separator + file_name;
		File excelFile = new File(tem_file);

		BufferedInputStream fs = new BufferedInputStream(new FileInputStream(excelFile));
		// 获得工作簿
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fs);

		// 获得sheet
		XSSFSheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();

		// 获取标题行
		XSSFRow titleRow = sheet.getRow(0);
		int colnum = titleRow.getLastCellNum();
		Map<String, Integer> colMapping = new HashMap<String, Integer>();

		for (int i = 0; i < colnum; i++) {
			Cell cell = titleRow.getCell(i);
			String title = cell.getStringCellValue();
			colMapping.put(title, i);
		}

		for (int i = 1; i < rows; i++) {

			SupplierDepositBean deposit = new SupplierDepositBean();
			String comment = "";
			// 获取第i行
			XSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			// 成交编号
			Cell cell1 = row.getCell(colMapping.get("成交编号"));
			String dealNum = SimpletinyExcel.getCellValueByCell(cell1);

			if (SimpletinyString.isEmpty(dealNum)) {
				continue;
			}
			// 航段
			Cell cell2 = row.getCell(colMapping.get("航段"));
			String airLeg = SimpletinyExcel.getCellValueByCell(cell2);

			// 供应商
			Cell cell3 = row.getCell(colMapping.get("供应商"));
			String supplier_name = SimpletinyExcel.getCellValueByCell(cell3);

			// 航班日期
			Cell cell4 = row.getCell(colMapping.get("航班日期"));
			String air_date = SimpletinyExcel.getCellValueByCell(cell4).substring(0, 10);

			// 押金
			Cell cell5 = row.getCell(colMapping.get("押金"));
			String deposit_money = SimpletinyExcel.getCellValueByCell(cell5);

			// 支付方
			Cell cell6 = row.getCell(colMapping.get("支付方"));
			String account = SimpletinyExcel.getCellValueByCell(cell6);

			// 支付时间
			Cell cell7 = row.getCell(colMapping.get("支付时间"));
			String payTime = SimpletinyExcel.getCellValueByCell(cell7);

			// 备注
			Cell cell8 = row.getCell(colMapping.get("备注"));
			String excelComment = SimpletinyExcel.getCellValueByCell(cell8);

			// 支付序列
			Cell cell9 = row.getCell(colMapping.get("序号"));
			int pay_index = (int) Float.parseFloat(SimpletinyExcel.getCellValueByCell(cell9));

			comment = "成交编号：" + dealNum + ";" + "航段：" + airLeg + ";" + "航班日期：" + air_date + ";" + excelComment;

			deposit.setAccount(account);
			deposit.setSupplier_name(supplier_name);
			deposit.setMoney(new BigDecimal(deposit_money));

			deposit.setReturn_date(DateUtil.addDate(air_date, 10));

			deposit.setComment(comment);
			deposit.setTime(payTime);

			deposit.setPay_index(pay_index);

			deposits.add(deposit);
		}
		excelFile.delete();
		return deposits;
	}

	@Override
	public String batSaveDeposit(String json) {
		JSONArray array = JSONArray.fromObject(json);
		Map<String, String> leader = new HashMap<String, String>();

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String supplier_name = obj.getString("supplier_name");
			String account = obj.getString("account");
			String pay_index = obj.getString("pay_index");
			String pay_time = obj.getString("time");

			if (SimpletinyString.isEmpty(supplier_name)) {
				return "第" + (i + 1) + "行缺少供应商信息！";
			}

			if (SimpletinyString.isEmpty(account)) {
				return "第" + (i + 1) + "行缺少支付方信息！";
			}

			if (SimpletinyString.isEmpty(pay_index)) {
				return "第" + (i + 1) + "行缺少序号！";
			}
			if (SimpletinyString.isEmpty(pay_time)) {
				return "第" + (i + 1) + "行缺少支付时间！";
			}

			if (!leader.keySet().contains(pay_index)) {
				leader.put(pay_index, supplier_name + "##" + account + "##" + pay_time);
			}
		}

		// 验证信息计算合计
		Map<String, BigDecimal> pay_moneys = new HashMap<String, BigDecimal>();

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);

			String supplier_name = obj.getString("supplier_name");
			String account = obj.getString("account");
			String comment = obj.getString("comment");
			String pay_index = obj.getString("pay_index");
			String pay_time = obj.getString("time");

			String[] lea = leader.get(pay_index).split("##");
			if (!lea[0].equals(supplier_name)) {
				return "序号" + pay_index + "下出现了不同供应商！";
			}
			if (!lea[1].equals(account)) {
				return "序号" + pay_index + "下出现了不同支付方！";
			}
			if (!lea[2].equals(pay_time)) {
				return "序号" + pay_index + "下出现了不同支付时间！";
			}

			if (comment.length() > 200) {
				comment = comment.substring(0, 200);
			}

			BigDecimal money = new BigDecimal(obj.getString("money"));
			pay_moneys.put(pay_index, pay_moneys.get(pay_index) == null ? BigDecimal.ZERO.add(money)
					: pay_moneys.get(pay_index).add(money));

		}

		// 生成银行流水账
		Map<String, String> voucher_numbers = new HashMap<String, String>();

		Iterator<String> iterator = leader.keySet().iterator();
		while (iterator.hasNext()) {
			String k = iterator.next();
			String[] lea = leader.get(k).split("##");

			String supplier_name = lea[0];
			String account = lea[1];
			String pay_time = lea[2];

			BigDecimal money = pay_moneys.get(k);

			SupplierBean option = new SupplierBean();
			option.setSupplier_name(supplier_name);
			List<SupplierBean> suppliers = supplierDao.getAllByParam(option);

			if (null == suppliers || suppliers.size() == 0) {
				DBCommonUtil.rollBackData();
				return supplier_name + "供应商不存在！";
			}

			SupplierBean supplier = suppliers.get(0);

			// 生成银行流水账
			String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
					ResourcesConstants.PAY_TYPE_DEPOSIT_AIR, DateUtil.getDateStr(DateUtil.YYYYMMDD));

			voucher_numbers.put(k, voucher_number);

			// 生成银行流水数据
			String msg = SUCCESS;

			CardBean card = cardDao.getCardByAccount(account);

			if (card == null) {
				DBCommonUtil.rollBackData();
				return account + "账户不存在！";
			}

			BigDecimal balance = card.getBalance().subtract(money);

			PaymentDetailBean payment = new PaymentDetailBean();
			payment.setVoucher_number(voucher_number);
			payment.setAccount(account);
			payment.setTime(pay_time);
			payment.setMoney(money);
			payment.setBalance(balance);
			payment.setType("支出");
			payment.setComment(supplier.getSupplier_short_name() + "机票押金,凭证号：" + voucher_number);

			msg = paymentDetailService.insert(payment);
			if (!msg.equals(SUCCESS)) {
				DBCommonUtil.rollBackData();
				return "账户" + account + "银行流水存在相同时间的记录，请修改支付时间！";
			}
		}

		// 保存航司押金
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String supplier_name = obj.getString("supplier_name");
			String account = obj.getString("account");
			String comment = obj.getString("comment");
			String pay_index = obj.getString("pay_index");
			BigDecimal money = new BigDecimal(obj.getString("money"));
			String return_date = obj.getString("return_date");

			SupplierBean option = new SupplierBean();
			option.setSupplier_name(supplier_name);
			List<SupplierBean> suppliers = supplierDao.getAllByParam(option);

			SupplierBean supplier = suppliers.get(0);

			SupplierDepositBean deposit = new SupplierDepositBean();

			// 保存航司押金记录
			deposit.setSupplier_pk(supplier.getPk());
			deposit.setAccount(account);
			deposit.setVoucher_number(voucher_numbers.get(pay_index));
			deposit.setMoney(money);
			deposit.setReturn_date(return_date);
			deposit.setComment(comment);
			deposit.setReceived(BigDecimal.ZERO);
			deposit.setBalance(deposit.getMoney());
			dao.insert(deposit);
		}
		return SUCCESS;
	}

	@Override
	public List<SupplierDepositBean> selectByVoucherNumber(String voucher_number) {
		SupplierDepositBean option = new SupplierDepositBean();
		option.setVoucher_number(voucher_number);

		List<SupplierDepositBean> deposits = dao.selectByParam(option);

		return deposits;
	}

	@Autowired
	private DepositTicketPaidDAO depositTicketPaidDao;

	@Override
	public List<DepositTicketPaidBean> selectDepositTicketPaidsByDepositPk(String deposit_pk) {
		return depositTicketPaidDao.selectByDepositPk(deposit_pk);
	}

	@Override
	public List<SupplierDepositBean> selectDepositWithoutNumber() {
		return dao.selectDepositWithoutNumber();
	}

	@Override
	public List<SupplierDepositBean> selectDepositSummary(SupplierDepositBean bean) {

		return dao.selectDepositSummary(bean);
	}

	@Override
	public SupplierDepositBean selectSumDeposit() {
		return dao.selectSumDeposit();
	}

}