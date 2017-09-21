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
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.deleteDiv {
	position: relative;
	display: block;
	width: 12px;
	height: 12px;
	background: url(../../static/img/mc-icon-cancel.png) no-repeat;
	background-size: cover;
	margin-right: 5px;
	padding-top: 10px;
	z-index: 999;
	float: right;
	cursor: pointer;
}

.right-div {
	display: none;
	width: 25%;
	float: left;
	margin-left: 10px;
	margin-top: 10px;
	background: #fff;
}

.deletePassenger {
	position: absolute;
	background: #FFEC8B;
	border: solid 1px red;
	z-index: 200
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>名单操作</h2>
		</div>

		<div class="main-container">
			<div class="main-box" id="div-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { operate() }">分配</button>
						</div>
					</div>
				</form>
				<div class="list-result" id="div-table">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th><input type="checkbox" onclick="checkAll(this)"/>全选</th>
								<th>客户</th>
								<th>团号</th>
								<th>首段日期</th>
								<th>首航段</th>
								<th>乘机人</th>
								<th>身份证号</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: passengers">
							<tr style="overflow: hidden" onclick="showDetail(this)">
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk+':'+$data.name}, checked: $root.chosenPassengers" /></td>
								<td data-bind="text: $data.client_name"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.first_ticket_date"></td>
								<td data-bind="text: $data.first_from_to"></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.id"></td>
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
				<div class="right-div">
					<div style="float: right; display: block; padding-top: 10px">
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { finishChosen() }">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="source-pick" style="display: none; width: 300px">

		<div class="form-group">
			<label class="col-md-3 control-label" style="float: left">票源</label>
			<div style="width: 70%; float: left">
				<select class="form-control" style="height: 34px" id="select-ticket-source" data-bind="options: existsSources,optionsText:'name',optionsValue:'index', optionsCaption: '新增'"></select>
			</div>
		</div>
		<div class="form-group" style="float: right; margin-top: 10px">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { pickTicketSource() }">确认</button>
		</div>
	</div>
	<form id="data-form" method="post" action="<%=basePath%>ticket/operatePassengers" style="display: none">
		<input name="json" id="json-data"/>
	</form>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/ticket/name-list.js"></script>
</body>
</html>