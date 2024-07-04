package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderNameFlightSegmentDAO;
import com.xinchi.bean.ProductOrderNameFlightSegmentBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductOrderNameFlightSegmentDAOImpl extends SqlSessionDaoSupport
		implements ProductOrderNameFlightSegmentDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductOrderNameFlightSegmentBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderNameFlightSegmentMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderNameFlightSegmentBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderNameFlightSegmentMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderNameFlightSegmentMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderNameFlightSegmentBean selectByPrimaryKey(String id) {
		return (ProductOrderNameFlightSegmentBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductOrderNameFlightSegmentMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductOrderNameFlightSegmentBean> selectByParam(ProductOrderNameFlightSegmentBean bean) {
		List<ProductOrderNameFlightSegmentBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderNameFlightSegmentMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductOrderNameFlightSegmentBean> selectByNeedPk(String need_pk) {
		List<ProductOrderNameFlightSegmentBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderNameFlightSegmentMapper.selectByNeedPk", need_pk);
		return list;
	}

	@Override
	public void deleteByNeedPk(String need_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ProductOrderNameFlightSegmentMapper.deleteByNeedPk", need_pk);
	}

}