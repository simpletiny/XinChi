package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductProfitDAO;
import com.xinchi.bean.ProductProfitBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductProfitDAOImpl extends SqlSessionDaoSupport implements ProductProfitDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<ProductProfitBean> selectByParam(ProductProfitBean bean) {
		List<ProductProfitBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductProfitMapper.selectByParam",
				bean);
		return list;
	}

}