package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.AccurateSaleDAO;
import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AccurateSaleDAOImpl extends SqlSessionDaoSupport implements AccurateSaleDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AccurateSaleBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AccurateSaleMapper.insert", bean);
	}

	@Override
	public void update(AccurateSaleBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AccurateSaleMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AccurateSaleMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AccurateSaleBean selectByPrimaryKey(String id) {
		return (AccurateSaleBean) daoUtil.selectByPK("com.xinchi.bean.mapper.AccurateSaleMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<AccurateSaleBean> selectByParam(AccurateSaleBean bean) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AccurateSaleMapper.selectByParam", bean);
	}

	@Override
	public List<AccurateSaleBean> selectByPage(Page page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AccurateSaleMapper.selectByPage", page);
	}

}