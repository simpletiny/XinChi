package com.xinchi.backend.util.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.util.dao.EveryoneCountDAO;
import com.xinchi.backend.util.dao.TeamNumberDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.EveryoneCountBean;
import com.xinchi.bean.TeamNumberBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.Utils;
import com.xinchi.common.XinChiApplicationContext;

@Service
@Transactional
public class NumberServiceImpl implements NumberService {

	@Autowired
	private TeamNumberDAO dao;

	@Override
	public String generateTeamNumber() {
		String team_number = "";
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String sale_pk = sessionBean.getPk();
		String user_number = sessionBean.getUser_number();

		String prefix = "N" + user_number.substring(user_number.length() - 2, user_number.length());
		TeamNumberBean tb = dao.selectTeamNumberBySalePk(sale_pk);

		String next = "";
		if (null == tb) {
			team_number = prefix + first;
			next = addOne(first);

			tb = new TeamNumberBean();
			tb.setSale_pk(sale_pk);
			tb.setTeam_number(next);
			dao.insert(tb);
		} else {
			String current = tb.getTeam_number();
			team_number = prefix + current;
			next = addOne(current);

			tb.setTeam_number(next);
			dao.update(tb);
		}

		return team_number;
	}

	@Autowired
	private EveryoneCountDAO countDao;

	/**
	 * 生成支付单号
	 * 
	 * @return
	 */
	@Override
	public String generatePayOrderNumber(String type, String orderType, String date) {
		String number = orderType;
		EveryoneCountBean count = countDao.selectCountByType(type);
		String tail = Utils.fullFill(count.getCount(), "0", 7);
		number += date + tail;
		count.setCount(count.getCount() + 1);
		countDao.update(count);
		return number;
	}

	private String addOne(String value) {
		if (null == value || value.equals(""))
			return "";
		int strLength = value.length();
		String last = value.substring(strLength - 1);
		String first = value.substring(0, strLength - 1);
		int nextCharIndex = source.indexOf(last) + 1;
		if (nextCharIndex >= 36) {
			String result = addOne(first) + "G";
			if (result.equals("GGGG")) {
				return "YOU ARE RICH!";
			} else {
				return result;
			}
		} else {
			return first + source.charAt(source.indexOf(last) + 1);
		}
	}

}
