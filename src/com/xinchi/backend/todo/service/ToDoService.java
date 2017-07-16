package com.xinchi.backend.todo.service;

import com.xinchi.bean.TodoListBean;
import com.xinchi.common.LogDescription;

@LogDescription(ignore = true)
public interface ToDoService {

	public String insert(TodoListBean todo);

}
