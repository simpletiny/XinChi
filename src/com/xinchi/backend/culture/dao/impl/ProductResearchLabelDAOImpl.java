package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.ProductResearchLabelDAO;
import com.xinchi.bean.ProductResearchLabelBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ProductResearchLabelDAOImpl extends SqlSessionDaoSupport implements ProductResearchLabelDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductResearchLabelBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductResearchLabelMapper.insert", bean);
	}

	@Override
	public List<ProductResearchLabelBean> selectByParam(ProductResearchLabelBean bean) {
		List<ProductResearchLabelBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductResearchLabelMapper.selectByParam", bean);
		return list;
	}

	@Override
	public ProductResearchLabelBean selectByPk(String pk) {
		return (ProductResearchLabelBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductResearchLabelMapper.selectByPrimaryKey", pk);
	}

	@Override
	public void update(ProductResearchLabelBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductResearchLabelMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductResearchLabelMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public void deleteByName(String label_name) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductResearchLabelMapper.deleteByName", label_name);
	}
}
