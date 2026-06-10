<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
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
			<h2>
				支付详情<a href="javascript:void(0)" onclick="javascript:history.back(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>返回</a>
			</h2>
		</div>
		<input type="hidden" id="pay-number" value="<%=key%>" />
		<div class="main-container">
			<div class="main-box">
			<div class="form-box info-form">
					<div class="input-row clearfloat">
						<h3>单号：<%=key%></h3>
						<div class="col-md-6">
							<label class="l">支付总金额</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: ps().money"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">支付人</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: ps().pay_user"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">申请人</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: ps().apply_user"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">申请日期</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: moment(ps().apply_date-0).format('YYYY-MM-DD')"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">审批人</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: ps().approval_user"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">审批日期</label>
							<div class="ip">
								<p class="ip-default" data-bind="text:  moment(ps().approval_date-0).format('YYYY-MM-DD')"></p>
							</div>
						</div>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>支出账户</th>
								<th>支出时间</th>
								<th>金额</th>
								<th>收款方</th>
								<th>凭证</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: details">
							<tr>
								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.time"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.receiver" ></td>
								<td><a href="javascript:void(0)" data-bind="click: function() {$parent.checkVoucherPic($data.voucher_file_name,$data.account_pk)} ">查看</a></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
<div id="pic-check" style="display:none">
 	<jsp:include page="../common/check-picture.jsp" />
 </div>
	<script>
		$(".finance").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/accounting/paid-detail-view.js"></script>
</body>
</html>