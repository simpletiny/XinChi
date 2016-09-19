package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.common.DaoUtil;


@Repository
@Scope("prototype")
public class ClientDAOImpl extends SqlSessionDaoSupport implements ClientDAO{

	private SqlSession sqlSession;
	private DaoUtil daoUtil;
	public void initDao(){
		if(daoUtil==null){
			sqlSession=sqlSession==null?getSqlSession():sqlSession;
			daoUtil=new DaoUtil(sqlSession);
		}
	}
	
	@Override
	public void insert(com.xinchi.bean.ClientBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientMapper.insert", bo);
	}

	@Override
	public void update(com.xinchi.bean.ClientBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientMapper.deleteByPrimaryKey", id);
	}

	@Override
	public com.xinchi.bean.ClientBean selectByPrimaryKey(String id) {
		return (com.xinchi.bean.ClientBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ClientMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<com.xinchi.bean.ClientBean> getAllByParam(com.xinchi.bean.ClientBean bo) {
		List<com.xinchi.bean.ClientBean> list=daoUtil.selectByBOParamT("com.xinchi.bean.mapper.ClientMapper.selectByParam", bo);
		return list;
	}
	
}