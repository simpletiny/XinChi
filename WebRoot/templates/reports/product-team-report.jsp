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

.fix_width {
	width: 60% !important
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品单团报表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: statuses" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="option.report_statuses" /><label data-bind="text: $root.approvedMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" data-bind="value:month" placeholder="起始月"
									name="option.confirm_date_from" />
							</div>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" data-bind="value:month" placeholder="截止月"
									name="option.confirm_date_to" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品经理</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: sales, optionsCaption: '全部',optionsValue:'user_number',optionsText:'user_name',event{change:refresh}"
									name="option.product_manager_number"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="padding-top: 3px; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<hr />
				<div class="form-box info-form">
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">团款：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().receivable"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">立款：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().discount_receivable"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">98清尾：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().tail98"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">人数：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().adult_count+report().special_count"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">机票：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().air_ticket_cost"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">地接成本：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().product_cost"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">其他成本：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().other_cost"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">fy：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().fy"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">后台成本：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().sys_cost"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">销售费用：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().sale_cost"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">毛利：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().gross_profit"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">人均毛利：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: report().per_profit"></p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/reports/product-team-report.js"></script>
</body>
</html>