package com.xinchi.backend.supplier.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.supplier.dao.DepositPaymentDAO;
import com.xinchi.bean.DepositPaymentBean;
import com.xinchi.common.DaoUtil;

@Repository
public class DepositPaymentDAOImpl extends SqlSessionDaoSupport implements DepositPaymentDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(DepositPaymentBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.DepositPaymentMapper.insert", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.DepositPaymentMapper.deleteByPrimaryKey", id);
	}

	@Override
	public DepositPaymentBean selectByPrimaryKey(String id) {
		return (DepositPaymentBean) daoUtil.selectByPK("com.xinchi.bean.mapper.DepositPaymentMapper.selectByPrimaryKey",
				id);
	}

}