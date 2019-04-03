package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.FlightDAO;
import com.xinchi.bean.FlightBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class FlightDAOImpl extends SqlSessionDaoSupport implements FlightDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(FlightBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.FlightMapper.insert", bean);
	}

	@Override
	public void update(FlightBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.FlightMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.FlightMapper.deleteByPrimaryKey", id);
	}

	@Override
	public FlightBean selectByPrimaryKey(String id) {
		return (FlightBean) daoUtil.selectByPK("com.xinchi.bean.mapper.FlightMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<FlightBean> selectByParam(FlightBean bean) {
		List<FlightBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.FlightMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<FlightBean> selectByPage(Page<FlightBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.FlightMapper.selectByPage", page);
	}

	@Override
	public FlightBean selectByProductPk(String product_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.FlightMapper.selectByProductPk", product_pk);
	}

}