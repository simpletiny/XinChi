<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style type="text/css">
span {
	line-height: 150%;
}
</style>
<%-- <script>
	document.oncontextmenu = new Function("event.returnValue=false;");
	document.onselectstart = new Function("event.returnValue=false;");
</script> --%>
</head>
<body>
	<input type="hidden" id="view_key" value="<%=key%>" />
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				欣驰世界观<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editView() }">编辑</button>
					<hr />
				</s:if>
				<div class="form-box info-form">
					<h1 data-bind="text: view().title" style="text-align: center"></h1>
					<div class="ip">
						<p class="ip-default" style="padding: 30px 120px 100px 120px" data-bind="html: view().content"></p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".culture").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>

	<script src="<%=basePath%>static/js/culture/world-view-preview.js"></script>
</body>
</html>