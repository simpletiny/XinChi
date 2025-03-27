package com.xinchi.backend.user.dao;

import java.util.List;

import com.xinchi.bean.AssistantManagerBean;

public interface AssistantManagerDAO {
	public void insert(AssistantManagerBean bo);

	public void update(AssistantManagerBean bo);

	public void delete(String id);

	public AssistantManagerBean selectByPrimaryKey(String pk);

	public List<AssistantManagerBean> selectByParam(AssistantManagerBean option);

	public void deleteByAssistantNumber(String user_number);

	public List<AssistantManagerBean> selectByAssistantNumber(String assistant_number);

}