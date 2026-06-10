<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/upload.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css?v1.001" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.confirmed {
	font-weight: bold;
}

h2 {
	padding-left: 20px !important;
}
</style>

</head>

<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>押金查看</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: status" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="deposit.statuses" /><label data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">金额</label>
						<div class="col-md-1" style="float: left">
							<input type="number" class="form-control" placeholder="大于等于" name="deposit.money_from" />
						</div>
						<div class="col-md-1" style="float: left">
							<input type="number" class="form-control" placeholder="小于等于" name="deposit.money_to" />
						</div>
						<label class="col-md-1 control-label">供应商</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control" name="deposit.supplier_name" />
						</div>
						<label class="col-md-1 control-label">押金单号</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control" name="deposit.deposit_number" />
						</div>
						<label class="col-md-1 control-label">备注</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control" name="deposit.comment" placeholder="填写部分信息即可" />
						</div>

					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">精确金额</label>
						<div class="col-md-2" style="float: left">
							<input type="number" class="form-control" style="width: 44%" placeholder="精确金额" name="deposit.money" />
						</div>
						<label class="col-md-1 control-label">凭证号</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control" name="deposit.voucher_number" placeholder="凭证号" />
						</div>
						<button type="submit" style="float: right" class="btn btn-green" data-bind="click: refresh">搜索</button>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" id="table-main">
						<thead>
							<tr role="row">
								<th>收款方</th>
								<th>金额</th>
								<th>已退</th>
								<th>剩余</th>
								<th>到期时间</th>
								<th>状态</th>
								<th>退还途径</th>
								<th>流水凭证号</th>
								<th>备注</th>
								<th>押金单号</th>
							</tr> 
						</thead>
						<tbody id="tbody-data" data-bind="foreach:deposits">
							<tr>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.received" class="rmb"></td>
								<td data-bind="text: $data.balance" class="rmb"></td>
								<td data-bind="text: $data.return_date"></td>
								<!-- ko if:$data.status=='N' -->
								<td data-bind="text: $root.statusMapping[$data.status]" style="color: red"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='Y' -->
								<td data-bind="text: $root.statusMapping[$data.status]" style="color: green"></td>
								<!-- /ko -->
								<td><a href="javascript:void(0)" data-bind="text: $data.return_way_cn,click:$root.viewDetail"></a></td>

								<td data-bind="text: $data.voucher_number"></td>
								<td data-bind="text: $data.comment"></td>
								<!-- ko if:$data.status=='I' -->
								<!-- <td data-bind="text: $root.statusMapping[$data.status]"></td> -->
								<!-- /ko -->
								<!-- <td><a href="javascript:void(0)" data-bind="click:$root.viewDetail">查看</a></td> -->
								<td data-bind="text: $data.deposit_number"></td>
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

	<div id="div-return-detail" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
		<!-- ko if: detail_return_way().indexOf("T")>-1 -->
		<h2>押金退还</h2>
		<div class="input-row clearfloat">
			<label class="l">退还总额</label>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:sum_receiveds()"></p>
				</div>
			</div>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover" id="table-received">
				<thead>
					<tr role="row">
						<th>收款账户</th>
						<th>退还金额</th>
						<th>收入时间</th>
						<th>状态</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody id="tbody-data" data-bind="foreach:deposit_receiveds">
					<tr>
						<td data-bind="text: $data.card_account"></td>
						<td data-bind="text: $data.received" class="rmb"></td>
						<td data-bind="text: $data.received_time"></td>
						<td data-bind="text: $data.status"></td>
						<td data-bind="text: $data.comment"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<hr />
		<!-- /ko -->
		<!-- ko if: detail_return_way().indexOf("K")>-1 -->
		<h2>押金扣款</h2>
		<div class="input-row clearfloat">
			<label class="l">扣款总额</label>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:sum_deducts()"></p>
				</div>
			</div>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover" id="table-received">
				<thead>
					<tr role="row">
						<th>收款方</th>
						<th>扣款金额</th>
						<th>入账日期</th>
						<th>备注</th>
						<th>责任产品</th>
						<th>归属月份</th>
					</tr>
				</thead>
				<tbody id="tbody-data" data-bind="foreach:deposit_deducts">
					<tr>
						<td data-bind="text: $data.receiver"></td>
						<td data-bind="text: $data.money" class="rmb"></td>
						<td data-bind="text: $data.time"></td>
						<td data-bind="text: $data.comment"></td>
						<td data-bind="text: $data.product_manager" class="rmb"></td>
						<td data-bind="text: $data.belong_month" class="rmb"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<hr />
		<!-- /ko -->
		<!-- ko if:detail_return_way().indexOf("C")>-1 -->
		<h2>押金冲账</h2>
		<div class="input-row clearfloat">
			<label class="l">冲账总额</label>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:sum_strikes()"></p>
				</div>
			</div>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover" id="table-received">
				<thead>
					<tr role="row">
						<th>供应商</th>
						<th>冲账金额</th>
						<th>首航日期</th>
						<th>首航段</th>
						<th>入账日期</th>
					</tr>
				</thead>
				<tbody id="tbody-data" data-bind="foreach:deposit_strikes">
					<tr>
						<td data-bind="text: $data.financial_body_name"></td>
						<td data-bind="text: $data.money" class="rmb"></td>
						<td data-bind="text: $data.first_date"></td>
						<td data-bind="text: $data.frist_from_to"></td>
						<td data-bind="text: $data.time"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- /ko -->
	</div>


	<script>
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/deposit.js?v1.001"></script>
</body>
</html>