package com.xinchi.backend.user.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.user.dao.AssistantManagerDAO;
import com.xinchi.bean.AssistantManagerBean;
import com.xinchi.common.DaoUtil;

@Repository
public class AssistantManagerDAOImpl extends SqlSessionDaoSupport implements AssistantManagerDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(AssistantManagerBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.AssistantManagerMapper.insert", bo);
	}

	@Override
	public void update(AssistantManagerBean bo) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.AssistantManagerMapper.updateByPrimaryKey", bo);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AssistantManagerMapper.deleteByPrimaryKey", id);
	}

	@Override
	public AssistantManagerBean selectByPrimaryKey(String id) {
		return (AssistantManagerBean) daoUtil
				.selectByPK("com.xinchi.bean.mapper.AssistantManagerMapper.selectByPrimaryKey", id);
	}

	@Override
	public List<AssistantManagerBean> selectByParam(AssistantManagerBean option) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.AssistantManagerMapper.selectByParam", option);
	}

	@Override
	public void deleteByAssistantNumber(String user_number) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.AssistantManagerMapper.deleteByAssistantNumber", user_number);
	}

	@Override
	public List<AssistantManagerBean> selectByAssistantNumber(String assistant_number) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.AssistantManagerMapper.selectByAssistantNumber",
				assistant_number);

	}
}