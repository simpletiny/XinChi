package com.xinchi.backend.util.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.util.UserUtilService;
import com.xinchi.common.Utils;

@Service
public class UserUtilServiceImpl implements UserUtilService {
	@Autowired
	private UserDAO dao;

	@Override
	public String getNextUserNumber() {
		String maxNow = dao.getMaxUserNumber();
		if (maxNow == null)
			return "N00001";
		int max = Integer.valueOf(maxNow.substring(1));
		int next = max + 1;
		String nextNumber = "N" + Utils.fullFill(next, "0", maxNow.length() - 1);
		return nextNumber;
	}

}
