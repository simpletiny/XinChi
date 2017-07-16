package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductGroupDAO;
import com.xinchi.bean.ProductGroupBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductGroupDAOImpl extends SqlSessionDaoSupport implements ProductGroupDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(ProductGroupBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.ProductGroupMapper.insert", bean);
	}

	@Override
	public void update(ProductGroupBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductGroupMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductGroupMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductGroupBean selectByPrimaryKey(String id) {
		return (ProductGroupBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductGroupMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductGroupBean> selectByParam(ProductGroupBean bean) {
		List<ProductGroupBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.ProductGroupMapper.selectByParam", bean);
		return list;
	}

	public List<ProductGroupBean> selectByPage(Page page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductGroupMapper.selectByPage", page);
	}
}