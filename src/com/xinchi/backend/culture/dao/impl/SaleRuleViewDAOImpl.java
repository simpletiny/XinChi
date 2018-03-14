package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.SaleRuleViewDAO;
import com.xinchi.bean.SaleRuleViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SaleRuleViewDAOImpl extends SqlSessionDaoSupport implements SaleRuleViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SaleRuleViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SaleRuleViewMapper.insert", view);
	}

	@Override
	public List<SaleRuleViewBean> getAllViewsByPage(Page<SaleRuleViewBean> page) {
		List<SaleRuleViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.SaleRuleViewMapper.selectByPage",
				page);
		return list;
	}

	@Override
	public SaleRuleViewBean selectByPk(String view_pk) {
		return (SaleRuleViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.SaleRuleViewMapper.selectByPrimaryKey",
				view_pk);
	}

	@Override
	public void update(SaleRuleViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SaleRuleViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SaleRuleViewMapper.deleteByPrimaryKey", view_pk);
	}
}
