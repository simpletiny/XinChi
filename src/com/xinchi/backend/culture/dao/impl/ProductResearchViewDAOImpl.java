package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.ProductResearchViewDAO;
import com.xinchi.bean.ProductResearchViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductResearchViewDAOImpl extends SqlSessionDaoSupport implements ProductResearchViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductResearchViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductResearchViewMapper.insert", view);
	}

	@Override
	public List<ProductResearchViewBean> getAllViewsByPage(Page<ProductResearchViewBean> page) {
		List<ProductResearchViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductResearchViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public ProductResearchViewBean selectByPk(String view_pk) {
		return (ProductResearchViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductResearchViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(ProductResearchViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductResearchViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductResearchViewMapper.deleteByPrimaryKey", view_pk);
	}
}
