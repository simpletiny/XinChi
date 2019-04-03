package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class OrderDAOImpl extends SqlSessionDaoSupport implements OrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<OrderDto> selectTbcByPage(Page<OrderDto> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectTbcByPage", page);
	}

	@Override
	public List<OrderDto> selectCByPage(Page<OrderDto> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectCByPage", page);
	}

	@Override
	public List<OrderDto> selectFByPage(Page<OrderDto> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectFByPage", page);
	}

	@Override
	public OrderDto selectByTeamNumber(String team_number) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.OrderMapper.selectByTeamNumber", team_number);
	}

	@Override
	public List<OrderDto> selectTbcByParam(OrderDto option) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectTbcByParam", option);
	}

	@Override
	public OrderDto searchCOrderByPk(String order_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.OrderMapper.searchCOrderByPk", order_pk);
	}

	@Override
	public List<SaleScoreDto> searchSaleScore(Page<SaleScoreDto> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.searchSaleScore", page);
	}
	@Override
	public List<SaleScoreDto> searchBackMoneyScoreByPage(Page<SaleScoreDto> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.searchBackMoneyScore", page);
	}

	@Override
	public List<SaleScoreDto> searchSaleScoreByParam(SaleScoreDto ssd) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.searchSaleScoreByParam", ssd);
	}

	@Override
	public List<OrderDto> selectByParam(OrderDto order) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectByParam", order);
	}
	@Override
	public String selectMaxConfirmDateByEmployeePk(String employee_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.OrderMapper.selectMaxConfirmDateByEmployeePk", employee_pk);
	}

}