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
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {createOrder() }">生成订单</button>
							<!-- <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {onlyTicket() }">单售票</button> -->
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">客户</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="客户" name="airTicketNeed.client_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="团号" name="airTicketNeed.team_number" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="产品" name="airTicketNeed.product_name" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="airTicketNeed.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="airTicketNeed.date_to" />
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
								<th>首段日期</th>
								<th>首航段</th>
								<th>人数</th>
								<th>产品</th>
								<th>出团日期</th>
								<th>航段信息</th>
								<th>乘机人信息</th>
								<th>需求备注</th>
								<th>团号</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: needs">
							<tr style="overflow: hidden">
								<td><input type="checkbox" data-bind="attr: {'value': $data.team_number}, checked: $root.chosenNeeds" /></td>
								<td data-bind="text: $data.ticket_client_name"></td>
								<td data-bind="text: $data.first_ticket_date"></td>
								<td data-bind="text: $data.first_from_to"></td>
								<td data-bind="text: $data.people_count"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.departure_date"></td>
								<!-- ko if: $data.first_ticket_date==null -->
								<td></td>
								<!-- /ko -->
								<!-- ko if: $data.first_ticket_date!=null -->
								<td><a href="javascript:void(0)" data-bind="click:function(){$root.checkTicketPart($data.team_number)}">查看</a></td>
								<!-- /ko -->
								<!-- ko if: $data.people_count==0 -->
								<td></td>
								<!-- /ko -->
								<!-- ko if: $data.people_count!=0 -->
								<td><a href="javascript:void(0)"
									data-bind="text:$data.passenger,click:function(){$root.checkPassengers($data.team_number)}">查看</a></td>
								<!-- /ko -->
								<td data-bind="text:$data.comment"></td>
								<td data-bind="text: $data.team_number"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td>汇总</td>
							<td data-bind="text:totalPeople"></td>
							<td></td>
							<td></td>
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
	<div id="air-ticket-check" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 15%">日期</th>
							<th style="width: 15%">城市对</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:airTickets">
						<tr>
							<td data-bind="text:$data.air_leg"></td>
							<td data-bind="text:$data.air_date"></td>
							<td data-bind="text:$data.from_to_city"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="passengers-check" style="display: none; width: 800px;height:450px;overflow-y: scroll;">
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
							<td data-bind="text:$index() + 1"></td>
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
			<h2>确认航段信息</h2>
			<div style="margin-top: 60px; height: 300px">

				<table style="width: 100%" id="leg-table" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 15%">日期</th>
							<th style="width: 15%">城市对</th>
							<th style="width: 15%; color: red">票务航段</th>
							<th style="width: 15%">操作</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:airTickets">
						<tr>


							<td data-bind="text:$data.air_leg"></td>
							<td data-bind="text:$data.air_date"></td>
							<td data-bind="text:$data.from_to_city"></td>
							<td><input type="text" class="ticket-air-leg" onclick="choseAirLeg(event)" /> <input type="hidden"
								data-bind="value:$index()+1" st="leg-index" /> <input type="hidden" data-bind="value:$data.air_date"
								st="leg-date" /> <input type="hidden" st="leg-from-city" /> <input type="hidden" st="leg-to-city" /></td>
							<td><a href="javascript:void(0)" data-bind="click:$root.deleteAirInfo">删除</a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { doCreateOrder() }">确认</button>
		</div>
	</div>
	<div id="air-leg-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">城市</label>
						<div class="col-md-6">
							<input type="text" id="city" class="form-control" placeholder="城市" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchAirLeg }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>出发城市</th>
								<th>抵达城市</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: airLegs">
							<tr data-bind="event: {click: function(){ $parent.pickAirLeg($data.from_city,$data.to_city)}}">
								<td data-bind="text: $data.from_city"></td>
								<td data-bind="text: $data.to_city"></td>
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
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/ticket-need.js"></script>
</body>
</html>