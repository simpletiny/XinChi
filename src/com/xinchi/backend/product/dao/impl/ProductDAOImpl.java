package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.bean.ProductBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductDAOImpl extends SqlSessionDaoSupport implements ProductDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductMapper.insert", bean);
	}

	@Override
	public void update(ProductBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductMapper.updateByPrimaryKey", bean);
	}
	
	@Override
	public void sysUpdate(ProductBean bean) {
		daoUtil.sysUpdateByPK("com.xinchi.bean.mapper.ProductMapper.updateByPrimaryKey", bean);
	}
	
	

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductBean selectByPrimaryKey(String id) {
		return (ProductBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductBean> selectByParam(ProductBean bean) {
		List<ProductBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductBean> selectByPage(Page<ProductBean> page) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductMapper.selectByPage", page);
	}

	@Override
	public List<ProductBean> selectByPks(String[] pks) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductMapper.selectByPks", pks);
	}

}