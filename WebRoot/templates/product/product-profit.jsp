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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css?v1.001" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品利润</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form id="form-search" class="form-horizontal search-panel">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" st="btn-search" class="btn btn-green" data-bind="click: addReceive">添加收入</button>
							<button type="submit" st="btn-search" class="btn btn-green" data-bind="click: addPay">添加支出</button>
							<button type="submit" st="btn-search" class="btn btn-green" data-bind="click: confirm">扎账</button>
						</div>
					</div>
					<div class="form-group">
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'"
										name="productProfit.user_number"></select>
								</div>
							</div>
						</s:if>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">出团年份</label>
							<div class="col-md-2">
								<select id="sel-year" class="form-control" data-bind="options: years,optionsText: 'name', optionsValue: 'key'"
									name="productProfit.option_year"></select>
							</div>
						</div>

						<div style="width: 30%; float: right">
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
								<th></th>
								<th>出团月份</th>
								<th>状态</th>
								<th>实发人数</th>
								<!-- ko if:isSpecificDate -->
								<th>分数</th>
								<!-- /ko -->
								<th>产品毛利</th>
								<th>机票手续费</th>
								<th>押金扣款</th>
								<th>产品费用</th>
								<th>唯品费</th>
								<th>投诉赔偿</th>
								<th>其它费用</th>
								<th>产品净利</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: reports">
							<tr>
								<td><input type="checkbox" data-bind="value:$data, checked: $root.chosenReports" /></td>
								<td data-bind="text: $data.departure_month"></td>
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<td data-bind="text: $data.people_count"></td>
								<!-- ko if:$root.isSpecificDate -->
								<td data-bind="text: $data.score"></td>
								<!-- /ko -->
								<td data-bind="text: $data.gross_profit" class="rmb"></td>
								<td data-bind="text: $data.service_fees" class="rmb"></td>
								<td data-bind="text: $data.deposit_deduct" class="rmb"></td>
								<td data-bind="text: $data.product_cost" class="rmb"></td>
								<td data-bind="text: $data.keep_cost" class="rmb"></td>
								<td></td>
								<td data-bind="text:$data.other_cost"></td>
								<td
									data-bind="text: $data.gross_profit-$data.service_fees- $data.deposit_deduct-$data.product_cost-$data.keep_cost-$data.other_cost"
									class="rmb"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="div-reconciliation" style="display: none; width: 1000px; height: 300px; overflow: auto; padding-top: 30px;">
		<form id="form-reconciliation">
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">金额</label>
					<div class="ip" style="width: 40%">
						<input type="number" placeholder="金额" class="ip-" name="reconciliation.money" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">归属月份</label>
					<div class="ip" style="width: 40%">
						<input type="text" class="ip- month-picker-st" name="reconciliation.belong_month" required placeholder="归属月份" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-8 required">
					<label class="l">备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="7" maxlength="200" required name="reconciliation.comment" placeholder="需要备注说明的信息"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: doReconciliation">提交</a> <a type="button"
							class="btn btn-green btn-r" data-bind="click: cancelReconciliation">取消</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<script>
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-profit.js?v=1.004"></script>
</body>
</html>