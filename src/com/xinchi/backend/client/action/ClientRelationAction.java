package com.xinchi.backend.client.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.AccurateSaleService;
import com.xinchi.backend.client.service.ClientRelationService;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.bean.AccurateSaleDto;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeQuitConnectLogBean;
import com.xinchi.bean.ClientEmployeeTypeCountBean;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.ConnectDto;
import com.xinchi.bean.IncomingCallBean;
import com.xinchi.bean.IncomingCountDto;
import com.xinchi.bean.MeterDto;
import com.xinchi.bean.MobileTouchBean;
import com.xinchi.bean.PotentialDto;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.bean.WorkOrderDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope("prototype")
public class ClientRelationAction extends BaseAction {
	private static final long serialVersionUID = -5702112495549468432L;

	private ClientVisitBean visit;

	private ClientRelationSummaryBean relation;
	private List<ClientRelationBean> relations;
	@Autowired
	private ClientRelationService service;

	public String createVisit() {
		resultStr = service.createVisit(visit);
		return SUCCESS;
	}

	private MobileTouchBean mobile;

	/**
	 * 创建主动电联记录
	 * 
	 * @return
	 */
	public String createMobileTouch() {
		resultStr = service.createMobileTouch(mobile);
		return SUCCESS;
	}

	private IncomingCallBean incoming;

	/**
	 * 创建被动咨询记录
	 * 
	 * @return
	 */
	public String createIncomingCall() {
		resultStr = service.createIncomingCall(incoming);
		return SUCCESS;
	}

	private ClientEmployeeQuitConnectLogBean quit;

	/**
	 * 放弃维护客户
	 * 
	 * @return
	 */
	public String quitConnectEmployee() {
		resultStr = service.quitConnectEmployee(quit);
		return SUCCESS;
	}

