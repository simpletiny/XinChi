package com.xinchi.backend.supplier.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.supplier.dao.DepositTicketPaidDAO;
import com.xinchi.bean.DepositTicketPaidBean;
import com.xinchi.common.DaoUtil;

@Repository
public class DepositTicketPaidDAOImpl extends SqlSessionDaoSupport implements DepositTicketPaidDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(DepositTicketPaidBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.DepositTicketPaidMapper.insert", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.DepositTicketPaidMapper.deleteByPrimaryKey", id);
	}

	@Override
	public DepositTicketPaidBean selectByPrimaryKey(String id) {
		return (DepositTicketPaidBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.DepositTicketPaidMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<DepositTicketPaidBean> selectByRelatedPk(String related_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.DepositTicketPaidMapper.selectByRelatedPk", related_pk);
	}

	@Override
	public void deleteByRelatedPk(String related_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.DepositTicketPaidMapper.deleteByRelatedPk", related_pk);
	}

	@Override
	public List<DepositTicketPaidBean> selectByDepositPk(String deposit_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.DepositTicketPaidMapper.selectByDepositPk", deposit_pk);
	}

}