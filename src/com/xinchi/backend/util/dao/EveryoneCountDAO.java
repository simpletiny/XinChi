package com.xinchi.backend.util.dao;

import com.xinchi.bean.EveryoneCountBean;

public interface EveryoneCountDAO {
	public EveryoneCountBean selectCountByType(String type);
	
	public void update(EveryoneCountBean bean);

}
