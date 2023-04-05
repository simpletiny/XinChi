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
tr td {
	text-overflow: ellipsis; /* for IE */
	-moz-text-overflow: ellipsis; /* for Firefox,mozilla */
	overflow: hidden;
	white-space: nowrap;
	text-align: left
}

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
			<h2>已决算订单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
						<div class="form-group">
							<div style="float: right; padding-right: 100px">
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: rollBack">打回重报</button>
							</div>
						</div>
					</s:if>
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
							<label class="col-md-1 control-label"><input type="radio" value="1" onclick="check(this)" checked
								name="radio_date" />出团日期</label>
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
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="团号" name="option.team_number"></input>
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="event:{change:refresh()},options: sales,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'"
										name="option.sale_number"></select>
								</div>
							</div>
						</s:if>
						<s:else>
							<div class="col-md-3">&nbsp;</div>
						</s:else>
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" value="2" onclick="check(this)"
								name="radio_date" />确认日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-2" disabled="disabled" placeholder="from"
									name="option.confirm_date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-2" disabled="disabled" placeholder="to"
									name="option.confirm_date_to" />
							</div>
						</div>
						<div style="float: right">
							<div>
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>状态</th>
								<th>团号</th>
								<th>客户</th>
								<th>财务主体</th>
								<th>产品名称</th>
								<th>确认月</th>
								<th>出团日期</th>
								<th>游客信息</th>
								<th>预算团款</th>
								<th>决算团款</th>
								<th>决算单</th>
								<th>确认件</th>
								<th>产品经理</th>
								<th>决算详情</th>
								<th>凭证</th>
								<th>文件下载</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>销售</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenOrders" /></td>
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.client_employee_name"></td>
								<td data-bind="text: $data.client_name"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: moment($data.confirm_date).format('YYYY-MM')"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkPassengers,text: $data.passenger_captain"></a></td>
								<td class="rmb" data-bind="text: $data.budget_receivable"></td>
								<td class="rmb" data-bind="text: $data.final_receivable"></td>
								<td><a href="javascript:void(0)"
									data-bind="click: function() {$root.checkConfirmPic($data.confirm_file,$data.team_number)} ">查看</a></td>
								<td><a href="javascript:void(0)"
									data-bind="click: function() {$root.checkBudgetConfirmPic($data.confirm_file,$data.create_user)} ">查看</a></td>
								<td data-bind="text: $data.product_manager"></td>
								<td><a href="javascript:void(0)" data-bind="click: function() {$root.checkFinalDetail($data)} ">查看</a></td>
								<td><a href="javascript:void(0)"
									data-bind="click: function() {$root.checkVoucherPic($data.voucher_file,$data.team_number)} ">查看</a></td>
								<td><a href="javascript:void(0)" class="download" data-bind="click:$root.downloadFile">下载</a></td>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text:$data.sale_name"></td>
								</s:if>
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

	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 9%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 35%">身份证号</th>
							<th style="width: 23%">电话1</th>
							<th style="width: 23%">电话2</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
							<td data-bind="text:$data.cellphone_A"></td>
							<td data-bind="text:$data.cellphone_B"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="final-check" style="display: none; width: 800px">
		<div style="display: none" id="div-1">
			<h1 data-bind="text:detailMsg()"></h1>
		</div>
		<div style="display: none" id="div-2">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l">增加款项</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:order().raise_money"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l">减少款项</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:order().reduce_money"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l">增加款项说明</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:order().raise_comment"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l">减少款项说明</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:order().reduce_comment"></p>
					</div>
				</div>
			</div>
		</div>
		<div style="display: none" id="div-3">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l">投诉扣款</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:order().complain_money"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l">投诉原因</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:order().complain_reason"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l">解决方案</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:order().complain_solution"></p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<div id="comment-edit" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<div>
				<input type="hidden" data-bind="value:order().pk" id="txt-order-pk" /> <input type="hidden"
					data-bind="value:order().standard_flg" id="txt-standard-flg" /> <label class="l">备注</label>
				<div class="ip">
					<textarea type="text" class="ip-default" rows="10" maxlength="200" id="txt-comment"
						data-bind="value: order().comment" placeholder="备注"></textarea>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" data-bind="click: cancelEditComment">取消</a> <a type="submit"
					class="btn btn-green btn-r" data-bind="click: updateComment">保存</a>
			</div>
		</div>
	</div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/f-order.js"></script>
</body>
</html>