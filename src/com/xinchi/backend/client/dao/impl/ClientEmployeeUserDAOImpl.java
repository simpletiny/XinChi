package com.xinchi.backend.client.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ClientEmployeeUserDAOImpl extends SqlSessionDaoSupport implements ClientEmployeeUserDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientEmployeeUserBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientEmployeeUserMapper.insert", bo);
	}

	@Override
	public void update(ClientEmployeeUserBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientEmployeeUserMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientEmployeeUserMapper.deleteByPrimaryKey", id);
	}

	@Override
	public void deleteByEmployeePk(String employee_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientEmployeeUserMapper.deleteByEmployeePk", employee_pk);
	}

}