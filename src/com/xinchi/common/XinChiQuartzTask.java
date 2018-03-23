package com.xinchi.common;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.xinchi.bean.TaskBean;
import com.xinchi.sys.xinchitask.dao.XinChiTaskDAO;

/**
 * 定时执行的任务
 * 
 * @author niushixing 2015-1-19 15:38:50
 *
 */
@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class XinChiQuartzTask extends BaseAction {
	private static final long serialVersionUID = 7808764618728193840L;
	@Autowired
	private WebApplicationContext ctx;
	private static Logger logger = Logger.getLogger(XinChiQuartzTask.class);
	@Autowired
	private XinChiTaskDAO taskDao;

	// 执行任务方法
	public synchronized String execute() {
		try {
			Timestamp timeNow = new Timestamp(System.currentTimeMillis());
			Iterator<TaskBean> iter = ResourcesConstants.ARRAY_TASK.iterator();
			// 遍历task列表
			while (iter.hasNext()) {
				TaskBean task = iter.next();
				Timestamp exeTime = task.getExecuteTime();
				if (exeTime.before(timeNow)) {
					Object o = ctx.getBean(task.getExecutor());
					Class<? extends Object> c = o.getClass();
					Method m = c.getDeclaredMethod(task.getMethod(), String[].class);
					String param = task.getParameters();
					Object[] params = null;
					if (param != null && !param.equals("")) {
						params = param.split(",");
					}
					m.invoke(o, (Object) params);
					// 更新列表
					if (task.getTaskType().equals(ResourcesConstants.TASK_ONETIME)) {
						iter.remove();
						task.setIsdone("Y");
						taskDao.update(task);
					} else if (task.getTaskType().equals(ResourcesConstants.TASK_EVERYDAY)) {
						Calendar x = Calendar.getInstance();
						x.setTime(task.getExecuteTime());
						x.add(Calendar.DATE, 1);
						task.setExecuteTime(new Timestamp(x.getTimeInMillis()));
						taskDao.update(task);
					} else if (task.getTaskType().equals(ResourcesConstants.TASK_EVERYWEEK)) {
						Calendar x = Calendar.getInstance();
						x.setTime(task.getExecuteTime());
						x.add(Calendar.WEEK_OF_MONTH, 1);
						task.setExecuteTime(new Timestamp(x.getTimeInMillis()));
						taskDao.update(task);
					} else if (task.getTaskType().equals(ResourcesConstants.TASK_EVERYMONTH)) {
						Calendar x = Calendar.getInstance();
						x.setTime(task.getExecuteTime());
						x.add(Calendar.MONTH, 1);
						task.setExecuteTime(new Timestamp(x.getTimeInMillis()));
						taskDao.update(task);
					} else if (task.getTaskType().equals(ResourcesConstants.TASK_EVERYYEAR)) {
						Calendar x = Calendar.getInstance();
						x.setTime(task.getExecuteTime());
						x.add(Calendar.YEAR, 1);
						task.setExecuteTime(new Timestamp(x.getTimeInMillis()));
						taskDao.update(task);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}
}
