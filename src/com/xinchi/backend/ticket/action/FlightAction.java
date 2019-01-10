package com.xinchi.backend.ticket.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.ticket.service.FlightService;
import com.xinchi.bean.FlightBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FlightAction extends BaseAction {
	private static final long serialVersionUID = -6429269342357456682L;

	@Autowired
	private FlightService service;

	private FlightBean flight;

	private String json;

	private List<FlightBean> flights;

	public String searchFlightByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", flight);
		page.setParams(params);

		flights = service.selectByPage(page);
		return SUCCESS;

	}

	/**
	 * 创建航班
	 * 
	 * @return
	 */
	public String createFlight() {
		resultStr = service.createFlight(flight, json);
		return SUCCESS;
	}

	public FlightBean getFlight() {
		return flight;
	}

	public void setFlight(FlightBean flight) {
		this.flight = flight;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<FlightBean> getFlights() {
		return flights;
	}

	public void setFlights(List<FlightBean> flights) {
		this.flights = flights;
	}
}