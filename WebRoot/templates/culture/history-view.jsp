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
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>欣驰历史</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="width: 30%; float: right">
							<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
								<div>
									<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createView() }">新建</button>
								</div>
								<div>
									<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {deleteView() }">删除</button>
								</div>
							</s:if>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th></th>
								</s:if>
								<th>标题</th>
								<th>作者</th>
								<th>日期</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: views">
							<tr style="overflow: hidden">
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenViews" /></td>
								</s:if>
								
								<td><a href="javascript:void(0)" data-bind="text: $data.title,attr: {href: 'history-view-preview.jsp?key='+$data.pk}"></a></td>
								<td data-bind="text: $data.create_user"></td>
								<td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD HH:mm')"></td>
							</tr>
						</tbody>
					</table>
					<div class="pagination clearfloat">
						<a data-bind="click: previousPage, enable: currentPage() > 1" class="prev">Prev</a>
						<!-- ko foreach: pageNums -->
						<!-- ko if: $data == $root.currentPage() -->
						<span class="current" data-bind="text: $data"></span>
						<!-- /ko -->
						<!-- ko ifnot: $data == $root.currentPage() -->
						<a data-bind="text: $data, click: $root.turnPage"></a>
						<!-- /ko -->
						<!-- /ko -->
						<a data-bind="click: nextPage, enable: currentPage() < pageNums().length" class="next">Next</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".culture").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/culture/history-view.js?1.0"></script>
</body>
</html>