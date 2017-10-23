package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientChangeSaleLogDAO;
import com.xinchi.bean.ClientChangeSaleLogBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ClientChangeSaleLogDAOImpl extends SqlSessionDaoSupport implements ClientChangeSaleLogDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientChangeSaleLogBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientChangeSaleLogMapper.insert", bean);
	}

	@Override
	public void update(ClientChangeSaleLogBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientChangeSaleLogMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientChangeSaleLogMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ClientChangeSaleLogBean selectByPrimaryKey(String id) {
		return (ClientChangeSaleLogBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ClientChangeSaleLogMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ClientChangeSaleLogBean> selectByParam(ClientChangeSaleLogBean bean) {
		List<ClientChangeSaleLogBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ClientChangeSaleLogMapper.selectByParam", bean);
		return list;
	}

}