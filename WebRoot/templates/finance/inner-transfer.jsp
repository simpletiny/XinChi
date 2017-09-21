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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>内转明细</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createDetail('inner') }">内转</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: allTypes" style="padding-left: 40px;">
	                           <em class="small-box">
	                                 <input type="checkbox" name="options.exchange_types" data-bind="attr: {'value': $data},click:function(){$root.refresh();return true;}, checked: $root.chosenTypes"/><label data-bind="text: $root.typeMapping[$data]"></label>
	                            </em>
	                        </div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">转出日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="转出日期" name="options.from_date" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">转入日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="转入日期" name="options.to_date" />
							</div>
						</div>

					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">转出账户</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --',event: {change:refresh}" name="options.from_account"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">转入账户</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --',event: {change:refresh}" name="options.to_account"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">转出月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" placeholder="出团月份" name="options.from_month" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">转入月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" placeholder="出团月份" name="options.to_month" />
							</div>
						</div>

					</div>
					<div class="form-group">
						<div style="float: right;">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>转出账户</th>
								<th>转出时间</th>
								<th>转出金额</th>
								<th>转入账户</th>
								<th>转入时间</th>
								<th>转入金额</th>
								<th>汇兑账户</th>
								<th>汇兑费用</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: details">
							<tr>
								<td data-bind="text: $data.from_account"></td>
								<td data-bind="text: $data.from_time"></td>
								<td data-bind="text: $data.from_money" class="rmb"></td>
								<td data-bind="text: $data.to_account"></td>
								<td data-bind="text: $data.to_time"></td>
								<td data-bind="text: $data.to_money" class="rmb"></td>
								<td data-bind="text: $data.exchange_account"></td>
								<td data-bind="text: $data.exchange_money" class="rmb"></td>
								<td data-bind="text: $data.comment"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td>汇总</td>
							<td data-bind="text:totalOut" class="rmb"></td>
							<td></td>
							<td></td>
							<td data-bind="text:totalIn" class="rmb"></td>
							<td></td>
							<td data-bind="text:totalExchange" class="rmb"></td>
							<td></td>
						</tr>
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
		$(".accounting").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/finance/inner-transfer.js"></script>
</body>
</html>