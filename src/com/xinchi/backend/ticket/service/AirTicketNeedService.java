package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface AirTicketNeedService extends BaseService {

	public List<AirTicketNeedBean> selectByPage(Page<AirTicketNeedBean> page);

	public List<AirTicketNeedBean> selectOrderByPage(Page page);
}
