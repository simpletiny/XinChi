package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.ProductViewDAO;
import com.xinchi.bean.ProductViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductViewDAOImpl extends SqlSessionDaoSupport implements ProductViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductViewMapper.insert", view);
	}

	@Override
	public List<ProductViewBean> getAllViewsByPage(Page page) {
		List<ProductViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public ProductViewBean selectByPk(String view_pk) {
		return (ProductViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(ProductViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductViewMapper.deleteByPrimaryKey", view_pk);
	}
}
