package com.xinchi.backend.supplier.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.supplier.dao.SupplierDAO;
import com.xinchi.bean.SupplierBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SupplierDAOImpl extends SqlSessionDaoSupport implements SupplierDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(SupplierBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.SupplierMapper.insert", bo);
	}

	@Override
	public void update(SupplierBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.SupplierMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.SupplierMapper.deleteByPrimaryKey", id);
	}

	@Override
	public SupplierBean selectByPrimaryKey(String id) {
		return (SupplierBean) daoUtil.selectByPK("com.xinchi.bean.mapper.SupplierMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<SupplierBean> getAllByParam(SupplierBean bo) {
		List<SupplierBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.SupplierMapper.selectByParam", bo);
		return list;
	}

	@Override
	public List<SupplierBean> getAllByPage(Page<SupplierBean> page) {
		List<SupplierBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.SupplierMapper.selectByPage", page);
		return list;
	}

	@Override
	public List<SupplierBean> selectByPks(List<String> supplier_pks) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.SupplierMapper.selectByPks", supplier_pks);
	}

}