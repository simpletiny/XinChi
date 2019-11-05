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

.fix-width {
	width: 45% !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品分析</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="width: 20%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { costEdit() }">成本编辑</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { valueEdit() }">更新分值</button>
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
								<th>产品编号</th>
								<th>名称</th>
								<th>型号</th>
								<th>产品价格</th>
								<th>机票成本</th>
								<th>地接成本</th>
								<th>其他成本</th>
								<th>毛利</th>
								<th>毛利率</th>
								<th>现金流</th>
								<th>现付资金</th>
								<th>资金效率</th>
								<th title="成人/儿童">销售分值</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>产品经理</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: products">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenProducts" /></td>
								<td data-bind="text:$index()+1"></td>
								<td data-bind="text: $data.product_number"></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.product_model"></td>
								<td
									data-bind="text: $data.adult_price -($data.business_profit_substract?$data.business_profit_substract:0)- ($data.max_profit_substract?$data.max_profit_substract:0)"></td>
								<td data-bind="text: $data.air_ticket_cost"></td>
								<td data-bind="text: $data.local_adult_cost"></td>
								<td data-bind="text: $data.other_cost"></td>
								<td data-bind="text: $data.gross_profit"></td>
								<td data-bind="text: $data.gross_profit_rate+'%'"></td>
								<td data-bind="text: $data.cash_flow"></td>
								<td data-bind="text: $data.spot_cash"></td>
								<td data-bind="text: ($data.gross_profit/$data.spot_cash).toFixed(2)*100+'%'"></td>
								<td data-bind="text: $data.product_value +'/'+($data.product_child_value?$data.product_child_value:'')"></td>
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
	<div id="cost-update" style="display: none; width: 800px">
		<div style="margin-top: 20px; height: 300px" id="div-ticket">
			<div class="input-row clearfloat">
				<div class="col-md-3" style="width: 33%">
					<label class="l">产品编号</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().product_number"></p>
					</div>
				</div>
				<div class="col-md-3" style="width: 33%">
					<label class="l">产品名称</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().name"></p>
					</div>
				</div>
				<div class="col-md-3" style="width: 33%">
					<label class="l">型号</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().product_model"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">

				<input type="hidden" id="business-profit-substract" data-bind="value:product().business_profit_substract" /> <input
					type="hidden" id="adult-price" data-bind="value:product().adult_price" /> <input type="hidden" id="child-price"
					data-bind="value:product().child_price" /> <input type="hidden" id="max-profit-substract"
					data-bind="value:product().max_profit_substract" />
				<form id="form-cost">
					<input type="hidden" data-bind="value:product().pk" name="product.pk" />
					<table style="width: 100%" id="table-ticket">

						<tr>
							<td style="width: 3%">&nbsp;</td>
							<td style="width: 28%"><label class="l">&nbsp;</label>
								<div class="ip required">
									<label class="l" style="text-align: center">成人</label>
								</div></td>
							<td style="width: 20%">
								<div class="ip">
									<label class="l" style="text-align: center">儿童</label>
								</div>
							</td>
							<td style="width: 25%"><label class="l">&nbsp;</label>
								<div class="ip">
									<label class="l" style="text-align: center">成人</label>
								</div></td>
							<td style="width: 24%">
								<div class="ip">
									<label class="l" style="text-align: center">儿童</label>
								</div>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>
								<div>
									<label class="l">产品报价</label>
									<div class="ip fix-width">
										<p class="ip-default" data-bind="text: product().adult_price"></p>
									</div>
								</div>
							</td>
							<td>
								<div class="ip">
									<p class="ip-default" data-bind="text: product().child_price"></p>
								</div>
							</td>
							<td><label class="l">毛利</label>
								<div class="ip  fix-width">
									<p class="ip-default" id="gross-profit" data-bind="text: product().gross_profit"></p>
									<input type="hidden" id="txt-gross-profit" data-bind="value: product().gross_profit"
										name="product.gross_profit" />
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" id="gross-child-profit" data-bind="text: product().gross_child_profit"></p>
									<input type="hidden" id="txt-gross-child-profit" data-bind="text: product().gross_child_profit"
										name="product.gross_child_profit" />
								</div></td>
						</tr>
						<tr>
							<td><input type="checkbox" value="Y" id="chk-air-ticket" onclick="caculateGrossProfit()"
								name="product.cash_flow_air_flg" /></td>
							<td>

								<div class="required">

									<label class="l">机票成本</label>
									<div class="ip fix-width">
										<input type="number" class="ip-" required="required" id="air-ticket-cost" onkeyup="caculateGrossProfit()"
											data-bind="value: product().air_ticket_cost" placeholder="成人机票" name="product.air_ticket_cost" />
									</div>
								</div>
							</td>
							<td>
								<div class="ip">
									<input type="number" class="ip-" id="air-ticket-child-cost" onkeyup="caculateGrossProfit()"
										data-bind="value: product().air_ticket_child_cost" placeholder="儿童机票" name="product.air_ticket_child_cost" />
								</div>
							</td>
							<td><label class="l">毛利率</label>
								<div class="ip fix-width">
									<p class="ip-default" id="gross-profit-rate" data-bind="text: product().gross_profit_rate"></p>
									<input type="hidden" id="txt-gross-profit-rate" data-bind="value: product().gross_profit_rate"
										name="product.gross_profit_rate" />
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" id="gross-child-profit-rate" data-bind="text: product().gross_child_profit_rate"></p>
									<input type="hidden" id="txt-gross-child-profit-rate" data-bind="text: product().gross_child_profit_rate"
										name="product.gross_child_profit_rate" />
								</div></td>
						</tr>
						<tr>
							<td><input type="checkbox" value="Y" id="chk-local" onclick="caculateGrossProfit()"
								name="product.cash_flow_local_flg" /></td>
							<td>
								<div class="required">
									<label class="l">地接成本</label>
									<div class="ip fix-width">
										<input type="number" class="ip-" id="local-adult-cost" onkeyup="caculateGrossProfit()" required="required"
											data-bind="value: product().local_adult_cost" placeholder="成人" name="product.local_adult_cost" />
									</div>
								</div>
							</td>
							<td>
								<div class="ip">
									<input type="number" class="ip-" id="local-child-cost" onkeyup="caculateGrossProfit()"
										data-bind="value: product().local_child_cost" placeholder="儿童" name="product.local_child_cost" />
								</div>
							</td>
							<td><label class="l">现金流</label>
								<div class="ip fix-width">
									<p class="ip-default" id="cash-flow" data-bind="text: product().cash_flow"></p>
									<input type="hidden" id="txt-cash-flow" data-bind="value: product().cash_flow" name="product.cash_flow" />
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" id="cash-child-flow" data-bind="text: product().cash_child_flow"></p>
									<input type="hidden" id="txt-cash-child-flow" data-bind="text: product().cash_child_flow"
										name="product.cash_child_flow" />
								</div></td>
						</tr>
						<tr>
							<td><input type="checkbox" id="chk-other" value="Y" onclick="caculateGrossProfit()"
								name="product.cash_flow_other_flg" /></td>
							<td><label class="l">其他成本</label>
								<div class="ip fix-width">
									<input type="number" class="ip-" id="other-cost" onkeyup="caculateGrossProfit()"
										data-bind="value: product().other_cost" placeholder="成人" name="product.other_cost" />
								</div></td>
							<td>
								<div class="ip">
									<input type="number" class="ip-" id="other-child-cost" onkeyup="caculateGrossProfit()"
										data-bind="value: product().other_child_cost" placeholder="儿童" name="product.other_child_cost" />
								</div>
							</td>
							<td><label class="l">现付资金</label>
								<div class="ip fix-width">
									<p class="ip-default" id="spot-cash" data-bind="text: product().spot_cash"></p>
									<input type="hidden" id="txt-spot-cash" data-bind="value: product().spot_cash" name="product.spot_cash" />
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" id="spot-child-cash" data-bind="text: product().spot_child_cash"></p>
									<input type="hidden" id="txt-spot-child-cash" data-bind="text: product().spot_child_cash"
										name="product.spot_child_cash" />
								</div></td>
						</tr>
					</table>
				</form>
			</div>
			<div style="margin-top: 50px; width: 700px; float: right">
				<button type="submit" style="float: right" class="btn btn-green col-md-1" onclick="cancelUpdateCost()">取消</button>
				<button type="submit" style="float: right" class="btn btn-green col-md-1" onclick="updateCost()">保存</button>
			</div>
		</div>

	</div>
	<div id="value-update" style="display: none; width: 800px">
		<div style="margin-top: 20px; height: 300px" id="div-ticket">
			<div class="input-row clearfloat">
				<div class="col-md-3" style="width: 33%">
					<label class="l">产品编号</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().product_number"></p>
					</div>
				</div>
				<div class="col-md-3" style="width: 33%">
					<label class="l">产品名称</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().name"></p>
					</div>
				</div>
				<div class="col-md-3" style="width: 33%">
					<label class="l">型号</label>
					<div class="ip fix-width">
						<p class="ip-default" data-bind="text: product().product_model"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<form id="form-value">
					<input type="hidden" data-bind="value:product().pk" name="product.pk" />
					<table style="width: 60%" id="table-ticket">

						<tr>
							<td style="width: 3%">&nbsp;</td>
							<td style="width: 48%"><label class="l">&nbsp;</label>
								<div class="ip">
									<label class="l" style="text-align: center">成人</label>
								</div></td>
							<td style="width: 49%">
								<div class="ip">
									<label class="l" style="text-align: center">儿童</label>
								</div>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><label class="l">毛利</label>
								<div class="ip  fix-width">
									<p class="ip-default" data-bind="text: product().gross_profit"></p>
								</div></td>
							<td><div class="ip  fix-width">
									<p class="ip-default" data-bind="text: product().gross_child_profit"></p>
								</div></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><label class="l">毛利率</label>
								<div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().gross_profit_rate"></p>
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().gross_child_profit_rate"></p>
								</div></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><label class="l">现金流</label>
								<div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().cash_flow"></p>
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().cash_child_flow"></p>
								</div></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><label class="l">现付资金</label>
								<div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().spot_cash"></p>
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" data-bind="text: product().spot_child_cash"></p>
								</div></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><label class="l">原分值</label>
								<div class="ip fix-width">
									<p class="ip-default" id="spot-cash" data-bind="text: product().product_value"></p>
								</div></td>
							<td><div class="ip fix-width">
									<p class="ip-default" id="spot-cash" data-bind="text: product().product_child_value"></p>
								</div></td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div class="required">
									<label class="l">新分值</label>
									<div class="ip fix-width">
										<input type="number" class="ip-" required="required" step="1" min="0" max="20" placeholder="0-20整数"
											name="delay.product_value" />
									</div>
								</div>
							</td>
							<td>
								<div class="ip fix-width">
									<input type="number" class="ip-" step="1" min="0" max="20" placeholder="0-20整数"
										name="delay.product_child_value" required="required" />
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="margin-top: 50px; width: 700px; float: right">
				<button type="submit" style="float: right" class="btn btn-green col-md-1" onclick="cancelUpdateValue()">取消</button>
				<button type="submit" style="float: right" class="btn btn-green col-md-1" onclick="updateValue()">保存</button>
			</div>
		</div>

	</div>
	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/js/product/product-analysis.js"></script>
</body>
</html>