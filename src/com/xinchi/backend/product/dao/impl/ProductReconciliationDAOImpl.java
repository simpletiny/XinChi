package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductReconciliationDAO;
import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.ProductReconciliationBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductReconciliationDAOImpl extends SqlSessionDaoSupport implements ProductReconciliationDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductReconciliationBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductReconciliationMapper.insert", bean);
	}

	@Override
	public void update(ProductReconciliationBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductReconciliationMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductReconciliationMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductReconciliationBean selectByPrimaryKey(String id) {
		return (ProductReconciliationBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductReconciliationMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductReconciliationBean> selectByParam(ProductReconciliationBean bean) {
		List<ProductReconciliationBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductReconciliationMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductReconciliationBean> selectSumReconciliation(AirServiceFeeDto bean) {
		List<ProductReconciliationBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductReconciliationMapper.selectSumReconciliation", bean);
		return list;
	}

	@Override
	public List<ProductReconciliationBean> selectByPage(Page<ProductReconciliationBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductReconciliationMapper.selectByPage", page);
	}

}