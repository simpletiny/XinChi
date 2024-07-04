package com.xinchi.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoDeleteDishonestRecord {
	@Autowired
	private BaseDataDAO baseDataDao;

	@Autowired
	private CommonDAO commonDao;

	public void deleteDishonestRecord(String[] param) {

		BaseDataBean option = new BaseDataBean();
		option.setName("失信信息保存时长");
		option.setType("DISHONEST");
		List<BaseDataBean> bds = baseDataDao.selectByParam(option);

		if (null == bds || bds.size() < 1)
			return;

		BaseDataBean config = bds.get(0);
		String days = config.getExt1();
		String sql1 = "delete from dishonest_person where datediff(now(),from_unixtime(create_time/1000)) = " + days
				+ ";";
		SqlBean ss = new SqlBean();
		ss.setSql(sql1);
		commonDao.exeBySql(ss);

		String sql2 = "delete from dishonest_log where datediff(now(),from_unixtime(create_time/1000)) = " + days + ";";
		ss.setSql(sql2);
		commonDao.exeBySql(ss);
	}
}
