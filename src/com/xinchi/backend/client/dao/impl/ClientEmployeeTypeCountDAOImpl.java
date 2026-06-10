package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientEmployeeTypeCountDAO;
import com.xinchi.bean.ClientEmployeeTypeCountBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ClientEmployeeTypeCountDAOImpl extends SqlSessionDaoSupport implements ClientEmployeeTypeCountDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientEmployeeTypeCountBean bo) {
		daoUtil.insertWithoutLogin("com.xinchi.bean.mapper.ClientEmployeeTypeCountMapper.insert", bo);
	}

	@Override
	public void update(ClientEmployeeTypeCountBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientEmployeeTypeCountMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientEmployeeTypeCountMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ClientEmployeeTypeCountBean selectByPrimaryKey(String id) {
		return (ClientEmployeeTypeCountBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ClientEmployeeTypeCountMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ClientEmployeeTypeCountBean> selectByParam(ClientEmployeeTypeCountBean bo) {
		List<ClientEmployeeTypeCountBean> list = daoUtil
				.selectByBOParamT("com.xinchi.bean.mapper.ClientEmployeeTypeCountMapper.selectByParam", bo);
		return list;
	}

	@Override
	public ClientEmployeeTypeCountBean selectSumByParam(ClientEmployeeTypeCountBean bean) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientEmployeeTypeCountMapper.selectSumByParam",
				bean);

	}

}