package com.xinchi.backend.util.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.SqlBean;
import com.xinchi.common.DaoUtil;

@Repository
public class CommonDAOImpl extends SqlSessionDaoSupport implements CommonDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void exeBySql(SqlBean sql) {
		daoUtil.executeBySql(sql);
	}

}