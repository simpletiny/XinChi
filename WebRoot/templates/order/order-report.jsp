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

<style type="text/css">
.detail-header .title {
	color: #6fa8dc;
	margin-left: 20px;
}

.detail-header .content {
	margin-left: 10px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>单团核算单</h2>
		</div>
		<s:hidden id="user_roles" value="%{#session.user.user_roles}" />
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: statuses" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="option.report_statuses" /><label data-bind="text: $root.approvedMapping[$data]"></label>
								</em>
							</div>
						</div>
						<div style="float: right">
							<button type="submit" st="btn-search" class="btn btn-green" data-bind="click: addReceive">添加收入</button>
							<button type="submit" st="btn-search" class="btn btn-green" data-bind="click: addPay">添加支出</button>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="团号" name="option.team_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品" name="option.product_name"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" value="1" onclick="check(this)"
								name="radio_date" />出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="from"
									name="option.departure_date_from" disabled="disabled" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="to"
									name="option.departure_date_to" disabled="disabled" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">出团月</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" st="st-month" placeholder="出团月"
									data-bind="value:confirm_month()" name="option.confirm_month" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">销售</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'"
									name="option.sale_number"></select>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label"><input type="radio" value="2" onclick="check(this)" checked
								name="radio_date" />确认日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-2" placeholder="from"
									name="option.confirm_date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-2" placeholder="to"
									name="option.confirm_date_to" />
							</div>
						</div>
						<div style="float: right">
							<div>
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
					<s:if test="#session.user.user_roles.contains('ADMIN')">
						<div class="form-group">

							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: product_managers,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'"
										name="option.product_manager_number"></select>
								</div>
							</div>

						</div>
					</s:if>
				</form>
				<div class="list-result" id="main-table">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>团号</th>
								<th>预/决</th>
								<th>出团日期</th>
								<th>产品名称</th>
								<th>人数</th>
								<th>总团款</th>
								<th>营收扣点</th>
								<th>立款应收</th>
								<th>机票</th>
								<th>地接成本</th>
								<th>其他支出</th>
								<th>其他收入</th>
								<th>FLY</th>
								<th>后台成本</th>
								<th>销售费用</th>
								<th>毛利润</th>
								<th>人均毛利</th>
								<th>确认日期</th>
								<th>销售</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: reports">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenOrders"/></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.team_number,event:{click:function(){$root.viewTeamDetail($data.team_number)}}"></a></td>
								<!-- ko if:$data.order_type=="Y" -->
								<td data-bind="text: $root.orderTypeMapping[$data.order_type]"></td>
								<!-- /ko -->
								<!-- ko if:$data.order_type=="F" -->
								<td style="color: red" data-bind="text: $root.orderTypeMapping[$data.order_type]"></td>
								<!-- /ko -->
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.people_count"></td>
								<td><a href="javascript:void(0)" class="rmb"
									data-bind="text: $data.receivable,event:{click:function(){$root.viewReceivable($data.team_number)}}"></a></td>
								<td></td>
								<td data-bind="text: $data.discount_receivable" class="rmb"></td>

								<!-- ko if:($root.user_roles.indexOf('TICKET')>=0 ||$root.user_roles.indexOf('ADMIN')>=0)  && $data.air_ticket_cost==null -->
								<td><a href="javascript:void(0)" data-bind="event:{click:$root.fillAirTicketCost}">填报</a></td>
								<!-- /ko -->
								<!-- ko if:$data.air_ticket_cost!=null -->
								<td data-bind="text: $data.air_ticket_cost" class="rmb"></td>
								<!-- /ko -->
								<td data-bind="text: $data.product_cost" class="rmb"></td>
								<td data-bind="text: $data.other_pay" class="rmb"></td>
								<td data-bind="text: $data.other_receive" class="rmb"></td>
								<td data-bind="text: $data.fy" class="rmb"></td>
								<td data-bind="text: $data.sys_cost" class="rmb"></td>
								<td data-bind="text: $data.sale_cost" class="rmb"></td>
								<td data-bind="text: $data.gross_profit" class="rmb"></td>
								<td data-bind="text: $data.per_profit" class="rmb"></td>
								<td data-bind="text: $data.confirm_date"></td>
								<td data-bind="text: $data.sale_name"></td>
								<!-- ko if: $data.approved == "Y" -->
								<td><a href="javascript:void(0)" data-bind="event:{click:function(){$root.rollBackReport($data)}}">打回</a></td>
								<!-- /ko -->
								<!-- ko if: $data.approved == "N" -->
								<td><a href="javascript:void(0)" data-bind="event:{click:function(){$root.confirmReport($data)}}">确认</a></td>
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
			</div>
		</div>
	</div>
	<div id="fill-cost" style="display: none; width: 600px; height: 200px; overflow-y: auto">
		<div class="input-row clearfloat">
			<label class="col-md-2 control-label" style="color: red">机票款</label>
			<div class="col-md-4">
				<input type="number" class="form-control" placeholder="机票款" id="air-ticket-cost" />
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { doFill() }">确认</button>
		</div>
	</div>
	<div id="team-detail"
		style="display: none; width: 1000px; height: 700px; overflow-y: scroll; padding-top: 30px; padding-bottom: 20px">
		<div class="detail-header">
			<span class="title">出团日期</span><span class="content" data-bind="text:order().departure_date"></span> <span
				class="title">产品</span><span class="content" data-bind="text:order().product_name"></span> <span class="title">团号</span><span
				class="content" data-bind="text:order().team_number"></span> <span class="title">销售</span><span class="content"
				data-bind="text:order().create_user"></span>
		</div>
		<hr />
		<h3 style="padding-left: 40px">预算团款</h3>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">预算价格</label>
				<div class="ip" style="width: 70%" data-bind="text:order().receivable">
					<p class="ip-default"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">其他费用</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().other_cost" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">总团款</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().receivable"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">团款备注</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().receivable_comment"></p>
				</div>
			</div>
		</div>
		<hr />
		<h3 style="padding-left: 40px">决算团款</h3>

		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">决算总团款</label>
				<div class="ip" style="width: 70%" data-bind="text:final_order().final_receivable">

					<p class="ip-default"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">决算详情</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:final_order().final_comment" class="rmb"></p>
				</div>
			</div>
		</div>

		<hr />
		<h3 style="padding-left: 40px">机票款</h3>
		<div class="input-row clearfloat">
			<table style="width: 100%; margin-left: 30px; margin-top: 20px;" id="table-ticket-price">
				<thead>
					<tr>
						<th class="r" style="width: 5%">序号</th>
						<th class="r" style="width: 10%">姓名</th>
						<th class="r" style="width: 10%">预算价格</th>
						<th class="r" style="width: 10%">决算价格</th>
						<th class="r" style="width: 65%">票务备注</th>
					</tr>
				</thead>
				<tbody data-bind="foreach: name_infos">
					<tr>
						<td st="index" data-bind="text:$index()+1" />
						<td data-bind="text:$data.name" />
						<td data-bind="text:$data.budget_cost" />
						<td data-bind="text:$data.final_cost" />
						<td data-bind="text:$data.detail_comment" />
					</tr>
				</tbody>
			</table>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">总票款：</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().air_ticket_cost"></p>
				</div>
			</div>
		</div>
		<hr />
		<h3 style="padding-left: 40px">地接款</h3>
		<!-- ko foreach:payable_orders -->
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">供应商</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">预算地接款</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:$data.budget_payable"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">决算地接款</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:$data.final_payable"></p>
				</div>
			</div>
		</div>

		<!-- /ko -->
		<!-- ko if: order().independent_flg =='A' -->
		<!-- <hr />
		<h3>航班信息</h3>
		<table style="width: 100%; margin-left: 30px; margin-top: 20px;" id="table-ticket">
			<thead>
				<tr>
					<th class="r" style="width: 10%">航段</th>
					<th class="r" style="width: 30%">日期</th>
					<th class="r" style="width: 30%">起飞城市</th>
					<th class="r" style="width: 30%">降落城市</th>
				</tr>
			</thead>
			<tbody data-bind="foreach: ticket_infos">
				<tr>
					<td st="index" data-bind="text:$data.ticket_index"></td>
					<td data-bind="text:$data.ticket_date" />
					</td>
					<td data-bind="text:$data.from_city" />
					</td>
					<td data-bind="text:$data.to_city" />
					</td>
				</tr>
			</tbody>
		</table> -->
		<!-- /ko -->
	</div>
	<div style="display: none; width: 900px; padding-top: 30px; padding-bottom: 20px" id='div-receiveds'>
		<table style="width: 100%; margin-left: 30px; margin-top: 20px;" id="table-ticket">
			<thead>
				<th>金额</th>
				<th>类型</th>
				<th>时间</th>
				<th>我方账户</th>
				<th>填报日期</th>
				<th>状态</th>
			</thead>
			<tbody data-bind="foreach: receiveds">
				<tr>
					<td data-bind="text: $data.received" class="rmb"></td>
					<td data-bind="text: $root.typeMapping[$data.type]"></td>
					<td data-bind="text: $data.received_time"></td>
					<td data-bind="text: $data.card_account"></td>
					<td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD')"></td>
					<!-- ko if:$data.status=='I' || $data.status=='Y' -->
					<td data-bind="text: $root.statusMapping[$data.status]"></td>
					<!-- /ko -->
					<!-- ko if:$data.status=='N' -->
					<td><a href="javascript:void(0)" style="color: red"
						data-bind="text: $root.statusMapping[$data.status],click: function() {$root.viewRejectReason($data.related_pk)} "></a></td>
					<!-- /ko -->
					<!-- ko if:$data.status=='E' -->
					<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
					<!-- /ko -->
				</tr>
			</tbody>
		</table>
	</div>
	<div id="div-reconciliation" style="display: none; width: 500px; height: 150px; ">
		<form id="form-reconciliation">
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" data-bind="text:reconciliation_type()+'金额'"></label>
					<div class="ip" style="width: 40%">
						<input type="number" placeholder="金额" class="ip-" id="other-money" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: doReconciliation">提交</a> <a type="button"
							class="btn btn-green btn-r" data-bind="click: cancelReconciliation">取消</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<script>
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/order-report.js?v=1.005"></script>
</body>
</html>