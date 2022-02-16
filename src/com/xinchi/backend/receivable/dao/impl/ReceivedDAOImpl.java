package com.xinchi.backend.receivable.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ReceivedDAOImpl extends SqlSessionDaoSupport implements ReceivedDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ClientReceivedDetailBean detail) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientReceivedDetailMapper.insert", detail);
	}

	@Override
	public void insertWithPk(ClientReceivedDetailBean detail) {
		daoUtil.insertBOWithPk("com.xinchi.bean.mapper.ClientReceivedDetailMapper.insert", detail);
	}

	@Override
	public List<ClientReceivedDetailBean> getAllByPage(Page<ClientReceivedDetailBean> page) {
		List<ClientReceivedDetailBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ClientReceivedDetailMapper.selectByPage", page);
		return list;
	}

	@Override
	public ClientReceivedDetailBean selectByPk(String pk) {
		return (ClientReceivedDetailBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ClientReceivedDetailMapper.selectByPrimaryKey", pk);
	}

	@Override
	public void deleteByPk(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientReceivedDetailMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public List<ClientReceivedDetailBean> selectByRelatedPks(String related_pks) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientReceivedDetailMapper.selectByRelatedPks",
				related_pks);
	}

	@Override
	public void update(ClientReceivedDetailBean detail) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientReceivedDetailMapper.updateByPrimaryKey", detail);
	}

	@Override
	public ClientReceivedDetailBean selectReceivedDetailByRelatedPk(String related_pk) {

		return daoUtil.selectOneValueByParam(
				"com.xinchi.bean.mapper.ClientReceivedDetailMapper.selectReceivedDetailByRelatedPk", related_pk);
	}

	@Override
	public List<ClientReceivedDetailBean> selectByParam(ClientReceivedDetailBean bean) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ClientReceivedDetailMapper.selectByParam", bean);
	}

	@Override
	public BigDecimal selectSumReceivedByTeamNumber(String team_number) {
		return (BigDecimal) daoUtil.selectOneValueByParam(
				"com.xinchi.bean.mapper.ClientReceivedDetailMapper.searchSumReceivedByTeamNumber", team_number);
	}

}
