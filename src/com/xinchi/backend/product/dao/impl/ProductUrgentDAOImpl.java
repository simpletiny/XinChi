package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductUrgentDAO;
import com.xinchi.bean.ProductUrgentBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductUrgentDAOImpl extends SqlSessionDaoSupport implements ProductUrgentDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductUrgentBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductUrgentMapper.insert", bean);
	}

	@Override
	public void update(ProductUrgentBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductUrgentMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductUrgentMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductUrgentBean selectByPrimaryKey(String id) {
		return (ProductUrgentBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductUrgentMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<ProductUrgentBean> selectByParam(ProductUrgentBean bean) {
		List<ProductUrgentBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductUrgentMapper.selectByParam",
				bean);
		return list;
	}

}