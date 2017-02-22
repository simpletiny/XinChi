package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.WorldViewDAO;
import com.xinchi.bean.WorldViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class WorldViewDAOImpl extends SqlSessionDaoSupport implements WorldViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(WorldViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.WorldViewMapper.insert", view);
	}

	@Override
	public List<WorldViewBean> getAllViewsByPage(Page page) {
		List<WorldViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.WorldViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public WorldViewBean selectByPk(String view_pk) {
		return (WorldViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.WorldViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(WorldViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.WorldViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.WorldViewMapper.deleteByPrimaryKey", view_pk);
	}
}
