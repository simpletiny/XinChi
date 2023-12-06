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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css?v1.001" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>额外收支</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<div data-bind="foreach: statuses">
								<em class="small-box "> <input name="reconciliation.statuses" type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatus, event:{click:function(){$root.refresh();return true}}" /><label
									data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
						<div style="float: right">
							<button type="submit" class="btn btn-green" data-bind="click: function() { delete_reconciliation() }">删除</button>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="reconciliation.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="reconciliation.money_to" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">精确金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="精确金额" name="reconciliation.money" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'"
										name="reconciliation.product_manager_number"></select>
								</div>
							</div>
						</s:if>
						<label class="col-md-1 control-label">归属月份</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control month-picker-st" placeholder="from" name="reconciliation.belong_month" />
						</div>
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>归属月份</th>
								<th>金额</th>
								<th>类型</th>
								<th>产品经理</th>
								<th>录入人</th>
								<th>状态</th>
								<th>备注</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>操作</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: reconciliations">
							<tr>
								<td><input type="checkbox" data-bind="value:$data, checked: $root.chosenReconciliations" /></td>
								<td data-bind="text: $data.belong_month"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text:$data.type"></td>
								<td data-bind="text: $data.product_manager_name"></td>
								<td data-bind="text: $data.create_user_name"></td>
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<td data-bind="text: $data.comment"></td>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td></td>
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
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-reconciliation.js?v=1.001"></script>
</body>
</html>