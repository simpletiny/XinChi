package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.AccurateSaleDto;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ConnectDto;
import com.xinchi.bean.MeterDto;
import com.xinchi.bean.PotentialDto;
import com.xinchi.bean.WorkOrderDto;
import com.xinchi.tools.Page;

public interface ClientRelationDAO {

	public List<ClientRelationSummaryBean> selectByPage(Page page);

	public String selectClientCount(String sales_name);

	public String selectWeekOrderCount(String sales_name);

	public String selectMonthOrderCount(String sales_name);

	public String selectYesterdayVisitCount(String sales_name);

	public String selectWeekVisitCount(String sales_name);

	public String selectMonthVisitCount(String sales_name);

	public String selectYesterdayChatCount(String sales_name);

	public String selectWeekChatCount(String sales_name);

	public String selectMonthChatCount(String sales_name);

	public List<ClientSummaryDto> selectMarketSummary(String sales_name);

	public List<ClientSummaryDto> selectRelationSummary(String sales_name);

	public List<ClientSummaryDto> selectBackSummary(String sales_name);

	public List<ClientSummaryDto> selectSummary(String sales_name);

	public PotentialDto selectPotentialData(String user_pk);

	public MeterDto selectMeterData(String user_pk);

	public WorkOrderDto selectWorkOrderData(String user_pk);

	public AccurateSaleDto selectAccurateSaleData(String user_pk);

	public List<ConnectDto> selectConnectsByPage(Page<ConnectDto> page);

	public List<ClientRelationBean> selectByParam(ClientRelationBean option);

	public void insert(ClientRelationBean two);

	public void update(ClientRelationBean clientRelation);

	public ClientRelationBean selectByEmployeePk(String employee_pk);

	public void delete(String pk);

}
