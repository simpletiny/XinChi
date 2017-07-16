package com.xinchi.backend.user.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.user.dao.UserLogDAO;
import com.xinchi.bean.UserLogBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class UserLogDAOImpl extends SqlSessionDaoSupport implements UserLogDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(UserLogBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.UserLogMapper.insert", bean);
	}

	@Override
	public void update(UserLogBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.UserLogMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.UserLogMapper.deleteByPrimaryKey", id);
	}

	@Override
	public UserLogBean selectByPrimaryKey(String id) {
		return (UserLogBean) daoUtil.selectByPK("com.xinchi.bean.mapper.UserLogMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<UserLogBean> getAllByParam(UserLogBean bean) {
		List<UserLogBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserLogMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<UserLogBean> selectByPage(Page page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.UserLogMapper.selectByPage", page);
	}

}