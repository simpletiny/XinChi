package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.TeamEvolutionDAO;
import com.xinchi.bean.TeamEvolutionBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class TeamEvolutionDAOImpl extends SqlSessionDaoSupport implements TeamEvolutionDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(TeamEvolutionBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TeamEvolutionMapper.insert", view);
	}

	@Override
	public List<TeamEvolutionBean> getAllViewsByPage(Page<TeamEvolutionBean> page) {
		List<TeamEvolutionBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.TeamEvolutionMapper.selectByPage",
				page);
		return list;
	}

	@Override
	public TeamEvolutionBean selectByPk(String view_pk) {
		return (TeamEvolutionBean) daoUtil.selectByPK("com.xinchi.bean.mapper.TeamEvolutionMapper.selectByPrimaryKey",
				view_pk);
	}

	@Override
	public void update(TeamEvolutionBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.TeamEvolutionMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.TeamEvolutionMapper.deleteByPrimaryKey", view_pk);
	}
}
