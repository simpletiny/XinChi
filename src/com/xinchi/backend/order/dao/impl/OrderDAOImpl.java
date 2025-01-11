package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.SaleOrderBean;
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
	public OrderDto searchOrderByPk(String order_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.OrderMapper.searchOrderByPk", order_pk);
	}

	@Override
	public List<SaleScoreDto> searchSaleScore(SaleScoreDto score) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectSaleScore", score);
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
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.OrderMapper.selectMaxConfirmDateByEmployeePk",
				employee_pk);
	}

	@Override
	public List<OrderDto> selectConfirmingOrders(OrderDto order) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectConfirmingOrders", order);
	}

	@Override
	public List<OrderDto> selectByTeamNumbers(List<String> team_numbers) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectByTeamNumbers", team_numbers);
	}

	@Override
	public List<OrderDto> selectWithProductByParam(OrderDto order) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectWithProductByParam", order);
	}

	@Override
	public List<SaleScoreDto> search3MonthScoreByUserNumber(String user_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.search3MonthScoreByUserNumber", user_number);
	}

	@Override
	public List<OrderDto> selectPayableInfoByParam(OrderDto option) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectPayableInfoByParam", option);
	}

	@Override
	public OrderDto selectFinalOrderByTeamNumber(String team_number) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.OrderMapper.selectFinalOrderByTeamNumber",
				team_number);
	}

	@Override
	public List<SaleScoreDto> searchNonStandardSaleData(SaleScoreDto score) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectNonStandardSaleData", score);
	}

	@Override
	public List<SaleScoreDto> searchSaleCost(SaleScoreDto score) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectSaleCost", score);
	}

	@Override
	public List<OrderDto> selectOrderWithNames(List<String> team_numbers) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderMapper.selectOrderWithNames", team_numbers);
	}

	@Override
	public void insertWithPk(SaleOrderBean bean) {
		daoUtil.insertBOWithPk("com.xinchi.bean.mapper.SaleOrderMapper.insert", bean);
	}

	@Override
	public void update(SaleOrderBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SaleOrderMapper.updateByPrimaryKey", bean);
	}

	@Override
	public SaleOrderBean selectByPrimaryKey(String pk) {
		return (SaleOrderBean) daoUtil.selectByPK("com.xinchi.bean.mapper.SaleOrderMapper.selectByPrimaryKey", pk);
	}

	@Override
	public void deleteByPk(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SaleOrderMapper.deleteByPrimaryKey", pk);
	}
}