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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>需求列表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="width: 30%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createOrder() }">生成订单</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {onlyTicket() }">单售票</button>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<div class="col-md-6">
								<div data-bind="foreach: status" style="padding-top: 4px;">
									<em class="small-box"> <input type="checkbox" checked data-bind="attr: {'value': $data},click:function(){$root.refresh();return true;}" name="airTicketNeed.status" /><label
										data-bind="text: $root.statusMapping[$data]"></label>
									</em>
								</div>
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
								<th>客户</th>
								<th>首段日期</th>
								<th>首航段</th>
								<th>人数</th>
								<th>航段信息</th>
								<th>乘机人信息</th>
								<th>需求备注</th>
								<th>团号</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: needs">
							<tr style="overflow: hidden">
								<td><input type="checkbox" data-bind="attr: {'value': $data.sale_order_pk+';'+$data.standard_flg}, checked: $root.chosenNeeds" /></td>
								<td data-bind="text: $root.statusMapping[$data.confirm_flg]"></td>
								<td data-bind="text: $data.ticket_client_name"></td>
								<td data-bind="text: $data.first_ticket_date"></td>
								<td data-bind="text: $data.first_from_to"></td>
								<td data-bind="text: $data.people_count"></td>
								<!-- ko if: $data.first_ticket_date==null -->
								<td></td>
								<!-- /ko -->
								<!-- ko if: $data.first_ticket_date!=null -->
								<td><a href="javascript:void(0)" data-bind="click:function(){$root.checkTicketPart($data.product_pk,$data.first_ticket_date)}">查看</a></td>
								<!-- /ko -->
								<!-- ko if: $data.people_count==0 -->
								<td></td>
								<!-- /ko -->
								<!-- ko if: $data.people_count!=0 -->
								<td><a href="javascript:void(0)" data-bind="text:$data.passenger,click:function(){$root.checkPassengers($data.sale_order_pk,$data.standard_flg)}">查看</a></td>
								<!-- /ko -->
								<td></td>
								<td data-bind="text: $data.team_number"></td>
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
	<div id="air-ticket-check" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 10%">天次</th>
							<th style="width: 15%">起飞日期</th>
							<th style="width: 15%">起飞城市</th>
							<th style="width: 10%">天次</th>
							<th style="width: 15%">抵达日期</th>
							<th style="width: 15%">抵达城市</th>
							<th style="width: 20%">航班号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:airTickets">
						<tr>
							<td data-bind="text:$data.ticket_index"></td>
							<td data-bind="text:$data.start_day"></td>
							<td data-bind="text:$data.off_date"></td>
							<td data-bind="text:$data.start_city"></td>
							<td data-bind="text:$data.end_day"></td>
							<td data-bind="text:$data.land_date"></td>
							<td data-bind="text:$data.end_city"></td>
							<td data-bind="text:$data.ticket_number"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="passengers-check" style="display: none; width: 800px">
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
							<td data-bind="text:$data.index"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="order-create" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<label class="col-md-2 control-label" style="color: red">机票款</label>
			<div class="col-md-4">
				<input type="number" class="form-control" placeholder="机票款" id="air_ticket_cost" />
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { doCreateOrder() }">确认</button>
		</div>
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/ticket/ticket-need.js"></script>
</body>
</html>