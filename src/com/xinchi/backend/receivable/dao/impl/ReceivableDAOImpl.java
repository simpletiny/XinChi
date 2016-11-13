package com.xinchi.backend.receivable.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ReceivableDAOImpl extends SqlSessionDaoSupport implements
		ReceivableDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ReceivableBean receivable) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ReceivableMapper.insert",
				receivable);
	}
}
