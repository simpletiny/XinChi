package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.IncomingCallDAO;
import com.xinchi.bean.IncomingCallBean;
import com.xinchi.common.DaoUtil;

@Repository
public class IncomingCallDAOImpl extends SqlSessionDaoSupport implements IncomingCallDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(IncomingCallBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.IncomingCallMapper.insert", bean);
	}

	@Override
	public void update(IncomingCallBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.IncomingCallMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.IncomingCallMapper.deleteByPrimaryKey", id);
	}

	@Override
	public IncomingCallBean selectByPrimaryKey(String id) {
		return (IncomingCallBean) daoUtil.selectByPK("com.xinchi.bean.mapper.IncomingCallMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<IncomingCallBean> selectByParam(IncomingCallBean bean) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.IncomingCallMapper.selectByParam", bean);
	}

	@Override
	public String selectMaxCallDateByEmployeePk(String employee_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.IncomingCallMapper.selectMaxCallDateByEmployeePk",
				employee_pk);
	}

}