package com.xinchi.backend.finance.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.bean.CardBean;
import com.xinchi.common.DaoUtil;

@Repository
public class CardDAOImpl extends SqlSessionDaoSupport implements CardDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(CardBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.CardMapper.insert", bo);
	}
}
