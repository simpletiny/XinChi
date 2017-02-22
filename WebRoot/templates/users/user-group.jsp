<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
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
			<h2>小组管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { create() }">新建</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editOrder() }">编辑</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); searchResumes() }">删除</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>名称</th>
								<th>组长</th>
								<th>主要职责</th>
								<th>包含员工</th>
								<th>上级领导</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: groups">
							<tr>
								<td data-bind="text: $data.group_name"></td>
								<td data-bind="text: $data.group_leader"></td>
								<td data-bind="text: $data.group_duty"></td>
								<td data-bind="text: $root.getUsers($data.pk)"></td>
								<td data-bind="text: $data.higher_ups"></td>
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
	<script src="<%=basePath%>static/js/users/user-group.js"></script>
</body>
</html>