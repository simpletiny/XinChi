package com.xinchi.struts.parse;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.JakartaMultiPartRequest;

public class SimpletinyRequestParser extends JakartaMultiPartRequest {
	@Override
	public void parse(HttpServletRequest request, String arg1) throws IOException {
		String url = request.getRequestURI().toString();// 取得请求的URL
		if (url.indexOf("kindeditor") >= 0) {

		} else {
			super.parse(request, arg1);
		}
	}
}
