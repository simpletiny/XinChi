package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.common.DaoUtil;

@Repository
public class BudgetNonStandardOrderDAOImpl extends SqlSessionDaoSupport implements BudgetNonStandardOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(BudgetNonStandardOrderBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.BudgetNonStandardOrderMapper.insert", bean);
	}

	@Override
	public void update(BudgetNonStandardOrderBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.BudgetNonStandardOrderMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.BudgetNonStandardOrderMapper.deleteByPrimaryKey", id);
	}

	@Override
	public BudgetNonStandardOrderBean selectByPrimaryKey(String id) {
		return (BudgetNonStandardOrderBean) daoUtil.selectByPK("com.xinchi.bean.mapper.BudgetNonStandardOrderMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<BudgetNonStandardOrderBean> selectByParam(BudgetNonStandardOrderBean bean) {
		List<BudgetNonStandardOrderBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.BudgetNonStandardOrderMapper.selectByParam", bean);
		return list;
	}

}