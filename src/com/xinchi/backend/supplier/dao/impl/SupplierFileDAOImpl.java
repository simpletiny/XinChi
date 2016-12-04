package com.xinchi.backend.supplier.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.supplier.dao.SupplierFileDAO;
import com.xinchi.bean.SupplierFileBean;
import com.xinchi.common.DaoUtil;

@Repository
public class SupplierFileDAOImpl extends SqlSessionDaoSupport implements
		SupplierFileDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SupplierFileBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SupplierFileMapper.insert", bo);
	}

	@Override
	public void update(SupplierFileBean bo) {
		daoUtil.updateByPK(
				"com.xinchi.bean.mapper.SupplierFileMapper.updateByPrimaryKey",
				bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK(
				"com.xinchi.bean.mapper.SupplierFileMapper.deleteByPrimaryKey",
				id);
	}

	@Override
	public List<SupplierFileBean> selectSupplierFilesBySupplierPk(
			String supplier_pk) {

		return daoUtil
				.selectByParam(
						"com.xinchi.bean.mapper.SupplierFileMapper.selectSupplierFilesBySupplierPk",
						supplier_pk);
	}

	@Override
	public List<SupplierFileBean> selectByParam(SupplierFileBean bo) {
		return daoUtil.selectByParam(
				"com.xinchi.bean.mapper.SupplierFileMapper.selectByParam", bo);
	}

	@Override
	public void deleteFileByParam(SupplierFileBean sfb) {
		daoUtil.deleteByBOParam(
				"com.xinchi.bean.mapper.SupplierFileMapper.deleteByParam", sfb);
	}
}