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

.info {
	white-space: pre-line;
}

.fix-width {
	width: 45% !important;
}

.col-md-3 {
	width: 24% !important;
}

.col-md-1 {
	width: 4% !important;
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
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { create() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { clone() }">克隆</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { edit() }">维护</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { onSale('Y') }">紧急上架</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { delayOnSale() }">预约上架</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { cancelDelay() }">取消预约</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { onSale('N') }">下架</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { abandon() }">废弃</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { recovery() }">恢复</button>
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
							<label class="col-md-1 control-label" style="width: 5% !important">产品编号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品编号" name="product.product_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label" style="width: 5% !important">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品名称" name="product.name"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label" style="width: 5% !important">产品线</label>
							<div class="col-md-2" style="float: left">
								<select class="form-control" style="height: 34px"
									data-bind="options: locations,optionsText:'name',optionsValue:'name', optionsCaption: '--请选择--',event:{change:refresh}"
									name="product.location"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label" style="width: 5% !important">产品经理</label>
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
								<th><input type="checkbox" id="chk-all" onclick="checkAll(this)" />全选</th>
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
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>产品经理</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: products">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+';'+$data.sale_flg}, checked: $root.chosenProducts" /></td>
								<td data-bind="text:$index()+1"></td>
								<!-- ko if: $data.sale_flg =='N' && $data.product_number ==null -->
								<td data-bind="text:'新建（'+ $root.keepMapping[$data.keep_flg]+'）'"></td>
								<!-- /ko -->
								<!-- ko if: $data.sale_flg =='N' && $data.product_number !=null -->
								<td style="color: grey" data-bind="text: $root.saleMapping[$data.sale_flg]+'（'+ $root.keepMapping[$data.keep_flg]+'）'"></td>
								<!-- /ko -->
								<!-- ko if: $data.sale_flg =='Y' -->
								<td style="color: green" data-bind="text: $root.saleMapping[$data.sale_flg]+'（'+ $root.keepMapping[$data.keep_flg]+'）'"></td>
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
	<div id="product-info" style="display: none; width: 1200px">
		<div class="input-row clearfloat">
			<div class="input-row clearfloat">
				<!-- ko if:product().strict_price_flg=="Y" -->
				<label class="l" style="width: 170px">严格执行定价（是）</label>
				<!-- /ko -->

				<!-- ko if:product().strict_price_flg=="N" -->
				<label class="l" style="width: 170px">严格执行定价（否）</label>
				<!-- /ko -->
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-3">

					<label class="l">产品线</label>
					<div class="ip fix-width">
						<div class="ip">
							<p class="ip-default" data-bind="text: product().location"></p>
						</div>
					</div>
				</div>
				<div class="col-md-3 ">
					<label class="l">产品名称</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().name"></p>
					</div>
				</div>
				<div class="col-md-3">
					<label class="l">型号</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().product_model"></p>
					</div>
				</div>
				<div class="col-md-3">
					<label class="l">天数</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().days"></p>
					</div>
				</div>
			</div>

			<div class="input-row clearfloat" style="margin-top: 40px">
				<table style="width: 100%" id="table-product">
					<tr>
						<td style="width: 24%"><label class="l">&nbsp;</label>
							<div class="ip fix-width">
								<label class="l" style="text-align: center">成人</label>
							</div></td>
						<td style="width: 24%">
							<div class="ip fix-width">
								<label class="l" style="text-align: center">儿童</label>
							</div>
						</td>
						<td style="width: 24%"></td>
						<td style="width: 24%"></td>
					</tr>
					<tr>
						<td>
							<div class="">
								<label class="l">直客报价</label>
								<div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().adult_price"></p>
								</div>
							</div>
						</td>
						<td>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text: product().child_price"></p>
							</div>
						</td>
						<td>
							<div class="">
								<label class="l">同业返利</label>
								<div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().business_profit_substract"></p>
								</div>
							</div>
						</td>
						<td>
							<div class="">
								<label class="l">最大让利</label>
								<div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().max_profit_substract"></p>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td><label class="l">其他成本</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text: product().other_cost"></p>
							</div></td>
						<td>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text: product.other_child_cost"></p>
							</div>
						</td>
						<td><label class="l">首段出港</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text: product().first_air_start"></p>
							</div></td>
						<td><label class="l">首段目的</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text: product().first_air_end"></p>
							</div></td>
					</tr>
					<tr>
						<td>
							<div class="">
								<label class="l">产品分值</label>
								<div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().product_value"></p>
								</div>
							</div>
						</td>
						<td>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text: product().product_child_value"></p>
							</div>
						</td>
						<td></td>
					</tr>
					<tr>
						<td style="width: 48%" colspan="2" rowspan="3"><label class="l">销售注意</label>
							<div class="ip">
								<p class="ip-default info" data-bind="text: product().sale_attention"></p>
							</div></td>
						<td style="width: 48%" colspan="2" rowspan="3"><label class="l">儿童策略</label>
							<div class="ip">
								<p class="ip-default info" data-bind="text: product().sale_strategy"></p>
							</div></td>
					</tr>
				</table>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12">
					<label class="l">备注</label>
					<div class="ip">
						<p class="ip-default info" data-bind="text: product().comment"></p>
					</div>
				</div>
			</div>
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" data-bind="text:'紧急上架(剩余：'+urgentCnt()+'次)',click: doOnSale"></a> <a
					type="submit" class="btn btn-green btn-r" data-bind="click: cancelOnSale">取消</a>
			</div>
		</div>
	</div>
	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/js/product/product.js?v=1.004"></script>
</body>
</html>