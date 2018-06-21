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
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<!-- tbc for to be confirmed -->
			<h2>单团核算单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="团号" name="option.team_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品" name="option.product_name"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" value="1" onclick="check(this)" checked name="radio-date" />出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="from" name="option.departure_date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="to" name="option.departure_date_to" />
							</div>
						</div>
					</div>
					<div class="form-group">


						<div class="span6" style="text-align: center">
							<div class="col-md-3">
								<input value="budget" type="checkbox" name="option.order_type" />预算 <input value="final" type="checkbox" name="option.order_type" />决算
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">销售</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px" id="select-sales" data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'"
									name="option.sale_number"></select>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" value="2" onclick="check(this)" name="radio-date" />确认日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-2" disabled="disabled" placeholder="from" name="option.confirm_date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-2" disabled="disabled" placeholder="to" name="option.confirm_date_to" />
							</div>
						</div>
						<div style="float: right">
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
								<th>团号</th>
								<th>预/决</th>
								<th>出团日期</th>
								<th>产品名称</th>
								<th>人数</th>
								<th>总团款</th>
								<th>机票</th>
								<th>火车票</th>
								<th>产品成本</th>
								<th>其他费用</th>
								<th>FLY</th>
								<th>毛利润</th>
								<th>人均毛利</th>
								<th>确认日期</th>
								<th>销售</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: reports">
							<tr>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $root.orderTypeMapping[$data.order_type]"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.people_count"></td>
								<td data-bind="text: $data.receivable" class="rmb"></td>
								<td data-bind="text: $data.air_ticket_cost" class="rmb"></td>
								<td data-bind="text: $data.train_ticket_cost" class="rmb"></td>
								<td data-bind="text: $data.product_cost" class="rmb"></td>
								<td data-bind="text: $data.other_cost" class="rmb"></td>
								<td data-bind="text: $data.fy" class="rmb"></td>
								<td data-bind="text: $data.gross_profit" class="rmb"></td>
								<td data-bind="text: $data.per_profit" class="rmb"></td>
								<td data-bind="text: $data.confirm_date"></td>
								<td data-bind="text: $data.sale_name"></td>
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
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/order-report.js"></script>
</body>
</html>