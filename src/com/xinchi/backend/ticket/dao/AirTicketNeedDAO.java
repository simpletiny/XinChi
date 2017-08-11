package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.tools.Page;

public interface AirTicketNeedDAO {
	public List<AirTicketNeedBean> selectByPage(Page<AirTicketNeedBean> page);

	public List<AirTicketNeedBean> selectOrderByPage(Page page);
}
