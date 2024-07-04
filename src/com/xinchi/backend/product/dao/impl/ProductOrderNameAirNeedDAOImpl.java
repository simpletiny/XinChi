package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderNameAirNeedDAO;
import com.xinchi.bean.ProductOrderNameAirNeedBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderNameAirNeedDAOImpl extends SqlSessionDaoSupport implements ProductOrderNameAirNeedDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductOrderNameAirNeedBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderNameAirNeedBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderNameAirNeedBean selectByPrimaryKey(String id) {
		return (ProductOrderNameAirNeedBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductOrderNameAirNeedBean> selectByParam(ProductOrderNameAirNeedBean bean) {
		List<ProductOrderNameAirNeedBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductOrderNameAirNeedBean> selectByNamePk(String name_pk) {
		List<ProductOrderNameAirNeedBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.selectByNamePk", name_pk);
		return list;

	}

	@Override
	public List<ProductOrderNameAirNeedBean> selectByNamePks(List<String> name_pks) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.selectByNamePks", name_pks);
	}

	@Override
	public void deleteByNeedPk(String need_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ProductOrderNameAirNeedMapper.deleteByNeedPk", need_pk);
	}

}