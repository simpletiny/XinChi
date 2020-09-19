package com.xinchi.backend.util.action;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.File;
import java.io.FileInputStream;
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
import com.xinchi.backend.product.service.ProductOrderService;
import com.xinchi.backend.product.service.ProductOrderSupplierService;
import com.xinchi.backend.product.service.ProductOrderTeamNumberService;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.product.service.ProductSupplierService;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.PassengerTicketInfoDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.OrderSupplierBean;
import com.xinchi.bean.OrderSupplierInfoBean;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.bean.ProductSupplierBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
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

	private String supplier_employee_pk;
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderNameListService nameService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductOrderSupplierService posService;

	@Autowired
	private ProductSupplierService psService;

	@Autowired
	private ProductOrderService productOrderService;

	public String downloadProductFile() throws IOException {

		// 默认模板文件夹
		String defaultTempletFolder = PropertiesUtil.getProperty("defaultTempletFolder");
		String tempDownloadFolder = PropertiesUtil.getProperty("tempDownloadFolder");

		// 目的文件
		File dest = new File(tempDownloadFolder + File.separator + DBCommonUtil.genPk() + ".doc");

		String src_file_path = "";
		Map<String, String> data = new HashMap<String, String>();
		String _fileName = "";

		if (fileType.equals(ResourcesConstants.FILE_TYPE_OUT_NOTICE)) {
			OrderDto order = orderService.selectByTeamNumber(team_number);
			src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";
			if (order.getStandard_flg().equals("Y")) {
				String product_pk = order.getProduct_pk();
				ProductBean product = productService.selectByPrimaryKey(product_pk);
				if (product.getOut_notice_templet().equals("default") || product.getOut_notice_templet().equals("no")) {
					// 调用默认模板
					src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";

				} else {
					String outNoticeTempletFolder = PropertiesUtil.getProperty("outNoticeTempletFolder");
					src_file_path = outNoticeTempletFolder + File.separator + product.getOut_notice_templet();
				}
			}
			// 产品名称
			data.put("product", order.getProduct_name());
			// 游玩天数
			data.put("days", order.getDays().toString());
			// 出团日期
			data.put("departuredate", order.getDeparture_date());
			// 回团日期
			data.put("backdate", DateUtil.addDate(order.getDeparture_date(), order.getDays() - 1));

			Map<String, String> name_data = getNameData(order.getPk());
			Map<String, String> ticket_data = getTicketData(team_number);

			data.putAll(name_data);
			data.putAll(ticket_data);
			// 人数
			int people_cnt = order.getAdult_count() + (order.getSpecial_count() == null ? 0 : order.getSpecial_count());

			_fileName = order.getDeparture_date() + data.get("chairman") + people_cnt + "人出团通知.doc";

		} else if (fileType.equalsIgnoreCase(ResourcesConstants.FILE_TYPE_CLIENT_CONFIRM)) {
			OrderDto order = orderService.selectByTeamNumber(team_number);
			src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";

			// 如果是标准订单更改模板路径
			if (order.getStandard_flg().equals("Y")) {
				String product_pk = order.getProduct_pk();
				ProductBean product = productService.selectByPrimaryKey(product_pk);

				if (product.getClient_confirm_templet().equals("default")
						|| product.getClient_confirm_templet().equals("no")) {
					// 调用默认模板
					src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";
				} else {
					String clientConfirmTempletFolder = PropertiesUtil.getProperty("clientConfirmTempletFolder");
					src_file_path = clientConfirmTempletFolder + File.separator + product.getClient_confirm_templet();
				}
			}
			// 销售信息
			UserCommonBean sale = userService.selectUserCommonByUserNumber(order.getCreate_user_number());
			data.put("saleinfo", sale.getUser_name() + "：" + sale.getCellphone());
			data.put("salename", sale.getUser_name());
			data.put("saletel", sale.getCellphone());

			// 名单信息
			Map<String, String> name_data = getNameData(order.getPk());

			// 客户信息
			Map<String, String> client_data = getClientData(order);

			// 机票信息
			Map<String, String> ticket_data = getTicketData(team_number);
			// 人数
			int people_cnt = order.getAdult_count() + (order.getSpecial_count() == null ? 0 : order.getSpecial_count());

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

			data.putAll(name_data);
			data.putAll(client_data);

			data.putAll(ticket_data);

			_fileName = null == data.get("clientshort") ? data.get("client")
					: data.get("clientshort") + data.get("departuredate") + data.get("chairman") + people_cnt
							+ "人确认件.doc";

		} else if (fileType.equals(ResourcesConstants.FILE_TYPE_SUPPLIER_CONFIRM)) {
			ProductOrderBean order = productOrderService.selectByOrderNumber(team_number);
			src_file_path = defaultTempletFolder + File.separator + fileType + ".doc";

			// 查找要下载的地接社信息
			OrderSupplierBean option = new OrderSupplierBean();
			option.setOrder_pk(order.getPk());
			option.setSupplier_employee_pk(supplier_employee_pk);
			List<OrderSupplierBean> suppliers = posService.selectByParam(option);
			if (null == suppliers || suppliers.size() == 0)
				return INPUT;

			OrderSupplierBean supplier = suppliers.get(0);

			// 如果没有操作模板
			if (supplier.getConfirm_file_templet().equals("default")) {

				// 如果是标准订单，则用产品上传的模板
				if (order.getStandard_flg().equals("Y")) {

					// 查找要下载的产品地接社信息
					ProductSupplierBean psOption = new ProductSupplierBean();
					psOption.setProduct_pk(order.getProduct_pk());
					psOption.setSupplier_employee_pk(supplier_employee_pk);
					List<ProductSupplierBean> pSuppliers = psService.selectByParam(psOption);

					if (null != pSuppliers && pSuppliers.size() > 0) {
						ProductSupplierBean pSupplier = pSuppliers.get(0);
						if (!pSupplier.getConfirm_file_templet().equals("default")) {
							// 产品地接确认模板
							String supplierConfirmTempletFolder = PropertiesUtil
									.getProperty("supplierConfirmTempletFolder");

							src_file_path = supplierConfirmTempletFolder + File.separator
									+ pSupplier.getConfirm_file_templet();

						}
					}
				}

			} else {
				String supplierConfirmTempletFolder = PropertiesUtil.getProperty("orderSupplierTempletFolder");
				src_file_path = supplierConfirmTempletFolder + File.separator + supplier.getConfirm_file_templet();
			}

			data.put("sename", supplier.getSupplier_employee_name());
			data.put("setel", supplier.getTelephone());
			data.put("sefax", supplier.getFax());
			data.put("secellphone", supplier.getCellphone());

			data.put("suppliername", supplier.getSupplier_name());
			data.put("suppliershortname", supplier.getSupplier_short_name());
			data.put("supplierproduct", supplier.getSupplier_product_name());
			data.put("spdays", supplier.getDays().toString());
			data.put("suppliercost", supplier.getSupplier_cost().toString());

			Map<String, String> pick_send_data = getPsData(supplier);

			Map<String, String> name_data = getProductOrderNameData(team_number);
			Map<String, String> ticket_data = getTicketData(team_number);

			// 人数
			int people_cnt = order.getAdult_count() + (order.getSpecial_count() == null ? 0 : order.getSpecial_count());
			data.put("peoplecnt", String.valueOf(people_cnt));

			// 产品经理信息
			UserCommonBean sale = userService.selectUserCommonByUserNumber(order.getProduct_manager_number());
			data.put("managerinfo", sale.getUser_name() + "：" + sale.getCellphone());
			data.put("managername", sale.getUser_name());
			data.put("managertel", sale.getCellphone());

			data.putAll(name_data);
			data.putAll(ticket_data);
			data.putAll(pick_send_data);

			_fileName = null == data.get("suppliershortname") ? data.get("suppliername")
					: data.get("suppliershortname") + data.get("pickdate") + data.get("chairman") + people_cnt
							+ "人确认件.doc";

		}

		File src = new File(src_file_path);

		replace(src, dest, data);

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

	private Map<String, String> getPsData(OrderSupplierBean supplier) {

		Map<String, String> data = new HashMap<String, String>();
		if (supplier.getInfos().size() == 1) {
			OrderSupplierInfoBean info = supplier.getInfos().get(0);
			// 接机段
			data.put("pickleg", info.getPick_leg());
			data.put("pickdate", DateUtil.addDate(supplier.getDeparture_date(), info.getPick_day() - 1));
			data.put("picktime", info.getPick_time());
			data.put("picktraffic", info.getPick_traffic());
			data.put("pickcity", info.getPick_city());
			data.put("pickplace", info.getPick_place());

			// 送机段
			data.put("sendleg", info.getSend_leg());
			data.put("senddate", DateUtil.addDate(supplier.getDeparture_date(), info.getSend_day() - 1));
			data.put("sendtime", info.getSend_time());
			data.put("sendtraffic", info.getSend_traffic());
			data.put("sendcity", info.getSend_city());
			data.put("sendplace", info.getSend_place());
		}

		return data;
	}

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Autowired
	private PassengerTicketInfoDAO passengerTicketInfoDao;

	private Map<String, String> getTicketData(String team_number) {
		Map<String, String> data = new HashMap<String, String>();

		List<AirTicketNameListBean> names = new ArrayList<AirTicketNameListBean>();

		if (team_number.startsWith("P")) {
			List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
			potns = productOrderTeamNumberService.selectByOrderNumber(team_number);

			List<String> team_numbers = new ArrayList<String>();

			if (null == potns || potns.size() == 0) {
				team_numbers.add(team_number);
			} else {
				for (ProductOrderTeamNumberBean potn : potns) {
					team_numbers.add(potn.getTeam_number());
				}
			}
			names = airTicketNameListDao.selectByTeamNumbers(team_numbers);

		} else if (team_number.startsWith("N")) {
			names = airTicketNameListDao.selectByTeamNumber(team_number);
		}

		String ticket = "";

		if (null != names && names.size() > 0) {
			AirTicketNameListBean name = names.get(0);
			List<PassengerTicketInfoBean> infos = passengerTicketInfoDao.selectByPassengerPk(name.getPk());
			if (null != infos && infos.size() > 0) {
				for (PassengerTicketInfoBean info : infos) {
					ticket += info.getTicket_date() + "   " + info.getFrom_to_time() + "   " + info.getTicket_number()
							+ "   " + info.getFrom_to_city() + "   " + info.getFrom_airport() + "--"
							+ info.getTo_airport() + "   " + "；" + (char) 11;
				}

				data.put("ticket", ticket);
			} else {
				data.put("ticket", "");
			}

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
		data.put("tel", allTel);
		data.put("nameid", allNameId);
		data.put("chairman", chairman);
		return data;
	}

	@Autowired
	private ProductOrderTeamNumberService productOrderTeamNumberService;

	private Map<String, String> getProductOrderNameData(String order_number) {

		List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
		potns = productOrderTeamNumberService.selectByOrderNumber(order_number);

		List<String> team_numbers = new ArrayList<String>();

		if (null == potns || potns.size() == 0) {
			team_numbers.add(order_number);
		} else {
			for (ProductOrderTeamNumberBean potn : potns) {
				team_numbers.add(potn.getTeam_number());
			}
		}

		// 获取名单
		List<SaleOrderNameListBean> names = nameService.selectByTeamNumbers(team_numbers);
		Map<String, String> data = new HashMap<String, String>();

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

			if (name.getChairman().equals("Y") && SimpletinyString.isEmpty(chairman)) {
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
				range.replaceText(source, null == k_v.get(k) ? "" : k_v.get(k));
			}

			OutputStream os = new FileOutputStream(destFile);

			// PicturesTable pictureTable = doc.getPicturesTable();
			//
			// List<Picture> pics = pictureTable.getAllPictures();
			//
			// for (Picture pic : pics) {
			// pic.writeImageContent(os);
			// }

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

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}
}
