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

</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>在线员工</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>姓名</th>
								<th>员工号</th>
								<th>登录时间</th>
								<th>当前页面</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: users">
							<tr>
								<td data-bind="text: $data.user_name"></td>
								<td data-bind="text: $data.user_number"></td>
								<td data-bind="text: $data.login_time"></td>
								<td><a data-bind="text:$data.current_url,attr{href: $data.current_url}"></a></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".user").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/users/user-online.js"></script>
</body>
</html>