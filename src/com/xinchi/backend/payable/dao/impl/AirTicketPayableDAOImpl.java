package com.xinchi.backend.payable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AirTicketPayableDAOImpl extends SqlSessionDaoSupport implements AirTicketPayableDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AirTicketPayableBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AirTicketPayableMapper.insert", bean);
	}

	@Override
	public void insertWithPk(AirTicketPayableBean bean) {
		daoUtil.insertBOWithPk("com.xinchi.bean.mapper.AirTicketPayableMapper.insert", bean);
	}

	@Override
	public void update(AirTicketPayableBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirTicketPayableMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirTicketPayableMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AirTicketPayableBean selectByPrimaryKey(String id) {
		return (AirTicketPayableBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.AirTicketPayableMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AirTicketPayableBean> selectByParam(AirTicketPayableBean bean) {
		List<AirTicketPayableBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirTicketPayableMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<AirTicketPayableBean> selectByPage(Page<AirTicketPayableBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketPayableMapper.selectByPage", page);
	}

	@Override
	public List<AirTicketPayableBean> selectByPks(List<String> pks) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketPayableMapper.selectByPks", pks);
	}

	@Override
	public List<AirTicketPayableBean> selectByRelatedPk(String related_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirTicketPayableMapper.selectByRelatedPk", related_pk);
	}

	@Override
	public void deleteByRelatedPk(String related_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.AirTicketPayableMapper.deleteByRelatedPk", related_pk);
	}

	@Override
	public List<AirServiceFeeDto> searchServiceFees(AirServiceFeeDto summary_option) {
		return daoUtil.selectObjectsByParam("com.xinchi.bean.mapper.AirTicketPayableMapper.selectServiceFees",
				summary_option);
	}

	@Override
	public List<AirServiceFeeDto> selectServiceFeeSummary(AirServiceFeeDto summary_option) {
		return daoUtil.selectObjectsByParam("com.xinchi.bean.mapper.AirTicketPayableMapper.selectAirTicketFeeSummary",
				summary_option);
	}

	@Override
	public List<AirServiceFeeDto> selectAirTicketDeductSummary(AirServiceFeeDto summary_option) {
		return daoUtil.selectObjectsByParam(
				"com.xinchi.bean.mapper.AirTicketPayableMapper.selectAirTicketDeductSummary", summary_option);
	}

}