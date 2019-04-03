package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderSupplierInfoDAO;
import com.xinchi.bean.OrderSupplierInfoBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderSupplierInfoDAOImpl extends SqlSessionDaoSupport implements ProductOrderSupplierInfoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(OrderSupplierInfoBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.OrderSupplierInfoMapper.insert", bean);
	}

	@Override
	public void update(OrderSupplierInfoBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.OrderSupplierInfoMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.OrderSupplierInfoMapper.deleteByPrimaryKey", id);
	}

	@Override
	public OrderSupplierInfoBean selectByPrimaryKey(String id) {
		return (OrderSupplierInfoBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.OrderSupplierInfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<OrderSupplierInfoBean> selectByParam(OrderSupplierInfoBean bean) {
		List<OrderSupplierInfoBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.OrderSupplierInfoMapper.selectByParam", bean);
		return list;
	}

}