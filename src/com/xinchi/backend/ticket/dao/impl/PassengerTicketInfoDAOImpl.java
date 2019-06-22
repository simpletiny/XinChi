package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.PassengerTicketInfoDAO;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class PassengerTicketInfoDAOImpl extends SqlSessionDaoSupport implements PassengerTicketInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PassengerTicketInfoBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PassengerTicketInfoMapper.insert", bean);
	}

	@Override
	public void update(PassengerTicketInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PassengerTicketInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PassengerTicketInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public PassengerTicketInfoBean selectByPrimaryKey(String id) {
		return (PassengerTicketInfoBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.PassengerTicketInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<PassengerTicketInfoBean> selectByParam(PassengerTicketInfoBean bean) {
		List<PassengerTicketInfoBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.PassengerTicketInfoMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<PassengerTicketInfoBean> selectByPassengerPk(String passenger_pk) {
		List<PassengerTicketInfoBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.PassengerTicketInfoMapper.selectByPassengerPk", passenger_pk);
		return list;
	}
}