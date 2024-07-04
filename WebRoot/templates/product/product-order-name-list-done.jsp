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

.required th[class="r"]:after {
	content: " *";
	color: red;
	font-weight: bold;
}

#table-ticket th, #table-ticket td {
	text-align: center;
}

#table-ticket tr td input {
	width: 90%;
}

#table-ticket input[type="button"] {
	font-size: 36px;
	width: 40%;
}

#table-ticket {
	border-collapse: separate;
	border-spacing: 0px 10px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>已操作名单</h2>
		</div>
		<div class="main-container">
			<div class="main-box" id="div-box" style="overflow: hidden">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green" data-bind="click: function() { operate()}">增加票务需求</button>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">订单号</label>
						<div class="col-md-2">
							<input type="text" class="form-control" placeholder="订单号" name="name_option.product_order_number" />
						</div>
						<label class="col-md-1 control-label">团号</label>
						<div class="col-md-2">
							<input type="text" class="form-control" placeholder="团号" name="name_option.team_number" />
						</div>
						<label class="col-md-1 control-label">游客</label>
						<div class="col-md-2">
							<input type="text" class="form-control" placeholder="游客" name="name_option.name" />
						</div>
					</div>

					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">首航日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="name_option.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="name_option.date_to" />
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>

				<div class="list-result" id="div-table" style="float: left; width: 100%">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th><input type="checkbox" id="chk-all" onclick="checkAll(this)" />全选</th>
								<th>序号</th>
								<th>姓名</th>
								<th>身份证号</th>
								<th>产品</th>
								<th>产品型号</th>
								<th>电话1</th>
								<th>电话2</th>
								<th>订单号</th>
								<th>团号</th>
								<th>出团日期</th>
								<th>票务需求</th>
								<th>出票信息</th>
								<th>天数</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: passengers">
							<tr style="overflow: hidden;" ondblclick="checkSameOrderNumber(this)">

								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenPassengers" /></td>
								<td data-bind="text: $index()+1"></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.id"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.product_model"></td>
								<td data-bind="text: $data.cellphone_A"></td>
								<td data-bind="text: $data.cellphone_B"></td>
								<td st="order-number" data-bind="text: $data.product_order_number"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.departure_date"></td>
								<!-- ko if:$data.operate_status=='D' -->
								<td>单地接</td>
								<!-- /ko -->
								<!-- ko if:$data.operate_status=='Y' -->
								<td><a href="javascript:void(0)" data-bind="click:function(){$root.checkAirNeed($data.pk)}">查看</a></td>
								<!-- /ko -->
								<!-- ko if:$data.ticked=='N' -->
								<td>未出票</td>
								<!-- /ko -->
								<!-- ko if:$data.ticked=='I' -->
								<td>待出票</td>
								<!-- /ko -->
								<!-- ko if:$data.ticked=='Y' -->
								<td><a href="javascript:void(0)" data-bind="click:function(){$root.checkAirNeed($data.pk)}">查看</a></td>
								<!-- /ko -->
								<td data-bind="text: $data.days"></td>
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
	<!-- 机票信息 -->
	<div id="air-ticket-edit" style="display: none; width: 800px; height: 470px; overflow: auto">
		<div class="input-row clearfloat" id="air-ticket">
			<div class="col-md-12">
				<table style="width: 100%" id="table-ticket">
					<thead>
						<tr class="required">
							<th style="width: 10%">航段</th>
							<th class="r" style="width: 6%">天次</th>
							<th style="width: 10%">航班号</th>
							<th class="r" style="width: 12%">城市对</th>
							<th style="width: 10%"></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td st="index">A</td>
							<td><input st="start-day" type="text" /></td>
							<td><input st="flight-number" type="text" /></td>
							<td><input st="from-to-city" type="text" onclick="choseAirLeg(event)"/></td>
							<td><input type="button" value="-" onclick="deleteRow(this)"></input></td>
						</tr>
					</tbody>
				</table>
				<div style="margin-top: 20px; float: right">
					<input type="button" value="+" onclick="addRow()" style="font-size: 24px; width: 40px"></input>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat" id="air-comment">
			<div class="col-md-12">
				<label class="l">票务要求</label>
				<div class="ip">
					<textarea type="text" class="ip-default air_comment" rows="5" maxlength="200" placeholder="票务需求"></textarea>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-12" style="text-align: right">
				<a type="submit" class="btn btn-green btn-r" data-bind="click: function(){doSendAir();}">提交</a> <a type="submit"
					class="btn btn-green btn-r" data-bind="click: cancelSendAir">取消</a>
			</div>
		</div>
	</div>
	<!-- 查看已发送票务需求 -->
	<div id="div-check-air-need" style="display: none; width: 800px; height: 470px; overflow: auto">
		<div data-bind="foreach:flight_segments">
			<div class="input-row clearfloat">
				<label class="l" data-bind="text:'发送时间：'+key" style="width:60% !important"></label>
				<a href="javascript:void(0)" data-bind="click:deleteSendedAirNeed"  style="line-height:40px">删除</a>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12">
					<table style="width: 100%">
						<thead>
							<tr class="required">
								<th style="width:10%">航段</th>
								<th style="width:20%">航班日期</th>
								<th style="width:10%">航班号</th>
								<th style="width:20%">城市对</th>
								<th style="width:40%">票务要求</th>
							</tr>
						</thead>
						<tbody data-bind="foreach:value">
							<tr>
								<td st="index" data-bind="text:alphabetMap[ticket_index]"></td>
								<td data-bind="text:ticket_date"></td>
								<td data-bind="text:ticket_number"></td>
								<td data-bind="text:from_to_city"></td>
								<td data-bind="text:air_comment"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<hr /> 
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
							<tr data-bind="event: {click: function(){ $parent.pickAirLeg($data)}}">
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
		$(".order-operate").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-properties.js"></script>
	<script src="<%=basePath%>static/js/product/product-order-name-list-done.js"></script>
	<script src="<%=basePath%>static/vendor/clipboard.min.js"></script>
</body>
</html>