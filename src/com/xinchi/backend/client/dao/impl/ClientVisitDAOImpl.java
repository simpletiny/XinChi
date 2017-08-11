package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ClientVisitDAOImpl extends SqlSessionDaoSupport implements ClientVisitDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientVisitBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientVisitMapper.insert", bo);
	}

	@Override
	public List<ClientVisitBean> selectByPage(Page page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientVisitMapper.selectByPage", page);
	}

	@Override
	public List<ClientVisitBean> selectByParam(ClientVisitBean visit) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientVisitMapper.selectByParam", visit);
	}

}