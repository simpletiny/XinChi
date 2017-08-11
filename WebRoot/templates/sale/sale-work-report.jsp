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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
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
			<h2>销售工作报表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="日期" name="option.date" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" data-bind="value:month" placeholder="月份" name="option.month" />
							</div>
						</div>
						<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales" data-bind="options: sales_name, optionsCaption: '全部'" name="option.sale_name"></select>
								</div>
							</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">客户</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="客户" name="option.client_employee_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">财务主体</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="财务主体" name="option.client_name" />
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>销售</th>
								<th>日期</th>
								<th>订单总数</th>
								<th>客户</th>
								<th>财务主体</th>
								<th style="width: 10%">拜访目的</th>
								<th style="width: 10%">辅助目的</th>
								<th style="width: 10%">效果评估</th>
								<th>精推产品</th>
								<th>效果评估</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: reports">
							<tr>
								<td data-bind="text: $data.sale_name"></td>
								<td data-bind="text: $data.date"></td>
								<td data-bind="text: $data.order_count"></td>
								<td data-bind="text: $data.client_employee_name"></td>
								<td data-bind="text: $data.client_name"></td>
								<td colspan="3">
									<table class="table table-striped table-hover">
										<tbody data-bind="foreach: $data.visits">
											<tr>
												<td style="width: 33.3%; word-break: break-all" data-bind="text: $data.target"></td>
												<td style="width: 33.3%; word-break: break-all" data-bind="text: $data.sub_target"></td>
												<td style="width: 33.3%; word-break: break-all" data-bind="text: $data.summary"></td>
											</tr>
										</tbody>
									</table>
								</td>
								<td colspan="2">
									<table class="table table-striped table-hover">
										<tbody data-bind="foreach: $data.accurates">
											<tr>
												<td style="width: 33.3%; word-break: break-all" data-bind="text: $data.product"></td>
												<td style="width: 33.3%; word-break: break-all" data-bind="text: $data.summary"></td>
											</tr>
										</tbody>
									</table>
								</td>
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
		$(".manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/sale/sale-work-report.js"></script>
</body>
</html>