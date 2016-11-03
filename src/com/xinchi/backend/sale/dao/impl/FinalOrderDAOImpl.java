package com.xinchi.backend.sale.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sale.dao.FinalOrderDAO;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;
import com.xinchi.common.DaoUtil;

@Repository
public class FinalOrderDAOImpl extends SqlSessionDaoSupport implements
		FinalOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(FinalOrderBean order) {
		daoUtil.insertBO("com.xinchi.bean.mapper.FinalOrderMapper.insert",
				order);

	}

	@Override
	public void saveOrderSupplier(List<FinalOrderSupplierBean> arrSupplier) {
		for (FinalOrderSupplierBean bean : arrSupplier) {
			daoUtil.insertBO(
					"com.xinchi.bean.mapper.FinalOrderSupplierMapper.insert",
					bean);

		}
	}

	@Override
	public List<FinalOrderBean> selectAllByParam(FinalOrderBean order) {
		return daoUtil
				.selectByBOParamT(
						"com.xinchi.bean.mapper.FinalOrderMapper.selectByParam",
						order);
	}

}
