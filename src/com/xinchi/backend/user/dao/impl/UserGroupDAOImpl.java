package com.xinchi.backend.user.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.user.dao.UserGroupDAO;
import com.xinchi.bean.UserGroupBean;
import com.xinchi.common.DaoUtil;

@Repository
@Scope("prototype")
public class UserGroupDAOImpl extends SqlSessionDaoSupport implements UserGroupDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(UserGroupBean bo) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.UserGroupMapper.insert", bo);
	}

	@Override
	public void update(UserGroupBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.UserGroupMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.UserGroupMapper.deleteByPrimaryKey", id);
	}

	@Override
	public UserGroupBean selectByPrimaryKey(String id) {
		return (UserGroupBean) daoUtil.selectByPK("com.xinchi.bean.mapper.UserGroupMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<UserGroupBean> getAllByParam(UserGroupBean bo) {
		List<UserGroupBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserGroupMapper.selectByParam", bo);
		return list;
	}
}