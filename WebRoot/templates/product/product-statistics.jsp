<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head profile="http://gmpg.org/xfn/11">
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品销售统计</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">时间范围</label>
							<div class="col-md-2">
								<input id="txt-month" data-bind="value:current_month" type="text" class="form-control month-picker-st"
									name="productOption.date_from" placeholder="from" />
							</div>
							<div class="col-md-2">
								<input id="txt-month" data-bind="value:current_month" type="text" class="form-control month-picker-st"
									name="productOption.date_to" placeholder="to" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
										name="productOption.product_manager_number"></select>
								</div>
							</div>
						</s:if>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">图标按</label>
							<div class="col-md-2">
								<select class="form-control"
									data-bind="options: chartTypes,optionsText: 'name', optionsValue: 'key',event:{change:changeRangeType}"
									name="order_count.horizontal"></select>
							</div>
						</div>
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result" style="height: 2200px">
					<table class="table table-striped table-hover" style="width: 50%; float: left">
						<thead>
							<tr role="row">
								<th>地区</th>
								<th>收客数</th>
								<th>订单数</th>
								<th>总分值</th>
							</tr>
						</thead>
						<tbody data-bind="foreach:areaData">
							<tr>
								<td data-bind="text:$data.area"></td>
								<td data-bind="text:$data.sum_people"></td>
								<td data-bind="text:$data.order_cnt"></td>
								<td data-bind="text:$data.score"></td>
							</tr>
						</tbody>
					</table>
					<div id="chart-area" style="margin-left: 50px; width: 45%; height: 350px; float: left"></div>
					<hr style="width: 100%">
					<table class="table table-striped table-hover" style="width: 50%; float: left">
						<thead>
							<tr role="row">
								<th>产品名称</th>
								<th>收客数</th>
								<th>订单数</th>
								<th>总分值</th>
							</tr>
						</thead>
						<tbody data-bind="foreach:productData">
							<tr>
								<td data-bind="text:$data.product_name"></td>
								<td data-bind="text:$data.sum_people"></td>
								<td data-bind="text:$data.order_cnt"></td>
								<td data-bind="text:$data.score"></td>
							</tr>
						</tbody>
					</table>
					<div id="chart-product" style="margin-left: 50px; width: 45%; height: 350px; float: left"></div>
					<hr style="width: 100%;">
					<table class="table table-striped table-hover" style="width: 50%; float: left">
						<thead>
							<tr role="row">
								<th>销售</th>
								<th>收客数</th>
								<th>订单数</th>
								<th>单均人数</th>
							</tr>
						</thead>
						<tbody data-bind="foreach:saleData">
							<tr>
								<td data-bind="text:$data.sale_name"></td>
								<td data-bind="text:$data.sum_people"></td>
								<td data-bind="text:$data.order_cnt"></td>
								<td data-bind="text:$data.score"></td>
							</tr>
						</tbody>
					</table>
					<div id="chart-sale" style="margin-left: 50px; width: 45%; height: 350px; float: left"></div>
				</div>

			</div>
		</div>
	</div>

	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/echart/echarts.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-statistics.js?v=1.0"></script>
</body>
</html>
