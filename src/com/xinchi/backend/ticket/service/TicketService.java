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

	public String rollBackTicketChange(String change_pk);

	public AirTicketChangeLogBean searchFlightChangeLogByPassengerPk(String passenger_pk);

	public List<AirTicketChangeLogBean> searchTicketChangeByPage(Page page);

	public String toggleLockOrder(String team_number, String lock_flg);

	public AirTicketChangeLogBean searchFlightChangeLogByPk(String change_pk);

}