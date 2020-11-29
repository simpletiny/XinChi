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
			<h2 style="width: 30%; float: left">分值查询(三个月内的标准订单)</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<div class="list-result">
					<table class="table table-striped table-hover" style="width: 50%">
						<thead>
							<tr role="row">
								<th>分值</th>
								<th>月份</th>
								<th>收人总数</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: scores">
							<tr>
								<td data-bind="text: $data.score"></td>
								<td data-bind="text: $data.confirm_month"></td>
								<td data-bind="text: $data.sum_people"></td>
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

	<script src="<%=basePath%>static/js/sale/sale-score-sale.js"></script>
</body>
</html>