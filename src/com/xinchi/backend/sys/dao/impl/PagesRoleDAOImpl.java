package com.xinchi.backend.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sys.dao.PagesRoleDAO;
import com.xinchi.bean.PagesRoleBean;
import com.xinchi.common.DaoUtil;

@Repository
public class PagesRoleDAOImpl extends SqlSessionDaoSupport implements PagesRoleDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PagesRoleBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PagesRoleMapper.insert", bean);
	}

	@Override
	public void update(PagesRoleBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PagesRoleMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PagesRoleMapper.deleteByPrimaryKey", id);
	}

	@Override
	public PagesRoleBean selectByPrimaryKey(String id) {
		return (PagesRoleBean) daoUtil.selectByPK("com.xinchi.bean.mapper.PagesRoleMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<PagesRoleBean> selectByParam(PagesRoleBean bean) {
		List<PagesRoleBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.PagesRoleMapper.selectByParam", bean);
		return list;
	}

}