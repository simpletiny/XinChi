package com.xinchi.backend.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientEmployeeQuitConnectLogDAO;
import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.dao.IncomingCallDAO;
import com.xinchi.backend.client.dao.MobileTouchDAO;
import com.xinchi.backend.client.service.ClientRelationService;
import com.xinchi.bean.AccurateSaleDto;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeQuitConnectLogBean;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.ConnectDto;
import com.xinchi.bean.IncomingCallBean;
import com.xinchi.bean.MeterDto;
import com.xinchi.bean.MobileTouchBean;
import com.xinchi.bean.PotentialDto;
import com.xinchi.bean.WorkOrderDto;
import com.xinchi.common.DateUtil;
import com.xinchi.common.SimpletinyString;
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

	@Override
	public String quitConnectEmployee(ClientEmployeeQuitConnectLogBean quit) {
		ClientEmployeeBean employee = employeeDao.selectByPrimaryKey(quit.getClient_employee_pk());
		employee.setQuit_flg("Y");
		employeeDao.update(employee);

		quit.setDate(DateUtil.today());
		quitDao.insert(quit);
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
		dao.updateClientRelation(clientRelation);
		ClientEmployeeBean employee = new ClientEmployeeBean();
		employee.setPk(clientRelation.getClient_employee_pk());

		employee.setRelation_level(clientRelation.getRelation_level());
		employeeDAO.update(employee);
		return SUCCESS;
	}
}