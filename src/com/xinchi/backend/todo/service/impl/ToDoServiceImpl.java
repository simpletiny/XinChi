package com.xinchi.backend.todo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.todo.dao.ToDoDAO;
import com.xinchi.backend.todo.service.ToDoService;
import com.xinchi.bean.TodoListBean;

@Service
@Transactional
public class ToDoServiceImpl implements ToDoService {

	@Autowired
	private ToDoDAO dao;

	@Override
	public String insert(TodoListBean todo) {
		dao.insert(todo);
		return "OK";
	}

}
