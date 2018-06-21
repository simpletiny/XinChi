package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductDelayDAO;
import com.xinchi.bean.ProductDelayBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductDelayDAOImpl extends SqlSessionDaoSupport implements ProductDelayDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductDelayBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductDelayMapper.insert", bean);
	}

	@Override
	public void update(ProductDelayBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductDelayMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductDelayMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductDelayBean selectByPrimaryKey(String id) {
		return (ProductDelayBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductDelayMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<ProductDelayBean> selectByParam(ProductDelayBean bean) {
		List<ProductDelayBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductDelayMapper.selectByParam",
				bean);
		return list;
	}

	@Override
	public ProductDelayBean selectByProductPk(String product_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ProductDelayMapper.selectByProductPk", product_pk);
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ProductDelayMapper.deleteByProductPk", product_pk);

	}

}