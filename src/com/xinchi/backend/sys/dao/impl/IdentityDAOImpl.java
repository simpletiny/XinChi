package com.xinchi.backend.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sys.dao.IdentityDAO;
import com.xinchi.bean.IdentityBean;
import com.xinchi.common.DaoUtil;

@Repository
public class IdentityDAOImpl extends SqlSessionDaoSupport implements IdentityDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(IdentityBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.IdentityMapper.insert", bean);
	}

	@Override
	public void update(IdentityBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.IdentityMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.IdentityMapper.deleteByPrimaryKey", id);
	}

	@Override
	public IdentityBean selectByPrimaryKey(String id) {
		return (IdentityBean) daoUtil.selectByPK("com.xinchi.bean.mapper.IdentityMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<IdentityBean> selectByParam(IdentityBean bean) {
		List<IdentityBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.IdentityMapper.selectByParam", bean);
		return list;
	}

	@Override
	public IdentityBean selectById(String id) {
		return (IdentityBean) daoUtil.selectByPK("com.xinchi.bean.mapper.IdentityMapper.selectById", id);
	}

}