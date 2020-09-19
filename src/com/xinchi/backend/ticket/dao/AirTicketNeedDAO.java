package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.OrderAirInfoBean;
import com.xinchi.tools.Page;

public interface AirTicketNeedDAO {
	public AirTicketNeedBean selectByPk(String pk);

	public List<AirTicketNeedBean> selectByPage(Page<AirTicketNeedBean> page);

	public List<AirTicketNeedBean> selectOrderByPage(Page page);

	public List<OrderAirInfoBean> selectOrderAirInfoByTeamNumber(String team_number);

	public String insert(AirTicketNeedBean atn);

	public List<OrderAirInfoBean> selectOrderAirInfoByProductOrderNumber(String order_number);

	public void update(AirTicketNeedBean atn);

	public AirTicketNeedBean selectByProductOrderNumber(String order_number);

	public void delete(String pk);
}
