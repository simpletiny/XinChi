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
#table-supplier th, #table-supplier td {
	text-align: center;
}

#table-supplier tr td input {
	width: 90%;
}

#table-supplier {
	border-collapse: separate;
	border-spacing: 0px 10px;
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

#table-ticket {
	border-collapse: separate;
	border-spacing: 0px 10px;
}

#air-ticket input[type="button"] {
	width: 30px;
	font-weight: bold;
	font-size: 20px;
}
.error{
	color:red;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品需求</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green" data-bind="click: function() { createOrder() }">生成订单</button>

								<button type="submit" class="btn btn-green" data-bind="click: function() { tipSales() }">提示销售确认</button>
								<button type="submit" class="btn btn-green" data-bind="click: function() { unlockOrder() }">解锁</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品型号</label>
							<div class="col-md-2">
								<input class="form-control" name="order_option.product_model" placeholder="产品型号"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" name="order_option.product_name" placeholder="产品名称"></input>
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
										name="order_option.product_manager_number"></select>
								</div>
							</div>
						</s:if>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input class="form-control" name="order_option.team_number" placeholder="团号"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="order_option.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="order_option.date_to" />
							</div>
						</div>
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
								<th><input type="checkbox" id="chk-all" onclick="checkAll(this)" />全选</th>
								<th>状态</th>
								<th>团号</th>
								<th>出团日期</th>
								<th>产品名称</th>
								<th>产品型号</th>
								<th>成人</th>
								<th>特殊</th>
								<th>游客信息</th>
								<th>航班信息</th>
								<th>销售</th>
								<th>接待特请</th>
								<th>销售锁定</th>
								<th>名单确认</th>
								<th>标/非标</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+';'+$data.product_pk+';'+$data.team_number+';'+$data.operate_flg+';'+$data.name_confirm_status+';'+$data.standard_flg+';'+$data.departure_date+';'+$data.product_name+';'+$data.product_model}, checked: $root.chosenOrders" /></td>
								<td data-bind="text:$root.statusMapping[$data.operate_flg]"></td> 
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.product_model"></td>
								<td data-bind="text: $data.adult_count"></td>
								<td data-bind="text: $data.special_count"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkPassengers,text: $data.passenger"></a></td>
								<td data-bind="text: $data.air_info"></td>
								<td data-bind="text: $data.sale_name"></td>
								<td data-bind="text: $data.treat_comment"></td>
								<td data-bind="text:$root.lockMapping[$data.lock_flg]"></td>

								<!-- ko if:$data.name_confirm_status=="1" -->
								<td style="color: red" data-bind="text:$root.nameMapping[$data.name_confirm_status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.name_confirm_status=="2" -->
								<td data-bind="text:$root.nameMapping[$data.name_confirm_status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.name_confirm_status=="3" -->
								<td style="color: green" data-bind="text:$root.nameMapping[$data.name_confirm_status]"></td>
								<!-- /ko -->
								<td data-bind="text: $root.standardMapping[$data.standard_flg]"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>汇总</td>
							<td data-bind="text:totalAdult"></td>
							<td data-bind="text:totalSpecial"></td>
							<td></td>
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

	<div id="supplier-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">姓名</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="姓名" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchSupplierEmployee }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>姓名</th>
								<th>财务主体</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: supplierEmployees">
							<tr data-bind="event: {click: function(){ $parent.pickSupplierEmployee($data.name,$data.pk)}}">
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
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
	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
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
							<td data-bind="text:$data.cellphone_B"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 机票信息 -->
	<div id="air-ticket-edit" style="display: none; width: 800px; height: 700px; overflow: auto">
		<form class="form-box info-form" id="form-air">
			<div class="input-row clearfloat">
				<label class="col-md-2 control-label">团号</label>
				<div class="col-md-6">
					<p data-bind="text:team_numbers()"></p>
					<input type="hidden" name="team_numbers" id="txt-team-numbers" data-bind="value:team_numbers()" />

				</div>
			</div>
			<div class="input-row clearfloat" style="float:right;padding-bottom: 20px">
				<em class="small-box"> <input type="checkbox" onclick="hasTicket(this)" id="chk-has-ticket"/><label style="font:bold;color:red">无机票</label>
				</em>
			</div>
			<div class="input-row clearfloat" id="air-ticket">
				<div class="col-md-12">
					<table style="width: 100%" id="table-ticket">
						<thead>
							<tr class="required">
								<th class="r" style="width: 10%">航段</th>
								<th class="r" style="width: 10%">天次</th>
								<th class="r" style="width: 15%">起飞城市</th>
								<th class="r" style="width: 15%">降落城市</th>
								<th style="width: 10%"></th>
							</tr>
						</thead>
						<tbody data-bind="foreach :flight()">
							<tr>
								<input type="hidden" st="flight-index" data-bind="value:$data.ticket_index" />
								<td st="index" data-bind="text:$data.ticket_index"></td>
								<td><input st="start-day" type="text" data-bind="value:$data.start_day" /></td>
								<td><input st="start-city" type="text" data-bind="value:$data.start_city"/></td>
								<td><input st="end-city" type="text" data-bind="value:$data.end_city"/></td>
								<td><input type="button" value="-" onclick="deleteRow(this)"></input></td>
							</tr>
						</tbody>
					</table>
					<div style="margin-top: 20px; float: right">
						<input type="button" value="+" onclick="addRow()"></input>
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
				<div class="col-md-12">
					<label class="l">订单备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default comment" rows="5" maxlength="200" placeholder="需要备注说明的信息"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="text-align: right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: function(){doCreateOrder();}">提交</a> <a
						type="submit" class="btn btn-green btn-r" data-bind="click: cancelSendAir">取消</a>
				</div>
			</div>
		</form>
	</div>
	<script>
		$(".order-operate").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-need.js"></script>
</body>
</html>