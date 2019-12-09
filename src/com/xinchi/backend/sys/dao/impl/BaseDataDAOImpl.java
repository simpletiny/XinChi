package com.xinchi.backend.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.common.DaoUtil;

@Repository
public class BaseDataDAOImpl extends SqlSessionDaoSupport implements BaseDataDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(BaseDataBean base) {
		daoUtil.insertBO("com.xinchi.bean.mapper.BaseDataMapper.insert", base);
	}

	@Override
	public BaseDataBean selectByPk(String pk) {
		return (BaseDataBean) daoUtil.selectByPK("com.xinchi.bean.mapper.BaseDataMapper.selectByPrimaryKey", pk);
	}

	@Override
	public void update(BaseDataBean base) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.BaseDataMapper.updateByPrimaryKey", base);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.BaseDataMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public List<BaseDataBean> selectByParam(BaseDataBean base) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.BaseDataMapper.selectByParam", base);
	}

	@Override
	public List<BaseDataBean> selectByFatherLevelPk(String father_level_pk) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.BaseDataMapper.selectByFatherLevelPk", father_level_pk);
	}

	@Override
	public void sysUpdateByPK(BaseDataBean cleanDay) {
		daoUtil.sysUpdateByPK("com.xinchi.bean.mapper.BaseDataMapper.updateByPrimaryKey", cleanDay);
	}

}
