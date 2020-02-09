package com.xinchi.common;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.bean.BaseDataBean;

@Service
@Transactional
public class AutoCleanBadInterest {

	@Autowired
	private BaseDataDAO baseDataDao;

	public void cleanBadInterest(String[] param) {

		BaseDataBean option = new BaseDataBean();
		option.setName("呆账设置");
		option.setType("BAD");
		List<BaseDataBean> bds = baseDataDao.selectByParam(option);

		if (null == bds || bds.size() < 1)
			return;

		BaseDataBean config = bds.get(0);
		if (!config.getCode().equals("AUTO")) {
			return;
		}

		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int configDay = Integer.valueOf(config.getExt3());

		if (day >= configDay) {
			option.setName("呆罚清除日");
			option.setType("BAD");
			List<BaseDataBean> bds1 = baseDataDao.selectByParam(option);
			if (null == bds1 || bds1.size() < 1)
				return;

			BaseDataBean cleanDay = bds1.get(0);
			if (DateUtil.compare(DateUtil.today(), cleanDay.getExt1()) <= 1) {
				String cleanDate = DateUtil.getDateStr("yyyy-MM");

				if (configDay < 10) {
					cleanDate += "-0" + configDay;
				} else {
					cleanDate += "-" + configDay;
				}
				if (DateUtil.compare(cleanDay.getExt1(), cleanDate) <= 1)
					return;

				cleanDay.setExt1(cleanDate);

				baseDataDao.sysUpdateByPK(cleanDay);
			}

		}
	}
}
