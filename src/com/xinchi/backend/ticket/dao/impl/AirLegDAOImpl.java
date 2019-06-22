package com.xinchi.backend.ticket.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.ticket.dao.AirLegDAO;
import com.xinchi.bean.AirLegBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class AirLegDAOImpl extends SqlSessionDaoSupport implements AirLegDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public String insert(AirLegBean bean) {
		return daoUtil.insertBO("com.xinchi.bean.mapper.AirLegMapper.insert", bean);
	}

	@Override
	public void update(AirLegBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AirLegMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AirLegMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AirLegBean selectByPrimaryKey(String id) {
		return (AirLegBean) daoUtil.selectByPK("com.xinchi.bean.mapper.AirLegMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AirLegBean> selectByParam(AirLegBean bean) {
		List<AirLegBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.AirLegMapper.selectByParam", bean);
		return list;
	}

	@Override
	public List<AirLegBean> selectByPage(Page<AirLegBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AirLegMapper.selectByPage", page);
	}

}