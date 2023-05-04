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
	public String insert(SaleOrderNameListBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.SaleOrderNameListMapper.insert", bean);
	}

	@Override
	public List<SaleOrderNameListBean> selectByTeamNumber(String team_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.SaleOrderNameListMapper.selectByTeamNumber", team_number);
	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.SaleOrderNameListMapper.deleteByTeamNumber", team_number);
	}

	@Override
	public List<SaleOrderNameListBean> selectByOrderPk(String order_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.SaleOrderNameListMapper.selectByOrderPk", order_pk);
	}

	@Override
	public void deleteByOrderPk(String order_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.SaleOrderNameListMapper.deleteByOrderPk", order_pk);
	}

	@Override
	public void update(SaleOrderNameListBean bean) {
		daoUtil.updateByBOParam("com.xinchi.bean.mapper.SaleOrderNameListMapper.updateByPrimaryKey", bean);

	}

	@Override
	public List<SaleOrderNameListBean> selectByTeamNumbers(List<String> team_numbers) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.SaleOrderNameListMapper.selectByTeamNumbers",
				team_numbers);
	}

	@Override
	public SaleOrderNameListBean selectByPrimaryKey(String pk) {

		return (SaleOrderNameListBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.SaleOrderNameListMapper.selectByPrimaryKey", pk);

	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SaleOrderNameListMapper.deleteByPrimaryKey", pk);
	}

}