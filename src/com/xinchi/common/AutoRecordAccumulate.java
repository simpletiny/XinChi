package com.xinchi.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoRecordAccumulate {

	@Autowired
	private CommonDAO commonDao;

	public void recordAccumulate(String[] param) {
		String sql1 = "insert into accumulate_balance(user_pk,date,receivable_balance,user_number) SELECT"
				+ "	C.pk AS user_pk,LEFT(NOW(), 10) AS date,SUM(IF(final_flg = 'N',budget_balance,final_balance)) AS receivable_balance,"
				+ "sales AS user_number FROM receivable A LEFT JOIN budget_order_view B ON A.team_number = B.team_number"
				+ " LEFT JOIN user_base C ON A.sales=C.user_number WHERE B.confirm_date <= LEFT(NOW(), 10)"
				+ "GROUP BY sales having receivable_balance >0;";
		SqlBean ss = new SqlBean();
		ss.setSql(sql1);
		commonDao.exeBySql(ss);

		String sql2 = "INSERT INTO bad_interest(user_number,date,bad_interest)"
				+ " SELECT A.user_number,LEFT(NOW(), 10) AS date,ROUND(A.bad*B.ext2/B.ext1, 2) AS bad_interest"
				+ " FROM view_receivable_meter A LEFT JOIN base_data B ON B.name = '呆账设置' WHERE A.bad > 0;";
		SqlBean ss1 = new SqlBean();
		ss1.setSql(sql2);
		commonDao.exeBySql(ss1);
	}
}
