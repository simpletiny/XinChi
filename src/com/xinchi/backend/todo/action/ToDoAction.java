package com.xinchi.backend.todo.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.todo.service.ToDoService;
import com.xinchi.bean.TodoListBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ToDoAction extends BaseAction {
	private static final long serialVersionUID = 702705021386812447L;

	@Autowired
	private ToDoService service;
	
	private TodoListBean todo;

	public String createToDo() {
		resultStr = service.insert(todo);
		return SUCCESS;
	}

	public String updateToDo() {

		return SUCCESS;
	}

	public String doneToDo() {

		return SUCCESS;
	}

	public TodoListBean getTodo() {
		return todo;
	}

	public void setTodo(TodoListBean todo) {
		this.todo = todo;
	}
}
