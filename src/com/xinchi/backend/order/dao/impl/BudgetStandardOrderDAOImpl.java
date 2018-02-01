package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.common.DaoUtil;

@Repository
public class BudgetStandardOrderDAOImpl extends SqlSessionDaoSupport implements BudgetStandardOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(BudgetStandardOrderBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.BudgetStandardOrderMapper.insert", bean);
	}

	@Override
	public void update(BudgetStandardOrderBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.BudgetStandardOrderMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.BudgetStandardOrderMapper.deleteByPrimaryKey", id);
	}

	@Override
	public BudgetStandardOrderBean selectByPrimaryKey(String id) {
		return (BudgetStandardOrderBean) daoUtil.selectByPK("com.xinchi.bean.mapper.BudgetStandardOrderMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<BudgetStandardOrderBean> selectByParam(BudgetStandardOrderBean bean) {
		List<BudgetStandardOrderBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.BudgetStandardOrderMapper.selectByParam", bean);
		return list;
	}

	@Override
	public BudgetStandardOrderBean selectByTeamNumber(String team_number) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.BudgetStandardOrderMapper.selectByTeamNumber", team_number);
	}

	@Override
	public void insertWithPk(BudgetStandardOrderBean bean) {
		daoUtil.insertBOWithPk("com.xinchi.bean.mapper.BudgetStandardOrderMapper.insert", bean);
	}

}