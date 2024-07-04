package com.xinchi.backend.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sys.dao.DishonestPersonDAO;
import com.xinchi.bean.DishonestPersonBean;
import com.xinchi.common.DaoUtil;

@Repository
public class DishonestPersonDAOImpl extends SqlSessionDaoSupport implements DishonestPersonDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(DishonestPersonBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.DishonestPersonMapper.insert", bean);
	}

	@Override
	public void update(DishonestPersonBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.DishonestPersonMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.DishonestPersonMapper.deleteByPrimaryKey", id);
	}

	@Override
	public DishonestPersonBean selectByPrimaryKey(String id) {
		return (DishonestPersonBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.DishonestPersonMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<DishonestPersonBean> selectByParam(DishonestPersonBean bean) {
		List<DishonestPersonBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.DishonestPersonMapper.selectByParam", bean);
		return list;
	}

	@Override
	public DishonestPersonBean selectByPersonId(String id) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.DishonestPersonMapper.selectByPersonId", id);
	}

}