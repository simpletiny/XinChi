package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.ProductRuleViewDAO;
import com.xinchi.bean.ProductRuleViewBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductRuleViewDAOImpl extends SqlSessionDaoSupport implements ProductRuleViewDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductRuleViewBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductRuleViewMapper.insert", view);
	}

	@Override
	public List<ProductRuleViewBean> getAllViewsByPage(Page<ProductRuleViewBean> page) {
		List<ProductRuleViewBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ProductRuleViewMapper.selectByPage", page);
		return list;
	}

	@Override
	public ProductRuleViewBean selectByPk(String view_pk) {
		return (ProductRuleViewBean) daoUtil.selectByPK("com.xinchi.bean.mapper.ProductRuleViewMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(ProductRuleViewBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductRuleViewMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductRuleViewMapper.deleteByPrimaryKey", view_pk);
	}
}
