package com.xinchi.backend.user.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.user.dao.UserGroupRelatedDAO;
import com.xinchi.bean.UserGroupRelatedBean;
import com.xinchi.common.DaoUtil;

@Repository
@Scope("prototype")
public class UserGroupRelatedDAOImpl extends SqlSessionDaoSupport implements UserGroupRelatedDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(UserGroupRelatedBean bo) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.UserGroupRelatedMapper.insert", bo);
	}

	@Override
	public void update(UserGroupRelatedBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.UserGroupRelatedMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.UserGroupRelatedMapper.deleteByPrimaryKey", id);
	}

	@Override
	public UserGroupRelatedBean selectByPrimaryKey(String id) {
		return (UserGroupRelatedBean) daoUtil.selectByPK("com.xinchi.bean.mapper.UserGroupRelatedMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<UserGroupRelatedBean> getAllByParam(UserGroupRelatedBean bo) {
		List<UserGroupRelatedBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserGroupRelatedMapper.selectByParam", bo);
		return list;
	}
}