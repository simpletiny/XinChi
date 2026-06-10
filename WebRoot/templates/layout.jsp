
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta name="renderer" content="webkit" />
<link href="<%=basePath%>static/img/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/hint.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/style.css?v=1.1" />
<link rel="stylesheet" href="<%=basePath%>static/vendor/font-awesome-4.2.0/css/font-awesome.min.css" />
<title>欣驰国际</title>
<style>
input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
	-webkit-appearance: none !important;
}

.fa-users1:before {
	content: url("<%=basePath%>/templates/favicon.png");
}

.floatPanel {
	position: fixed;
	top: 70%;
	right: 0;
	z-index: 9999999;
}

.timer {
	text-align: center;
	border: solid 1px;
	width: 100px;
	height: 80px;
	display: block;
	background: white;
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

	<!--  	<div class="floatPanel timer">
		<label>重启倒计时</label>
		<h3><b id="reboot-timer">05:00</b></h3>
	</div> 
	 -->
	<!-- head start -->
	<input type="hidden" id="hidden_apiurl" value="<%=basePath%>" />
	<div class="main-header">

		<div class="header-min-width" style="text-align: center">
			<%-- <img src="<%=basePath%>/static/img/head.jpg"></img>  --%>
			<div style="display: block;">
				<font size="4" color="white">让组团收客多快好省。&nbsp;&nbsp;&nbsp;以价值创造为核心：开放、透明、坦诚、共赢。&nbsp;&nbsp;&nbsp;做世界一流旅游运营商。&nbsp;&nbsp;&nbsp;以奋斗者为本，与优秀者为伍。</font>
			</div>
			<div class="user-time">
				<input type="hidden" id="hidden-server-date" value='${user.current_date}' />
				<s:property value="#session.user.current_date" />
				<input type="hidden" id="user-nick-name" value='${user.nick_name}' /> <input type="hidden" id="user-number"
					value='${user.user_number}' /> <input type="hidden" id="user-roles" value='${user.user_roles}' /> <input
					type="hidden" id="user-pk" value='${user.pk}' />
			</div>
			<div class="user-status">
				<%-- 				<a href="<%=basePath%>templates/order/confirm-name-list.jsp"
					style="margin-right: 100px; color: pink; font-size: 150%; text-decoration: none;">名单确认</a>
 --%>
				<s:property value="#session.user.nick_name" />
				（
				<s:property value="#session.user.user_number" />
				）&nbsp;&nbsp;&nbsp;&nbsp;授信额度：
				<s:property value="#session.user.credit_limit" />
				&nbsp;&nbsp;剩余额度：
				<s:property value="#session.user.credit_balance" />
				&nbsp;&nbsp;<i class="ic-user"
					onclick="javascript:window.location.href='<%=basePath%>templates/users/user-center.jsp'"><img
					src="<%=basePath%>static/img/mc-default-userphoto.png" width="36" height="36" alt="" /></i> <span></span>&nbsp;&nbsp;&nbsp;&nbsp;<a
					href="<%=basePath%>user/logout">退出</a><i class="fa fa-lg fa-sign-out"></i>
			</div>
		</div>
	</div>
	<!-- head end -->
	`
	<!-- sidebar start -->
	<div class="main-sidebar">
		<ul class="menu-tree" style="padding-top: 30px;">
			<%= session.getAttribute("navigation_html") %>
		</ul>
	</div>
	<!-- sidebar end -->
	<script src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-formatCurrency.js"></script>
	<script src="<%=basePath%>static/vendor/knockout-3.2.0.js"></script>
	<script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
	<%-- <script src="<%=basePath%>static/vendor/layer-v3.1.1/layer.js"></script> --%>
	<script src="<%=basePath%>static/vendor/momentjs/moment-with-locales.min.js"></script>
	<script src="<%=basePath%>static/vendor/autocomplete/jquery.autocomplete.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/nanobar.js"></script>

	<script src="<%=basePath%>static/js/utils.js?v1.010"></script>
	<script src="<%=basePath%>static/js/layout.js?v=1.001"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
</body>
</html>
