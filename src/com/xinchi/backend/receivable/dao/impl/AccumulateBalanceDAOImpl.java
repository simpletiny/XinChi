package com.xinchi.backend.receivable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.receivable.dao.AccumulateBalanceDAO;
import com.xinchi.bean.AccumulateBalanceBean;
import com.xinchi.common.DaoUtil;

@Repository
public class AccumulateBalanceDAOImpl extends SqlSessionDaoSupport implements AccumulateBalanceDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AccumulateBalanceBean bean) {
		daoUtil.insertObject("com.xinchi.bean.mapper.AccumulateBalanceBeanMapper.insert", bean);
	}

	@Override
	public List<AccumulateBalanceBean> selectNeedInsertAccumulateBalance() {

		return daoUtil.selectAllOut("com.xinchi.bean.mapper.AccumulateBalanceBeanMapper.selectNeedInsert");
	}

}
