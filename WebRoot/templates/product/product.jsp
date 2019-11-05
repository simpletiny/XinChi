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
			<h2>产品管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="width: 50%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { create() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { clone() }">克隆</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { edit() }">维护</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { onSale('Y') }">上架</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { onSale('N') }">下架</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { bindingTicket() }">机票</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { abandon() }">删除</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: status" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="product.statuses" /><label data-bind="text: $root.saleMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品编号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品编号" name="product.product_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品名称" name="product.name"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">产品线</label>
							<div class="col-md-2" style="float: left">
								<select class="form-control" style="height: 34px"
									data-bind="options: locations,optionsText:'name',optionsValue:'name',value:product().location, optionsCaption: '--请选择--',event:{change:refresh}"
									name="product.location"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
										name="product.product_manager"></select>
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
								<th>序</th>
								<th>状态</th>
								<th>产品编号</th>
								<th>名称</th>
								<th>型号</th>
								<th>天数</th>
								<th>直客报价</th>
								<th>同业返利</th>
								<th>最大让利</th>
								<th title="成人/儿童">销售分值</th>
								<th>首段城市对</th>
								<th>销售注意</th>
								<th>儿童策略</th>
								<th>机票</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>产品经理</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: products">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenProducts" /></td>
								<td data-bind="text:$index()+1"></td>
								<!-- ko if: $data.sale_flg =='N' && $data.product_number ==null -->
								<td>新建</td>
								<!-- /ko -->
								<!-- ko if: $data.sale_flg =='N' && $data.product_number !=null -->
								<td style="color: grey" data-bind="text: $root.saleMapping[$data.sale_flg]"></td>
								<!-- /ko -->
								<!-- ko if: $data.sale_flg =='Y' -->
								<td style="color: green" data-bind="text: $root.saleMapping[$data.sale_flg]"></td>
								<!-- /ko -->
								<!-- ko if: $data.sale_flg =='D' -->
								<td style="color: red" data-bind="text: $root.saleMapping[$data.sale_flg]"></td>
								<!-- /ko -->
								<td data-bind="text: $data.product_number"></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.product_model"></td>
								<td data-bind="text: $data.days"></td>
								<td data-bind="text: $data.adult_price"></td>
								<td data-bind="text: $data.business_profit_substract"></td>
								<td data-bind="text: $data.max_profit_substract"></td>

								<td data-bind="text: $data.product_value +'/'+($data.product_child_value?$data.product_child_value:'')"></td>
								<td
									data-bind="text: ($data.first_air_start?$data.first_air_start:'') + '--' + ($data.first_air_end?$data.first_air_end:'')"></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.sale_attention, click:function(){msg($data.sale_attention)}"></a></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.sale_strategy, click:function(){msg($data.sale_strategy)}"></a></td>

								<!-- ko if: $data.air_ticket_charge=='NO' -->
								<td>未绑定</td>
								<!-- /ko -->
								<!-- ko if: $data.air_ticket_charge!='NO' -->
								<td><a href="javascript:void(0)" data-bind="click: function() {$root.checkAirTicket($data.pk)} ">查看</a></td>
								<!-- /ko -->
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.product_manager"></td>
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
	<div id="air-ticket" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<form id="form-ticket">
				<div style="width: 100%">
					<label class="l">产品名称</label> <label class="l" data-bind="text:product().name"></label> <input type="hidden"
						data-bind="value:product().pk" name="product_pk" /> <label class="l">产品编号</label> <label class="l"
						data-bind="text:product().product_number"></label>
					<div data-bind="foreach: allCharges" style="padding-top: 4px;">
						<em class="small-box"> <input type="radio" value="PRODUCT" name="ticket_charge"
							data-bind="attr: {'value': $data}, checked: $root.chosenCharge" onclick="changeCharge($(this).val())" /><label
							data-bind="text:$root.chargeMapping[$data]"></label>
						</em>
					</div>
				</div>
			</form>
			<div style="margin-top: 20px; height: 300px" id="div-ticket">
				<table style="width: 100%" id="table-ticket">
					<thead>
						<tr class="required">
							<th class="r" style="width: 10%">航段</th>
							<th class="r" style="width: 10%">天次</th>
							<th class="r" style="width: 30%">起飞城市</th>
							<th class="r" style="width: 10%">天次</th>
							<th class="r" style="width: 30%">抵达城市</th>
							<th style="width: 20%">航班号</th>
						</tr>
					</thead>
					<!-- ko if:airTickets().length>0 -->
					<tbody data-bind="foreach:airTickets">
						<tr>
							<td st="index" data-bind="text:$data.ticket_index"></td>
							<td><input onkeyup="sameEnd(this)" st="start-day" type="text" data-bind="value:$data.start_day" /></td>
							<td><input st="start-city" type="text" data-bind="value:$data.start_city" /></td>
							<td><input st="end-day" type="text" data-bind="value:$data.end_day" /></td>
							<td><input st="end-city" type="text" data-bind="value:$data.end_city" /></td>
							<td><input st="ticket-number" type="text" data-bind="value:$data.ticket_number" /></td>
						</tr>
					</tbody>
					<!-- /ko -->
					<!-- ko if:airTickets().length<1 -->
					<tbody>
						<tr>
							<td st="index">1</td>
							<td><input onkeyup="sameEnd(this)" st="start-day" type="text" /></td>
							<td><input st="start-city" type="text" /></td>
							<td><input st="end-day" type="text" /></td>
							<td><input st="end-city" type="text" /></td>
							<td><input st="ticket-number" type="text" /></td>
						</tr>
					</tbody>
					<!-- /ko -->

				</table>
				<div style="margin-top: 20px; float: right">
					<input type="button" value="-" onclick="deleteRow()""></input> <input type="button" value="+" onclick="addRow()"></input>
				</div>
			</div>

			<div style="margin-top: 50px; width: 700px; float: right">
				<button type="submit" style="float: right" class="btn btn-green col-md-1" onclick="cancelTicket()">取消</button>
				<button type="submit" style="float: right" class="btn btn-green col-md-1" onclick="saveTicket()">保存</button>
			</div>
		</div>

	</div>
	<div id="air-ticket-check" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<div style="width: 100%">
				<label class="l">产品名称</label> <label class="l" data-bind="text:product().name"></label> <label class="l">产品编号</label>
				<label class="l" data-bind="text:product().product_number"></label> <label class="l"
					data-bind="text:chargeMapping[product().air_ticket_charge]"></label>
			</div>
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 10%">天次</th>
							<th style="width: 30%">起飞城市</th>
							<th style="width: 10%">天次</th>
							<th style="width: 30%">抵达城市</th>
							<th style="width: 20%">航班号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:airTickets">
						<tr>
							<td data-bind="text:$data.ticket_index"></td>
							<td data-bind="text:$data.start_day"></td>
							<td data-bind="text:$data.start_city"></td>
							<td data-bind="text:$data.end_day"></td>
							<td data-bind="text:$data.end_city"></td>
							<td data-bind="text:$data.ticket_number"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/js/product/product.js"></script>
</body>
</html>