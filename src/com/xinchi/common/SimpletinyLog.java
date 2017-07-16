package com.xinchi.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.sf.json.JSONObject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinchi.backend.user.service.UserLogService;
import com.xinchi.bean.UserLogBean;

public class SimpletinyLog {
	@Autowired
	private UserLogService service;

	public void after(JoinPoint jp) {
		Signature st = jp.getSignature();
		// 方法名
		String method = st.getName();
		// 类名
		String class_name = st.getDeclaringTypeName();
		String class_des = "";

		for (Annotation anno : st.getDeclaringType().getDeclaredAnnotations()) {
			if (anno.annotationType().equals(LogDescription.class)) {
				if (((LogDescription) anno).ignore())
					return;
				class_des = ((LogDescription) anno).des();
			}
		}

		Method[] meths = st.getDeclaringType().getMethods();

		Method meth = null;

		for (Method m : meths) {
			if (m.getName().equals(method)) {
				meth = m;
				break;
			}
		}
		String method_des = "";
		for (Annotation anno : meth.getDeclaredAnnotations()) {
			if (anno.annotationType().equals(LogDescription.class)) {
				if (((LogDescription) anno).ignore())
					return;
				method_des = ((LogDescription) anno).des();
			}

		}

		// 代理类名
		String target = jp.getTarget().getClass().getName();
		// 参数
		String parameter = "";
		// 获取传入目标方法的参数
		Object[] args = jp.getArgs();
		for (int i = 0; i < args.length; i++) {
			if (null == args[i])
				continue;
			if (args[i].getClass().isInstance(SupperBO.class)) {
				JSONObject jsonObj = JSONObject.fromObject(args[i]);
				parameter += jsonObj.toString() + "$$";
			} else {
				parameter += args[i] + "$$";
			}
		}

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		// 操作用户编号
		String user_number = sessionBean.getUser_number();
		if (user_number.equals("N00000"))
			return;

		// 操作时间
		String time = DateUtil.getTimeStr();

		UserLogBean userLog = new UserLogBean();
		userLog.setMethod(method);
		userLog.setMethod_des(method_des);
		userLog.setClass_name(class_name);
		userLog.setClass_des(class_des);
		userLog.setTarget(target);
		userLog.setParameter(parameter);
		userLog.setUser_number(user_number);
		userLog.setTime(time);
		service.insert(userLog);
	}
}
