package com.xinchi.struts.interceptor;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.exception.BusinessException;


public class BusinessInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 608013227508765392L;

	private Logger logger=Logger.getLogger(BusinessInterceptor.class);
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		before(invocation);

		String result = "";
		
		try {
			result = invocation.invoke();
		} catch (DataAccessException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("数据库操作失败！");
		} catch (NullPointerException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("调用了未经初始化的对象或者是不存在的对象！");
		} catch (IOException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("IO异常！");
		} catch (ClassNotFoundException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("指定的类不存在！");
		} catch (ArithmeticException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("数学运算异常！");
		} catch (ArrayIndexOutOfBoundsException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("数组下标越界!");
		} catch (IllegalArgumentException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("方法的参数错误！");
		} catch (ClassCastException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("类型强制转换错误！");
		} catch (SecurityException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("违背安全原则异常！");
		} catch (SQLException ex) {
			this.logException(invocation, ex);
			throw new BusinessException("操作数据库异常！");
		} catch (NoSuchMethodError ex) {
			this.logError(invocation, ex);
			throw new BusinessException("方法末找到异常！");
		} catch (InternalError ex) {
			this.logError(invocation, ex);
			throw new BusinessException("Java虚拟机发生了内部错误");
		} catch (Exception ex) {
			this.logException(invocation, ex);
			throw new BusinessException("程序内部错误，操作失败！");
		}
		
		after(invocation, result);
		
		return result ;
	}
	
	/**
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	public void before(ActionInvocation invocation) throws Exception {
//		String actionName=invocation.getProxy().getAction().toString();
//		String methodName=invocation.getProxy().getMethod();
//		String user_id=SeentaoApplicationContext.getCurrentUser();
//		logger.info(Thread.currentThread()+":用户"+user_id+":"+actionName+"."+methodName+"请求开始");
	}
	
	/**
	 * 记录日志等...
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	public void after(ActionInvocation invocation,String result) throws Exception{
//		String actionName=invocation.getProxy().getAction().toString();
//		String methodName=invocation.getProxy().getMethod();
//		String user_id=SeentaoApplicationContext.getCurrentUser();
//		logger.info(Thread.currentThread()+":用户"+user_id+":"+actionName+"."+methodName+"请求结束");
	}	
	
	public void logException(ActionInvocation invocation,Exception ex) throws Exception{
		String actionName=invocation.getProxy().getAction().toString();
		String methodName=invocation.getProxy().getMethod();
		String user_id=XinChiApplicationContext.getCurrentUser();
		logger.error(Thread.currentThread()+":用户"+user_id+":"+actionName+"."+methodName+"请求异常:"+ex);
	}	
	
	public void logError(ActionInvocation invocation,Error ex) throws Exception{
		String actionName=invocation.getProxy().getAction().toString();
		String methodName=invocation.getProxy().getMethod();
		String user_id=XinChiApplicationContext.getCurrentUser();
		logger.error(Thread.currentThread()+":用户"+user_id+":"+actionName+"."+methodName+"请求错误:"+ex);
	}	
	
}
