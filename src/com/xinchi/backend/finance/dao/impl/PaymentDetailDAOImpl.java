package com.xinchi.backend.finance.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.DaoUtil;

@Repository
public class PaymentDetailDAOImpl extends SqlSessionDaoSupport implements
		PaymentDetailDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PaymentDetailBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PaymentDetailMapper.insert",
				bo);
	}

	@Override
	public List<PaymentDetailBean> selectAllDetailsByParam(
			PaymentDetailBean bean) {
		return daoUtil.selectByBOParamT(
				"com.xinchi.bean.mapper.PaymentDetailMapper.selectByParam",
				bean);
	}
}
