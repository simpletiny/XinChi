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
			<h2>往来详表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: rollBack">打回重报</button>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="option.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="option.money_to" />
							</div>
						</div>
						<div>
							<label class="col-md-1 control-label">收款方</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.receiver" />
							</div>
						</div>

					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">入账日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateFrom" placeholder="from"
									name="option.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateTo" placeholder="to"
									name="option.date_to" />
							</div>
						</div>
						<!-- 						<div  align="left">
							<label class="col-md-1 control-label">产品经理</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.create_user" />
							</div>
						</div> -->
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>金额</th>
								<th>类型</th>
								<th>供应商员工</th>
								<th>收款方</th>
								<th>入账日期</th>
								<th>状态</th>
								<th>支付详情</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: paids">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.related_pk}, checked: $root.chosenPaids" /></td>
								<td data-bind="text: $data.allot_money" class="rmb"></td>
								<td style="color: green" data-bind="text: $root.typeMapping[$data.type]"></td>
								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
								<td data-bind="text: $data.time"></td>
								<!-- ko if:$data.status=='I' -->
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='Y' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='P' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='N' -->
								<td style="color: red" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<td><a href="javascript:void(0)" data-bind="click:$root.viewDetail">查看</a></td>
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
	<div id="sum_detail" style="display: none; width: 1200px; height: 500px; overflow-y: auto; padding-top: 30px;">

		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">收款方</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">支付金额</label>
			</div>
		</div>
		<!-- ko foreach:details -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.financial_body_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
		<div class="input-row clearfloat">
			<div class="col-md-2">
				<label class="l" style="width: 100%">收支账户</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">收支时间</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">收款方账户</label>
			</div>
			<div class="col-md-2">
				<label class="l" style="width: 100%">收支金额</label>
			</div>
			<div class="col-md-2">
				<label class="l" style="width: 100%">凭证</label>
			</div>
		</div>
		<!-- ko foreach:paymentDetails -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.account"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.time"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.receiver"></p>
				</div>
			</div>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money"></p>
				</div>
			</div>

			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default">
						<a href="javascript:void(0)"
							data-bind="click: function() {$root.checkVoucherPic($data.voucher_file_name,$data.account_pk)} ">查看</a>
					</p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/paid.js"></script>
</body>
</html>