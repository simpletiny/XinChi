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
			<h2>机票往来</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
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
							<div data-bind="foreach: types">
								<em class="small-box"> <input type="checkbox" required="required" data-bind="attr: {'value': $data}, checked: $root.chosenTypes,event:{click:$root.changeType}" /><label
									data-bind="text: $data"></label>
								</em>
							</div>
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
								<th>决否</th>
								<th>总款项</th>
								<th>已付款</th>
								<th>应付款</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: payables">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenPayables" /></td>
								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
								<td data-bind="text: $data.PNR"></td>
								<td data-bind="text: $data.final_flg"></td>

								<td st="budget" style="display: none" data-bind="text:$data.budget_payable" class="rmb"></td>

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="final" style="display: none" data-bind="text:$data.final_payable" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="final" style="display: none">未决算</td>
								<!-- /ko -->

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="all" data-bind="text:$data.final_payable" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="all" data-bind="text:$data.budget_payable" class="rmb"></td>
								<!-- /ko -->

								<td data-bind="text: $data.paid"></td>

								<td st="budget" style="display: none" data-bind="text:$data.budget_balance" class="rmb"></td>

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="final" style="display: none" data-bind="text:$data.final_balance" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="final" style="display: none">未决算</td>
								<!-- /ko -->

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="all" data-bind="text:$data.final_balance" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="all" data-bind="text:$data.budget_balance" class="rmb"></td>
								<!-- /ko -->
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>合计</td>
							<td st="all" data-bind="text:totalPayable" class="rmb"></td>
							<td st="budget" style="display: none" data-bind="text:totalBudgetPayable" class="rmb"></td>
							<td st="final" style="display: none" data-bind="text:totalFinalPayable" class="rmb"></td>
							<td data-bind="text:totalPaid" class="rmb"></td>
							<td st="all" data-bind="text:totalBalance" class="rmb"></td>
							<td st="budget" style="display: none" data-bind="text:totalBudgetBalance" class="rmb"></td>
							<td st="final" style="display: none" data-bind="text:totalFinalBalance" class="rmb"></td>
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
						<input type="text" name="detail.limit_time" class="form-control date-picker" data-bind="value:today()" required="required" />
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
					<input type="hidden" data-bind="value: $data.pk" st="base-pk"/>
					<input type="hidden" data-bind="value: $data.PNR" st="PNR"/>
				<div class="col-md-4">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						<input type="hidden" data-bind="value:$data.supplier_employee_pk" st="supplier_employee_pk" /> <input type="hidden" data-bind="value:$data.supplier_employee_name" st="supplier_employee_name" />
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
						<input type="number" class="form-control" st="paid" data-bind="attr:{'name':'name-'+$data.pk},value: $data.final_balance" required="required" />
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<input type="number" class="form-control" st="paid" data-bind="attr:{'name':'name-'+$data.pk},value: $data.budget_balance" required="required" />
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