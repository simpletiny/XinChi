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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>已操作订单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
					<!-- 	<div style="width: 30%; float: right">
							<button type="submit" class="btn btn-green" data-bind="click: function() { lockOrder() }">锁定操作</button>
							<button type="submit" class="btn btn-green" data-bind="click: function() { lockOrder() }">生成待操作名单</button>
						</div> -->
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">客户</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="客户" name="airTicketOrder.client_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="团号" name="airTicketOrder.team_number" />
							</div>
						</div>
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
								<th>客户</th>
								<th>机票款</th>
								<th>首段日期</th>
								<th>首航段</th>
								<th>人数</th>
								<th>航段信息</th>
								<th>乘机人信息</th>
								<th>需求备注</th>
								<th>团号</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr style="overflow: hidden">
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenOrders" /></td>
								<td data-bind="text: $data.client_name"></td>
								<!-- ko if: $data.cost_done_flg=="N" -->
								<td style="color:red" data-bind="text: $data.ticket_cost"></td>
								<!-- /ko -->
								<!-- ko if: $data.cost_done_flg=="Y" -->
								<td style="color:green" data-bind="text: $data.ticket_cost"></td>
								<!-- /ko -->
								<td data-bind="text: $data.first_ticket_date"></td>
								<td data-bind="text: $data.first_from_to"></td>
								<td data-bind="text: $data.people_count"></td>
								<!-- ko if: $data.first_ticket_date==null -->
								<td></td>
								<!-- /ko -->
								<!-- ko if: $data.first_ticket_date!=null -->
								<td><a href="javascript:void(0)" data-bind="click:function(){$root.checkTicketPart($data.pk)}">查看</a></td>
								<!-- /ko -->
								<!-- ko if: $data.people_count==0 -->
								<td></td>
								<!-- /ko -->
								<!-- ko if: $data.people_count!=0 -->
								<td><a href="javascript:void(0)"
									data-bind="text:$data.passenger,click:function(){$root.checkPassengers($data.team_number)}">查看</a></td>
								<!-- /ko -->
								<td></td>
								<td data-bind="text: $data.team_number"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td>汇总</td>
							<td data-bind="text:totalCost"></td>
							<td></td>
							<td></td>
							<td data-bind="text:totalPeople"></td>
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
	<div id="air-ticket-check" style="display: none; width: 800px;height:450px;overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 15%">日期</th>
							<th style="width: 15%">起飞城市</th>
							<th style="width: 15%">抵达城市</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:airTickets">
						<tr>
							<td data-bind="text:$data.sort_index"></td>
							<td data-bind="text:$data.date"></td>
							<td data-bind="text:$data.from_city"></td>
							<td data-bind="text:$data.to_city"></td>
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
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/ticket/ticket-order-done.js"></script>
</body>
</html>