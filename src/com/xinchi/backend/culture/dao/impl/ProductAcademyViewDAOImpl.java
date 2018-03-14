package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.ProductAcademyViewDAO;
import com.xinchi.bean.ProductAcademyViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductAcademyViewDAOImpl extends SqlSessionDaoSupport implements ProductAcademyViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductAcademyViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductAcademyViewMapper.insert", view);
	}

	@Override
	public List<ProductAcademyViewBean> getAllViewsByPage(Page<ProductAcademyViewBean> page) {
		List<ProductAcademyViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductAcademyViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public ProductAcademyViewBean selectByPk(String view_pk) {
		return (ProductAcademyViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductAcademyViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(ProductAcademyViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductAcademyViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductAcademyViewMapper.deleteByPrimaryKey", view_pk);
	}
}
