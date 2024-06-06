package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientInoutImgDAO;
import com.xinchi.bean.ClientInoutImgBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ClientInoutImgDAOImpl extends SqlSessionDaoSupport implements ClientInoutImgDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientInoutImgBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientInoutImgMapper.insert", bean);
	}

	@Override
	public ClientInoutImgBean selectByPk(String pk) {
		return (ClientInoutImgBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ClientInoutImgMapper.selectByPrimaryKey",
				pk);
	}

	@Override
	public void update(ClientInoutImgBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientInoutImgMapper.updateByPrimaryKey", bean);
	}

	@Override
	public List<ClientInoutImgBean> selectByParam(ClientInoutImgBean bean) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientInoutImgMapper.selectByPrimaryKey", bean);
	}

	@Override
	public List<ClientInoutImgBean> selectByClientPk(String client_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientInoutImgMapper.selectByClientPk", client_pk);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientInoutImgMapper.deleteByPrimaryKey", pk);
	}
}