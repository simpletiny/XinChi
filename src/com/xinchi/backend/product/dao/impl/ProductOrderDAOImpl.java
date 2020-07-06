package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderDAO;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductOrderDAOImpl extends SqlSessionDaoSupport implements ProductOrderDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductOrderBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderBean selectByPrimaryKey(String id) {
		return (ProductOrderBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductOrderMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<ProductOrderBean> selectByParam(ProductOrderBean bean) {
		List<ProductOrderBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderMapper.selectByParam",
				bean);
		return list;
	}

	@Override
	public List<ProductOrderBean> selectByPage(Page<ProductOrderBean> page) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderMapper.selectByPage", page);
	}

	@Override
	public ProductOrderBean selectByOrderNumber(String order_number) {
		return (ProductOrderBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductOrderMapper.selectByOrderNumber",
				order_number);
	}

}