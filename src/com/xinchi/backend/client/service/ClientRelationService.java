package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.AccurateSaleDto;
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
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "客户关系")
public interface ClientRelationService extends BaseService {

	@LogDescription(des = "新增拜访")
	public String createVisit(ClientVisitBean visit);

	@LogDescription(des = "客户关系查看")
	public List<ClientRelationSummaryBean> getRelationsByPage(Page page);

	@LogDescription(ignore = true)
	public List<ClientSummaryDto> getClientSummary(ClientRelationSummaryBean relation);

	@LogDescription(ignore = true)
	public String selectClientEmployeeCount(ClientRelationSummaryBean relation);

	@LogDescription(ignore = true)
	public String selectMonthOrderCount(ClientRelationSummaryBean relation);

	@LogDescription(des = "拜访查看")
	public List<ClientVisitBean> selectVisitByPage(Page page);

	public List<ClientVisitBean> selectVisitByParam(ClientVisitBean param);

	public PotentialDto selectPotentialData(String pk);

	public MeterDto selectMeterData(String user_pk);

	public WorkOrderDto selectWorkOrderData(String pk);

	public AccurateSaleDto selectAccurateSaleData(String user_pk);

	public String createMobileTouch(MobileTouchBean mobile);

	public String createIncomingCall(IncomingCallBean incoming);

	public String quitConnectEmployee(ClientEmployeeQuitConnectLogBean quit);

	public List<ConnectDto> searchConnectsByPage(Page<ConnectDto> page);

	public String updateEmployeeRelationLevel(ClientRelationBean clientRelation);

}
