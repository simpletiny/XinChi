package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.PreciseEmployeeDAO;
import com.xinchi.bean.PreciseClientEmployeeBean;
import com.xinchi.bean.PreciseEmployeeBindingBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class PreciseEmployeeDAOImpl extends SqlSessionDaoSupport implements PreciseEmployeeDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PreciseClientEmployeeBean bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PreciseClientEmployeeMapper.insert", bean);
	}

	@Override
	public void update(PreciseClientEmployeeBean bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PreciseClientEmployeeMapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PreciseClientEmployeeMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public PreciseClientEmployeeBean selectByPrimaryKey(String pk) {
		return (PreciseClientEmployeeBean) daoUtil.selectByPK("com.xinchi.bean.mapper.PreciseClientEmployeeMapper.selectByPrimaryKey", pk);
	}

	@Override
	public List<PreciseClientEmployeeBean> selectByParam(PreciseClientEmployeeBean option) {
		List<PreciseClientEmployeeBean> list = daoUtil.selectByParam("com.xinchi.bean.mapper.PreciseClientEmployeeMapper.selectByParam", option);
		return list;
	}

	@Override
	public List<PreciseClientEmployeeBean> selectByPage(Page<PreciseClientEmployeeBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.PreciseClientEmployeeMapper.selectByPage", page);
	}

	@Override
	public int deleteBindingByEmployeePk(String employee_pk) {
		return daoUtil.deleteByParam("com.xinchi.bean.mapper.PreciseEmployeeBindingMapper.deleteByEmployeePk", employee_pk);
	}

	@Override
	public void insertBinding(PreciseEmployeeBindingBean peb) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PreciseEmployeeBindingMapper.insert", peb);
	}

	@Override
	public List<PreciseEmployeeBindingBean> selectBindingsByPrecisePk(String precise_pk) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.PreciseEmployeeBindingMapper.selectByPrecisePk", precise_pk);
	}

	@Override
	public void deleteBindingByPrecisePk(String precise_pk) {
		daoUtil.deleteByParam("com.xinchi.bean.mapper.PreciseEmployeeBindingMapper.deleteByPrecisePk", precise_pk);
	}

}