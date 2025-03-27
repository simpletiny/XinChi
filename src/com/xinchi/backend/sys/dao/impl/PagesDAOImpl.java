package com.xinchi.backend.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sys.dao.PagesDAO;
import com.xinchi.bean.PagesBean;
import com.xinchi.common.DaoUtil;

@Repository
public class PagesDAOImpl extends SqlSessionDaoSupport implements PagesDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PagesBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PagesMapper.insert", bean);
	}

	@Override
	public void update(PagesBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PagesMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PagesMapper.deleteByPrimaryKey", id);
	}

	@Override
	public PagesBean selectByPrimaryKey(String id) {
		return (PagesBean) daoUtil.selectByPK("com.xinchi.bean.mapper.PagesMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<PagesBean> selectByParam(PagesBean bean) {
		List<PagesBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.PagesMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<PagesBean> selectByRoles(PagesBean bean) {
		List<PagesBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.PagesMapper.selectByRoles", bean);
		return list;
	}

	@Override
	public void batchUpdate(List<PagesBean> list) {
		daoUtil.updateBOList("com.xinchi.bean.mapper.PagesMapper.updateByPrimaryKey", list);

	}

}