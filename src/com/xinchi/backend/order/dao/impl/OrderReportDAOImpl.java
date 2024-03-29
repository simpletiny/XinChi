package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class OrderReportDAOImpl extends SqlSessionDaoSupport implements OrderReportDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<OrderReportDto> selectOrderReportByPage(Page<OrderReportDto> page) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderReportMapper.selectOrderReportByPage", page);
	}

	@Override
	public TeamReportBean selectTeamReportByTn(String team_number) {
		return (TeamReportBean) daoUtil
				.selectOneValueByParam("com.xinchi.bean.mapper.TeamReportMapper.selectByTeamNumber", team_number);
	}

	@Override
	public void updateTeamReport(TeamReportBean tr) {
		daoUtil.updateByParam("com.xinchi.bean.mapper.TeamReportMapper.updateByPrimaryKey", tr);

	}

	@Override
	public OrderReportDto selectSumReport(OrderReportDto option) {

		return (OrderReportDto) daoUtil
				.selectOneValueByParam("com.xinchi.bean.mapper.OrderReportMapper.selectSumReport", option);
	}

	@Override
	public void insert(TeamReportBean tr) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TeamReportMapper.insert", tr);
	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.TeamReportMapper.deleteByTeamNumber", team_number);

	}

}