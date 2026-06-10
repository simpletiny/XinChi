package com.xinchi.backend.finance.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.finance.dao.ReceivedMatchDAO;
import com.xinchi.bean.ReceivedMatchBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ReceivedMatchDAOImpl extends SqlSessionDaoSupport implements ReceivedMatchDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(ReceivedMatchBean bo) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.ReceivedMatchMapper.insert", bo);
	}

	@Override
	public void update(ReceivedMatchBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ReceivedMatchMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ReceivedMatchMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public List<ReceivedMatchBean> selectByDetailPk(String detail_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ReceivedMatchMapper.selectByDetailPk", detail_pk);
	}

	@Override
	public List<ReceivedMatchBean> selectByReceivedPk(String received_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ReceivedMatchMapper.selectByReceivedPk", received_pk);
	}

}
