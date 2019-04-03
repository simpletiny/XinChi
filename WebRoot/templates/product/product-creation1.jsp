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

#table-supplier tr td input[type="text"], input[type="number"], input[type="button"]
	{
	width: 90%;
}

#table-supplier {
	border-collapse: separate;
	border-spacing: 0px 10px;
	border-spacing: 0px 10px;
}

#table-product {
	border-collapse: separate;
	border-spacing: 0px 10px;
}

.required th[class="r"]:after, td[class="r"]:after {
	content: " *";
	color: red;
	font-weight: bold;
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

.fix-width1 {
	display: inline-block;
}

.fix-label {
	width: 80px !important;
}

.hr-big {
	border-top: 1px solid black !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				新建产品<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<div class="input-row clearfloat">
						<label class="l"><input type="checkbox" checked="checked" value="Y" name="product.strict_price_flg" />严格执行定价</label>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-1">&nbsp;</div>
						<div class="col-md-3 required">

							<label class="l">产品线</label>
							<div class="ip fix-width">
								<select class="form-control" style="height: 34px"
									data-bind="options: locations,value:product().location, optionsCaption: '--请选择--'" name="product.location"
									required="required"></select>
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l">产品名称</label>
							<div class="ip fix-width">
								<input type="text" class="ip-" data-bind="value: product().name" placeholder="8字以内" maxlength="8"
									name="product.name" required="required" />
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l">型号</label>
							<div class="ip fix-width">
								<input type="text" class="ip-" maxlength="6" data-bind="value: product().product_model" placeholder="6字以内"
									name="product.product_model" required="required" />
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l">天数</label>
							<div class="ip fix-width">
								<input type="number" class="ip-" data-bind="value: product().days" placeholder="天数" name="product.days"
									required="required" />
							</div>
						</div>
					</div>

					<div class="input-row clearfloat">
						<table style="width: 100%" id="table-product">
							<tr>
								<td style="width: 4%"></td>
								<td style="width: 24%"><label class="l">&nbsp;</label>
									<div class="ip fix-width required">
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
								<td>现付</td>
								<td>
									<div class="required">
										<label class="l">直客报价</label>
										<div class="ip fix-width">
											<input type="number" class="ip-" id="adult-price" onkeyup="caculateGrossProfit()"
												data-bind="value: product().adult_price" placeholder="成人报价" name="product.adult_price" required="required" />
										</div>
									</div>
								</td>
								<td>
									<div class="ip fix-width">
										<input type="number" class="ip-" id="child-price" onkeyup="caculateGrossProfit()"
											data-bind="value: product().child_price" placeholder="儿童报价" name="product.child_price" />
									</div>
								</td>
								<td>
									<div class=" required">
										<label class="l">同业返利</label>
										<div class="ip fix-width">
											<input type="number" class="ip-" id="business-profit-substract" onkeyup="caculateGrossProfit()"
												data-bind="value: product().business_profit_substract" placeholder="同业返利"
												name="product.business_profit_substract" required="required" />
										</div>
									</div>
								</td>
								<td>
									<div class=" required">
										<label class="l">最大让利</label>
										<div class="ip fix-width">
											<input type="number" class="ip-" required="required" id="max-profit-substract"
												onkeyup="caculateGrossProfit()" data-bind="value: product().max_profit_substract" placeholder="最大让利"
												name="product.max_profit_substract" />
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td><input type="checkbox" value="Y" id="chk-air-ticket" checked="checked" onclick="caculateGrossProfit()"
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
									<div class="ip fix-width">
										<input type="number" class="ip-" id="air-ticket-child-cost" onkeyup="caculateGrossProfit()"
											data-bind="value: product().air_ticket_child_cost" placeholder="儿童机票" name="product.air_ticket_child_cost" />
									</div>
								</td>
								<td><label class="l">首段出港</label>
									<div class="ip fix-width">
										<input type="text" class="ip-" data-bind="value: product().first_air_start" maxlength="10" placeholder="首段出港"
											name="product.first_air_start" />
									</div></td>
								<td><label class="l">首段目的</label>
									<div class="ip fix-width">
										<input type="text" class="ip-" maxlength="10" data-bind="value: product().first_air_end" placeholder="首段目的"
											name="product.first_air_end" />
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
									<div class="ip fix-width">
										<input type="number" class="ip-" id="local-child-cost" onkeyup="caculateGrossProfit()"
											data-bind="value: product().local_child_cost" placeholder="儿童" name="product.local_child_cost" />
									</div>
								</td>
								<td style="width: 48%" colspan="2" rowspan="3"><label class="l">销售注意</label>
									<div class="ip">
										<textarea type="text" class="ip-default" rows="8" maxlength="200" data-bind="value: product().sale_attention"
											name="product.sale_attention" placeholder="技术交底"></textarea>
									</div></td>
							</tr>
							<tr>
								<td><input type="checkbox" id="chk-other" value="Y" checked="checked" onclick="caculateGrossProfit()"
									name="product.cash_flow_other_flg" /></td>
								<td><label class="l">其他成本</label>
									<div class="ip fix-width">
										<input type="number" class="ip-" id="other-cost" onkeyup="caculateGrossProfit()"
											data-bind="value: product().other_cost" placeholder="成人" name="product.other_cost" />
									</div></td>
								<td>
									<div class="ip fix-width">
										<input type="number" class="ip-" id="other-child-cost" onkeyup="caculateGrossProfit()"
											data-bind="value: product().other_child_cost" placeholder="儿童" name="product.other_child_cost" />
									</div>
								</td>
							</tr>

							<tr>
								<td style="width: 4%">&nbsp;</td>
								<td style="width: 24%"><label class="l">毛利</label>
									<div class="ip fix-width">
										<p class="ip-default" id="gross-profit" data-bind="text: product().gross_profit"></p>
										<input type="hidden" id="txt-gross-profit" data-bind="text: product().gross_profit"
											name="product.gross_profit" />
									</div></td>
								<td style="width: 24%"><div class="ip fix-width">
										<p class="ip-default" id="gross-child-profit" data-bind="text: product().gross_child_profit"></p>
										<input type="hidden" id="txt-gross-child-profit" data-bind="text: product().gross_child_profit"
											name="product.gross_child_profit" />
									</div></td>
							</tr>
							<tr>
								<td style="width: 4%">&nbsp;</td>
								<td style="width: 24%"><label class="l">毛利率</label>
									<div class="ip fix-width">
										<p class="ip-default" id="gross-profit-rate" data-bind="text: product().gross_profit_rate"></p>
										<input type="hidden" id="txt-gross-profit-rate" data-bind="text: product().gross_profit_rate"
											name="product.gross_profit_rate" />
									</div></td>
								<td style="width: 24%"><div class="ip fix-width">
										<p class="ip-default" id="gross-child-profit-rate" data-bind="text: product().gross_child_profit_rate"></p>
										<input type="hidden" id="txt-gross-child-profit-rate" data-bind="text: product().gross_child_profit_rate"
											name="product.gross_child_profit_rate" />
									</div></td>
							</tr>
							<tr>
								<td style="width: 4%">&nbsp;</td>
								<td style="width: 24%"><label class="l">现金流</label>
									<div class="ip fix-width">
										<p class="ip-default" id="cash-flow" data-bind="text: product().cash_flow"></p>
										<input type="hidden" id="txt-cash-flow" data-bind="text: product().cash_flow" name="product.cash_flow" />
									</div></td>
								<td style="width: 24%"><div class="ip fix-width">
										<p class="ip-default" id="cash-child-flow" data-bind="text: product().cash_child_flow"></p>
										<input type="hidden" id="txt-cash-child-flow" data-bind="text: product().cash_child_flow"
											name="product.cash_child_flow" />
									</div></td>
								<td style="width: 48%" colspan="2" rowspan="3"><label class="l">儿童策略</label>
									<div class="ip">
										<textarea type="text" class="ip-default" rows="8" maxlength="200" data-bind="value: product().sale_strategy"
											name="product.sale_strategy" placeholder="收客建议"></textarea>
									</div></td>
							</tr>
							<tr>
								<td style="width: 4%">&nbsp;</td>
								<td style="width: 24%"><label class="l">现付资金</label>
									<div class="ip fix-width">
										<p class="ip-default" id="spot-cash" data-bind="text: product().spot_cash"></p>
										<input type="hidden" id="txt-spot-cash" data-bind="text: product().spot_cash" name="product.spot_cash" />
									</div></td>
								<td style="width: 24%"><div class="ip fix-width">
										<p class="ip-default" id="spot-child-cash" data-bind="text: product().spot_child_cash"></p>
										<input type="hidden" id="txt-spot-child-cash" data-bind="text: product().spot_child_cash"
											name="product.spot_child_cash" />
									</div></td>
							</tr>

							<tr>
								<td></td>
								<td>
									<div class="required">
										<label class="l">产品分值</label>
										<div class="ip fix-width">
											<input type="number" class="ip-" required="required" step="1" min="0" max="20"
												data-bind="value: product().product_value" placeholder="0-20整数" name="product.product_value" />
										</div>
									</div>
								</td>
								<td>
									<div class="ip fix-width">
										<input type="number" class="ip-" step="1" min="0" max="20" data-bind="value: product().product_child_value"
											placeholder="0-20整数" name="product.product_child_value" required="required" />
									</div>
								</td>
							</tr>
						</table>
					</div>
					<hr class="hr-big" />
					<h3>地接维护</h3>
					<div id="div-supplier">
						<div>
							<div class="input-row clearfloat">
								<div class="col-md-3 required">
									<label class="l" style="width: 70px !important">地接社</label>
									<div class="fix-width1">
										<input type="text" class="ip-" st="supplier-name" onclick="choseSupplierEmployee(event)" /> <input
											type="text" class="need" st="supplier-pk" style="display: none" />
									</div>

								</div>
								<div class="col-md-3 required">
									<label class="l" style="width: 70px !important">产品名称</label>
									<div class="fix-width1">
										<input type="text" class="ip-" />
									</div>
								</div>
								<div class="col-md-2 required">
									<label class="l" style="width: 70px !important">天数</label>
									<div class="ip" style="width: 50% !important">
										<input type="number" class="ip-" />
									</div>
								</div>
								<div class="col-md-2 required">
									<label class="l" style="width: 70px !important">成人</label>
									<div class="ip" style="width: 50% !important">
										<input type="number" class="ip-" />
									</div>
								</div>
								<div class="col-md-2 required">
									<label class="l" style="width: 70px !important">儿童</label>
									<div class="ip" style="width: 50% !important">
										<input type="number" class="ip-" />
									</div>
								</div>
							</div>
							<div style="margin-top: 20px; padding-left: 70px">
								<table style="width: 90%" id="table-supplier">
									<thead>
										<tr class="required">
											<th style="width: 5%"></th>
											<th style="width: 5%"></th>
											<th style="width: 5%"></th>
											<th style="width: 10%"></th>
											<th style="width: 5%"></th>
											<th style="width: 10%"></th>
											<th class="r" style="width: 10%">天次</th>
											<th class="r" style="width: 10%">交通工具</th>
											<th class="r" style="width: 10%">抵离时间</th>
											<th class="r" style="width: 10%">抵离城市</th>
											<th class="r" style="width: 10%">抵离地点</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
											<td class="r">接：</td>
											<td><input name="se" type="radio" />航段</td>
											<td><input type="text" /></td>
											<td><input name="se" type="radio" />其他</td>
											<td><input type="text" /></td>
											<td><input type="number" /></td>
											<td><input type="text" /></td>
											<td><input type="text" /></td>
											<td><input type="text" /></td>
											<td><input type="text" /></td>
										</tr>
										<tr>
											<td><input type="button" value="+" onclick="addRow(this)" /></td>
											<td class="r">送：</td>
											<td><input name="se" type="radio" />航段</td>
											<td><input type="text" /></td>
											<td><input name="se" type="radio" />其他</td>
											<td><input type="text" /></td>
											<td><input type="number" /></td>
											<td><input type="text" /></td>
											<td><input type="text" /></td>
											<td><input type="text" /></td>
											<td><input type="text" /></td>
										</tr>

									</tbody>
								</table>
							</div>

							<div class="input-row clearfloat">
								<div class="col-md-6">
									<label class="l" style="width: 70px !important">游客信息：</label>
									<div class="ip">
										<div style="padding-top: 4px;">
											<em class="small-box"> <input type="checkbox" checked="checked" /><label>姓名</label> <input
												type="checkbox" checked="checked" /><label>性别</label> <input type="checkbox" /><label>年龄</label> <input
												type="checkbox" /><label>身份证号码</label> <input type="checkbox" checked="checked" /><label>联系方式</label> <input
												type="checkbox" /><label>分房组</label>
											</em>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="ip">
										<a type="submit" class="btn btn-r" data-bind="click: createProduct">上传确认件</a>
									</div>
								</div>
							</div>
							<hr />
						</div>
					</div>
					<div class="input-row clearfloat" style="text-align: right">
						<div class="col-md-12">
							<div class="ip">
								<a type="submit" class="btn btn-r" href="javacript:void(0);" onclick="deleteSupplier()">-地接社</a> <a
									type="submit" class="btn btn-r" href="javacript:void(0);" onclick="addSupplier()">+地接社</a>
							</div>
						</div>
					</div>
					<hr class="hr-big" />
					<h3>本地维护</h3>
					<div id="div-location">
						<div>
							<div class="input-row clearfloat">
								<div class="col-md-3">
									<div class="ip">
										<em class="small-box"> <input type="checkbox" checked="checked" /><label>机场衔接</label>
										</em>
									</div>

								</div>
								<div class="col-md-2">
									<div class="ip">
										<em class="small-box"> <input type="checkbox" /><label>接送机场</label>
										</em>
									</div>
								</div>
								<div class="col-md-3 required">
									<label class="l" style="width: 70px !important">费用：</label>
									<div class="ip">
										<input class="ip- " type="number" />
									</div>
								</div>
							</div>
							<div class="input-row clearfloat">
								<div class="col-md-3 required">
									<label class="l" style="width: 80px !important"><input type="checkbox" />供应商</label>
									<div class="fix-width1">
										<input class="ip- " type="text" />
									</div>
								</div>
								<div class="col-md-3 required">
									<label class="l" style="width: 70px !important">服务名称</label>
									<div class="ip">
										<input class="ip- " type="text" />
									</div>
								</div>
								<div class="col-md-3 required">
									<label class="l" style="width: 70px !important">人均成人</label>
									<div class="ip">
										<input class="ip- " type="number" />
									</div>
								</div>
								<div class="col-md-3 required">
									<label class="l" style="width: 70px !important">人均儿童</label>
									<div class="ip">
										<input class="ip- " type="number" />
									</div>
								</div>
							</div>
							<div class="input-row clearfloat">
								<div class="col-md-12">
									<label class="l">服务要求：</label>
									<div class="ip">
										<textarea type="text" class="ip-default" rows="3" maxlength="200" data-bind="value: product().comment"
											name="product.comment" placeholder="服务要求"></textarea>
									</div>
								</div>
							</div>
							<div class="input-row clearfloat">
								<div class="col-md-6">
									<label class="l" style="width: 70px !important">游客信息：</label>
									<div class="ip">
										<div style="padding-top: 4px;">
											<em class="small-box"> <input type="checkbox" checked="checked" /><label>姓名</label> <input
												type="checkbox" checked="checked" /><label>性别</label> <input type="checkbox" /><label>年龄</label> <input
												type="checkbox" /><label>身份证号码</label> <input type="checkbox" checked="checked" /><label>联系方式</label> <input
												type="checkbox" /><label>分房组</label>
											</em>
										</div>
									</div>
								</div>
							</div>
							<hr />
						</div>
					</div>
					<div class="input-row clearfloat" style="text-align: right">
						<div class="col-md-12">
							<div class="ip">
								<a type="submit" class="btn btn-r" href="javacript:void(0);" onclick="deleteLocation()">-本地</a> <a type="submit"
									class="btn btn-r" href="javacript:void(0);" onclick="addLocation()">+本地</a>
							</div>
						</div>
					</div>
					<hr class="hr-big" />
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" maxlength="200" data-bind="value: product().comment"
									name="product.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
				</form>
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createProduct">保存</a> <a type="submit"
						class="btn btn-green btn-r" onclick="javascript:history.go(-1);return false;">放弃</a>
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
	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>

	<script src="<%=basePath%>static/js/product/product-creation1.js"></script>
</body>
</html>