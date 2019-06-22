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
	position: fixed;
	right: 8%;
	z-index: 100;
}

.deletePassenger {
	position: absolute;
	background: #FFEC8B;
	border: solid 1px red;
	z-index: 200
}

.fixed {
	font-size: 12px;
	display: block;
	position: fixed;
	right: 15%;
	top: 200px;
	margin-left: 10px;
	z-index: 100;
	width: 100px;
}

.fixed button {
	width: 80px;
	margin-top: 5px;
	display: block;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>待操作名单</h2>
		</div>
		<div class="fixed">
			<div style="width: 30%; float: right">
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { operate() }">分配</button>
			</div>
		</div>
		<div class="main-container">
			<div class="main-box" id="div-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">客户</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="客户" name="passenger.client_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="团号" name="passenger.team_number" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">乘机人</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="乘机人" name="passenger.name" />
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green" data-bind="click: refresh">搜索</button>
							<!-- <button type="submit" class="btn btn-green" data-bind="click: doCopy">复制选中的名单信息</button> -->
						</div>
					</div>
				</form>
				<div class="list-result" id="div-table">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th><input type="checkbox" onclick="checkAll(this)" />全选</th>
								<th>乘机人</th>
								<th>团号</th>
								<th>客户</th>
								<th>首段日期</th>
								<th>首航段</th>
								<th>身份证号</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: passengers">
							<tr style="overflow: hidden;">
								<!--  onclick="showDetail(this)" -->
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+':'+$data.name+':'+$data.id}, checked: $root.chosenPassengers" /></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.client_name"></td>
								<td data-bind="text: $data.first_ticket_date"></td>
								<td data-bind="text: $data.first_from_to"></td>

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
				<select class="form-control" style="height: 34px" id="select-ticket-source"
					data-bind="options: existsSources,optionsText:'name',optionsValue:'index', optionsCaption: '新增'"></select>
			</div>
		</div>
		<div class="form-group" style="float: right; margin-top: 10px">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { pickTicketSource() }">确认</button>
		</div>
	</div>

	<div id="supplier-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">姓名</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="姓名" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchSupplierEmployee }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>姓名</th>
								<th>财务主体</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: supplierEmployees">
							<tr data-bind="event: {click: function(){ $parent.pickSupplierEmployee($data.name,$data.pk)}}">
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
							</tr>
						</tbody>
					</table>
					<div class="pagination clearfloat">
						<a data-bind="click: previousPage1, enable: currentPage1() > 1" class="prev">Prev</a>
						<!-- ko foreach: pageNums1 -->
						<!-- ko if: $data == $root.currentPage1() -->
						<span class="current" data-bind="text: $data"></span>
						<!-- /ko -->
						<!-- ko ifnot: $data == $root.currentPage1() -->
						<a data-bind="text: $data, click: $root.turnPage1"></a>
						<!-- /ko -->
						<!-- /ko -->
						<a data-bind="click: nextPage1, enable: currentPage1() < pageNums1().length" class="next">Next</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<form id="data-form" method="post" action="<%=basePath%>ticket/operatePassengers" style="display: none">
		<input name="json" id="json-data" />
	</form>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/ticket/name-list.js"></script>
</body>
</html>