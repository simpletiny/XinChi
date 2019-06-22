package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductLocalDAO;
import com.xinchi.bean.ProductLocalBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductLocalDAOImpl extends SqlSessionDaoSupport implements ProductLocalDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(ProductLocalBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.ProductLocalMapper.insert", bean);
	}

	@Override
	public void update(ProductLocalBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductLocalMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductLocalMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductLocalBean selectByPrimaryKey(String id) {
		return (ProductLocalBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductLocalMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<ProductLocalBean> selectByParam(ProductLocalBean bean) {
		List<ProductLocalBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductLocalMapper.selectByParam",
				bean);
		return list;
	}

	@Override
	public List<ProductLocalBean> selectByProductPk(String product_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductLocalMapper.selectByProductPk", product_pk);
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductLocalMapper.deleteByProductPk", product_pk);
	}

}