package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderAirInfoDAO;
import com.xinchi.bean.ProductOrderAirInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderAirInfoDAOImpl extends SqlSessionDaoSupport implements ProductOrderAirInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductOrderAirInfoBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderAirInfoMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderAirInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderAirInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderAirInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderAirInfoBean selectByPrimaryKey(String id) {
		return (ProductOrderAirInfoBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductOrderAirInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductOrderAirInfoBean> selectByParam(ProductOrderAirInfoBean bean) {
		List<ProductOrderAirInfoBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderAirInfoMapper.selectByParam", bean);
		return list;
	}

	@Override
	public void deleteByBasePk(String base_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderAirInfoMapper.deleteByBasePk", base_pk);
	}

}