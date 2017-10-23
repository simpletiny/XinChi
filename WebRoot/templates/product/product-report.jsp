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
 <link rel="stylesheet" type="text/css" href="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.css"/>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品报表</h2>
		</div>
		
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品编号</label>
							<div class="col-md-2">
								<input class="form-control" name="report_option.product_number"></input>
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
						<div class="span6">
							<label class="col-md-1 control-label">产品经理</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px" data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'" name="report_option.product_manager_number"></select>
							</div>
						</div>
						</s:if>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="report_option.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="report_option.date_to" />
							</div>
						</div>
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>产品编号</th>
								<th>产品名称</th>
								<th>状态</th>
								<th>团号</th>
								<th>出团日期</th>
								<th>成人</th>
								<th>特殊</th>
								<th>销售</th>
								<th>操作</th>
								<th>产品经理</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: reports">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenReports" /></td>
								<td data-bind="text: $data.product_number"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.status"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.adult_count"></td>
								<td data-bind="text: $data.special_count"></td>
								<td data-bind="text: $data.sale_name"></td>
								<td></td>
								<td data-bind="text: $data.product_manager"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>汇总</td>
							<td data-bind="text:totalAdult" ></td>
							<td data-bind="text:totalSpecial"></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
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
	<script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath %>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-report.js"></script>
</body>
</html>