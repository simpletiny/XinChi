package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.MobileTouchDAO;
import com.xinchi.bean.MobileTouchBean;
import com.xinchi.common.DaoUtil;

@Repository
public class MobileTouchDAOImpl extends SqlSessionDaoSupport implements MobileTouchDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(MobileTouchBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.MobileTouchMapper.insert", bean);
	}

	@Override
	public void update(MobileTouchBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.MobileTouchMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.MobileTouchMapper.deleteByPrimaryKey", id);
	}

	@Override
	public MobileTouchBean selectByPrimaryKey(String id) {
		return (MobileTouchBean) daoUtil.selectByPK("com.xinchi.bean.mapper.MobileTouchMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<MobileTouchBean> selectByParam(MobileTouchBean bean) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.MobileTouchMapper.selectByParam", bean);
	}

	@Override
	public String selectMaxTouchDateByEmployeePk(String employee_pk) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.MobileTouchMapper.selectMaxTouchDateByEmployeePk",
				employee_pk);
	}

}