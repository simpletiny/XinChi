package com.xinchi.backend.receivable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

@Repository
public class ReceivableDAOImpl extends SqlSessionDaoSupport implements ReceivableDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ReceivableBean receivable) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ReceivableMapper.insert", receivable);
	}

	@Override
	public ReceivableSummaryBean selectReceivableSummary(String sales) {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		String user_number = sessionBean.getUser_number();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ReceivableSummaryMapper.selectByUserNumber", user_number);
		} else {
			if (!SimpletinyString.isEmpty(sales)) {
				return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ReceivableSummaryMapper.selectByUserNumber", sales);
			} else {
				return daoUtil.selectOneValue("com.xinchi.bean.mapper.ReceivableSummaryMapper.selectReceivableSummary");
			}

		}
	}

	@Override
	public ReceivableBean selectReceivableByTeamNumber(String team_number) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ReceivableMapper.selectReceivableByTeamNumber", team_number);
	}

	@Override
	public void update(ReceivableBean receivable) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ReceivableMapper.updateByPrimaryKey", receivable);

	}

	@Override
	public List<ReceivableBean> selectAllReceivablesWithFinancial() {
		return daoUtil.selectAll("com.xinchi.bean.mapper.ReceivableMapper.selectAllReceivablesWithFinancial");
	}

	@Override
	public List<ReceivableBean> selectByPage(Page<ReceivableBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ReceivableMapper.selectByPage", page);
	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ReceivableMapper.deleteByTeamNumber", team_number);
	}
}
