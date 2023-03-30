package com.xinchi.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.AccumulateBalanceDAO;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.AccumulateBalanceBean;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoRecordAccumulate {

	@Autowired
	private CommonDAO commonDao;

	@Autowired
	private AccumulateBalanceDAO accumulateBalanceDao;

	public void recordAccumulate(String[] param) {
		List<AccumulateBalanceBean> list = accumulateBalanceDao.selectNeedInsertAccumulateBalance();

		for (AccumulateBalanceBean ab : list) {

			accumulateBalanceDao.insert(ab);
		}

		String sql2 = "INSERT INTO bad_interest(user_number,date,bad_interest)"
				+ " SELECT A.user_number,LEFT(NOW(), 10) AS date,ROUND(A.bad*B.ext2/B.ext1, 2) AS bad_interest"
				+ " FROM view_receivable_meter A LEFT JOIN base_data B ON B.name = '呆账设置' WHERE A.bad > 0;";
		SqlBean ss1 = new SqlBean();
		ss1.setSql(sql2);
		commonDao.exeBySql(ss1);
	}
}
