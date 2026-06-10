package com.xinchi.backend.product.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderSupplierSaleOrderNameInfoDAO;
import com.xinchi.bean.OrderSupplierSaleOrderNameInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderSupplierSaleOrderNameInfoDAOImpl extends SqlSessionDaoSupport
		implements ProductOrderSupplierSaleOrderNameInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(OrderSupplierSaleOrderNameInfoBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.OrderSupplierSaleOrderNameInfoMapper.insert", bean);
	}

	@Override
	public void update(OrderSupplierSaleOrderNameInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderNameInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderNameInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public OrderSupplierSaleOrderNameInfoBean selectByPrimaryKey(String id) {
		return (OrderSupplierSaleOrderNameInfoBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderNameInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public void deleteByBasePk(String base_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.OrderSupplierSaleOrderNameInfoMapper.deleteByBasePk", base_pk);
	}
}