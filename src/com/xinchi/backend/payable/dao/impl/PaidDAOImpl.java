package com.xinchi.backend.payable.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class PaidDAOImpl extends SqlSessionDaoSupport implements PaidDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insertWithPk(SupplierPaidDetailBean detail) {
		daoUtil.insertBOWithPk("com.xinchi.bean.mapper.SupplierPaidDetailMapper.insert", detail);
	}

	@Override
	public void insert(SupplierPaidDetailBean detail) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SupplierPaidDetailMapper.insert", detail);
	}

	@Override
	public List<SupplierPaidDetailBean> getAllByPage(Page page) {
		List<SupplierPaidDetailBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.SupplierPaidDetailMapper.selectByPage", page);
		return list;
	}

	@Override
	public List<SupplierPaidDetailBean> selectByParam(SupplierPaidDetailBean options) {
		List<SupplierPaidDetailBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.SupplierPaidDetailMapper.selectByParam", options);
		return list;
	}

	@Override
	public void update(SupplierPaidDetailBean detail) {
		daoUtil.updateByBOParam("com.xinchi.bean.mapper.SupplierPaidDetailMapper.updateByPrimaryKey", detail);

	}

	@Override
	public SupplierPaidDetailBean selectByRelatedPk(String related_pk) {
		
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.SupplierPaidDetailMapper.selectByRelatedPk", related_pk);
	}
}
