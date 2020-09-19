package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.AirNeedTeamNumberDAO;
import com.xinchi.bean.AirNeedTeamNumberBean;
import com.xinchi.common.DaoUtil;

@Repository
public class AirNeedTeamNumberDAOImpl extends SqlSessionDaoSupport implements AirNeedTeamNumberDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(AirNeedTeamNumberBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.AirNeedTeamNumberMapper.insert", bean);
	}

	@Override
	public void update(AirNeedTeamNumberBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirNeedTeamNumberMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirNeedTeamNumberMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AirNeedTeamNumberBean selectByPrimaryKey(String id) {
		return (AirNeedTeamNumberBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.AirNeedTeamNumberMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AirNeedTeamNumberBean> selectByParam(AirNeedTeamNumberBean bean) {
		List<AirNeedTeamNumberBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirNeedTeamNumberMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<AirNeedTeamNumberBean> selectByNeedPk(String need_pk) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirNeedTeamNumberMapper.selectByNeedPk", need_pk);
	}

	@Override
	public void deleteByNeedPk(String need_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirNeedTeamNumberMapper.deleteByNeedPk", need_pk);
	}

}