package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.FlightInfoDAO;
import com.xinchi.bean.FlightInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class FlightInfoDAOImpl extends SqlSessionDaoSupport implements FlightInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(FlightInfoBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.FlightInfoMapper.insert", bean);
	}

	@Override
	public void update(FlightInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.FlightInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.FlightInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public FlightInfoBean selectByPrimaryKey(String id) {
		return (FlightInfoBean) daoUtil.selectByPK("com.xinchi.bean.mapper.FlightInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<FlightInfoBean> selectByParam(FlightInfoBean bean) {
		List<FlightInfoBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.FlightInfoMapper.selectByParam",
				bean);
		return list;
	}

}