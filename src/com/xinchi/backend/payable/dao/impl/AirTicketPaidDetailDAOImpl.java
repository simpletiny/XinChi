package com.xinchi.backend.payable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AirTicketPaidDetailDAOImpl extends SqlSessionDaoSupport implements AirTicketPaidDetailDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AirTicketPaidDetailBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AirTicketPaidDetailMapper.insert", bean);
	}

	@Override
	public void update(AirTicketPaidDetailBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirTicketPaidDetailMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirTicketPaidDetailMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AirTicketPaidDetailBean selectByPrimaryKey(String id) {
		return (AirTicketPaidDetailBean) daoUtil.selectByPK("com.xinchi.bean.mapper.AirTicketPaidDetailMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByParam(AirTicketPaidDetailBean bean) {
		List<AirTicketPaidDetailBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketPaidDetailMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<AirTicketPaidDto> selectByPage(Page<AirTicketPaidDto> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewAirTicketPaidDetailMapper.selectByPage", page);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByRelatedPk(String related_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketPaidDetailMapper.selectByRelatedPk", related_pk);
	}

	@Override
	public AirTicketPaidDetailBean selectGroupDetailByRelatedPk(String related_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.AirTicketPaidDetailMapper.selectGroupDetailByRelatedPk", related_pk);
	}

}