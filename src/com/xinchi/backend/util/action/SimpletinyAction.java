package com.xinchi.backend.util.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.SimpletinyString;

@Controller
@Scope("prototype")
public class SimpletinyAction extends BaseAction {

	private static final long serialVersionUID = 5764501280171459444L;

	@Autowired
	private UserService userService;

	public String changeAllPasswordToMD5() {
		List<UserBaseBean> users = new ArrayList<UserBaseBean>();
		users = userService.getAllByParam(null);
		for(UserBaseBean user:users){
			String password = SimpletinyString.MD5(user.getPassword());
			user.setPassword(password);
			
			userService.update(user);
		}
		
		return SUCCESS;
	}

}
