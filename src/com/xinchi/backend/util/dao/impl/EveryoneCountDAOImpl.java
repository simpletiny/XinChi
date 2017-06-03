package com.xinchi.backend.util.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.util.dao.EveryoneCountDAO;
import com.xinchi.bean.EveryoneCountBean;
import com.xinchi.common.DaoUtil;

@Repository
public class EveryoneCountDAOImpl extends SqlSessionDaoSupport implements EveryoneCountDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public EveryoneCountBean selectCountByType(String type) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.EveryoneCountMapper.selectByType", type);
	}

	@Override
	public void update(EveryoneCountBean bean) {
		daoUtil.updateByBOParam("com.xinchi.bean.mapper.EveryoneCountMapper.updateByPrimaryKey", bean);
		
	}

}