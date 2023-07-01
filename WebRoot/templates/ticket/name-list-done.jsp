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
	display: block;
	position: fixed;
	right: 10px;
	top: 200px;
	z-index: 100;
	width: 100px;
}

.fixed input {
	margin-top: 5px;
	display: inline-block;
}

.error {
	color: red;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>已出票名单</h2>
		</div>
		<div class="fixed">
			<input type="button" class="btn btn-green" id="copy" value="复制"></input>
		</div>
		<div class="main-container">
			<div class="main-box" id="div-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="float: left">
							<button type="submit" class="btn btn-green" data-bind="click: function() {unlockName()}">解锁名单</button>
						</div>
						<div style="float: right">
							<button type="submit" class="btn btn-green" data-bind="click: function() { lockName() }">锁定名单</button>
							<button type="button" class="btn btn-green" data-bind="click:flightChange">航变</button>
							<button type="button" class="btn btn-green" data-bind="click:rollBack">打回重出</button>
						</div>
					</div>
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
					</div>

					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">首航日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="passenger.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="passenger.date_to" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">首航段</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="首航段" name="passenger.first_from_to" />
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result" id="div-table">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th><input type="checkbox" id="chk-all" onclick="checkAll(this)" />全选</th>
								<th>序号</th>
								<th>乘机人</th>
								<th>身份证号</th>
								<th>电话1</th>
								<th>电话2</th>
								<th>订单号</th>
								<th>团号</th>
								<th>首段日期</th>
								<th>天数</th>
								<th>客户</th>
								<th>首航段</th>
								<th>价格</th>
								<th>需求备注</th>
								<th>退团</th>
								<th>名单</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: passengers">
							<tr style="overflow: hidden;" ondblclick="checkSameOrderNumber(this)">
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+':'+$data.name+':'+$data.id+':'+$data.team_number+':'+$data.order_number+':'+$data.name_confirm_status+':'+$data.status+':'+$data.cellphone_A}, checked: $root.chosenPassengers" /></td>
								<td data-bind="text: $index()+1"></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.id"></td>
								<td data-bind="text: $data.cellphone_A"></td>
								<td data-bind="text: $data.cellphone_B"></td>
								<td st="order-number" data-bind="text: $data.order_number"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.first_ticket_date"></td>
								<td data-bind="text: $data.days"></td>
								<td data-bind="text: $data.client_name"></td>
								<td data-bind="text: $data.first_from_to"></td>
								<td data-bind="text: $data.ticket_cost+$data.change_cost" class="rmb"></td>
								<td data-bind="text: $data.need_comment"></td>
								<td><span data-bind="text:$root.deleteMapping[$data.delete_flg]"></span></td>
								<td><span data-bind="text:$root.lockMapping[$data.lock_flg]"></span></td>
								<!-- ko if:$data.status=='Y' -->
								<td data-bind="text:$root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='C' -->
								<td><a href="javascript:void(0)" style="color: red"
									data-bind="text:$root.statusMapping[$data.status],click:function(){$root.checkChange($data.pk);}"></a></td>
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
				<div class="right-div">
					<div style="float: right; display: block; padding-top: 10px">
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { finishChosen() }">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="div-flight-change" style="display: none; width: 800px; height: 650px; overflow: auto">
		<form class="form-box info-form" id="form-change">
			<div class="input-row clearfloat" id="air-ticket">
				<table style="width: 100%" class="table table-striped table-hover" id="change-name-table">
					<thead>
						<tr>
							<th style="width: 15%">姓名</th>
							<th style="width: 40%">身份证号</th>
							<th style="width: 15%">价格</th>
							<th style="width: 30%">航变成本<input type="checkbox" id="change-all" /></th>
						</tr>
					</thead>
					<tbody data-bind="foreach:changeNames">
						<tr>
							<input type="hidden" data-bind="value:$data.pk" st="name-pk" />
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
							<td data-bind="text:$data.ticket_cost"></td>
							<td><input class="form-control" type="number" placeholder="负数即有退款" st="change-cost-person"
								oninput="calSum()" required /></td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 25%">航变原因</label>
					<div class="ip">
						<input class="form-control" maxlength="10" placeholder="10个字以内" st="change-reason" name="change_reason" required />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 25%">航变成本</label>
					<div class="ip">
						<input class="form-control" type="number" placeholder="增加的费用，负数即有退款" st="change-cost" name="change_cost" required />
					</div>
				</div>
			</div>

			<hr></hr>
			<div id="div-allot">
				<!-- ko foreach:changeTicketSources -->
				<div class="input-row clearfloat">
					<div class="col-md-6">
						<p class="ip-default" data-bind="text:$data.name"></p>
					</div>
					<div class="col-md-6 required">
						<label class="l" style="width: 25%">分配成本</label>
						<div class="ip">
							<input type="hidden" st="ticket-source-pk" data-bind="value:$data.pk" /> <input class="form-control"
								type="number" st="money" placeholder="分配成本" />
						</div>
					</div>
				</div>
				<!-- /ko -->
			</div>
			<hr></hr>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l">航变备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default comment" st="comment" name="comment" rows="5" maxlength="200"
							placeholder="需要备注说明的信息" required></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="text-align: right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: function(){doFlightChange();}">提交</a> <a
						type="submit" class="btn btn-green btn-r" data-bind="click: cancelChange()">取消</a>
				</div>
			</div>
		</form>
	</div>

	<div id="div-check-change" style="display: none; width: 800px; height: 300px; overflow: auto">

		<div class="input-row clearfloat">
			<div class="col-md-6 ">
				<label class="l" style="width: 25%">航变原因</label>
				<div class="ip">
					<p class="ip-default" data-bind="text:changeLog().change_reason"></p>
				</div>
			</div>
			<div class="col-md-6 ">
				<label class="l" style="width: 25%">航变成本</label>
				<div class="ip">
					<p class="ip-default" data-bind="text:changeLog().change_cost"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-12 ">
				<label class="l">航变备注</label>
				<div class="ip">
					<p class="ip-default" data-bind="text:changeLog().comment"></p>
				</div>
			</div>
		</div>
	</div>

	<script>
		$(".ticket-operation").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/name-list-done.js?v1.002"></script>
	<script src="<%=basePath%>static/vendor/clipboard.min.js"></script>
</body>
</html>