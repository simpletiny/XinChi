package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.DaoUtil;

@Repository
public class OrderNameListDAOImpl extends SqlSessionDaoSupport implements OrderNameListDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SaleOrderNameListBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SaleOrderNameListMapper.insert", bean);
	}

	@Override
	public List<SaleOrderNameListBean> selectByTeamNumber(String team_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.SaleOrderNameListMapper.selectByTeamNumber", team_number);
	}

}