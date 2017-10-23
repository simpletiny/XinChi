package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.FinalNonStandardOrderDAO;
import com.xinchi.bean.FinalNonStandardOrderBean;
import com.xinchi.common.DaoUtil;

@Repository
public class FinalNonStandardOrderDAOImpl extends SqlSessionDaoSupport implements FinalNonStandardOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(FinalNonStandardOrderBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.FinalNonStandardOrderMapper.insert", bean);
	}

	@Override
	public void update(FinalNonStandardOrderBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.FinalNonStandardOrderMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.FinalNonStandardOrderMapper.deleteByPrimaryKey", id);
	}

	@Override
	public FinalNonStandardOrderBean selectByPrimaryKey(String id) {
		return (FinalNonStandardOrderBean) daoUtil.selectByPK("com.xinchi.bean.mapper.FinalNonStandardOrderMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<FinalNonStandardOrderBean> selectByParam(FinalNonStandardOrderBean bean) {
		List<FinalNonStandardOrderBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.FinalNonStandardOrderMapper.selectByParam", bean);
		return list;
	}

}