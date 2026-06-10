package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductAccountingFirmDAO;
import com.xinchi.bean.ProductAccountingFirmBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductAccountingFirmDAOImpl extends SqlSessionDaoSupport implements ProductAccountingFirmDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductAccountingFirmBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductAccountingFirmMapper.insert", bean);
	}

	@Override
	public void update(ProductAccountingFirmBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductAccountingFirmMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductAccountingFirmMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public ProductAccountingFirmBean selectByPrimaryKey(String pk) {
		return (ProductAccountingFirmBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductAccountingFirmMapper.selectByPrimaryKey", pk);
	}

	@Override
	public List<ProductAccountingFirmBean> selectByParam(ProductAccountingFirmBean bean) {
		List<ProductAccountingFirmBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductAccountingFirmMapper.selectByParam", bean);
		return list;
	}

}