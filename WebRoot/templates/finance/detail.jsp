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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
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
			<h2>明细管理</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1"
									data-bind="click: function() { createDetail('normal-receive') }">收入</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createDetail('receive') }">财务收入</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createDetail('pay') }">财务支出</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createDetail('inner') }">内转</button>
								<s:if test="#session.user.user_roles.contains('ADMIN')">
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { modify() }">修改</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deleteDetail() }">删除</button>
								</s:if>
								<s:if test="#session.user.user_roles.contains('FINANCE')">
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { modify_temp() }">修改</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deleteDetail_temp() }">删除</button>
								</s:if>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">发生日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="发生日期" name="detail.time" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">账户</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --',event: {change:refresh}"
									name="detail.account" required="required"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">类型</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: type, optionsCaption: '-- 请选择 --',event: {change:refresh}"
									name="detail.type"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">发生月</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" placeholder="出团月份" name="detail.month" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="detail.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="detail.money_to" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">精确金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="精确金额" name="detail.money" />
							</div>
						</div>
						<div style="padding-top: 3px;float:right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>账户</th>
								<th>发生时间</th>
								<th>收入</th>
								<th>支出</th>
								<th>余额</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: details">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenDetails" /></td>
								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.time"></td>
								<!-- ko if: $data.type=='收入' -->
								<td data-bind="text: $data.money" class="rmb"></td>
								<td></td>
								<!-- /ko -->
								<!-- ko if: $data.type=='支出' -->
								<td></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<!-- /ko -->
								<td data-bind="text: $data.balance" class="rmb"></td>
								<td data-bind="text: $data.comment"></td>
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
		$(".finance").addClass("current").children("ol")
				.css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/finance/detail.js"></script>
</body>
</html>