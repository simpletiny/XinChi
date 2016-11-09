package com.xinchi.sys.xinchitask.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.xinchi.bean.TaskBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.sys.xinchitask.dao.XinChiTaskDAO;

@Repository
@Scope("prototype")
public class XinChiTaskDAOImpl extends SqlSessionDaoSupport implements XinChiTaskDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(TaskBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TaskMapper.insert", bo);
	}

	@Override
	public void update(TaskBean bo) {
		daoUtil.sysUpdateByPK("com.xinchi.bean.mapper.TaskMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.TaskMapper.deleteByPrimaryKey", id);
	}

	@Override
	public TaskBean selectByPrimaryKey(String id) {
		return (TaskBean) daoUtil.selectByPK("com.xinchi.bean.mapper.TaskMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<TaskBean> getAllByParam(TaskBean bo) {
		List<TaskBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.TaskMapper.selectByParam", bo);
		return list;
	}

}