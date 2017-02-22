package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.ValueViewDAO;
import com.xinchi.bean.ValueViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ValueViewDAOImpl extends SqlSessionDaoSupport implements ValueViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ValueViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ValueViewMapper.insert", view);
	}

	@Override
	public List<ValueViewBean> getAllViewsByPage(Page page) {
		List<ValueViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ValueViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public ValueViewBean selectByPk(String view_pk) {
		return (ValueViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ValueViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(ValueViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ValueViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ValueViewMapper.deleteByPrimaryKey", view_pk);
	}
}
