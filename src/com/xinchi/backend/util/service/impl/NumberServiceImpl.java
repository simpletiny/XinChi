package com.xinchi.backend.util.service.impl;

import java.util.Arrays;

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
		return doGenerateNumber(NUMBER_TYPE_TEAM, SOURCE_TEAM_NUMBER);
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

	private static String addOne(String value, String source) {

		if (null == value || value.equals(""))
			return "";
		int strLength = value.length();
		String last = value.substring(strLength - 1);
		String first = value.substring(0, strLength - 1);
		int nextCharIndex = source.indexOf(last) + 1;
		if (nextCharIndex >= 36) {
			String result = addOne(first, source) + source.charAt(0);
			char[] max = new char[4];
			Arrays.fill(max, source.charAt(0));
			if (result.equals(String.valueOf(max))) {
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
		String newC = String.valueOf(SOURCE_TEAM_NUMBER.charAt(mod));
		if (z > 0) {
			now--;
			result = doIndex(z, now) + newC;
		} else {
			result = SOURCE_TEAM_NUMBER.substring(0, now) + newC;
		}
		return result;
	}
	// 看看能不能用到下面的代码，（上面的方法index值大于1678244之后会报错）
	// public static String doIndex(int decimal, int len) {
	// StringBuilder result = new StringBuilder();
	// while (decimal > 0) {
	// int remainder = decimal % 36;
	// result.insert(0, source_team_number.charAt(remainder));
	// decimal /= 36;
	// }
	//
	// if (result.length() < len) {
	// char[] zero = new char[len - result.length()];
	// Arrays.fill(zero, source_team_number.charAt(0));
	// result.insert(0, zero);
	// } else {
	// return "YOU ARE RICH";
	// }
	//
	// return result.toString();
	// }

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
		return doGenerateNumber(NUMBER_TYPE_TEAM_AIR_TICKET_ORDER, SOURCE_TICKET_ORDER_NUMBER);
	}

	@Override
	public String generateProductOrderNumber() {
		return doGenerateNumber(NUMBER_TYPE_PRODUCT_ORDER, SOURCE_PRODUCT_ORDER_NUMBER);
	}

	@Override
	public String generateDepositNumber() {
		return doGenerateNumber(NUMBER_TYPE_TEAM_DEPOSIT, SOURCE_DEPOSIT_NUMBER);
	}

	private String doGenerateNumber(String prefix, String source) {
		String result = "";
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String user_pk = sessionBean.getPk();
		String user_number = sessionBean.getUser_number();

		String start = prefix + user_number.substring(user_number.length() - 2, user_number.length());

		TeamNumberBean option = new TeamNumberBean();
		option.setUser_pk(user_pk);
		option.setType(prefix);
		TeamNumberBean tb = dao.selectNextNumber(option);

		String next = "";
		if (null == tb) {
			String first_number = source.substring(0, 4);
			result = start + first_number;
			next = addOne(first_number, source);

			tb = new TeamNumberBean();
			tb.setUser_pk(user_pk);
			tb.setType(prefix);
			tb.setTeam_number(next);
			dao.insert(tb);
		} else {
			String current = tb.getTeam_number();
			result = start + current;
			next = addOne(current, source);

			tb.setTeam_number(next);
			dao.update(tb);
		}
		return result;
	}

}
