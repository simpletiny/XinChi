package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductSupplierInfoDAO;
import com.xinchi.bean.ProductSupplierInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductSupplierInfoDAOImpl extends SqlSessionDaoSupport implements ProductSupplierInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(ProductSupplierInfoBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.ProductSupplierInfoMapper.insert", bean);
	}

	@Override
	public void update(ProductSupplierInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductSupplierInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductSupplierInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductSupplierInfoBean selectByPrimaryKey(String id) {
		return (ProductSupplierInfoBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductSupplierInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductSupplierInfoBean> selectByParam(ProductSupplierInfoBean bean) {
		List<ProductSupplierInfoBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductSupplierInfoMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductSupplierInfoBean> selectByProductSupplierPk(String product_supplier_pk) {
		List<ProductSupplierInfoBean> list = daoUtil.selectByParam(
				"com.xinchi.bean.mapper.ProductSupplierInfoMapper.selectByProductSupplierPk", product_supplier_pk);
		return list;
	}

}