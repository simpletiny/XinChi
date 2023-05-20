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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.service-fee:after {
	content: "(月份为首航月份。合计与实际发生额会有误差，最大误差为：4元/1000人)";
	font-size: 30%;
	color: red;
}

.deposit-deduct:after {
	content: "(月份为归属月份)";
	color: red;
	font-size: 30%;
}

.none-business-payment:after {
	content: "(月份为归属月份)";
	color: red;
	font-size: 30%;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>票务财务汇总</h2>
		</div>

		<div class="main-container">

			<div class="main-box">

				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">

						<label class="col-md-1 control-label">月份</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control month-picker-st" placeholder="月份" name="summary_option.first_month" />
						</div>
						<label class="col-md-1 control-label">产品经理</label>
						<div class="col-md-2"> 
							<select class="form-control" style="height: 34px" id="select-sales"
								data-bind="event:{change: function() { refresh() }},options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
								name="summary_option.product_manager_number"></select>
						</div>
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
						</div>
					</div>
				</form>
				<h1 class="service-fee">手续费</h1>
				<hr></hr>
				<div class="list-result">
					<table class="table table-striped table-hover" id="main-table-1">
						<thead>
							<tr role="row">
								<th>应付总额</th>
								<th>已付总额</th>
								<th>涉及人数</th>
								<th>产品经理</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: service_fees">
							<tr>
								<td class="rmb" data-bind="text: $data.payable"></td>
								<td class="rmb" data-bind="text: $data.paid"></td>
								<td data-bind="text: $data.people_count"></td>
								<td data-bind="text: $data.product_manager_name"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<h1 class="deposit-deduct">押金扣款</h1>
				<hr></hr>
				<div class="list-result">
					<table class="table table-striped table-hover" id="main-table-2">
						<thead>
							<tr role="row">
								<th>扣款总额</th>
								<th>产品经理</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: deposit_deducts">
							<tr>
								<td class="rmb" data-bind="text: $data.money"></td>
								<td data-bind="text: $data.product_manager_name"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<h1 class="none-business-payment">无业务收支</h1>
				<hr></hr>
				<div class="list-result">
					<table class="table table-striped table-hover" id="main-table-3">
						<thead>
							<tr role="row">
								<th>支出</th>
								<th>收入</th>
								<th>合计</th>
								<th>产品经理</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: none_business_payments">
							<tr>
								<td class="rmb" data-bind="text: $data.pay_money"></td>
								<td class="rmb" data-bind="text: $data.receive_money"></td>
								<td class="rmb" data-bind="text: $data.money"></td>
								<td class="rmb" data-bind="text: $data.product_manager_name"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<script>
		$(".ticket-finance").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/ticket-payable-summary.js?v1.001"></script>
</body>
</html>