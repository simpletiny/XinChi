package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderNameDAO;
import com.xinchi.bean.ProductOrderNameBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductOrderNameDAOImpl extends SqlSessionDaoSupport implements ProductOrderNameDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductOrderNameBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderNameMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderNameBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderNameMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderNameMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderNameBean selectByPrimaryKey(String id) {
		return (ProductOrderNameBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductOrderNameMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductOrderNameBean> selectByParam(ProductOrderNameBean bean) {
		List<ProductOrderNameBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderNameMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductOrderNameBean> selectByPage(Page<ProductOrderNameBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderNameMapper.selectByPage", page);
	}

	@Override
	public List<ProductOrderNameBean> selectByPks(List<String> name_pks) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderNameMapper.selectByPks", name_pks);
	}

	@Override
	public List<ProductOrderNameBean> selectByNeedPk(String need_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderNameMapper.selectByNeedPk", need_pk);
	}

	@Override
	public List<ProductOrderNameBean> selectByTeamNumber(String team_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderNameMapper.selectByTeamNumber", team_number);
	}
}