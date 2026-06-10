package com.xinchi.backend.user.service;

import java.util.List;

import com.xinchi.bean.AssistantManagerBean;
import com.xinchi.common.BaseService;

public interface AssistantManagerService extends BaseService {

	public List<AssistantManagerBean> selectByParam(AssistantManagerBean option);

}