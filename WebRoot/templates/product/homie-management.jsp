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
			<h2>本地维护</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createFlight() }">新建</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editEmployee() }">编辑</button>
								<button type="submit" class="btn btn-green col-md-1"
									data-bind="click: function() { resetPage(); searchResumes() }">删除</button>
							</div>
						</div>
					</div>
					<!-- <div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">名称</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="姓名" name="flight.name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">编号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="供应商简称" name="flight.number" />
							</div>
						</div>
					</div> -->
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>名称</th>
								<th>供应商</th>
								<th>服务名称</th>
								<th>人均成人</th>
								<th>人均儿童</th>
								<th>服务要求</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: flights">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenFlights" /></td>
								<td><a href="javascript:void(0)" data-bind="text: $data.name"></a></td>
								<td data-bind="text: $data.number"></td>
								<td data-bind="text: $data.adult_price"></td>
								<td data-bind="text: $data.child_price"></td>
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
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/product/homie-management.js"></script>
</body>
</html>