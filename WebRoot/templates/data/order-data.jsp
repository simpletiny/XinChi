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
			<h2>订单数据</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">横轴</label>
							<div class="col-md-2">
								<select class="form-control"
									data-bind="options: horizontal,optionsText: 'name', optionsValue: 'key',event:{change:changeRangeType}"
									name="order_count.horizontal"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">纵轴</label>
							<div class="col-md-2">
								<select class="form-control" id="sel-vertical" data-bind="options: vertical,optionsText: 'name', optionsValue: 'key'"
									name="order_count.vertical"></select>
							</div>
						</div>
		<%-- 				<div class="span6">
							<label class="col-md-1 control-label">数据</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: data_type,optionsText: 'name', optionsValue: 'key'"
									name="order_count.data_type"></select>
							</div>
						</div> --%>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">时间范围</label>
							<div class="col-md-2">
								<select id="sel-year" class="form-control" data-bind="options: years,optionsText: 'name', optionsValue: 'key'"
									name="order_count.option_year"></select> <input id="txt-month" data-bind="value:current_month" type="text"
									style="display: none" class="form-control month-picker-st" name="order_count.option_month" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">生成图表</button>
							</div>
						</div>
					</div>
				</form>

				<div id="main" style="width: 1000px; height: 500px"></div>
			</div>
		</div>
	</div>

	<script>
		$(".data").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/echart/echarts.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/data/order-data.js"></script>
</body>
</html>
