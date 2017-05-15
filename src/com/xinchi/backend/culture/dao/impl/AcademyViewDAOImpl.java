package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.AcademyViewDAO;
import com.xinchi.bean.AcademyViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AcademyViewDAOImpl extends SqlSessionDaoSupport implements AcademyViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AcademyViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AcademyViewMapper.insert", view);
	}

	@Override
	public List<AcademyViewBean> getAllViewsByPage(Page page) {
		List<AcademyViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.AcademyViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public AcademyViewBean selectByPk(String view_pk) {
		return (AcademyViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.AcademyViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(AcademyViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AcademyViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AcademyViewMapper.deleteByPrimaryKey", view_pk);
	}
}
