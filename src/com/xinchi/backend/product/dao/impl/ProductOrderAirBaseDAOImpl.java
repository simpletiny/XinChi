package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderAirBaseDAO;
import com.xinchi.bean.ProductOrderAirBaseBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderAirBaseDAOImpl extends SqlSessionDaoSupport implements ProductOrderAirBaseDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(ProductOrderAirBaseBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderAirBaseMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderAirBaseBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderAirBaseMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderAirBaseMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderAirBaseBean selectByPrimaryKey(String id) {
		return (ProductOrderAirBaseBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductOrderAirBaseMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductOrderAirBaseBean> selectByParam(ProductOrderAirBaseBean bean) {
		List<ProductOrderAirBaseBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderAirBaseMapper.selectByParam", bean);
		return list;
	}

	@Override
	public ProductOrderAirBaseBean selectByTeamNumber(String team_number) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ProductOrderAirBaseMapper.selectByTeamNumber",
				team_number);
	}

}