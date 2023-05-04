package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.AirTicketChangeLogBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface TicketService extends BaseService {
	/**
	 * 航变处理
	 * 
	 * @param json
	 * @return
	 */
	public String changFlight(String json);

	public AirTicketChangeLogBean searchFlightChangeLogByPassengerPk(String passenger_pk);

	public List<AirTicketChangeLogBean> searchTicketChangeByPage(Page page);

	public String toggleLockOrder(String team_number, String lock_flg);

}