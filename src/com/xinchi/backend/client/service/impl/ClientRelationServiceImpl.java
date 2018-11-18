package com.xinchi.backend.client.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientEmployeeQuitConnectLogDAO;
import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.dao.IncomingCallDAO;
import com.xinchi.backend.client.dao.MobileTouchDAO;
import com.xinchi.backend.client.service.ClientRelationService;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.bean.AccurateSaleDto;
import com.xinchi.bean.BackPointDto;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeQuitConnectLogBean;
import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.ConnectDto;
import com.xinchi.bean.IncomingCallBean;
import com.xinchi.bean.MeterDto;
import com.xinchi.bean.MobileTouchBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PointDto;
import com.xinchi.bean.PotentialDto;
import com.xinchi.bean.WorkOrderDto;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ClientRelationServiceImpl implements ClientRelationService {

	@Autowired
	private ClientVisitDAO visitDao;
	@Autowired
	private ClientRelationDAO dao;

	@Override
	public String createVisit(ClientVisitBean visit) {
		visitDao.insert(visit);
		return "OK";
	}

	@Override
	public List<ClientRelationSummaryBean> getRelationsByPage(Page page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<ClientSummaryDto> getClientSummary(ClientRelationSummaryBean relation) {
		String sales_name = relation.getSales_name();
		List<ClientSummaryDto> cs = new ArrayList<ClientSummaryDto>();
		if (SimpletinyString.isEmpty(relation.getLevel())) {
			cs = dao.selectSummary(sales_name);
		} else if (relation.getLevel().equals("关系度")) {
			cs = dao.selectRelationSummary(sales_name);
		} else if (relation.getLevel().equals("回款誉")) {
			cs = dao.selectBackSummary(sales_name);
		} else if (relation.getLevel().equals("市场力")) {
			cs = dao.selectMarketSummary(sales_name);
		}

		return cs;
	}

	@Override
	public String selectClientEmployeeCount(ClientRelationSummaryBean relation) {
		String sales_name = relation.getSales_name();
		return dao.selectClientCount(sales_name);
	}

	@Override
	public String selectMonthOrderCount(ClientRelationSummaryBean relation) {
		String sales_name = relation.getSales_name();
		return dao.selectMonthOrderCount(sales_name);
	}

	@Override
	public List<ClientVisitBean> selectVisitByPage(Page page) {

		return visitDao.selectByPage(page);
	}

	@Override
	public List<ClientVisitBean> selectVisitByParam(ClientVisitBean param) {
		return visitDao.selectByParam(param);
	}

	@Override
	public PotentialDto selectPotentialData(String user_pk) {

		return dao.selectPotentialData(user_pk);
	}

	@Override
	public MeterDto selectMeterData(String user_pk) {

		return dao.selectMeterData(user_pk);
	}

	@Override
	public WorkOrderDto selectWorkOrderData(String user_pk) {

		return dao.selectWorkOrderData(user_pk);
	}

	@Override
	public AccurateSaleDto selectAccurateSaleData(String user_pk) {
		return dao.selectAccurateSaleData(user_pk);
	}

	@Autowired
	private MobileTouchDAO mobileTouchDao;

	@Override
	public String createMobileTouch(MobileTouchBean mobile) {
		mobileTouchDao.insert(mobile);
		return SUCCESS;
	}

	@Autowired
	private IncomingCallDAO incomingCallDao;

	@Override
	public String createIncomingCall(IncomingCallBean incoming) {
		incomingCallDao.insert(incoming);
		return SUCCESS;
	}

	@Autowired
	private EmployeeDAO employeeDao;

	@Autowired
	private ClientEmployeeQuitConnectLogDAO quitDao;

	@Autowired
	private ClientEmployeeUserDAO clientEmployeeUserDao;

	@Override
	public String quitConnectEmployee(ClientEmployeeQuitConnectLogBean quit) {
		ClientEmployeeBean employee = employeeDao.selectByPrimaryKey(quit.getClient_employee_pk());
		employee.setQuit_flg("Y");
		employee.setPublic_flg("Y");

		clientEmployeeUserDao.deleteByEmployeePk(quit.getClient_employee_pk());
		ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();
		ceub.setEmployee_pk(quit.getClient_employee_pk());
		ceub.setUser_pk("public");
		clientEmployeeUserDao.insert(ceub);

		quit.setDate(DateUtil.today());
		quitDao.insert(quit);

		employeeDao.update(employee);
		return SUCCESS;
	}

	@Override
	public List<ConnectDto> searchConnectsByPage(Page<ConnectDto> page) {
		return dao.selectConnectsByPage(page);
	}

	@Autowired
	private EmployeeDAO employeeDAO;

	@Override
	public String updateEmployeeRelationLevel(ClientRelationBean clientRelation) {
		dao.update(clientRelation);
		ClientEmployeeBean employee = new ClientEmployeeBean();
		employee.setPk(clientRelation.getClient_employee_pk());

		employee.setRelation_level(clientRelation.getRelation_level());
		employeeDAO.update(employee);
		return SUCCESS;
	}

	@Autowired
	private OrderDAO orderDao;

	@Override
	public int caculateTodayPoint() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		// 计算当日勤点
		int today_point = -10;
		String today = DateUtil.today();
		// 搜索当日确认订单
		OrderDto orderOption = new OrderDto();
		orderOption.setCreate_user_number(sessionBean.getUser_number());
		orderOption.setConfirm_date(today);
		List<OrderDto> orders = orderDao.selectByParam(orderOption);
		if (null != orders && orders.size() > 0) {
			today_point += orders.size() * 4;
		}
		// 搜索当日有效拜访
		ClientVisitBean visitOption = new ClientVisitBean();
		visitOption.setCreate_user(sessionBean.getUser_number());
		visitOption.setDate(today);
		visitOption.setType("VISIT");

		List<ClientVisitBean> visits = visitDao.selectByParam(visitOption);

		if (null != visits && visits.size() > 0) {
			today_point += visits.size() * 3;
		}

		// 搜索当日主动电联（精推）
		MobileTouchBean touchOption = new MobileTouchBean();
		touchOption.setCreate_user(sessionBean.getUser_number());
		touchOption.setDate(today);

		List<MobileTouchBean> touchs = mobileTouchDao.selectByParam(touchOption);
		if (null != touchs && touchs.size() > 0) {
			today_point += touchs.size() * 2;
		}

		// 搜索当日被动咨询（精推）
		IncomingCallBean callOption = new IncomingCallBean();
		callOption.setCreate_user(sessionBean.getUser_number());
		callOption.setDate(today);

		List<IncomingCallBean> calls = incomingCallDao.selectByParam(callOption);
		if (null != calls && calls.size() > 0) {
			today_point += calls.size() * 1;
		}

		return today_point;
	}

	@Override
	public BigDecimal caculatePointMoneyDeduct(String user_pk) {
		PointDto option = new PointDto();
		option.setUser_pk(user_pk);

		BigDecimal deduct = BigDecimal.ZERO;

		List<PointDto> point = dao.selectPointByParam(option);

		if (null == point) {
			deduct = new BigDecimal(100 * DateUtil.todayOfMonth());
		} else {
			for (int i = 0; i < point.size(); i++) {
				PointDto p = point.get(i);
				if (p.getPoint() < 10) {
					deduct = deduct.add(new BigDecimal(10 * (10 - p.getPoint())));
				}
			}

			int nullDays = DateUtil.todayOfMonth() - point.size();

			deduct = deduct.add(new BigDecimal(100 * nullDays));
		}

		return deduct;
	}

	@Autowired
	private ReceivedDAO receivedDao;

	@Override
	public float caculateBackPoint(String user_pk) {
		float point = 0;
		BackPointDto option = new BackPointDto();
		option.setUser_pk(user_pk);
		List<BackPointDto> enableBackPoints = dao.selectEnableBackPointByParam(option);

		ClientReceivedDetailBean rOption = new ClientReceivedDetailBean();

		if (null != enableBackPoints) {
			for (BackPointDto bp : enableBackPoints) {
				rOption.setTeam_number(bp.getTeam_number());
				rOption.setConfirm_time_end(DateUtil.addDate(bp.getConfirm_date(), 1));
				List<ClientReceivedDetailBean> crdbs = receivedDao.selectByParam(rOption);

				if (null == crdbs || crdbs.size() == 0)
					continue;
				boolean flg = true;
				for (ClientReceivedDetailBean crdb : crdbs) {
					if (!crdb.getStatus().equals("E")) {
						flg = false;
						continue;
					}
				}
				if (flg) {
					point += bp.getProduct_point() * 0.2;
				}
			}
		}

		return point;
	}
}