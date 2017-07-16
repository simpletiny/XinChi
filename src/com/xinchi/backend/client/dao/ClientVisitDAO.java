package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientVisitBean;
import com.xinchi.tools.Page;

public interface ClientVisitDAO {

	void insert(ClientVisitBean visit);

	List<ClientVisitBean> selectByPage(Page page);

}
