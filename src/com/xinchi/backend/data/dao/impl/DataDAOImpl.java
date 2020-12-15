package com.xinchi.backend.data.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.data.dao.DataDAO;
import com.xinchi.bean.ProductAreaBean;
import com.xinchi.bean.ProductProductBean;
import com.xinchi.bean.ProductSaleBean;
import com.xinchi.common.DaoUtil;

@Repository
public class DataDAOImpl extends SqlSessionDaoSupport implements DataDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<ProductAreaBean> selectProductAreaData(ProductAreaBean productOption) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewProductAreaMapper.selectAreaData", productOption);
	}

	@Override
	public List<ProductProductBean> selectProductProductData(ProductAreaBean productOption) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewProductProductMapper.selectProductData",
				productOption);
	}

	@Override
	public List<ProductSaleBean> selectProductSaleData(ProductAreaBean productOption) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewProductSaleMapper.selectSaleData", productOption);
	}

}
