package com.xinchi.backend.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sys.dao.DishonestLogDAO;
import com.xinchi.bean.DishonestLogBean;
import com.xinchi.common.DaoUtil;

@Repository
public class DishonestLogDAOImpl extends SqlSessionDaoSupport implements DishonestLogDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(DishonestLogBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.DishonestLogMapper.insert", bean);
	}

	@Override
	public void update(DishonestLogBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.DishonestLogMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.DishonestLogMapper.deleteByPrimaryKey", id);
	}

	@Override
	public DishonestLogBean selectByPrimaryKey(String id) {
		return (DishonestLogBean) daoUtil.selectByPK("com.xinchi.bean.mapper.DishonestLogMapper.selectByPrimaryKey",
				id);
	}

	@Override
	public List<DishonestLogBean> selectByParam(DishonestLogBean bean) {
		List<DishonestLogBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.DishonestLogMapper.selectByParam",
				bean);
		return list;
	}

	@Override
	public List<DishonestLogBean> selectByPersonId(String id) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.DishonestLogMapper.selectByPersonId", id);
	}

}