package com.xinchi.backend.util.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.util.dao.TeamNumberDAO;
import com.xinchi.bean.TeamNumberBean;
import com.xinchi.common.DaoUtil;

@Repository
@Scope("prototype")
public class TeamNumberDAOImpl extends SqlSessionDaoSupport implements
		TeamNumberDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(TeamNumberBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TeamNumberMapper.insert", bo);
	}

	@Override
	public void update(TeamNumberBean bo) {
		daoUtil.updateByPK(
				"com.xinchi.bean.mapper.TeamNumberMapper.updateByPrimaryKey",
				bo);
	}

	@Override
	public TeamNumberBean selectTeamNumberBySalePk(String salePk) {
		return daoUtil
				.selectOneValueByParam(
						"com.xinchi.bean.mapper.TeamNumberMapper.selectTeamNumberBySalePk",
						salePk);
	}

}