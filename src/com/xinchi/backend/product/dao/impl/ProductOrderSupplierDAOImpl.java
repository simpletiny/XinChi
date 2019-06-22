package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderSupplierDAO;
import com.xinchi.bean.OrderSupplierBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderSupplierDAOImpl extends SqlSessionDaoSupport implements ProductOrderSupplierDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(OrderSupplierBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.OrderSupplierMapper.insert", bean);
	}

	@Override
	public void update(OrderSupplierBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.OrderSupplierMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.OrderSupplierMapper.deleteByPrimaryKey", id);
	}

	@Override
	public OrderSupplierBean selectByPrimaryKey(String id) {
		return (OrderSupplierBean) daoUtil.selectByPK("com.xinchi.bean.mapper.OrderSupplierMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<OrderSupplierBean> selectByParam(OrderSupplierBean bean) {
		List<OrderSupplierBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.OrderSupplierMapper.selectByParam",
				bean);
		return list;
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.OrderSupplierMapper.deleteByProductPk", product_pk);
	}

	@Override
	public List<OrderSupplierBean> selectByProductPk(String product_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderSupplierMapper.selectByProductPk", product_pk);

	}

	@Override
	public List<OrderSupplierBean> selectByOrderPk(String order_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.OrderSupplierMapper.selectByOrderPk", order_pk);
	}

	@Override
	public void deleteByOrderPk(String order_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.OrderSupplierMapper.deleteByOrderPk", order_pk);
	}

}