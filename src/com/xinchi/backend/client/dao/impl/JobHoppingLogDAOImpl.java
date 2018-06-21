package com.xinchi.backend.client.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.JobHoppingLogDAO;
import com.xinchi.bean.JobHoppingLogBean;
import com.xinchi.common.DaoUtil;

@Repository
public class JobHoppingLogDAOImpl extends SqlSessionDaoSupport implements JobHoppingLogDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(JobHoppingLogBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.JobHoppingLogMapper.insert", bean);
	}

	@Override
	public void update(JobHoppingLogBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.JobHoppingLogMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.JobHoppingLogMapper.deleteByPrimaryKey", id);
	}

	@Override
	public JobHoppingLogBean selectByPrimaryKey(String id) {
		return (JobHoppingLogBean) daoUtil.selectByPK("com.xinchi.bean.mapper.JobHoppingLogMapper.selectByPrimaryKey",
				id);
	}

}