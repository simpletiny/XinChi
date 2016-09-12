package com.xinchi.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * 扩展Struts2返回类型,直接返回String.
 * 
 * @author niushixing
 * @version 1.00 2014-12-10 11:28:04
 */
@Controller("stringResult")
public class StringResult extends ServletRedirectResult {

	private static final long serialVersionUID = -8308338083005951877L;
	private Logger logger = Logger.getLogger(StringResult.class);

	public StringResult() {

		super();

	}

	public StringResult(String location) {

		super(location);

	}

	/**
	 * 扩展Struts2返回类型，直接返回String.
	 */
	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {

		// 取得HTTP Response对象
		HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(HTTP_RESPONSE);

		// 取得HTTP Request对象
		HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(HTTP_REQUEST);

		// 设置编码等
		response.setContentType("text/plain; charset=UTF-8");

		response.setHeader("Content-Disposition", "inline");

		PrintWriter writer = null;

		try {

			// 写入String
			writer = response.getWriter();

			writer.write(request.getAttribute(finalLocation).toString());

			// 异常处理
		} catch (NullPointerException e) {

			if (finalLocation.equals("")) {

				logger.warn("未指定value", e);

			} else {

				logger.error("空", e);

			}

		} finally {

			// 关闭PrintWriter
			if (writer != null) {

				writer.flush();

				writer.close();

			}

		}

	}

}
