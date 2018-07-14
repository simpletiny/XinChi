package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientUserDAO;
import com.xinchi.bean.ClientUserBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ClientUserDAOImpl extends SqlSessionDaoSupport implements ClientUserDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientUserBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientUserMapper.insert", bo);
	}

	@Override
	public void update(ClientUserBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientUserMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientUserMapper.deleteByPrimaryKey", id);
	}

	@Override
	public void deleteByClientPk(String client_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientUserMapper.deleteByClientPk", client_pk);
	}

	@Override
	public List<ClientUserBean> selectByClientPk(String client_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientUserMapper.selectByClientPk", client_pk);
	}

}