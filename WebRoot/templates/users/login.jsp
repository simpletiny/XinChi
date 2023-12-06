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
<style type="text/css">

#bottom-layer {
    width: 100%;
    position: fixed;
    z-index: 302;
    bottom: 0;
    left: 0;
    height: 39px;
    padding-top: 1px;
    zoom: 1;
    margin: 0;
    line-height: 39px;
    background: #fff;
}
#mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    color: white; /* 根据需要设置文字颜色 */
    background: transparent; /* 背景半透明 */
    display: block;
    z-index: 1000;
    pointer-events: none; 
    overflow: hidden; /* 防止内容溢出 */
}

.mask-text {
    color: rgba(0, 0, 0, 0.1); /* 白色文字，30% 不透明度 */
    font-size: 20px; /* 字体大小 */ 
    font-family: Arial, sans-serif; /* 字体 */
    transform: rotate(-30deg); /* 文字倾斜 */
    margin: 10px; /* 文字间距 */
    white-space: nowrap; /* 防止文字换行 */
    position: absolute; /* 文字也使用绝对定位 */
    pointer-events: none; /* 防止文字捕获鼠标事件 */
}
</style>
</head>
<body>
	<s:hidden id="login_result" value="%{#request.login_result}"></s:hidden>
	<!-- head start -->
	<div class="main-header">
		<div class="header-min-width" ></div>
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
					<input type="button" id="btn-login" class="btn btn-green" data-bind="click:login" value="登录" />
				</div></li>
			<li><div style="padding-top: 5px;">
					<a style="cursor: pointer" href="<%=basePath%>templates/users/register.jsp">注册</a>
				</div></li>
		</ul>
	</form>
	<div id="bottom-layer"><a style="padding-top: 20px;padding-left: 20px;color:black" href="http://beian.miit.gov.cn/">黑ICP备19007355号</a></div>
	
	<!-- login box end -->
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery-cookie/jquery.cookie.js"></script>
	<script src="<%=basePath%>static/vendor/knockout-3.2.0.js"></script>
	<script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/utils.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/users/login.js?v=1.001"></script>
	
	
	
</body>

</html>
