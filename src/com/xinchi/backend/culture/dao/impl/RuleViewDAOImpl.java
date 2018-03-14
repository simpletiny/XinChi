package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.RuleViewDAO;
import com.xinchi.bean.RuleViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class RuleViewDAOImpl extends SqlSessionDaoSupport implements RuleViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(RuleViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.RuleViewMapper.insert", view);
	}

	@Override
	public List<RuleViewBean> getAllViewsByPage(Page<RuleViewBean> page) {
		List<RuleViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.RuleViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public RuleViewBean selectByPk(String view_pk) {
		return (RuleViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.RuleViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(RuleViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.RuleViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.RuleViewMapper.deleteByPrimaryKey", view_pk);
	}
}
