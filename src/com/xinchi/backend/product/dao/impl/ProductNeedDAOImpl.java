package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductNeedDAO;
import com.xinchi.bean.ProductNeedDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductNeedDAOImpl extends SqlSessionDaoSupport implements ProductNeedDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<ProductNeedDto> selectByPage(Page<ProductNeedDto> page) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductNeedMapper.selectByPage", page);
	}

}