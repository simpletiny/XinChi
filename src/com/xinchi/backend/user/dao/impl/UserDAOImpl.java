package com.xinchi.backend.user.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.common.DaoUtil;

@Repository
@Scope("prototype")
public class UserDAOImpl extends SqlSessionDaoSupport implements UserDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(UserBaseBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.UserBaseMapper.insert", bo);
	}

	@Override
	public void update(UserBaseBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.UserBaseMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.UserBaseMapper.deleteByPrimaryKey", id);
	}

	@Override
	public UserBaseBean selectByPrimaryKey(String id) {
		return (UserBaseBean) daoUtil.selectByPK("com.xinchi.bean.mapper.UserBaseMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<UserBaseBean> getAllByParam(UserBaseBean bo) {
		List<UserBaseBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserBaseMapper.selectByParam", bo);
		return list;
	}

	@Override
	public String getMaxUserNumber() {
		String max = daoUtil.selectOneValue("com.xinchi.bean.mapper.UserBaseMapper.selectMaxUserNumber");
		return max;
	}

	@Override
	public List<UserCommonBean> getAllUserCommonByParam(UserCommonBean bo) {
		List<UserCommonBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserCommonMapper.selectAllByParam",
				bo);
		return list;
	}

}