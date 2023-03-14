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

.title-one {
	text-align: center;
	font-size: 24px !important;
}

.border {
	border-left: 1px solid green !important;
}

.title-two {
	text-align: center;
}
.budget{
	display:none;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>地接款汇总</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">供应商</label>
							<div class="col-md-2">
								<input type="text" class="form-control" name="summary.supplier_short_name" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: sales, optionsCaption: '全部',optionsText:'user_name',optionsValue:'user_number'"
										name="summary.user_number"></select>
								</div>
							</div>
						</s:if>
						<div class="span6">
							<div data-bind="foreach: types">
								<em class="small-box"> <input type="radio" required="required"
									data-bind="attr: {'value': $data}, checked: $root.chosenType,event:{click:$root.changeType}" name="bf"/><label
									data-bind="text: $data"></label>  
								</em> 
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="padding-top: 3px; float: right">
							<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: search">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th></th>
								<th></th>
								<th colspan="3" class="title-one border" data-bind="text:current_month"></th>
								<th colspan="3" class="title-one border" data-bind="text:last_month"></th>
								<th colspan="3" class="title-one border" data-bind="text:before_last_month"></th>
								<th colspan="3" class="title-one border">更早</th>
							</tr>
							<tr role="row" id="head">
								<th class="title-two">供应商</th>
								<th class="title-two">剩余应付</th>
								<th class="title-two">预计新增</th>
								<th class="title-two border">剩余</th>
								<th class="title-two">总计</th>
								<th class="title-two">已付</th>
								<th class="title-two border">剩余</th>
								<th class="title-two">总计</th>
								<th class="title-two">已付</th>
								<th class="title-two border">剩余</th>
								<th class="title-two">总计</th>
								<th class="title-two">已付</th>
								<th class="title-two border">剩余</th>
								<th class="title-two">总计</th>
								<th class="title-two">已付</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: payableSummarys">
							<tr>
								<td data-bind="text: $data.supplier_short_name" class="rmb"></td>
								<td data-bind="text: $data.all_budget_balance" class="rmb budget"></td>
								<td data-bind="text: $data.all_final_balance" class="rmb final"></td>
								<td data-bind="text: $data.all_expected_balance" class="rmb"></td>
								
								<td data-bind="text: $data.current_month_budget_balance" class="rmb border budget"></td>
								<td data-bind="text: $data.current_month_final_balance" class="rmb border final"></td>
								<td data-bind="text: $data.current_month_budget_payable" class="rmb budget"></td>
								<td data-bind="text: $data.current_month_final_payable" class="rmb final"></td>
								<td data-bind="text: $data.current_month_paid" class="rmb"></td>
								
								<td data-bind="text: $data.one_month_budget_balance" class="rmb border budget"></td>
								<td data-bind="text: $data.one_month_final_balance" class="rmb border final"></td>
								<td data-bind="text: $data.one_month_budget_payable" class="rmb budget"></td>
								<td data-bind="text: $data.one_month_final_payable" class="rmb final"></td>
								<td data-bind="text: $data.one_month_paid" class="rmb"></td>
								
								<td data-bind="text: $data.two_month_budget_balance" class="rmb border budget"></td>
								<td data-bind="text: $data.two_month_final_balance" class="rmb border final"></td>
								<td data-bind="text: $data.two_month_budget_payable" class="rmb budget"></td>
								<td data-bind="text: $data.two_month_final_payable" class="rmb final"></td>
								<td data-bind="text: $data.two_month_paid" class="rmb"></td>
								
								<td data-bind="text: $data.earlier_month_budget_balance" class="rmb border budget"></td>
								<td data-bind="text: $data.earlier_month_final_balance" class="rmb border final"></td>
								<td data-bind="text: $data.earlier_month_budget_payable" class="rmb budget"></td>
								<td data-bind="text: $data.earlier_month_final_payable" class="rmb final"></td>
								<td data-bind="text: $data.earlier_month_paid" class="rmb"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td>合计</td>
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb"></td>
							
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb"></td>
							
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb"></td>
							
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb"></td>
							
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb budget"></td>
							<td class="rmb final"></td>
							<td class="rmb"></td>
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
		$(".product").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/file-upload.js"></script>
	<script src="<%=basePath%>static/js/sale/payable-summary.js"></script>
</body>
</html>