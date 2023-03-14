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
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品利润</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
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
								<th>分数</th>
								<th>毛利润</th>
								<th>产品费用</th>
								<th>唯品费</th>
								<th>机票损失</th>
								<th>其它费用</th>
								<th>产品利润</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: reports">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenReports" /></td>
								<td data-bind="text: $data.departure_month"></td>
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<td data-bind="text: $data.people_count"></td>
								<td data-bind="text: $data.score"></td>
								<td data-bind="text: $data.gross_profit" class="rmb"></td>
								<td data-bind="text: $data.product_cost" class="rmb"></td>
								<td data-bind="text: $data.keep_cost" class="rmb"></td>
								<td></td>
								<td></td>
								<td data-bind="text: $data.gross_profit" class="rmb"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td>汇总</td>
							<td data-bind="text:peopleCount"></td>
							<td data-bind="text:grossProfit"></td>
							<td data-bind="text:productCost"></td>
							<td data-bind="text:keepCost"></td>
							<td></td>
							<td></td>
							<td data-bind="text:grossProfit"></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-profit.js"></script>
</body>
</html>