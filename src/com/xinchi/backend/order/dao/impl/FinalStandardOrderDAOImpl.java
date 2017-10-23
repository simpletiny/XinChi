package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.FinalStandardOrderDAO;
import com.xinchi.bean.FinalStandardOrderBean;
import com.xinchi.common.DaoUtil;

@Repository
public class FinalStandardOrderDAOImpl extends SqlSessionDaoSupport implements FinalStandardOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(FinalStandardOrderBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.FinalStandardOrderMapper.insert", bean);
	}

	@Override
	public void update(FinalStandardOrderBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.FinalStandardOrderMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.FinalStandardOrderMapper.deleteByPrimaryKey", id);
	}

	@Override
	public FinalStandardOrderBean selectByPrimaryKey(String id) {
		return (FinalStandardOrderBean) daoUtil.selectByPK("com.xinchi.bean.mapper.FinalStandardOrderMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<FinalStandardOrderBean> selectByParam(FinalStandardOrderBean bean) {
		List<FinalStandardOrderBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.FinalStandardOrderMapper.selectByParam", bean);
		return list;
	}

}