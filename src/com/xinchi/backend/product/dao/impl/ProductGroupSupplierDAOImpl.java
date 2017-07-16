package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductGroupSupplierDAO;
import com.xinchi.bean.ProductGroupSupplierBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductGroupSupplierDAOImpl extends SqlSessionDaoSupport implements ProductGroupSupplierDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductGroupSupplierBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductGroupSupplierMapper.insert", bean);
	}

	@Override
	public void update(ProductGroupSupplierBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductGroupSupplierMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductGroupSupplierMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductGroupSupplierBean selectByPrimaryKey(String id) {
		return (ProductGroupSupplierBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductGroupSupplierMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductGroupSupplierBean> selectByParam(ProductGroupSupplierBean bean) {
		List<ProductGroupSupplierBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductGroupSupplierMapper.selectByParam", bean);
		return list;
	}

	@Override
	public void deleteByGroupPk(String group_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ProductGroupSupplierMapper.deleteByGroupPk", group_pk);

	}

}