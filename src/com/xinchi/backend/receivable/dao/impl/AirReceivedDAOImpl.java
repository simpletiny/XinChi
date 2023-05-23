package com.xinchi.backend.receivable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.receivable.dao.AirReceivedDAO;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AirReceivedDAOImpl extends SqlSessionDaoSupport implements AirReceivedDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AirReceivedDetailBean detail) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AirReceivedDetailMapper.insert", detail);
	}

	@Override
	public List<AirReceivedDetailBean> selectByPage(Page<AirReceivedDetailBean> page) {
		List<AirReceivedDetailBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.AirReceivedDetailMapper.selectByPage", page);
		return list;
	}

	@Override
	public AirReceivedDetailBean selectByPk(String pk) {
		return (AirReceivedDetailBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.AirReceivedDetailMapper.selectByPrimaryKey", pk);
	}

	@Override
	public void deleteByPk(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirReceivedDetailMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public void update(AirReceivedDetailBean detail) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirReceivedDetailMapper.updateByPrimaryKey", detail);
	}

	@Override
	public List<AirReceivedDetailBean> selectByParam(AirReceivedDetailBean bean) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirReceivedDetailMapper.selectByParam", bean);
	}

	@Override
	public List<AirReceivedDetailBean> selectByRelatedPk(String related_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirReceivedDetailMapper.selectByRelatedPk", related_pk);
	}

	@Override
	public List<AirReceivedDetailBean> selectByBusinessNumber(String business_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirReceivedDetailMapper.selectByBusinessNumber",
				business_number);
	}

}
