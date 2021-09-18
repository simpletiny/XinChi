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
							<div>
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">添加支出</button>
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">添加收入</button>
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
									name="option.departure_date_from"  disabled="disabled"/>
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="to"
									name="option.departure_date_to"  disabled="disabled"/>
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
				<div class="list-result">
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
								<td><input type="checkbox" /></td>
								<td data-bind="text: $data.team_number"></td>
								<!-- ko if:$data.order_type=="Y" -->
								<td data-bind="text: $root.orderTypeMapping[$data.order_type]"></td>
								<!-- /ko -->
								<!-- ko if:$data.order_type=="F" -->
								<td style="color: red" data-bind="text: $root.orderTypeMapping[$data.order_type]"></td>
								<!-- /ko -->
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.people_count"></td>
								<td data-bind="text: $data.receivable" class="rmb"></td>
								<td data-bind="text: $data.discount_receivable" class="rmb"></td>

								<!-- ko if:($root.user_roles.indexOf('TICKET')>=0 ||$root.user_roles.indexOf('ADMIN')>=0)  && $data.air_ticket_cost==null -->
								<td><a href="javascript:void(0)" data-bind="event:{click:$root.fillAirTicketCost}">填报</a></td>
								<!-- /ko -->
								<!-- ko if:$data.air_ticket_cost!=null -->
								<td data-bind="text: $data.air_ticket_cost" class="rmb"></td>
								<!-- /ko -->
								<td data-bind="text: $data.product_cost" class="rmb"></td>
								<td data-bind="text: $data.other_cost" class="rmb"></td>
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
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>汇总</td>
							<td data-bind="text:sum_info().people_count"></td>
							<td class="rmb" data-bind="text:sum_info().receivable"></td>
							<td></td>
							<td class="rmb" data-bind="text:sum_info().air_ticket_cost"></td>
							<td class="rmb" data-bind="text:sum_info().product_cost"></td>
							<td class="rmb" data-bind="text:sum_info().other_cost"></td>
							<td class="rmb" data-bind="text:sum_info().other_receive"></td>
							<td class="rmb" data-bind="text:sum_info().other_fy"></td>
							<td class="rmb" data-bind="text:sum_info().sale_cost"></td>
							<td class="rmb" data-bind="text:sum_info().sys_cost"></td>
							<td class="rmb" data-bind="text:sum_info().gross_profit"></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
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
	<script>
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/order-report.js"></script>
</body>
</html>