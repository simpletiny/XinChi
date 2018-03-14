package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.TopAcademyViewDAO;
import com.xinchi.bean.TopAcademyViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class TopAcademyViewDAOImpl extends SqlSessionDaoSupport implements TopAcademyViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(TopAcademyViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TopAcademyViewMapper.insert", view);
	}

	@Override
	public List<TopAcademyViewBean> getAllViewsByPage(Page<TopAcademyViewBean> page) {
		List<TopAcademyViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.TopAcademyViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public TopAcademyViewBean selectByPk(String view_pk) {
		return (TopAcademyViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.TopAcademyViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(TopAcademyViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.TopAcademyViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.TopAcademyViewMapper.deleteByPrimaryKey", view_pk);
	}
}
