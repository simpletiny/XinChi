package com.xinchi.backend.sale.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderSupplierBean;
import com.xinchi.common.DaoUtil;

@Repository
public class SaleOrderDAOImpl extends SqlSessionDaoSupport implements
		SaleOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(com.xinchi.bean.SaleOrderBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SaleOrderMapper.insert", bo);
	}

	@Override
	public void saveNameList(List<SaleOrderNameListBean> arrName) {
		for (SaleOrderNameListBean bean : arrName) {
			daoUtil.insertBO(
					"com.xinchi.bean.mapper.SaleOrderNameListMapper.insert",
					bean);

		}
	}

	@Override
	public void saveOrderSupplier(List<SaleOrderSupplierBean> arrSupplier) {
		for (SaleOrderSupplierBean bean : arrSupplier) {
			daoUtil.insertBO(
					"com.xinchi.bean.mapper.SaleOrderSupplierMapper.insert",
					bean);

		}
	}

	@Override
	public List<SaleOrderBean> selectAllByParam(SaleOrderBean bo) {
		return daoUtil.selectByBOParamT("com.xinchi.bean.mapper.SaleOrderMapper.selectByParam", bo);
	
	}

}