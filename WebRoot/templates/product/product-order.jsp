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
#table-ticket th, #table-ticket td {
	text-align: center;
}

#table-ticket tr td input {
	width: 90%;
}

#table-ticket {
	border-collapse: separate;
	border-spacing: 0px 10px;
}

#air-ticket input[type="button"] {
	width: 30px;
	font-weight: bold;
	font-size: 20px;
}

.required th[class="r"]:after {
	content: " *";
	color: red;
	font-weight: bold;
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
			<h2>产品订单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group" style="float: right">
						<button type="submit" class="btn btn-green" data-bind="click: function() { createOperate() }">订单操作</button>
						<button type="submit" class="btn btn-green" data-bind="click: function() { rollBack() }">打回订单</button>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: status" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="order.statuses" /><label data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品名称" name="order.product_name"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品型号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品编号" name="order.product_model"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">订单号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="订单号" name="order.order_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="团号" name="order.team_number"></input>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="order.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="order.date_to" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--',event:{change:function(){refresh();}}"
										name="order.product_manager_number"></select>
								</div>
							</div>
						</s:if>
						<div style="width: 30%; float: right">
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
								<th>订单号</th>
								<th>单/合</th>
								<th>状态</th>
								<th>产品</th>
								<th>型号</th>
								<th>出团日期</th>
								<th>成人</th>
								<th>儿童</th>
								<th>乘客</th>
								<th>票务需求</th>
								<th>出票</th>
								<th>备注</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>产品经理</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.order_number+';'+$data.standard_flg+';'+$data.product_pk+';'+$data.status}, checked: $root.chosenOrders" /></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.order_number, click:function(){$root.checkOrders($data.order_number)}"></a></td>
								<!-- ko if: $data.single_flg == "N" -->
								<td><a href="javascript:void(0)"
									data-bind="text: $root.singleMapping[$data.single_flg], click:function(){$root.checkOrders($data.order_number)}"></a></td>
								<!-- /ko -->
								<!-- ko if: $data.single_flg == "Y" -->
								<td><a href="javascript:void(0)" style="color: red"
									data-bind="text: $root.singleMapping[$data.single_flg], click:function(){$root.checkOrders($data.order_number)}"></a></td>
								<!-- /ko -->
								<!-- ko if: $data.status == "Y" -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if: $data.status == "N" -->
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->

								<td data-bind="text: $data.product_name"></td>
								<!-- ko if: $data.standard_flg == "Y" -->
								<td data-bind="text: $data.product_model"></td>
								<!-- /ko -->
								<!-- ko if: $data.standard_flg == "N" -->
								<td style="color: red">非标</td>
								<!-- /ko -->
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.adult_count"></td>
								<td data-bind="text: $data.special_count"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkPassengers,text: $data.passenger_captain"></a></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.air_comment, click:function(){msg($data.air_comment)}"></a></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkTicketInfos">查看</a></td>
								<td><a href="javascript:void(0)" data-bind="text: $data.comment, click:function(){msg($data.comment)}"></a></td>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.product_manager"></td>
								</s:if>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>合计</td>
							<td data-bind="text:adult_cnt"></td>
							<td data-bind="text:special_cnt"></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td> 
							<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
								<td></td>
							</s:if>
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

	<div id="div-check-order" style="display: none; width: 1000px; height: 550px; overflow-y: auto;">
		<div class="list-result">
			<table class="table table-striped table-hover">
				<thead>
					<tr role="row">
						<th>团号</th>
						<th>出团日期</th>
						<th>产品名称</th>
						<th>成人</th>
						<th>儿童</th>
						<th>游客信息</th>
						<th>销售</th>
						<th>接待特情</th>
						<th>订单状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody data-bind="foreach: sale_orders">
					<tr>
						<td data-bind="text: $data.team_number"></td>
						<td data-bind="text: $data.departure_date"></td>
						<td data-bind="text: $data.product_name"></td>
						<td data-bind="text: $data.adult_count"></td>
						<td data-bind="text: $data.special_count"></td>
						<td><a href="javascript:void(0)" data-bind="click:$root.innerCheckPassengers,text: $data.passenger_captain"></a></td>
						<td data-bind="text:$data.create_user"></td>
						<td><a href="javascript:void(0)"
							data-bind="click:function(){msg($data.treat_comment)},text:$data.treat_comment"></a></td>
						<!-- ko if: $data.lock_flg == "Y" -->
						<td style="color: green" data-bind="text:$root.lockMapping[$data.lock_flg]"></td>
						<!-- /ko -->
						<!-- ko if: $data.lock_flg == "N" -->
						<td style="color: red" data-bind="text:$root.lockMapping[$data.lock_flg]"></td>
						<!-- /ko -->
						<!-- ko if: $data.lock_flg == "Y" -->
						<td><a href="javascript:void(0)" data-bind="click:function(){$root.lockOrder($data.team_number,'N');}">解锁</a></td>
						<!-- /ko -->
						<!-- ko if: $data.lock_flg == "N" -->
						<td><a href="javascript:void(0)" data-bind="click:function(){$root.lockOrder($data.team_number,'Y');}">锁定</a></td>
						<!-- /ko -->


					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px; height: 650px; overflow-y: auto;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">团号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
							<th style="width: 10%">电话1</th>
							<th style="width: 10%">电话2</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.team_number"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
							<td data-bind="text:$data.cellphone_A"></td>
							<td data-bind="text:$data.cellphone_A"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 订单详情查看乘客信息 -->
	<div id="passengers-check-inner" style="display: none; width: 800px; height: 550px; overflow-y: auto;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
							<th style="width: 10%">电话1</th>
							<th style="width: 10%">电话2</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
							<td data-bind="text:$data.cellphone_A"></td>
							<td data-bind="text:$data.cellphone_A"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 查看出票信息 -->
	<div id="ticket-check" style="display: none; width: 1000px; height: 650px; overflow-y: scroll;">
		<div data-bind="foreach:ticketInfos">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l">姓名</label>
					<div class="">
						<p class="ip-default" data-bind="text: $data.name"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l">证号</label>
					<p class="ip-default" data-bind="text: $data.id"></p>
				</div>
			</div>
			<div class="input-row clearfloat">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 10%">航班号</th>
							<th style="width: 10%">日期</th>
							<th style="width: 10%">起降时刻</th>
							<th style="width: 10%">起降城市</th>
							<th style="width: 10%">起降机场</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:{ data: $data.ticket_infos,as: 'info' }">
						<tr>
							<td data-bind="text:info.ticket_index"></td>
							<td data-bind="text:info.ticket_number"></td>
							<td data-bind="text:info.ticket_date"></td>
							<td data-bind="text:info.from_to_time"></td>
							<td data-bind="text:info.from_to_city"></td>
							<td data-bind="text:info.from_airport+'--'+info.to_airport"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".order-operate").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-order.js?v=1.0"></script>
</body>
</html>