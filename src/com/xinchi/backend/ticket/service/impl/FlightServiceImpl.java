package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.FlightDAO;
import com.xinchi.backend.ticket.dao.FlightInfoDAO;
import com.xinchi.backend.ticket.service.FlightService;
import com.xinchi.bean.FlightBean;
import com.xinchi.bean.FlightInfoBean;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightDAO dao;

	@Override
	public void insert(FlightBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(FlightBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public FlightBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<FlightBean> selectByParam(FlightBean bean) {
		return dao.selectByParam(bean);
	}

	@Autowired
	private FlightInfoDAO infoDao;

	@Override
	public String createFlight(FlightBean flight, String json) {
		String flight_pk = dao.insert(flight);

		JSONArray arr = JSONArray.fromObject(json);

		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			int flight_index = obj.getInt("flight_index");
			String flight_leg = obj.getString("flight_leg");

			int start_day = obj.getInt("start_day");
			String start_city = obj.getString("start_city");
			int end_day = obj.getInt("end_day");
			String end_city = obj.getString("end_city");
			String flight_number = obj.getString("flight_number");

			FlightInfoBean info = new FlightInfoBean();

			info.setFlight_pk(flight_pk);
			info.setFlight_index(flight_index);
			info.setFlight_leg(flight_leg);
			info.setStart_day(start_day);
			info.setStart_city(start_city);
			info.setEnd_day(end_day);
			info.setEnd_city(end_city);
			info.setFlight_number(flight_number);

			infoDao.insert(info);
		}
		return SUCCESS;
	}

	@Override
	public List<FlightBean> selectByPage(Page<FlightBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public FlightBean selectByProductPk(String product_pk) {
		return dao.selectByProductPk(product_pk);
	}

}