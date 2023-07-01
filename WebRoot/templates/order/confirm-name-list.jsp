<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<style>
.form-box {
	position: relative;
}

.flg {
	position: absolute;
	top: 50px;
	right: 150px;
	display: block;
	color: red; 
	z-index: 1;
	width:150px;
	height:50px; 
}

.confirm-text {
	display: inline-block;
	font-size: 20px ;
	font-weight:bold;
	transform: rotate(-15deg); 
}
</style>
</head>
<body>
	<div class="main-body">
		<input type="hidden" id="employee_key" value="<%=key%>" />
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				待确认名单<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create">返回</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box" data-bind="foreach:orders">
				<div class="form-box info-form">
					<div class="flg">
						<span class="confirm-text" data-bind="text:$root.statusMapping[$data.name_confirm_status]"></span>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">团号</label>
							<div class="ip" style="width: 40%">
								<p class="ip-default" data-bind="text: $data.team_number"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">客户</label>
							<div class="ip" style="width: 40%">
								<p class="ip-default" data-bind="text:  $data.client_employee_name"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">产品</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: $data.product_name"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">出团日期</label>
							<div class="ip" style="width: 40%">
								<p class="ip-" data-bind="text: $data.departure_date"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">确认日期</label>
							<div class="ip" style="width: 40%">
								<p class="ip-" data-bind="text: $data.confirm_date"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">人数</label>
							<div class="ip">
								<p class="ip-" data-bind="text: $data.people_count"></p>
							</div>
						</div>
					</div>

					<div class="input-row clearfloat">
						<div class="col-md-6">
							<table style="width: 100%" class="table table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 10%">序号</th>
										<th style="width: 15%">姓名</th>
										<th style="width: 25%">身份证号</th>
									</tr>
								</thead>
								<tbody data-bind="foreach:name_list">
									<!-- ko if:$index()%2==0 -->
									<tr>
										<td data-bind="text:$index()+1"></td>
										<td data-bind="text:$data.name"></td>
										<td data-bind="text:$data.id"></td>
									</tr>
									<!-- /ko -->
								</tbody>
							</table>
						</div>
						<!-- ko if:name_list.length>1 -->
						<div class="col-md-6">
							<table style="width: 100%" class="table table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 10%">序号</th>
										<th style="width: 15%">姓名</th>
										<th style="width: 25%">身份证号</th>
									</tr>
								</thead>
								<tbody data-bind="foreach:name_list">

									<!-- ko if:$index()%2!=0 -->
									<tr>
										<td data-bind="text:$index()+1"></td>
										<td data-bind="text:$data.name"></td>
										<td data-bind="text:$data.id"></td>
									</tr>
									<!-- /ko -->
								</tbody>
							</table>
						</div>
						<!-- /ko -->
					</div>
					<div class="input-row clearfloat">
						<button type="submit" class="btn btn-green" style="float: right"
							data-bind="click: function() { $root.confirmNameList($data.team_number) }">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/order/confirm-name-list.js?v=1.001"></script>
</body>
</html>