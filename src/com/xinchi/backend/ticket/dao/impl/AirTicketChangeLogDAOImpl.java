package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.AirTicketChangeLogDAO;
import com.xinchi.bean.AirTicketChangeLogBean;
import com.xinchi.common.DaoUtil;

@Repository
public class AirTicketChangeLogDAOImpl extends SqlSessionDaoSupport implements AirTicketChangeLogDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(AirTicketChangeLogBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.AirTicketChangeLogMapper.insert", bean);
	}

	@Override
	public void update(AirTicketChangeLogBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirTicketChangeLogMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirTicketChangeLogMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AirTicketChangeLogBean selectByPrimaryKey(String id) {
		return (AirTicketChangeLogBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.AirTicketChangeLogMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AirTicketChangeLogBean> selectByParam(AirTicketChangeLogBean bean) {
		List<AirTicketChangeLogBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirTicketChangeLogMapper.selectByParam", bean);
		return list;
	}

}