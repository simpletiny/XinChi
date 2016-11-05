package com.xinchi.backend.supplier.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.supplier.dao.SupplierEmployeeDAO;
import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SupplierEmployeeDAOImpl extends SqlSessionDaoSupport implements
		SupplierEmployeeDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(com.xinchi.bean.SupplierEmployeeBean bo) {
		daoUtil.insertBO(
				"com.xinchi.bean.mapper.SupplierEmployeeMapper.insert", bo);
	}

	@Override
	public void update(com.xinchi.bean.SupplierEmployeeBean bo) {
		daoUtil.updateByPK(
				"com.xinchi.bean.mapper.SupplierEmployeeMapper.updateByPrimaryKey",
				bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK(
				"com.xinchi.bean.mapper.SupplierEmployeeMapper.deleteByPrimaryKey",
				id);
	}

	@Override
	public com.xinchi.bean.SupplierEmployeeBean selectByPrimaryKey(String id) {
		return (com.xinchi.bean.SupplierEmployeeBean) daoUtil
				.selectByPK(
						"com.xinchi.bean.mapper.SupplierEmployeeMapper.selectByPrimaryKey",
						id);
	}

	@Override
	public List<com.xinchi.bean.SupplierEmployeeBean> getAllByParam(
			com.xinchi.bean.SupplierEmployeeBean bo) {
		List<com.xinchi.bean.SupplierEmployeeBean> list = daoUtil
				.selectByBOParamT(
						"com.xinchi.bean.mapper.SupplierEmployeeMapper.selectByParam",
						bo);
		return list;
	}

	@Override
	public List<SupplierEmployeeBean> getAllByPage(
			Page<SupplierEmployeeBean> page) {
		List<com.xinchi.bean.SupplierEmployeeBean> list = daoUtil
				.selectByParam(
						"com.xinchi.bean.mapper.SupplierEmployeeMapper.selectByPage",
						page);
		return list;
	}

}