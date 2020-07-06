package com.xinchi.backend.ticket.service;

import com.xinchi.bean.AirTicketChangeLogBean;
import com.xinchi.common.BaseService;

public interface TicketService extends BaseService {
	/**
	 * 航变处理
	 * 
	 * @param json
	 * @return
	 */
	public String changFlight(String json);

	public AirTicketChangeLogBean searchFlightChangeLogByPassengerPk(String passenger_pk);

}