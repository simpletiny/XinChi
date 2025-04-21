package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderSupplierSaleOrderDAO;
import com.xinchi.bean.OrderSupplierSaleOrderBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderSupplierSaleOrderDAOImpl extends SqlSessionDaoSupport
		implements ProductOrderSupplierSaleOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(OrderSupplierSaleOrderBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.OrderSupplierSaleOrderMapper.insert", bean);
	}

	@Override
	public void update(OrderSupplierSaleOrderBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderMapper.deleteByPrimaryKey", id);
	}

	@Override
	public OrderSupplierSaleOrderBean selectByPrimaryKey(String id) {
		return (OrderSupplierSaleOrderBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<OrderSupplierSaleOrderBean> selectByBasePk(String base_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderSupplierSaleOrderMapper.selectByBasePk", base_pk);
	}

	@Override
	public void deleteByBasePk(String base_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderMapper.deleteByBasePk", base_pk);
	}

	@Override
	public List<OrderSupplierSaleOrderBean> selectByParam(OrderSupplierSaleOrderBean option) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderSupplierSaleOrderMapper.selectByParam", option);
	}

}