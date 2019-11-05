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

tr td {
	text-overflow: ellipsis; /* for IE */
	-moz-text-overflow: ellipsis; /* for Firefox,mozilla */
	overflow: hidden;
	white-space: nowrap;
	text-align: left
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>收入详表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: rollBack">打回重报</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<div data-bind="foreach: allStatus" style="padding-top: 4px;" class="col-md-4">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data}, checked: $root.chosenStatus" name="detail.statuses" /><label
									data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">类型</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px" id="select-sales"
									data-bind="options: receivedTypes,  optionsText: 'value', optionsValue: 'key',value:chosenReceivedType,event:{change:refresh}, optionsCaption: '--全部--'"
									name="detail.type"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">账户</label>
							<div class="col-md-2" style="float: left">
								<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --'"
									name="detail.card_account"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">出团月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" onblur="baseMonth(this)" placeholder="出团月份"
									name="detail.month" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">收入日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="detail.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="detail.date_to" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="团号" name="detail.team_number" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="detail.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="detail.money_to" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">精确金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="精确金额" name="detail.money" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'user_number',event:{change:refresh}, optionsCaption: '--全部--'"
										name="detail.create_user"></select>
								</div>
							</div>
						</s:if>
						<div style="padding-top: 3px; float: right">
							<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>团号</th>
								<th>金额</th>
								<th>类型</th>
								<th>时间</th>
								<th>我方账户</th>
								<th>凭证</th>
								<th>客户</th>
								<th>摘要</th>
								<th>填报日期</th>
								<th>入账日期</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>销售</th>
								</s:if>
								<th>状态</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: receiveds">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+';'+$data.status}, checked: $root.chosenReceiveds" /></td>
								<td data-bind="text: $data.team_number"></td>
								<!-- ko if:$data.type=='SUM' -->
								<td data-bind="text: $data.allot_received" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if:$data.type!='SUM' -->
								<td data-bind="text: $data.received" class="rmb"></td>
								<!-- /ko -->
								<td data-bind="text: $root.typeMapping[$data.type]"></td>
								<td data-bind="text: $data.received_time"></td>
								<td data-bind="text: $data.card_account"></td>
								<!-- ko if:$data.type=='FLY' -->
								<td><a href="javascript:void(0)"
									data-bind="click: function() {$root.checkFlyVoucherPic($data.related_pk)} ">查看</a></td>
								<!-- /ko -->
								<!-- ko if:$data.type=='TAIL98' -->
								<td>无</td>
								<!-- /ko -->
								<!-- ko if:$data.type!='TAIL98' && $data.type!='FLY'-->
								<td><a href="javascript:void(0)"
									data-bind="click: function() {$root.checkVoucherPic($data.voucher_file,$data.received_time)} ">查看</a></td>
								<!-- /ko -->

								<!-- ko if:$data.type=='SUM' -->
								<td><a href="javascript:void(0)" data-bind="event:{click:function(){$root.viewDetail($data.related_pk)}}">详情</a></td>
								<!-- /ko -->
								<!-- ko if:$data.type!='SUM' -->
								<td data-bind="text: $data.client_employee_name"></td>
								<!-- /ko -->
								<td><a href="javascript:void(0)" data-bind="event:{click:function(){$root.viewComment($data)}}">详情</a></td>
								<td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD')"></td>
								<!-- ko if:$data.status!='E' -->
								<td></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='E' -->
									<!-- ko if:$data.type=='FLY' || $data.type=='PAY' -->
									<td data-bind="text: moment($data.confirm_time-0).format('YYYY-MM-DD')"></td>
									<!-- /ko -->
									<!-- ko if:$data.type!='FLY'&& $data.type!='PAY' -->
									<td data-bind="text: moment($data.confirm_time).format('YYYY-MM-DD')"></td>
									<!-- /ko -->
								<!-- /ko -->
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.user_name"></td>
								</s:if>

								<td data-bind="text: $root.statusMapping[$data.status]"></td>
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
	<div id="sum_detail" style="display: none; width: 800px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">账户</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().card_account"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">入账总金额</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().sum_received" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">入账时间</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().received_time"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">我组金额</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().allot_received" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">团号</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">客户</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">分配金额</label>
			</div>
		</div>
		<!-- ko foreach:sumDetails -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.team_number"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.client_employee_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.received" class="rmb"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<div id="comment" style="display: none; width: 800px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">团号</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().team_number"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">客户</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().client_employee_name" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">产品</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().product"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">人数</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().people_count" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">出团日期</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().departure_date"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">备注</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:comment()"></p>
				</div>
			</div>
		</div>
	</div>
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".sale").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/sale/received.js"></script>
</body>
</html>