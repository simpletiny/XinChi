package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderTeamNumberDAO;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderTeamNumberDAOImpl extends SqlSessionDaoSupport implements ProductOrderTeamNumberDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(ProductOrderTeamNumberBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderTeamNumberBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderTeamNumberBean selectByPrimaryKey(String id) {
		return (ProductOrderTeamNumberBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductOrderTeamNumberBean> selectByParam(ProductOrderTeamNumberBean bean) {
		List<ProductOrderTeamNumberBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductOrderTeamNumberBean> selectByOrderNumber(String order_number) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.selectByOrderNumber",
				order_number);
	}

	@Override
	public List<String> selectTeamNumbersByOrderNumber(String order_number) {
		return daoUtil.selectListByParam(
				"com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.selectTeamNumbersByOrderNumber", order_number);
	}

	@Override
	public void deleteByOrderNumber(String order_number) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderTeamNumberMapper.deleteByOrderNumber", order_number);
	}

}