	public String searchRelationsByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			relation.setSales(sessionBean.getPk());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", relation);
		page.setParams(params);
		relations = service.getRelationsByPage(page);
		return SUCCESS;
	}

	private String wechat;
	private String cellphone;

	private String sales_name;
	@Autowired
	private EmployeeService employeeService;

	private List<ClientEmployeeBean> employees;

	/**
	 * 查询系统客户
	 * 
	 * @return
	 */
	public String querySysClient() {
		if (SimpletinyString.isEmpty(cellphone) && SimpletinyString.isEmpty(wechat)) {
			resultStr = "no";
			return SUCCESS;
		}

		ClientEmployeeBean option = new ClientEmployeeBean();
		option.setCellphone(cellphone);
		option.setWechat(wechat);
		employees = employeeService.getAllClientEmployeeByParam(option);
		if (null == employees || employees.size() == 0) {
			if (SimpletinyString.isEmpty(cellphone)) {
				resultStr = "nowechat";
			} else if (SimpletinyString.isEmpty(wechat)) {
				resultStr = "nocellphone";
			} else {
				resultStr = "noclient";
			}
		} else {

			ClientEmployeeBean e = employees.get(0);
			ClientRelationBean cr = service.selectSummaryByEmployeePk(e.getPk());
			if (e.getDelete_flg().equals("Y")) {
				resultStr = "stopuse";
			} else if (e.getPublic_flg().equals("Y")) {
				resultStr = "public";
				relations = new ArrayList<ClientRelationBean>();
				relations.add(cr);
			} else {
				if (cr.getRelation_level().equals("主力级")) {
					resultStr = "main";
				} else if (cr.getRelation_level().equals("忽略级")) {
					resultStr = "ignore";
					sales_name = cr.getSales_name();
				} else {
					resultStr = "normal";
				}
			}
		}
		return SUCCESS;
	}

	private List<ConnectDto> connects;
	private String client_employee_pk;

	public String searchConnectsByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		ConnectDto connect = new ConnectDto();
		connect.setClient_employee_pk(client_employee_pk);

		params.put("bo", connect);
		page.setParams(params);
		connects = service.searchConnectsByPage(page);

		return SUCCESS;
	}

	private ClientRelationBean clientRelation;

	public String updateEmployeeRelationLevel() {
		resultStr = service.updateEmployeeRelationLevel(clientRelation);
		return SUCCESS;
	}

	private String employeeCount;
	private String monthOrderCount;
	private List<ClientSummaryDto> clientSummary;
	@Autowired
	private OrderService orderService;

	@Autowired
	private AccurateSaleService accurateSaleService;

	private BigDecimal sale_score;

	private int today_point;

	private PotentialDto potential;

	private MeterDto meter;

	private WorkOrderDto workOrder;

	private AccurateSaleDto accurateSale;

	private IncomingCountDto incomingCount;

	private ClientEmployeeTypeCountBean clientEmployeeTypeCount;

	public String searchClientSummary() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		SaleScoreDto ssd = new SaleScoreDto();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			relation.setSales_name(sessionBean.getUser_name());
		}
		ssd.setSale_number(sessionBean.getUser_number());
		ssd.setConfirm_month(DateUtil.getDateStr("yyyy-MM"));

		List<SaleScoreDto> scores = orderService.searchSaleScoreByParam(ssd);
		if (null != scores && scores.size() > 0) {
			sale_score = scores.get(0).getScore();
		}

		today_point = service.caculateTodayPoint();

		BigDecimal point_money_deduct = BigDecimal.ZERO;
		float back_score = 0;

		String user_pk = "";
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			user_pk = sessionBean.getPk();
		} else {
			user_pk = relation.getSales();
		}

		potential = service.selectPotentialData(user_pk);
		meter = service.selectMeterData(user_pk);
		workOrder = service.selectWorkOrderData(user_pk);
		accurateSale = service.selectAccurateSaleData(user_pk);
		incomingCount = service.selectIncomingDate(user_pk);

		ClientEmployeeTypeCountBean optionType = new ClientEmployeeTypeCountBean();
		optionType.setUser_pk(user_pk);
		optionType.setMonth(DateUtil.lastMonth());
		clientEmployeeTypeCount = service.selectTypeCount(optionType);

		if (!SimpletinyString.isEmpty(user_pk)) {
			point_money_deduct = service.caculatePointMoneyDeduct(user_pk);
		}

		back_score = service.caculateBackPoint(user_pk);

		if (meter == null)
			meter = new MeterDto();

		if (workOrder == null)
			workOrder = new WorkOrderDto();

		if (accurateSale == null)
			accurateSale = new AccurateSaleDto();

		if (incomingCount == null)
			incomingCount = new IncomingCountDto();

		meter.setPoint_money_deduct(point_money_deduct);
		meter.setBack_score(back_score);
		// clientSummary = service.getClientSummary(relation);
		// employeeCount = service.selectClientEmployeeCount(relation);
		// monthOrderCount = service.selectMonthOrderCount(relation);
		return SUCCESS;
	}

	private List<ClientVisitBean> visits;

	// 搜索拜访记录
	public String searchVisitByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", visit);
		page.setParams(params);

		visits = service.selectVisitByPage(page);
		return SUCCESS;
	}

	public ClientVisitBean getVisit() {
		return visit;
	}

	public void setVisit(ClientVisitBean visit) {
		this.visit = visit;
	}

	public ClientRelationSummaryBean getRelation() {
		return relation;
	}

	public void setRelation(ClientRelationSummaryBean relation) {
		this.relation = relation;
	}

	public List<ClientSummaryDto> getClientSummary() {
		return clientSummary;
	}

	public void setClientSummary(List<ClientSummaryDto> clientSummary) {
		this.clientSummary = clientSummary;
	}

	public String getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(String employeeCount) {
		this.employeeCount = employeeCount;
	}

	public String getMonthOrderCount() {
		return monthOrderCount;
	}

	public void setMonthOrderCount(String monthOrderCount) {
		this.monthOrderCount = monthOrderCount;
	}

	public List<ClientVisitBean> getVisits() {
		return visits;
	}

	public void setVisits(List<ClientVisitBean> visits) {
		this.visits = visits;
	}

	public BigDecimal getSale_score() {
		return sale_score;
	}

	public void setSale_score(BigDecimal sale_score) {
		this.sale_score = sale_score;
	}

	public int getToday_point() {
		return today_point;
	}

	public void setToday_point(int today_point) {
		this.today_point = today_point;
	}

	public PotentialDto getPotential() {
		return potential;
	}

	public void setPotential(PotentialDto potential) {
		this.potential = potential;
	}

	public MeterDto getMeter() {
		return meter;
	}

	public void setMeter(MeterDto meter) {
		this.meter = meter;
	}

	public WorkOrderDto getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrderDto workOrder) {
		this.workOrder = workOrder;
	}

	public AccurateSaleDto getAccurateSale() {
		return accurateSale;
	}

	public void setAccurateSale(AccurateSaleDto accurateSale) {
		this.accurateSale = accurateSale;
	}

	public MobileTouchBean getMobile() {
		return mobile;
	}

	public void setMobile(MobileTouchBean mobile) {
		this.mobile = mobile;
	}

	public IncomingCallBean getIncoming() {
		return incoming;
	}

	public void setIncoming(IncomingCallBean incoming) {
		this.incoming = incoming;
	}

	public ClientEmployeeQuitConnectLogBean getQuit() {
		return quit;
	}

	public void setQuit(ClientEmployeeQuitConnectLogBean quit) {
		this.quit = quit;
	}

	public List<ConnectDto> getConnects() {
		return connects;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public void setConnects(List<ConnectDto> connects) {
		this.connects = connects;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public ClientRelationBean getClientRelation() {
		return clientRelation;
	}

	public void setClientRelation(ClientRelationBean clientRelation) {
		this.clientRelation = clientRelation;
	}

	public IncomingCountDto getIncomingCount() {
		return incomingCount;
	}

	public void setIncomingCount(IncomingCountDto incomingCount) {
		this.incomingCount = incomingCount;
	}

	public String getWechat() {
		return wechat;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public List<ClientEmployeeBean> getEmployees() {
		return employees;
	}

	public void setEmployees(List<ClientEmployeeBean> employees) {
		this.employees = employees;
	}

	public String getSales_name() {
		return sales_name;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public List<ClientRelationBean> getRelations() {
		return relations;
	}

	public void setRelations(List<ClientRelationBean> relations) {
		this.relations = relations;
	}

	public ClientEmployeeTypeCountBean getClientEmployeeTypeCount() {
		return clientEmployeeTypeCount;
	}

	public void setClientEmployeeTypeCount(ClientEmployeeTypeCountBean clientEmployeeTypeCount) {
		this.clientEmployeeTypeCount = clientEmployeeTypeCount;
	}
}