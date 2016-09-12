package com.xinchi.sys.xinchitask.service.impl;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.bean.TaskBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.sys.xinchitask.dao.XinChiTaskDAO;
import com.xinchi.sys.xinchitask.service.XinChiTaskService;

@Service
public class XinChiTaskServiceImpl implements XinChiTaskService {
	@Autowired
	private XinChiTaskDAO taskDao;

	/**
	 * 创建任务
	 * 
	 * @param task
	 * @return
	 */
	@Override
	@Transactional
	public String addTask(TaskBean task) {
		Iterator<TaskBean> iter = ResourcesConstants.ARRAY_TASK.iterator();
		while (iter.hasNext()) {
			TaskBean bo = iter.next();
			if (bo.getExecutor().equals(task.getExecutor()) && bo.getParameters().equals(task.getParameters())
					&& bo.getMethod().equals(task.getMethod())) {
				iter.remove();
				bo.setIsdone("Y");
				taskDao.update(bo);
				break;
			}
		}
		ResourcesConstants.ARRAY_TASK.add(task);
		taskDao.insert(task);
		return "success";
	}

	@Override
	@Transactional
	public String removeTask(TaskBean task) {
		Iterator<TaskBean> iter = ResourcesConstants.ARRAY_TASK.iterator();
		while (iter.hasNext()) {
			TaskBean bo = iter.next();
			if (bo.getExecutor().equals(task.getExecutor()) && bo.getParameters().equals(task.getParameters())
					&& bo.getMethod().equals(task.getMethod())) {
				iter.remove();
				taskDao.delete(bo.getPk());
				break;
			}
		}
		return "success";
	}

	/**
	 * 更新任务
	 * 
	 * @param task
	 * @return
	 */
	@Override
	public String updateTask(TaskBean task) {

		return "";
	}
}
