package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductAirTicketDAO;
import com.xinchi.bean.ProductAirTicketBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductAirTicketDAOImpl extends SqlSessionDaoSupport implements ProductAirTicketDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductAirTicketBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductAirTicketMapper.insert", bean);
	}

	@Override
	public void update(ProductAirTicketBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductAirTicketMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductAirTicketMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductAirTicketBean selectByPrimaryKey(String id) {
		return (ProductAirTicketBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductAirTicketMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductAirTicketBean> selectByParam(ProductAirTicketBean bean) {
		List<ProductAirTicketBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductAirTicketMapper.selectByParam", bean);
		return list;
	}

	@Override
	public void deleteByProduct_pk(String product_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ProductAirTicketMapper.deleteByProductPk", product_pk);
	}

	@Override
	public List<ProductAirTicketBean> selectByProductPk(String product_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductAirTicketMapper.selectByProductPk", product_pk);
	}

}