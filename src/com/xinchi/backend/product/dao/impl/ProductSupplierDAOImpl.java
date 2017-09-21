package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductSupplierDAO;
import com.xinchi.bean.ProductSupplierBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductSupplierDAOImpl extends SqlSessionDaoSupport implements ProductSupplierDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductSupplierBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductSupplierMapper.insert", bean);
	}

	@Override
	public void update(ProductSupplierBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductSupplierMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductSupplierMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductSupplierBean selectByPrimaryKey(String id) {
		return (ProductSupplierBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductSupplierMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductSupplierBean> selectByParam(ProductSupplierBean bean) {
		List<ProductSupplierBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductSupplierMapper.selectByParam", bean);
		return list;
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ProductSupplierMapper.deleteByProductPk", product_pk);
	}

	@Override
	public List<ProductSupplierBean> selectByProductPk(String product_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductSupplierMapper.selectByProductPk", product_pk);

	}

}