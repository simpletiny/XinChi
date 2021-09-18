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

		String sql2 = "UPDATE team_report A LEFT JOIN view_discount_receivable B ON A.team_number = B.team_number"
				+ " SET A.discount_flg = 'Y',  A.discount_receivable = B.discount_received" + " WHERE B.update_time = '"
				+ DateUtil.yesterday() + "';";

		ss.setSql(sql2);
		commonDao.exeBySql(ss);
	}
}
