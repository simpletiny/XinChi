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

.tab {
	float: left;
	padding-top: 20px;
}

.tab label {
	font-size: 18px;
	font-weight: 500 !important;
	line-height: 1.2;
}
</style>
<jsp:include page="../layout.jsp" />
</head>
<body>
	<div class="main-body">
		<div class="subtitle" style="float: left">
			<h2 style="width: 30%; float: left">销售仪表盘-->分值查询(限标准订单)</h2>
			<div class="tab">
				<input type="radio" checked="checked" name="rad-page" value="s" onclick="changePage(this)" /><label>分值查询</label> <input
					type="radio" name="rad-page" value="b" onclick="changePage(this)" /><label>回款分值</label>
			</div>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">确认月份</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control month-picker-st" id="txt-confirm-month" data-bind="value:confirm_month()" placeholder="确认月份"
									name="score.confirm_month" />
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" st="btn-search" data-bind="click: refresh">搜索</button>
						</div>
					</div>
					<!-- <div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" value="date" onclick="switchType(this)"
								name="rad-confirm" />确认日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" disabled="disabled" placeholder="起始日期"
									name="score.date_from" />
							</div>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" disabled="disabled" placeholder="截止日期" name="score.date_to" />
							</div>
						</div>
						
					</div> -->
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" >
						<thead>
							<tr role="row">
								<th>销售</th>
								<th>上旬（分）</th>
								<th>中旬（分）</th>
								<th>下旬（分）</th>
								<th>月度（分）</th>
								<th style="border-right:1px dashed black">客均（分）</th>
								<th>上旬（人）</th>
								<th>中旬（人）</th>
								<th>下旬（人）</th>
								<th>标品（人）</th>
								<th>非标（人）</th>
								<th style="border-right:1px dashed black">机票（人）</th>
								<th>销售费用（元）</th>
								<th style="border-right:1px dashed black">个人工资（元）</th>
								<th>月份</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: scores">
							<tr>
								<td data-bind="text: $data.sale_name"></td>
								<td data-bind="text: $data.first_score"></td>
								<td data-bind="text: $data.middle_score"></td>
								<td data-bind="text: $data.last_score"></td>
								<td data-bind="text: $data.score"></td>
								<td data-bind="text: $data.ave_score" style="border-right:1px dashed black"></td>
								<td data-bind="text: $data.first_people"></td>
								<td data-bind="text: $data.middle_people"></td>
								<td data-bind="text: $data.last_people"></td>
								<td data-bind="text: $data.sum_people"></td>
								<td data-bind="text: $data.non_standard_people"></td>
								<td data-bind="text: $data.only_ticket_people" style="border-right:1px dashed black"></td>
								<td data-bind="text: $data.sale_cost" class="rmb"></td>
								<td data-bind="text: $data.sale_salary" class="rmb" style="border-right:1px dashed black"></td>
								<td data-bind="text: $data.confirm_month"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>

	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/sale/sale-score.js?v=1.001"></script>
</body>
</html>