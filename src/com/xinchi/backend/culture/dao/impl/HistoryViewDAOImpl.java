package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.HistoryViewDAO;
import com.xinchi.bean.HistoryViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class HistoryViewDAOImpl extends SqlSessionDaoSupport implements HistoryViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(HistoryViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.HistoryViewMapper.insert", view);
	}

	@Override
	public List<HistoryViewBean> getAllViewsByPage(Page<HistoryViewBean> page) {
		List<HistoryViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.HistoryViewMapper.selectByPage",
				page);
		return list;
	}

	@Override
	public HistoryViewBean selectByPk(String view_pk) {
		return (HistoryViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.HistoryViewMapper.selectByPrimaryKey",
				view_pk);
	}

	@Override
	public void update(HistoryViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.HistoryViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.HistoryViewMapper.deleteByPrimaryKey", view_pk);
	}
}
