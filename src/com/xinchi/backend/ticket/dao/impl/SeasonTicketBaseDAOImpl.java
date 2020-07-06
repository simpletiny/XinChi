package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.SeasonTicketBaseDAO;
import com.xinchi.bean.SeasonTicketBaseBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SeasonTicketBaseDAOImpl extends SqlSessionDaoSupport implements SeasonTicketBaseDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(SeasonTicketBaseBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.SeasonTicketBaseMapper.insert", bean);
	}

	@Override
	public void update(SeasonTicketBaseBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SeasonTicketBaseMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SeasonTicketBaseMapper.deleteByPrimaryKey", id);
	}

	@Override
	public SeasonTicketBaseBean selectByPrimaryKey(String id) {
		return (SeasonTicketBaseBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.SeasonTicketBaseMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<SeasonTicketBaseBean> selectByParam(SeasonTicketBaseBean bean) {
		List<SeasonTicketBaseBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.SeasonTicketBaseMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<SeasonTicketBaseBean> selectByPage(Page<SeasonTicketBaseBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.SeasonTicketBaseMapper.selectByPage", page);
	}

}