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
</style>
<jsp:include page="../layout.jsp" />
</head>
<body>
	<div class="main-body">
		<div class="subtitle" style="float: left">
			<h2 style="width: 30%; float: left">明日送机单</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">送机日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="送机日期" data-bind="value:first_ticket_date()"
									name="drop_off.first_ticket_date" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">出发城市</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="出发城市" data-bind="value:from_city()"
									name="drop_off.from_city" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">到达城市</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="到达城市" data-bind="value:from_city()"
									name="drop_off.to_city" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
										name="drop_off.client_number"></select>
								</div>
							</div>
						</s:if>
						<div style="padding-top: 15px;float:right"> 
							<button type="submit" class="btn btn-green col-md-1" st="btn-search" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result" id="div-table">
					<table class="table table-striped table-hover" style="white-space: pre-line">
						<thead>
							<tr role="row">
								<th style="width:10%">订单号</th>
								<th style="width:10%">团号</th>
								<th style="width:15%">航班信息</th>
								<th style="width:35%">名单</th>
								<th style="width:20%">电话</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')">
									<th style="width:10%">产品经理</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: drop_offs">
							<tr style="border-bottom: solid 1px black">
								<td data-bind="text: $data.product_order_number" st="order-number"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.infos"></td>
								<td data-bind="text: $data.name_list"></td>
								<td data-bind="text: $data.phones"></td>
								<s:if test="#session.user.user_roles.contains('ADMIN')">
									<td data-bind="text: $data.client_name"></td>
								</s:if>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".order-operate").addClass("current").children("ol").css("display",
				"block");
	</script>

	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/drop-off.js"></script>
</body>
</html>