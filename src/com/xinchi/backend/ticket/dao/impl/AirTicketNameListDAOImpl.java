package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AirTicketNameListDAOImpl extends SqlSessionDaoSupport implements AirTicketNameListDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AirTicketNameListBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AirTicketNameListMapper.insert", bean);
	}

	@Override
	public void update(AirTicketNameListBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirTicketNameListMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirTicketNameListMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AirTicketNameListBean selectByPrimaryKey(String id) {
		return (AirTicketNameListBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.AirTicketNameListMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AirTicketNameListBean> selectByParam(AirTicketNameListBean bean) {
		List<AirTicketNameListBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirTicketNameListMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<AirTicketNameListBean> selectByPage(Page<AirTicketNameListBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketNameListMapper.selectByPage", page);
	}

	@Override
	public List<AirTicketNameListBean> selectByPks(String[] pks) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketNameListMapper.selectByPks", pks);
	}

	@Override
	public List<PassengerAllotDto> selectByPassengerPks(List<String> passenger_pks) {
		List<PassengerAllotDto> list = daoUtil
				.selectListByParam("com.xinchi.bean.mapper.PassengerAllotMapper.selectByPassengerPks", passenger_pks);
		return list;
	}

	@Override
	public List<PassengerAllotDto> selectPassengerAllotByPassengerPk(String passenger_pk) {
		List<PassengerAllotDto> list = daoUtil.selectListByParam(
				"com.xinchi.bean.mapper.PassengerAllotMapper.selectByPassengerPk", passenger_pk);
		return list;
	}

	@Override
	public List<PassengerAllotDto> selectPassengerAllotByOrderPk(String order_pk) {
		List<PassengerAllotDto> list = daoUtil.selectListByParam(
				"com.xinchi.bean.mapper.PassengerAllotMapper.selectByOrderPk", order_pk);
		return list;
	}

}