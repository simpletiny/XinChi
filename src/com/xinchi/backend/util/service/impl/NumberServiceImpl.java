package com.xinchi.backend.util.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.util.dao.EveryoneCountDAO;
import com.xinchi.backend.util.dao.TeamNumberDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.EveryoneCountBean;
import com.xinchi.bean.TeamNumberBean;
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String user_pk = sessionBean.getPk();
		String user_number = sessionBean.getUser_number();

		String prefix = "N" + user_number.substring(user_number.length() - 2, user_number.length());

		TeamNumberBean option = new TeamNumberBean();
		option.setUser_pk(user_pk);
		option.setType("T");

		TeamNumberBean tb = dao.selectNextNumber(option);

		String next = "";
		String source = source_team_number;
		if (null == tb) {
			team_number = prefix + first_team_number;
			next = addOne(first_team_number, source);
			tb = new TeamNumberBean();
			tb.setUser_pk(user_pk);
			tb.setTeam_number(next);
			dao.insert(tb);
		} else {
			String current = tb.getTeam_number();
			team_number = prefix + current;
			next = addOne(current, source);

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

	private String addOne(String value, String source) {

		if (null == value || value.equals(""))
			return "";
		int strLength = value.length();
		String last = value.substring(strLength - 1);
		String first = value.substring(0, strLength - 1);
		int nextCharIndex = source.indexOf(last) + 1;
		if (nextCharIndex >= 36) {
			String result = addOne(first, source) + "G";
			if (result.equals("GGGG")) {
				return "YOU ARE RICH!";
			} else {
				return result;
			}
		} else {
			return first + source.charAt(source.indexOf(last) + 1);
		}
	}

	private static String getByIndex(int index) {
		return doIndex(index, 3);
	}

	private static String doIndex(int index, int now) {
		String result = "";
		int mod = (now + index) % 36;
		int z = ((now + index)) / 36;
		String newC = String.valueOf(source_team_number.charAt(mod));
		if (z > 0) {
			now--;
			result = doIndex(z, now) + newC;
		} else {
			result = source_team_number.substring(0, now) + newC;
		}
		return result;
	}

	@Override
	public String generateProductNumber() {
		String product_number = "";
		EveryoneCountBean count = countDao.selectCountByType(ResourcesConstants.COUNT_TYPE_PRODUCT);
		product_number = getByIndex(count.getCount());
		count.setCount(count.getCount() + 1);
		countDao.update(count);
		return product_number;
	}

	@Override
	public String generateTicketOrderNumber() {
		String order_number = "";
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String user_pk = sessionBean.getPk();
		String user_number = sessionBean.getUser_number();

		String prefix = "A" + user_number.substring(user_number.length() - 2, user_number.length());

		TeamNumberBean option = new TeamNumberBean();
		option.setUser_pk(user_pk);
		option.setType("A");
		TeamNumberBean tb = dao.selectNextNumber(option);

		String source = source_ticket_order_number;
		String next = "";
		if (null == tb) {
			order_number = prefix + first_ticket_order_number;
			next = addOne(first_ticket_order_number, source);

			tb = new TeamNumberBean();
			tb.setUser_pk(user_pk);
			tb.setType("A");
			tb.setTeam_number(next);
			dao.insert(tb);
		} else {
			String current = tb.getTeam_number();
			order_number = prefix + current;
			next = addOne(current, source);

			tb.setTeam_number(next);
			dao.update(tb);
		}

		return order_number;
	}

	@Override
	public String generateProductOrderNumber() {
		String order_number = "";
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String user_pk = sessionBean.getPk();
		String user_number = sessionBean.getUser_number();

		String prefix = "P" + user_number.substring(user_number.length() - 2, user_number.length());

		TeamNumberBean option = new TeamNumberBean();
		option.setUser_pk(user_pk);
		option.setType("P");
		TeamNumberBean tb = dao.selectNextNumber(option);

		String source = source_product_order_number;
		String next = "";
		if (null == tb) {
			order_number = prefix + first_product_order_number;
			next = addOne(first_product_order_number, source);

			tb = new TeamNumberBean();
			tb.setUser_pk(user_pk);
			tb.setType("P");
			tb.setTeam_number(next);
			dao.insert(tb);
		} else {
			String current = tb.getTeam_number();
			order_number = prefix + current;
			next = addOne(current, source);

			tb.setTeam_number(next);
			dao.update(tb);
		}

		return order_number;
	}
}
