package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.SaleAcademyViewDAO;
import com.xinchi.bean.SaleAcademyViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SaleAcademyViewDAOImpl extends SqlSessionDaoSupport implements SaleAcademyViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SaleAcademyViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SaleAcademyViewMapper.insert", view);
	}

	@Override
	public List<SaleAcademyViewBean> getAllViewsByPage(Page<SaleAcademyViewBean> page) {
		List<SaleAcademyViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.SaleAcademyViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public SaleAcademyViewBean selectByPk(String view_pk) {
		return (SaleAcademyViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.SaleAcademyViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(SaleAcademyViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SaleAcademyViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SaleAcademyViewMapper.deleteByPrimaryKey", view_pk);
	}
}
