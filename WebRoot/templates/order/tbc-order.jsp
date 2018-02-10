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

<style>
.download-panel {
	position: absolute;
	background: #FFEC8B;
	border: solid 1px red;
	z-index: 200;
	width: 150px;
	height: 50px;
	text-align: center;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<!-- tbc for to be confirmed -->
			<h2>待确认订单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deleteOrder() }">删除</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editOrder() }">编辑</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { confirmOrder() }">订单确认</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">客户</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="客户" name="option.client_employee_name"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品" name="option.product_name"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="from"
									name="option.departure_date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="to"
									name="option.departure_date_to" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
										name="option.create_user"></select>
								</div>
							</div>
						</s:if>
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
								<th>客户</th>
								<th>财务主体</th>
								<th>产品名称</th>
								<th>天数</th>
								<th>成人</th>
								<th>特殊</th>
								<th>出团日期</th>
								<th>首段城市对</th>
								<th>总团款</th>
								<th>确认件</th>
								<th>订单备注</th>
								<th>产品经理</th>
								<th>下载文件</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>销售</th>
								</s:if>
								<th title="订单将在编辑日期三天后自动删除！">编辑日期</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+';'+$data.standard_flg}, checked: $root.chosenOrders" /></td>
								<td data-bind="text: $data.client_employee_name"></td>
								<td data-bind="text: $data.client_name"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.days"></td>
								<td data-bind="text: $data.adult_count"></td>
								<td data-bind="text: $data.special_count"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.start_city +'-'+ $data.end_city"></td>
								<td data-bind="text: $data.receivable"></td>
								<!-- ko if:$data.confirm_file!=null && $data.confirm_file != '' -->
								<td><a href="javascript:void(0)"
									data-bind="click: function() {$root.checkIdPic($data.confirm_file,$data.create_user_number)} ">查看</a></td>
								<!-- /ko -->
								<!-- ko if: $data.confirm_file==null || $data.confirm_file == '' -->
								<td></td>
								<!-- /ko -->
								<td data-bind="text: $data.comment"></td>
								<td data-bind="text: $data.product_manager"></td>
								<td><a href="javascript:void(0)" class="download" data-bind="click:$root.downloadFile">下载</a></td>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.create_user"></td>
								</s:if>
								<!-- ko if:$data.update_time==null -->
								<td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD')"></td>
								<!-- /ko -->
								<!-- ko if:$data.update_time!=null -->
								<td data-bind="text: moment($data.update_time-0).format('YYYY-MM-DD')"></td>
								<!-- /ko -->
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
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/tbc-order.js"></script>
</body>
</html>