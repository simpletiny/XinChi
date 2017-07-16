package com.xinchi.backend.accounting.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ReimbursementDAOImpl extends SqlSessionDaoSupport implements ReimbursementDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(ReimbursementBean reimbursement) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.ReimbursementMapper.insert", reimbursement);
	}

	@Override
	public void update(ReimbursementBean reimbursement) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ReimbursementMapper.updateByPrimaryKey", reimbursement);
	}

	@Override
	public ReimbursementBean selectByPk(String reimbursement_pk) {
		return (ReimbursementBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ReimbursementMapper.selectByPrimaryKey", reimbursement_pk);
	}

	@Override
	public void deleteByPk(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ReimbursementMapper.deleteByPrimaryKey", pk);
	}
}
