package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.AirTicketOrderLegDAO;
import com.xinchi.bean.AirTicketOrderLegBean;
import com.xinchi.common.DaoUtil;

@Repository
public class AirTicketOrderLegDAOImpl extends SqlSessionDaoSupport implements AirTicketOrderLegDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(AirTicketOrderLegBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.AirTicketOrderLegMapper.insert", bean);
	}

	@Override
	public void update(AirTicketOrderLegBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirTicketOrderLegMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirTicketOrderLegMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AirTicketOrderLegBean selectByPrimaryKey(String id) {
		return (AirTicketOrderLegBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.AirTicketOrderLegMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AirTicketOrderLegBean> selectByParam(AirTicketOrderLegBean bean) {
		List<AirTicketOrderLegBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirTicketOrderLegMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<AirTicketOrderLegBean> selectByOrderPk(String order_pk) {
		List<AirTicketOrderLegBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirTicketOrderLegMapper.selectByOrderPk", order_pk);
		return list;
	}

	@Override
	public List<AirTicketOrderLegBean> selectAirLegByOrderPks(List<String> order_pks) {
		List<AirTicketOrderLegBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirTicketOrderLegMapper.selectAirLegByOrderPks", order_pks);
		return list;
	}

}