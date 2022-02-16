package com.xinchi.backend.order.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.order.dao.OrderTicketInfoDAO;
import com.xinchi.bean.SaleOrderTicketInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class OrderTicketInfoDAOImpl extends SqlSessionDaoSupport implements OrderTicketInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SaleOrderTicketInfoBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SaleOrderTicketInfoMapper.insert", bean);
	}

	@Override
	public void update(SaleOrderTicketInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SaleOrderTicketInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SaleOrderTicketInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public SaleOrderTicketInfoBean selectByPrimaryKey(String id) {
		return (SaleOrderTicketInfoBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.SaleOrderTicketInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<SaleOrderTicketInfoBean> selectByParam(SaleOrderTicketInfoBean bean) {
		List<SaleOrderTicketInfoBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.SaleOrderTicketInfoMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<SaleOrderTicketInfoBean> selectByOrderPk(String order_pk) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.SaleOrderTicketInfoMapper.selectByOrderPk", order_pk);
	}

	@Override
	public void deleteByOrderPk(String order_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SaleOrderTicketInfoMapper.deleteByOrderPk", order_pk);
	}

}