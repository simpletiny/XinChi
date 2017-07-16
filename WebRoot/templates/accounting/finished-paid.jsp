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
			<h2>已支付</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group" >
						<div style="float:right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: pay">打回重报</button>
						</div>
					</div>
					<div class="form-group">
						<!-- <div align="left">
							<label class="col-md-1 control-label">申请日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateFrom" placeholder="from" name="detail.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateTo" placeholder="to" name="detail.date_to" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">申请人</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="detail.create_user" />
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
								<th>支付单号</th>
								<th>项目</th>
								<th>收款方</th>
								<th>金额</th>
								<th>支付详情</th>
								<th>支付人</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: paids">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenPaids" /></td>

								<td data-bind="text: $data.pay_number"></td>
								<td data-bind="text: $root.itemMapping[$data.item]"></td>
								<td data-bind="text: $data.receiver"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td><a href="javascript:void(0)" data-bind="attr: {href: 'paid-detail-view.jsp?key='+$data.pay_number}">查看</a></td>
								<td data-bind="text: $data.pay_user"></td>

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
		$(".finance").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/accounting/finished-paid.js"></script>
</body>
</html>