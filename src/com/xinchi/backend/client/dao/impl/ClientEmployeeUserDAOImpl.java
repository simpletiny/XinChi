package com.xinchi.backend.client.dao.impl;

import java.util.List;

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

	@Override
	public List<ClientEmployeeUserBean> selectByEmployeePk(String employee_pk) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientEmployeeUserMapper.selectByEmployeePk", employee_pk);
	}

	@Override
	public void insertWithCreateTime(ClientEmployeeUserBean bo) {
		daoUtil.insertBOWithCreateTime("com.xinchi.bean.mapper.ClientEmployeeUserMapper.insert", bo);
	}

	@Override
	public List<ClientEmployeeUserBean> selectByParam(ClientEmployeeUserBean option) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientEmployeeUserMapper.selectByParam", option);
	}

	@Override
	public void insertWithoutLogin(ClientEmployeeUserBean ceub) {
		daoUtil.insertWithoutLogin("com.xinchi.bean.mapper.ClientEmployeeUserMapper.insert", ceub);
	}

}