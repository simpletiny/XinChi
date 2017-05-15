package com.xinchi.backend.payable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Repository
public class PayableDAOImpl extends SqlSessionDaoSupport implements PayableDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PayableBean payable) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PayableMapper.insert", payable);
	}

	@Override
	public List<PayableBean> selectByParam(PayableBean payable) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.PayableMapper.selectByParam", payable);
	}

	@Override
	public void update(PayableBean payable) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PayableMapper.updateByPrimaryKey", payable);
	}

	@Override
	public PayableSummaryBean selectPayableSummary(String sales) {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		String user_number = sessionBean.getUser_number();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.PayableSummaryMapper.selectByUserNumber", user_number);
		} else {
			if (!SimpletinyString.isEmpty(sales)) {
				return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.PayableSummaryMapper.selectByUserNumber", sales);
			} else {
				return daoUtil.selectOneValue("com.xinchi.bean.mapper.PayableSummaryMapper.selectPayableSummary");
			}

		}
	}

	@Override
	public List<PayableBean> selectAllPayableWithSupplier() {
		return daoUtil.selectAll("com.xinchi.bean.mapper.PayableMapper.selectAllWithSupplier");
	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.PayableMapper.deleteByTeamNumber", team_number);

	}

}
