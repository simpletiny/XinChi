package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ClientRelationDAOImpl extends SqlSessionDaoSupport implements ClientRelationDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<ClientRelationSummaryBean> selectByPage(Page page) {
		List<ClientRelationSummaryBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectByPage", page);
		return list;
	}

	@Override
	public String selectClientCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectClientCount", sales_name);
	}

	@Override
	public String selectWeekOrderCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectWeekOrder", sales_name);
	}

	@Override
	public String selectMonthOrderCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMonthOrder", sales_name);
	}

	@Override
	public String selectYesterdayVisitCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectYesterdayVisit", sales_name);
	}

	@Override
	public String selectWeekVisitCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectWeekVisit", sales_name);
	}

	@Override
	public String selectMonthVisitCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMonthVisit", sales_name);
	}

	@Override
	public String selectYesterdayChatCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectYesterdayChat", sales_name);
	}

	@Override
	public String selectWeekChatCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectWeekChat", sales_name);
	}

	@Override
	public String selectMonthChatCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMonthChat", sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectMarketSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMarketLevelSummary", sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectRelationSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectRelationLevelSummary", sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectBackSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectBackLevelSummary", sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectSummary", sales_name);
	}

}