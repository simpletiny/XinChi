package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.TicketRuleViewDAO;
import com.xinchi.bean.TicketRuleViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class TicketRuleViewDAOImpl extends SqlSessionDaoSupport implements TicketRuleViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(TicketRuleViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TicketRuleViewMapper.insert", view);
	}

	@Override
	public List<TicketRuleViewBean> getAllViewsByPage(Page<TicketRuleViewBean> page) {
		List<TicketRuleViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.TicketRuleViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public TicketRuleViewBean selectByPk(String view_pk) {
		return (TicketRuleViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.TicketRuleViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(TicketRuleViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.TicketRuleViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.TicketRuleViewMapper.deleteByPrimaryKey", view_pk);
	}
}
