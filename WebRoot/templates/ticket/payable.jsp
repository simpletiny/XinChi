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
			<h2>机票往来</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">首航日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="首航日期" name="option.first_date" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">首航段</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="首航段" name="option.from_to_city" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">供应商</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="供应商" name="option.supplier_employee_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">财务主体</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="财务主体" name="option.financial_body_name" />
							</div>
						</div>
						<div class="span6">
							<em class="small-box"> <label>尾款为0</label> <input type="checkbox" value="Y" name="option.zero_flg"
								data-bind="event:{click:zeroBalance}" />
							</em>
						</div>
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: search">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>供应商</th>
								<th>主体</th>
								<th>PNR</th>
								<th>首航日期</th>
								<th>首航段</th>
								<th>名单</th>
								<th>应付款</th>
								<th>已付款</th>
								<th>尾款</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: payables">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenPayables" /></td>
								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
								<td data-bind="text: $data.PNR"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkTicketInfo,text: $data.first_date"></a></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkTicketInfo,text: $data.from_to_city"></a></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkPassengers,text: $data.passenger"></a></td>
								<td data-bind="text:$data.budget_payable" class="rmb"></td>
								<td data-bind="text: $data.paid" class="rmb"></td>
								<td data-bind="text:$data.budget_balance" class="rmb"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>合计</td>
							<td data-bind="text:totalBudgetPayable" class="rmb"></td>
							<td data-bind="text:totalPaid" class="rmb"></td>
							<td data-bind="text:totalBudgetBalance" class="rmb"></td>
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

				<div class="fixed">
					<div style="margin-top: 5px">
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { pay()}">支付</button>
					</div>
				</div>
			</div>

		</div>
	</div>

	<!-- 支付申请-->
	<div id="pay" style="display: none; width: 1000px; height: 700px; overflow: auto; padding-top: 30px;">
		<form id="form-pay">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">供应商</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:supplier_name()"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">应付款总额</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default rmb" data-bind="text:totalPay()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">支付时限</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="detail.limit_time" class="form-control date-picker" data-bind="value:today()"
							required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">支付总额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.allot_money" class="ip-" st="sum_paid" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-4">
					<label class="l" style="width: 100%">供应商员工</label>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 100%">应付款</label>
				</div>
				<div class="col-md-4 required">
					<label class="l" style="width: 100%">支付金额</label>
				</div>
			</div>
			<!-- ko foreach:chosenPayables -->
			<div class="input-row clearfloat" st="pay_allot">
				<input type="hidden" data-bind="value: $data.pk" st="base-pk" /> <input type="hidden" data-bind="value: $data.PNR"
					st="PNR" />
				<div class="col-md-4">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						<input type="hidden" data-bind="value:$data.supplier_employee_pk" st="supplier_employee_pk" /> <input
							type="hidden" data-bind="value:$data.supplier_employee_name" st="supplier_employee_name" />
					</div>
				</div>
				<div class="col-md-4">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<p class="ip-default rmb" data-bind="text:$data.final_balance"></p>
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
						<!-- /ko -->
					</div>
				</div>
				<div class="col-md-4">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<input type="number" class="form-control" st="paid"
							data-bind="attr:{'name':'name-'+$data.pk},value: $data.final_balance" required="required" />
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<input type="number" class="form-control" st="paid"
							data-bind="attr:{'name':'name-'+$data.pk},value: $data.budget_balance" required="required" />
						<!-- /ko -->

					</div>
				</div>
			</div>
			<!-- /ko -->
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyPay">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 查看机票信息-->
	<div id="infos-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">日期</th>
							<th style="width: 10%">时间</th>
							<th style="width: 10%">城市对</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:infos">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.ticket_date"></td>
							<td data-bind="text:$data.from_to_time"></td>
							<td data-bind="text:$data.from_to_city"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/payable.js"></script>
</body>
</html>