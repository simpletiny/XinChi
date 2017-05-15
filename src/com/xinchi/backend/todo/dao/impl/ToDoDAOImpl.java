package com.xinchi.backend.todo.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.todo.dao.ToDoDAO;
import com.xinchi.bean.TodoListBean;
import com.xinchi.common.DaoUtil;

@Repository
public class ToDoDAOImpl extends SqlSessionDaoSupport implements ToDoDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(TodoListBean todo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.TodoListMapper.insert", todo);
	}

}
