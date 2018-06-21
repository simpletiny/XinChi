package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientVisitBean;
import com.xinchi.tools.Page;

public interface ClientVisitDAO {

	public void insert(ClientVisitBean visit);
	
	public void update(ClientVisitBean visit);

	public List<ClientVisitBean> selectByPage(Page page);
	
	public List<ClientVisitBean> selectByParam(ClientVisitBean visit);
	

}
