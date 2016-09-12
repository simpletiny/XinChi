package com.xinchi.sys.xinchitask.service;

import com.xinchi.bean.TaskBean;

/**
 * 任务Service
 * 
 * @author niushixing 2015-1-20 14:20:44
 *
 */
public interface XinChiTaskService {
	/**
	 * 添加任务
	 * 
	 * @param task
	 * @return
	 */
	public String addTask(TaskBean task);

	/**
	 * 删除任务
	 * 
	 * @param task
	 * @return
	 */
	public String removeTask(TaskBean task);

	/**
	 * 更新任务
	 * 
	 * @param task
	 * @return
	 */
	public String updateTask(TaskBean task);
}
