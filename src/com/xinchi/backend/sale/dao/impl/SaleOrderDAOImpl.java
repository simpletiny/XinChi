package com.xinchi.backend.sale.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SaleOrderDAOImpl extends SqlSessionDaoSupport implements
		SaleOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(com.xinchi.bean.BudgetOrderBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.BudgetOrderMapper.insert", bo);
	}

	@Override
	public void saveNameList(List<SaleOrderNameListBean> arrName) {
		for (SaleOrderNameListBean bean : arrName) {
			daoUtil.insertBO(
					"com.xinchi.bean.mapper.BudgetOrderNameListMapper.insert",
					bean);

		}
	}

	@Override
	public void saveOrderSupplier(List<BudgetOrderSupplierBean> arrSupplier) {
		for (BudgetOrderSupplierBean bean : arrSupplier) {
			daoUtil.insertBO(
					"com.xinchi.bean.mapper.BudgetOrderSupplierMapper.insert",
					bean);

		}
	}

	@Override
	public List<BudgetOrderBean> selectAllByParam(BudgetOrderBean bo) {
		return daoUtil.selectByBOParamT(
				"com.xinchi.bean.mapper.BudgetOrderMapper.selectByParam", bo);

	}

	@Override
	public BudgetOrderBean selectBudgetOrderByPk(String order_pk) {
		return (BudgetOrderBean) daoUtil.selectByPK(
				"com.xinchi.bean.mapper.BudgetOrderMapper.selectByPrimaryKey",
				order_pk);
	}

	@Override
	public List<BudgetOrderSupplierBean> searchBudgetSupplier(String team_number) {
		return daoUtil
				.selectByParam(
						"com.xinchi.bean.mapper.BudgetOrderSupplierMapper.selectByTeamNumber",
						team_number);
	}

	@Override
	public List<BudgetOrderSupplierBean> searchBudgetSupplierByParam(
			BudgetOrderSupplierBean bo) {
		return daoUtil
				.selectByParam(
						"com.xinchi.bean.mapper.BudgetOrderSupplierMapper.selectByParam",
						bo);
	}

	@Override
	public void deleteNameListByTeamNo(String team_number) {
		daoUtil.deleteByParam(
				"com.xinchi.bean.mapper.SaleOrderNameListMapper.deleteByTeamNumber",
				team_number);

	}

	@Override
	public void deleteOrderSupplierByTeamNumber(String team_number) {
		daoUtil.deleteByParam(
				"com.xinchi.bean.mapper.BudgetOrderSupplierMapper.deleteByTeamNumber",
				team_number);
	}

	@Override
	public void updateBudgetOrder(BudgetOrderBean order) {
		daoUtil.updateByPK(
				"com.xinchi.bean.mapper.BudgetOrderMapper.updateByPrimaryKey",
				order);
	}

	@Override
	public void saveReceivableDetail(ClientReceivedDetailBean detail) {
		daoUtil.insertBO(
				"com.xinchi.bean.mapper.ClientReceivedDetailMapper.insert",
				detail);

	}

	@Override
	public List<ClientReceivedDetailBean> searchReceivableDetails(
			String team_number) {

		return daoUtil
				.selectByParam(
						"com.xinchi.bean.mapper.ClientReceivedDetailMapper.searchReceivableDetailsByTeamNumber",
						team_number);
	}

	@Override
	public void deleteReceivableDetail(String detail_pk) {
		daoUtil.deleteByPK(
				"com.xinchi.bean.mapper.ClientReceivedDetailMapper.deleteByPrimaryKey",
				detail_pk);
	}

	@Override
	public BudgetOrderBean selectBudgetOrderByTeamNumber(String team_number) {
		return daoUtil
				.selectOneValueByParam(
						"com.xinchi.bean.mapper.BudgetOrderMapper.selectBudgetOrderByTeamNumber",
						team_number);
	}

	@Override
	public ClientReceivedDetailBean selectClientReceivedDetailByPk(
			String detail_pk) {
		return (ClientReceivedDetailBean) daoUtil
				.selectByPK(
						"com.xinchi.bean.mapper.ClientReceivedDetailMapper.selectByPrimaryKey",
						detail_pk);
	}

	@Override
	public List<BudgetOrderBean> selectAllByPage(Page<BudgetOrderBean> page) {
		return daoUtil.selectByParam(
				"com.xinchi.bean.mapper.BudgetOrderMapper.selectByPage", page);
	}

}