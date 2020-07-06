<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link href="<%=basePath%>static/img/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/style.css" />
<link rel="stylesheet" href="<%=basePath%>static/vendor/font-awesome-4.2.0/css/font-awesome.min.css" />

</head>
<body>
	<s:hidden id="login_result" value="%{#request.login_result}"></s:hidden>
	<!-- head start -->
	<div class="main-header">
		<div class="header-min-width" ><a style="padding-top: 20px;padding-left: 20px;color:white" href="http://www.beian.miit.gov.cn/">黑ICP备19007355号</a></div>
	</div>
	<!-- head end -->
	<input type="hidden" id="hidden_apiurl" value="<%=basePath%>" />

	<!-- login box start -->
	<form class="login"  id="login-form" >
		<h4>用户登录</h4>
		<ul>
			<li><input type="text" id="username" placeholder="用户名" class="ip-default" name="ubb.login_name"
				required="required" /></li>
			<li><input type="password" id="password" placeholder="密码" class="ip-default" name="ubb.password"
				required="required" /></li>
			<li><input type="checkbox" id="auto-login" /><label>记住密码</label></li>
			<li><div style="padding-top: 15px;">
					<input type="button" class="btn btn-green" data-bind="click:login" value="登录" />
				</div></li>
			<li><div style="padding-top: 5px;">
					<a style="cursor: pointer" href="<%=basePath%>templates/users/register.jsp">注册</a>
				</div></li>
		</ul>
	</form>
	<!-- login box end -->
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery-cookie/jquery.cookie.js"></script>
	<script src="<%=basePath%>static/vendor/knockout-3.2.0.js"></script>
	<script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/utils.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/users/login.js"></script>
</body>

</html>
