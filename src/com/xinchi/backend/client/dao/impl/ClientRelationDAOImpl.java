package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.bean.AccurateSaleDto;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ConnectDto;
import com.xinchi.bean.MeterDto;
import com.xinchi.bean.PotentialDto;
import com.xinchi.bean.WorkOrderDto;
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
		List<ClientRelationSummaryBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectByPage", page);
		return list;
	}

	@Override
	public String selectClientCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectClientCount",
				sales_name);
	}

	@Override
	public String selectWeekOrderCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectWeekOrder",
				sales_name);
	}

	@Override
	public String selectMonthOrderCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMonthOrder",
				sales_name);
	}

	@Override
	public String selectYesterdayVisitCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectYesterdayVisit",
				sales_name);
	}

	@Override
	public String selectWeekVisitCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectWeekVisit",
				sales_name);
	}

	@Override
	public String selectMonthVisitCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMonthVisit",
				sales_name);
	}

	@Override
	public String selectYesterdayChatCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectYesterdayChat",
				sales_name);
	}

	@Override
	public String selectWeekChatCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectWeekChat",
				sales_name);
	}

	@Override
	public String selectMonthChatCount(String sales_name) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMonthChat",
				sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectMarketSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMarketLevelSummary",
				sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectRelationSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectRelationLevelSummary",
				sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectBackSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectBackLevelSummary",
				sales_name);
	}

	@Override
	public List<ClientSummaryDto> selectSummary(String sales_name) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectSummary", sales_name);
	}

	@Override
	public PotentialDto selectPotentialData(String user_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectPotentialData",
				user_pk);
	}

	@Override
	public MeterDto selectMeterData(String user_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectMeterData",
				user_pk);
	}

	@Override
	public WorkOrderDto selectWorkOrderData(String user_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectWorkOrderData",
				user_pk);
	}

	@Override
	public AccurateSaleDto selectAccurateSaleData(String user_pk) {
		return daoUtil.selectOneValueByParam(
				"com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectAccurateSaleData", user_pk);
	}

	@Override
	public List<ConnectDto> selectConnectsByPage(Page<ConnectDto> page) {
		List<ConnectDto> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectConnectInfoByPage", page);
		return list;
	}

	@Override
	public List<ClientRelationBean> selectByParam(ClientRelationBean option) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientRelationSummaryMapper.selectByParam", option);
	}

	@Override
	public void insert(ClientRelationBean two) {
		daoUtil.insertWithoutLogin("com.xinchi.bean.mapper.ClientRelationMapper.insert", two);
	}

	@Override
	public void update(ClientRelationBean clientRelation) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientRelationMapper.updateByPrimaryKey", clientRelation);
	}

	@Override
	public ClientRelationBean selectByEmployeePk(String employee_pk) {

		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientRelationMapper.selectByEmployeePk",
				employee_pk);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientRelationMapper.deleteByPrimaryKey", pk);

	}

}