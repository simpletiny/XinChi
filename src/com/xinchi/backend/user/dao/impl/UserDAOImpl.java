package com.xinchi.backend.user.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserCommonBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.mybatis.param.MapParam;
import com.xinchi.tools.Page;

@Repository
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
		List<UserCommonBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserCommonMapper.selectAllByParam", bo);
		return list;
	}

	@Override
	public List<UserCommonBean> getAllNewUsers() {
		List<UserCommonBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserCommonMapper.selectAllNewUsers", null);
		return list;
	}

	@Override
	public List<UserCommonBean> getAllUsersByRole(String roles) {
		List<UserCommonBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.UserCommonMapper.selectAllUsersByRole", roles);
		return list;
	}

	@Override
	public List<UserBaseBean> getAllByPks(String[] pks) {
		List<UserBaseBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.UserBaseMapper.selectAllByPks", pks);
		return list;
	}

	@Override
	public UserBaseBean getByUserNumber(String user_number) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.UserBaseMapper.selectByUserNumber", user_number);
	}

	@Override
	public UserBaseBean selectUserByName(String user_name) {

		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.UserBaseMapper.selectByUserName", user_name);
	}

	@Override
	public Map<Object, Object> getUserMap(MapParam param) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.UserBaseMapper.selectUserMap", param);
	}

	@Override
	public List<UserCommonBean> selectByPage(Page page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.UserCommonMapper.selectByPage", page);
	}

	@Override
	public UserCommonBean selectUserCommonByPk(String user_pk) {
		return (UserCommonBean)daoUtil.selectByPK("com.xinchi.bean.mapper.UserCommonMapper.selectByPrimaryKey", user_pk);
	}

	@Override
	public UserCommonBean selectUserCommonByUserNumber(String user_number) {
		
		 return (UserCommonBean)daoUtil.selectByPK("com.xinchi.bean.mapper.UserCommonMapper.selectByUserNumber", user_number);
	}

}