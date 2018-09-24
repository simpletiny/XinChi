package com.xinchi.backend.client.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.RelationLevelDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class EmployeeDAOImpl extends SqlSessionDaoSupport implements EmployeeDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(com.xinchi.bean.ClientEmployeeBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.ClientEmployeeMapper.insert", bo);
	}

	@Override
	public void update(com.xinchi.bean.ClientEmployeeBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.ClientEmployeeMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.ClientEmployeeMapper.deleteByPrimaryKey", id);
	}

	@Override
	public com.xinchi.bean.ClientEmployeeBean selectByPrimaryKey(String id) {
		return (com.xinchi.bean.ClientEmployeeBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.ClientEmployeeMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<com.xinchi.bean.ClientEmployeeBean> getAllByParam(com.xinchi.bean.ClientEmployeeBean bo) {
		List<com.xinchi.bean.ClientEmployeeBean> list = daoUtil
				.selectByBOParamT("com.xinchi.bean.mapper.ClientEmployeeMapper.selectByParam", bo);
		return list;
	}

	@Override
	public List<ClientEmployeeBean> getAllByPage(Page<ClientEmployeeBean> page) {
		List<ClientEmployeeBean> list = daoUtil
				.selectByParam("com.xinchi.bean.mapper.ClientEmployeeMapper.selectByPage", page);
		return list;
	}

	@Override
	public List<String> getBodyPksByEmployeePks(String[] employee_pks) {
		return daoUtil.selectListByParam("com.xinchi.bean.mapper.ClientEmployeeMapper.selectBodyPksByEmployeePks",
				employee_pks);
	}

	@Override
	public void deleteClientEmployeeByPks(List<String> employee_pks) {
		daoUtil.updateByParam("com.xinchi.bean.mapper.ClientEmployeeMapper.stopEmployee", employee_pks);
	}

	@Override
	public void recoveryClientEmployeeByPks(List<String> employee_pks) {
		daoUtil.updateByParam("com.xinchi.bean.mapper.ClientEmployeeMapper.recoveryEmployee", employee_pks);
	}

	@Override
	public RelationLevelDto selectRelationCntBySales(String sales) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.ClientEmployeeMapper.selectRelationCntBySales",
				sales);
	}

	@Override
	public void publicClientEmployee(List<String> employee_pks) {
		daoUtil.updateByParam("com.xinchi.bean.mapper.ClientEmployeeMapper.publicClientEmployee", employee_pks);

	}

}