package com.xinchi.backend.product.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.product.dao.ProductOrderOperationDAO;
import com.xinchi.bean.DropOffBean;
import com.xinchi.bean.ProductOrderOperationBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class ProductOrderOperationDAOImpl extends SqlSessionDaoSupport implements ProductOrderOperationDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(ProductOrderOperationBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ProductOrderOperationMapper.insert", bean);
	}

	@Override
	public void update(ProductOrderOperationBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ProductOrderOperationMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ProductOrderOperationMapper.deleteByPrimaryKey", id);
	}

	@Override
	public ProductOrderOperationBean selectByPrimaryKey(String id) {
		return (ProductOrderOperationBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ProductOrderOperationMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<ProductOrderOperationBean> selectByParam(ProductOrderOperationBean bean) {
		List<ProductOrderOperationBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ProductOrderOperationMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<ProductOrderOperationBean> selectByPage(Page<ProductOrderOperationBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderOperationMapper.selectByPage", page);
	}

	@Override
	public List<ProductOrderOperationBean> selectByTeamNumber(String team_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ProductOrderOperationMapper.selectByTeamNumber",
				team_number);
	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.ProductOrderOperationMapper.deleteByTeamNumber", team_number);
	}

	@Override
	public List<DropOffBean> selectDropOff(DropOffBean drop_off) {
		List<DropOffBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.ViewDropOffMapper.selectByParam",
				drop_off);
		return list;
	}

}