package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.TravelAgencyDAO;
import com.xinchi.bean.TravelAgencyBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class TravelAgencyDAOImpl extends SqlSessionDaoSupport implements TravelAgencyDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(TravelAgencyBean agency) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TravelAgencyMapper.insert", agency);
	}

	@Override
	public List<TravelAgencyBean> getAllByPage(Page page) {
		List<TravelAgencyBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.TravelAgencyMapper.selectByPage", page);
		return list;
	}

	@Override
	public TravelAgencyBean selectByPk(String agency_pk) {
		return (TravelAgencyBean) daoUtil.selectByPK("com.xinchi.bean.mapper.TravelAgencyMapper.selectByPrimaryKey", agency_pk);
	}

	@Override
	public void update(TravelAgencyBean agency) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.TravelAgencyMapper.updateByPrimaryKey", agency);
	}

	@Override
	public List<TravelAgencyBean> selectSameName(String content) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.TravelAgencyMapper.selectSameName", content);
	}

	@Override
	public List<TravelAgencyBean> selectSameCode(String content) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.TravelAgencyMapper.selectSameCode", content);
	}

}