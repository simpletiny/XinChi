package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.TicketNameTempletDAO;
import com.xinchi.bean.TicketNameTempletBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class TicketNameTempletDAOImpl extends SqlSessionDaoSupport implements TicketNameTempletDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(TicketNameTempletBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.TicketNameTempletMapper.insert", bean);
	}

	@Override
	public void update(TicketNameTempletBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.TicketNameTempletMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.TicketNameTempletMapper.deleteByPrimaryKey", id);
	}

	@Override
	public TicketNameTempletBean selectByPrimaryKey(String id) {
		return (TicketNameTempletBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.TicketNameTempletMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<TicketNameTempletBean> selectByParam(TicketNameTempletBean bean) {
		List<TicketNameTempletBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.TicketNameTempletMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<TicketNameTempletBean> selectByPage(Page<TicketNameTempletBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.TicketNameTempletMapper.selectByPage", page);
	}

}