package com.xinchi.backend.accounting.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class PayApprovalDAOImpl extends SqlSessionDaoSupport implements PayApprovalDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PayApprovalBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PayApprovalMapper.insert", bean);
	}

	@Override
	public void update(PayApprovalBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PayApprovalMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PayApprovalMapper.deleteByPrimaryKey", id);
	}

	@Override
	public PayApprovalBean selectByPrimaryKey(String id) {
		return (PayApprovalBean) daoUtil.selectByPK("com.xinchi.bean.mapper.PayApprovalMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<PayApprovalBean> selectByParam(PayApprovalBean bean) {
		List<PayApprovalBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.PayApprovalMapper.selectByParam",
				bean);
		return list;
	}

	@Override
	public List<PayApprovalBean> selectByPage(Page<PayApprovalBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.PayApprovalMapper.selectByPage", page);
	}

	@Override
	public PayApprovalBean selectByBackPk(String back_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.PayApprovalMapper.selectByBackPk", back_pk);
	}

	@Override
	public BigDecimal selectSumBalance() {
		return daoUtil.selectOneValue("com.xinchi.bean.mapper.PayApprovalMapper.selectSumBalance");
	}

}