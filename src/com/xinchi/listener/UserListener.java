package com.xinchi.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.xinchi.common.DateUtil;
import com.xinchi.common.UserList;
import com.xinchi.common.UserSessionBean;

public class UserListener implements HttpSessionAttributeListener, ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// ApplicationContext ac =
		// WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		// userService = (IUserService) ac.getBean("userServiceImpl");
		// userOnlineTimeService = (IUserOnlineTimeService)
		// ac.getBean("userOnlineTimeServiceImpl");
	}

	private UserList userList = UserList.getInstance();

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if ((event != null) && event.getName().equals("user")) {
			UserSessionBean userFind = (UserSessionBean) event.getValue();

			if (userList.IsExist(userFind.getPk()) >= 0) {
				userList.RemoveUser(userFind);
			}
			String login_time = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
			userFind.setLogin_time(login_time);
			
			userList.addUser(userFind);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		if ((event != null) && event.getName().equals("user")) {
			UserSessionBean userFind = (UserSessionBean) event.getValue();
			userList.RemoveUser(userFind);
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {

	}

}
