package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.OrderAirInfoBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AirTicketNeedDAOImpl extends SqlSessionDaoSupport implements AirTicketNeedDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<AirTicketNeedBean> selectByPage(Page<AirTicketNeedBean> page) {
		List<AirTicketNeedBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketNeedMapper.selectByPage",
				page);
		return list;
	}

	@Override
	public List<AirTicketNeedBean> selectOrderByPage(Page page) {
		List<AirTicketNeedBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirTicketNeedMapper.selectOrderByPage", page);
		return list;
	}

	@Override
	public List<OrderAirInfoBean> selectOrderAirInfoByTeamNumber(String team_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketNeedMapper.selectOrderAirInfoByTeamNumber",
				team_number);
	}

}
