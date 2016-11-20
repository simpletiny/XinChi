package com.xinchi.backend.receivable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ReceivedDAOImpl extends SqlSessionDaoSupport implements
		ReceivedDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientReceivedDetailBean detail) {
		daoUtil.insertBO(
				"com.xinchi.bean.mapper.ClientReceivedDetailMapper.insert",
				detail);
	}

	@Override
	public void insertWithPk(ClientReceivedDetailBean detail) {
		daoUtil.insertBOWithPk(
				"com.xinchi.bean.mapper.ClientReceivedDetailMapper.insert",
				detail);
	}

	@Override
	public List<ClientReceivedDetailBean> getAllByPage(
			Page<ClientReceivedDetailBean> page) {
		List<ClientReceivedDetailBean> list = daoUtil
				.selectByParam(
						"com.xinchi.bean.mapper.ClientReceivedDetailMapper.selectByPage",
						page);
		return list;
	}

}
