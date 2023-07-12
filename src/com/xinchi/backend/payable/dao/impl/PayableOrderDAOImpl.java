package com.xinchi.backend.payable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.payable.dao.PayableOrderDAO;
import com.xinchi.bean.PayableOrderBean;
import com.xinchi.common.DaoUtil;

@Repository
public class PayableOrderDAOImpl extends SqlSessionDaoSupport implements PayableOrderDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PayableOrderBean payable) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PayableOrderMapper.insert", payable);
	}

	@Override
	public List<PayableOrderBean> selectByParam(PayableOrderBean payable) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.PayableOrderMapper.selectByParam", payable);
	}

	@Override
	public void update(PayableOrderBean payable) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PayableOrderMapper.updateByPrimaryKey", payable);
	}

	@Override
	public void deleteByPk(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PayableOrderMapper.deleteByPrimaryKey", pk);

	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PayableOrderMapper.deleteTeamNumber", team_number);
	}

	@Override
	public List<PayableOrderBean> selectByTeamNumber(String team_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.PayableOrderMapper.selectByTeamNumber", team_number);
	}

}
