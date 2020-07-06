package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.SeasonTicketBaseDAO;
import com.xinchi.backend.ticket.dao.SeasonTicketInfoDAO;
import com.xinchi.backend.ticket.service.SeasonTicketService;
import com.xinchi.bean.SeasonTicketBaseBean;
import com.xinchi.bean.SeasonTicketInfoBean;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class SeasonTicketServiceImpl implements SeasonTicketService {

	@Autowired
	private SeasonTicketBaseDAO baseDao;

	@Autowired
	private SeasonTicketInfoDAO infoDao;

	@Override
	public String createSeasonTicket(String json, SeasonTicketBaseBean base) {
		// 保存套票基本信息
		String base_pk = baseDao.insert(base);

		// 保存套票详细信息
		JSONArray arr = JSONArray.fromObject(json);
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = JSONObject.fromObject(arr.get(i));

			int start_day = obj.getInt("start_day");
			int ticket_index = obj.getInt("ticket_index");
			String add_day_flg = obj.getString("add_day_flg");
			String index_leg = obj.getString("index_leg");
			String ticket_number = obj.getString("ticket_number");
			String ticket_leg = obj.getString("ticket_leg");
			String start_time = obj.getString("start_time");
			String end_time = obj.getString("end_time");
			String start_place = obj.getString("start_place");
			String end_place = obj.getString("end_place");

			SeasonTicketInfoBean info = new SeasonTicketInfoBean();
			info.setStart_day(start_day);
			info.setTicket_index(ticket_index);
			info.setAdd_day_flg(add_day_flg);
			info.setIndex_leg(index_leg);
			info.setTicket_number(ticket_number);
			info.setTicket_leg(ticket_leg);
			info.setStart_time(start_time);
			info.setEnd_time(end_time);
			info.setStart_place(start_place);
			info.setEnd_place(end_place);
			info.setBase_pk(base_pk);

			infoDao.insert(info);

		}

		return SUCCESS;
	}

	@Override
	public List<SeasonTicketBaseBean> selectByPage(Page<SeasonTicketBaseBean> page) {
		return baseDao.selectByPage(page);
	}

}