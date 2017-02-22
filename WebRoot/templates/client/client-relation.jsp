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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.fixed {
	font-size: 12px;
	display: block;
	position: fixed;
	right: 0px;
	top: 200px;
	margin-left: 10px;
	z-index: 100;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>客户关系管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createVisit() }">新增维护</button>
					</div>
					<div class="form-group">
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales" data-bind="options: sales_name, optionsCaption: '全部',value:chosenSales,event:{change:fetchSummary}"
										name="relation.sales_name"></select>
								</div>
							</div>
						</s:if>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<td>客户总数</td>
								<td data-bind="text:clientSummary().client_count"></td>
								<td>昨日新增拜访</td>
								<td data-bind="text:clientSummary().yesterday_visit_count"></td>
								<td>昨日新增通话</td>
								<td data-bind="text:clientSummary().yesterday_chat_count"></td>
							</tr>
							<tr>
								<td>本周订单</td>
								<td data-bind="text:clientSummary().week_order_count"></td>
								<td>本周新增拜访</td>
								<td data-bind="text:clientSummary().week_visit_count"></td>
								<td>本周新增通话</td>
								<td data-bind="text:clientSummary().week_chat_count"></td>
							</tr>
							<tr>
								<td>本月订单</td>
								<td data-bind="text:clientSummary().month_order_count"></td>
								<td>本月新增拜访</td>
								<td data-bind="text:clientSummary().month_visit_count"></td>
								<td>本月新增通话</td>
								<td data-bind="text:clientSummary().month_chat_count"></td>
							</tr>
						</thead>
					</table>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>客户姓名</th>
								<th>年订单</th>
								<th>月订单</th>
								<th>签单期间</th>
								<th>拜访累计</th>
								<th>拜访期间</th>
								<th>有效通话</th>
								<th>通话期间</th>
								<th>应收款总计</th>
								<th>最长账期</th>
								<th>待办事宜</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>销售</th>
								</s:if>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: relations">
							<tr>
								<td data-bind="text: $data.client_employee_name"></td>
								<td data-bind="text: $data.year_order_count"></td>
								<td data-bind="text: $data.month_order_count"></td>
								<td data-bind="text: $data.last_order_period"></td>
								<td data-bind="text: $data.visit_count"></td>
								<td data-bind="text: $data.last_visit_period"></td>
								<td data-bind="text: $data.chat_count"></td>
								<td data-bind="text: $data.last_chat_period"></td>
								<td data-bind="text: $data.receivable"></td>
								<td data-bind="text: $data.last_receivable_period"></td>
								<td></td>
								
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.sales_name"></td>
								</s:if>
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
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/client/client-relation.js"></script>
</body>
</html>