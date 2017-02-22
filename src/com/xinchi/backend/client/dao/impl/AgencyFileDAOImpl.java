package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.AgencyFileDAO;
import com.xinchi.bean.AgencyFileBean;
import com.xinchi.common.DaoUtil;

@Repository
public class AgencyFileDAOImpl extends SqlSessionDaoSupport implements AgencyFileDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AgencyFileBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AgencyFileMapper.insert", bo);
	}

	@Override
	public void update(AgencyFileBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AgencyFileMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AgencyFileMapper.deleteByPrimaryKey", id);
	}

	@Override
	public List<AgencyFileBean> searchAgencyFilesByAgencyPk(String agency_pk) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.AgencyFileMapper.selectAgencyFilesByAgencyPk", agency_pk);
	}

	@Override
	public List<AgencyFileBean> selectByParam(AgencyFileBean bo) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AgencyFileMapper.selectByParam", bo);
	}

	@Override
	public void deleteFileByParam(AgencyFileBean sfb) {
		daoUtil.deleteByBOParam("com.xinchi.bean.mapper.AgencyFileMapper.deleteByParam", sfb);
	}
}