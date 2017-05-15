package com.xinchi.backend.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sys.dao.SystemGuideDAO;
import com.xinchi.bean.SystemGuideBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SystemGuideDAOImpl extends SqlSessionDaoSupport implements SystemGuideDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SystemGuideBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SystemGuideMapper.insert", view);
	}

	@Override
	public List<SystemGuideBean> getAllViewsByPage(Page page) {
		List<SystemGuideBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.SystemGuideMapper.selectByPage", page);
		return list;
	}

	@Override
	public SystemGuideBean selectByPk(String view_pk) {
		return (SystemGuideBean) daoUtil.selectByPK("com.xinchi.bean.mapper.SystemGuideMapper.selectByPrimaryKey", view_pk);
	}

	@Override
	public void update(SystemGuideBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SystemGuideMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SystemGuideMapper.deleteByPrimaryKey", view_pk);
	}
}
