package com.xinchi.backend.supplier.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.supplier.dao.SupplierDepositDAO;
import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SupplierDepositDAOImpl extends SqlSessionDaoSupport implements SupplierDepositDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SupplierDepositBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SupplierDepositMapper.insert", bean);
	}

	@Override
	public void update(SupplierDepositBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SupplierDepositMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SupplierDepositMapper.deleteByPrimaryKey", id);
	}

	@Override
	public SupplierDepositBean selectByPrimaryKey(String id) {
		return (SupplierDepositBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.SupplierDepositMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<SupplierDepositBean> selectByParam(SupplierDepositBean bean) {
		List<SupplierDepositBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.SupplierDepositMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<SupplierDepositBean> selectByPage(Page page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.SupplierDepositMapper.selectByPage", page);
	}

	@Override
	public BigDecimal selectSumBalanceByType(String type) {

		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.SupplierDepositMapper.selectSumBalance", type);
	}

	@Override
	public SupplierDepositBean selectByDepositNumber(String deposit_number) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.SupplierDepositMapper.selectByDepositNumber",
				deposit_number);
	}

}