package com.xinchi.backend.finance.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.finance.dao.InnerTransferDAO;
import com.xinchi.bean.InnerTransferBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class InnerTransferDAOImpl extends SqlSessionDaoSupport implements InnerTransferDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public InnerTransferBean selectByPrimaryKey(String id) {
		return (InnerTransferBean) daoUtil.selectByPK("com.xinchi.bean.mapper.InnerTransferMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<InnerTransferBean> selectByParam(InnerTransferBean bean) {
		List<InnerTransferBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.InnerTransferMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<InnerTransferBean> selectByPage(Page<InnerTransferBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.InnerTransferMapper.selectByPage", page);
	}

}