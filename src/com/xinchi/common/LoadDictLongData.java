package com.xinchi.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.TaskBean;
import com.xinchi.mybatis.param.MapParam;
import com.xinchi.sys.xinchitask.dao.XinChiTaskDAO;

@Repository
// 交给Spring管理，如果不是自动扫描加载bean的方式，则在xml里配一个即可
public class LoadDictLongData implements ApplicationListener {
	@Autowired
	private XinChiTaskDAO taskDao;
	@Autowired
	private UserDAO userDao;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		TaskBean task = new TaskBean();
		task.setIsdone("N");
		ResourcesConstants.ARRAY_TASK = taskDao.getAllByParam(task);
		
		// 加载用户map
		MapParam param = new MapParam("user_number", "user_name");
		ResourcesConstants.MAP_USER_NO = userDao.getUserMap(param);
		
		param = new MapParam("user_name", "user_number");
		ResourcesConstants.MAP_USER_NAME = userDao.getUserMap(param);
	}
}
