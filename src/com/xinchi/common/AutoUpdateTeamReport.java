package com.xinchi.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoUpdateTeamReport {

	@Autowired
	private CommonDAO commonDao;

	public void updateTeamReport(String[] param) {
		String sql1 = "call update_team_report();";
		SqlBean ss = new SqlBean();
		ss.setSql(sql1);
		commonDao.exeBySql(ss);
	}
}
