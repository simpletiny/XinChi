package com.xinchi.backend.util.action;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.ClientService;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.order.service.OrderNameListService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.ticket.service.FlightService;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.FlightBean;
import com.xinchi.bean.FlightInfoBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class ProductFileAction extends BaseAction {

	private static final long serialVersionUID = -4686523277751323355L;
	private InputStream fips;
	private String subFolder;
	private String fileName;
	private String fileFileName;
	// A 出团通知
	private String fileType;

	private String team_number;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderNameListService nameService;

	@Autowired
	private FlightService flightService;

	@Autowired
	private UserService userService;

	public String downloadProductFile() throws IOException {

		OrderDto order = orderService.selectByTeamNumber(team_number);

		// 默认模板文件夹
		String defaultTempletFolder = PropertiesUtil.getProperty("defaultTempletFolder");
		String tempDownloadFolder = PropertiesUtil.getProperty("tempDownloadFolder");

		// 目的文件
		File dest = new File(tempDownloadFolder + File.separator + DBCommonUtil.genPk() + ".doc");
		// 人数
		int people_cnt = order.getAdult_count() + (order.getSpecial_count() == null ? 0 : order.getSpecial_count());

		String src_file_path = "";
		Map<String, String> data = new HashMap<String, String>();
		// 行程相关
		// 应收款
		data.put("receivable", order.getReceivable().toString());
		// 产品名称
		data.put("product", order.getProduct_name());
		// 出团日期
		data.put("departuredate", order.getDeparture_date());
		// 回团日期
		data.put("backdate", DateUtil.addDate(order.getDeparture_date(), order.getDays() - 1));
		// 游玩天数
		data.put("days", order.getDays().toString());
		// 人数
		data.put("peoplecnt", String.valueOf(people_cnt));

		// 销售信息
		UserCommonBean sale = userService.selectUserCommonByUserNumber(order.getCreate_user_number());
		data.put("saleinfo", sale.getUser_name() + "：" + sale.getCellphone());
		data.put("salename", sale.getUser_name());
		data.put("saletel", sale.getCellphone());

		// 如果是非标订单
		if (order.getStandard_flg().equals("N")) {
			src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";
			if (fileType.equals(ResourcesConstants.FILE_TYPE_OUT_NOTICE)) {
				Map<String, String> name_data = getNameData(order.getPk());
				data.putAll(name_data);
			} else if (fileType.equalsIgnoreCase(ResourcesConstants.FILE_TYPE_CLIENT_CONFIRM)) {
				Map<String, String> name_data = getNameData(order.getPk());
				Map<String, String> client_data = getClientData(order);
				data.putAll(name_data);
				data.putAll(client_data);
			}
		}
		// 标准订单
		else {
			String product_pk = order.getProduct_pk();
			ProductBean product = productService.selectByPrimaryKey(product_pk);

			// 如果是出团通知
			if (fileType.equals(ResourcesConstants.FILE_TYPE_OUT_NOTICE)) {

				if (product.getOut_notice_templet().equals("default") || product.getOut_notice_templet().equals("no")) {
					// 调用默认模板
					src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";

				} else {
					String outNoticeTempletFolder = PropertiesUtil.getProperty("outNoticeTempletFolder");
					src_file_path = outNoticeTempletFolder + File.separator + product.getOut_notice_templet();
				}
				Map<String, String> name_data = getNameData(order.getPk());
				Map<String, String> ticket_data = getTicketData(product, order);

				data.putAll(name_data);
				data.putAll(ticket_data);
			}
			// 组团社确认
			else if (fileType.equalsIgnoreCase(ResourcesConstants.FILE_TYPE_CLIENT_CONFIRM)) {

				if (product.getClient_confirm_templet().equals("default")
						|| product.getClient_confirm_templet().equals("no")) {
					// 调用默认模板
					src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";
				} else {
					String clientConfirmTempletFolder = PropertiesUtil.getProperty("clientConfirmTempletFolder");
					src_file_path = clientConfirmTempletFolder + File.separator + product.getClient_confirm_templet();
				}

				Map<String, String> name_data = getNameData(order.getPk());
				Map<String, String> client_data = getClientData(order);
				data.putAll(name_data);
				data.putAll(client_data);
			}
		}

		File src = new File(src_file_path);

		replace(src, dest, data);
		String _fileName = "";
		// 设置下载的文件名
		// 如果是出团通知
		if (fileType.equals(ResourcesConstants.FILE_TYPE_OUT_NOTICE)) {
			_fileName = order.getDeparture_date() + data.get("chairman") + people_cnt + "人出团通知.doc";
		}
		// 组团社确认
		else if (fileType.equals(ResourcesConstants.FILE_TYPE_CLIENT_CONFIRM)) {
			_fileName = data.get("clientshort") + data.get("departuredate") + data.get("chairman") + people_cnt
					+ "人确认件.doc";
		}

		fileName = new String(_fileName.getBytes(), "ISO8859-1");
		fips = new FileInputStream(dest);
		return SUCCESS;
	}

	private List<String> operate_pks;

	public String batDownloadSupplierConfirm() throws IOException {
		fileName = new String("哈哈.zip".getBytes(), "ISO8859-1");
		String dest_path = "D://" + DBCommonUtil.genPk() + ".zip";
		File dest = new File(dest_path);

		File file1 = new File("D://test.doc");
		File file2 = new File("D://haha.doc");

		List<File> src = new ArrayList<File>();
		src.add(file1);
		src.add(file2);

		OutputStream out = new FileOutputStream(dest);

		FileUtil.toZip(src, out);

		fips = new FileInputStream(dest);

		return SUCCESS;
	}

	@Autowired
	private EmployeeService clientEmployeeService;

	@Autowired
	private ClientService clientService;

	private Map<String, String> getClientData(OrderDto order) {
		Map<String, String> data = new HashMap<String, String>();
		String client_employee_pk = order.getClient_employee_pk();
		ClientEmployeeBean employee = clientEmployeeService.selectByPrimaryKey(client_employee_pk);
		if (null != employee) {
			ClientBean client = clientService.selectByPrimaryKey(employee.getFinancial_body_pk());
			String clientName = client.getClient_name();
			String shortName = client.getClient_short_name();
			String employeeName = employee.getName();
			String employeeTel = employee.getCellphone();

			data.put("client", clientName);
			data.put("clientshort", shortName);
			data.put("employee", employeeName);
			data.put("employeetel", employeeTel);
			data.put("employeeinfo", employeeName + "：" + employeeTel);

		}

		return data;
	}

	private Map<String, String> getTicketData(ProductBean product, OrderDto order) {
		Map<String, String> data = new HashMap<String, String>();
		String ticket = "";
		String departure_date = order.getDeparture_date();
		// 获取航班信息
		if (product.getAir_ticket_upkeep_flg().equals("Y")) {
			FlightBean flight = flightService.selectByProductPk(product.getPk());

			for (FlightInfoBean info : flight.getInfos()) {
				String fly_date = DateUtil.addDate(departure_date, info.getStart_day() - 1);
				String city = info.getStart_city() + "--" + info.getEnd_city();
				String flight_number = (isEmpty(info.getFlight_number()) ? "" : info.getFlight_number());
				ticket += fly_date + "   " + city + "   " + flight_number + "；" + (char) 11;
			}
			data.put("ticket", ticket);
		} else {
			data.put("ticket", "");
		}
		return data;
	}

	private Map<String, String> getNameData(String order_pk) {
		Map<String, String> data = new HashMap<String, String>();
		// 获取名单
		List<SaleOrderNameListBean> names = nameService.selectByOrderPk(order_pk);
		String allName = "";
		String allId = "";
		String allTel = "";
		String allNameId = "";
		String chairman = "";
		for (SaleOrderNameListBean name : names) {
			allName += name.getName() + (char) 11;
			allId += name.getId() + (char) 11;
			if (!isEmpty(name.getCellphone_A())) {
				allTel += name.getCellphone_A() + ";";
			}
			if (!isEmpty(name.getCellphone_B())) {
				allTel += name.getCellphone_B() + ";";
			}
			allNameId += name.getName() + "：" + name.getId() + "；" + (char) 11;

			if (name.getChairman().equals("Y")) {
				chairman = name.getName();
			}
		}

		data.put("name", allName);
		data.put("id", allId);
		data.put("tel", chairman + "：" + allTel);
		data.put("nameid", allNameId);
		data.put("chairman", chairman);
		return data;
	}

	private File file;

	private void replace(File srcFile, File destFile, Map<String, String> k_v) throws IOException {

		if (k_v != null && srcFile.exists()) {

			InputStream is = new FileInputStream(srcFile);
			HWPFDocument doc = new HWPFDocument(is);
			Range range = doc.getRange();

			Iterator<String> iter = k_v.keySet().iterator();
			while (iter.hasNext()) {
				String k = iter.next();
				String source = "${" + k + "}";
				range.replaceText(source, k_v.get(k));
			}

			OutputStream os = new FileOutputStream(destFile);
			doc.write(os);

			os.flush();
			os.close();

			doc.close();
			is.close();
		}

	}

	public InputStream getFips() {
		return fips;
	}

	public void setFips(InputStream fips) {
		this.fips = fips;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getSubFolder() {
		return subFolder;
	}

	public void setSubFolder(String subFolder) {
		this.subFolder = subFolder;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public List<String> getOperate_pks() {
		return operate_pks;
	}

	public void setOperate_pks(List<String> operate_pks) {
		this.operate_pks = operate_pks;
	}
}
