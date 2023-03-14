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
			<h2 style="width: 30%; float: left">销售仪表盘-->回款分值</h2>
			<div class="tab">
					<input type="radio"  name="rad-page" value="s" onclick="changePage(this)"/><label>分值查询</label>
				<input type="radio" checked="checked" name="rad-page" value="b" onclick="changePage(this)"/><label>回款分值</label>
			</div>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" name="rad-confirm"  value="month" onclick="switchType(this)" checked="checked"/>确认月份</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control month-picker-st" data-bind="value:confirm_month()" placeholder="确认月份"
									name="score.confirm_month" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" value="date" onclick="switchType(this)" name="rad-confirm"/>确认日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" disabled="disabled" placeholder="起始日期"
									name="score.date_from" />
							</div>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" disabled="disabled" placeholder="截止日期"
									name="score.date_to" />
							</div>
							<label class="col-md-1 control-label">立款折扣</label>
							<div class="col-md-2" style="float: left">
								<input type="number" class="form-control" placeholder="折扣（默认1）"
									name="score.discount" id="txt-discount"/>
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" st="btn-search" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" style="width:50%">
						<thead>
							<tr role="row">
								<th>销售</th>
								<th>分值</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: scores">
							<tr>
								<td data-bind="text: $data.sale_name"></td>
								<td data-bind="text: $data.score.toFixed(2)"></td>
							</tr>
						</tbody>
					</table>
					<div class="pagination clearfloat" style="width:50%">
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
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>

	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/sale/back-money-score.js"></script>
</body>
</html>