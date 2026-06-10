package com.xinchi.backend.culture.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.culture.dao.XinChiPromiseDAO;
import com.xinchi.bean.XinChiPromiseBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class XinChiPromiseDAOImpl extends SqlSessionDaoSupport implements XinChiPromiseDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(XinChiPromiseBean view) {
		daoUtil.insertBO("com.xinchi.bean.mapper.XinChiPromiseMapper.insert", view);
	}

	@Override
	public List<XinChiPromiseBean> getAllViewsByPage(Page<XinChiPromiseBean> page) {
		List<XinChiPromiseBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.XinChiPromiseMapper.selectByPage",
				page);
		return list;
	}

	@Override
	public XinChiPromiseBean selectByPk(String view_pk) {
		return (XinChiPromiseBean) daoUtil.selectByPK("com.xinchi.bean.mapper.XinChiPromiseMapper.selectByPrimaryKey",
				view_pk);
	}

	@Override
	public void update(XinChiPromiseBean view) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.XinChiPromiseMapper.updateByPrimaryKey", view);
	}

	@Override
	public void delete(String view_pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.XinChiPromiseMapper.deleteByPrimaryKey", view_pk);
	}
}
