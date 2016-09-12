package com.xinchi.backend.userinfo.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.userinfo.dao.UserInfoDAO;
import com.xinchi.bean.UserInfoBean;
import com.xinchi.common.DaoUtil;


@Repository
@Scope("prototype")
public class UserInfoDAOImpl extends SqlSessionDaoSupport implements UserInfoDAO{

	private SqlSession sqlSession;
	private DaoUtil daoUtil;
	public void initDao(){
		if(daoUtil==null){
			sqlSession=sqlSession==null?getSqlSession():sqlSession;
			daoUtil=new DaoUtil(sqlSession);
		}
	}
	
	@Override
	public void insert(com.xinchi.bean.UserInfoBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.UserInfoMapper.insert", bo);
	}

	@Override
	public void update(com.xinchi.bean.UserInfoBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.UserInfoMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.UserInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public com.xinchi.bean.UserInfoBean selectByPrimaryKey(String id) {
		return (com.xinchi.bean.UserInfoBean) daoUtil.selectByPK("com.xinchi.bean.mapper.UserInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<com.xinchi.bean.UserInfoBean> getAllByParam(com.xinchi.bean.UserInfoBean bo) {
		List<com.xinchi.bean.UserInfoBean> list=daoUtil.selectByBOParamT("com.xinchi.bean.mapper.UserInfoMapper.selectByParam", bo);
		return list;
	}

	@Override
	public UserInfoBean selectByUserId(String id) {
		return (com.xinchi.bean.UserInfoBean) daoUtil.selectByPK("com.xinchi.bean.mapper.UserInfoMapper.selectByUserId", id);
	}
	
}