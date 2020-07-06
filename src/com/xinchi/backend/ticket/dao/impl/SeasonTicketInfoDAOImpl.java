package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.SeasonTicketInfoDAO;
import com.xinchi.bean.SeasonTicketInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class SeasonTicketInfoDAOImpl extends SqlSessionDaoSupport implements SeasonTicketInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SeasonTicketInfoBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SeasonTicketInfoMapper.insert", bean);
	}

	@Override
	public void update(SeasonTicketInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SeasonTicketInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SeasonTicketInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public SeasonTicketInfoBean selectByPrimaryKey(String id) {
		return (SeasonTicketInfoBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.SeasonTicketInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<SeasonTicketInfoBean> selectByParam(SeasonTicketInfoBean bean) {
		List<SeasonTicketInfoBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.SeasonTicketInfoMapper.selectByParam", bean);
		return list;
	}

}