package com.xinchi.backend.accounting.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.accounting.dao.AccountingDAO;
import com.xinchi.bean.ReceivedDetailDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AccountingDAOImpl extends SqlSessionDaoSupport implements AccountingDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<ReceivedDetailDto> selectByPage(Page<ReceivedDetailDto> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewAllNeedMatchReceivedMapper.selectByPage", page);
	}
}
