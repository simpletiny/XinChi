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
			<h2>航变列表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>航变原因</th>
								<th>票损</th>
								<th>首航日期</th>
								<th>首航段</th>
								<th>名单</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: changes">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value':$data.pk}, checked: $root.chosenChanges" /></td>
								<td data-bind="text: $data.change_reason"></td>
								<td data-bind="text: $data.change_cost"></td>
								<td><a href="javascript:void(0)" data-bind="text:$data.first_date,click:function(){$root.checkTicketInfo($data);}"></a></td>
								<td><a href="javascript:void(0)" data-bind="text:$data.from_to_city,click:function(){$root.checkTicketInfo($data);}"></a></td>
								<td><a href="javascript:void(0)" data-bind="text:$data.captain,click:function(){$root.checkPassengers($data);}"></a></td>
								<td data-bind="text: $data.comment"></td>
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
	<div id="passengers-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index() + 1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 查看机票信息-->
	<div id="infos-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">日期</th>
							<th style="width: 10%">时间</th>
							<th style="width: 10%">城市对</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:infos">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.ticket_date"></td>
							<td data-bind="text:$data.from_to_time"></td>
							<td data-bind="text:$data.from_to_city"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/ticket-change.js?v=1.0"></script>
</body>
</html>