package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientEmployeeQuitConnectLogDAO;
import com.xinchi.bean.ClientEmployeeQuitConnectLogBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ClientEmployeeQuitConnectLogDAOImpl extends SqlSessionDaoSupport
		implements ClientEmployeeQuitConnectLogDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientEmployeeQuitConnectLogBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientEmployeeQuitConnectLogMapper.insert", bean);
	}

	@Override
	public void update(ClientEmployeeQuitConnectLogBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientEmployeeQuitConnectLogMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientEmployeeQuitConnectLogMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ClientEmployeeQuitConnectLogBean selectByPrimaryKey(String id) {
		return (ClientEmployeeQuitConnectLogBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ClientEmployeeQuitConnectLogMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ClientEmployeeQuitConnectLogBean> selectByParam(ClientEmployeeQuitConnectLogBean bean) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientEmployeeQuitConnectLogMapper.selectByParam", bean);
	}

}