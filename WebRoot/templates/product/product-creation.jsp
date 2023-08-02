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

#table-product {
	border-collapse: separate;
	border-spacing: 0px 10px;
}

.required th[class="r"]:after {
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
						<label class="l"><input type="checkbox" checked="checked" id="chk-strict" value="Y" name="product.strict_price_flg" />严格执行定价</label>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-1">&nbsp;</div>
						<div class="col-md-3 required">

							<label class="l">产品线</label>
							<div class="ip fix-width">
								<select class="form-control" style="height: 34px"
									data-bind="options: locations,optionsText:'name',optionsValue:'name',value:product().location, optionsCaption: '--请选择--'"
									name="product.location" required="required"></select>
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
									<div>
										<label class="l">最大让利</label>
										<div class="ip fix-width">
											<input type="number" class="ip-" id="max-profit-substract"
												onkeyup="caculateGrossProfit()" data-bind="value: product().max_profit_substract" placeholder="最大让利"
												name="product.max_profit_substract" />
										</div>
									</div>
								</td>
							</tr>
							<tr>
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
								<td><label class="l">证件类型</label><label class="l"><input type="checkbox" checked value="1" name="product.id_types" />身份证</label><label class="l"><input type="checkbox" value="2" name="product.id_types" />护照</label></td>
							</tr>
							<tr>
								<td colspan="2"><label class="l" style="width:100%" title="儿童价格和分值将按照成人标准"><input type="checkbox" value="Y" name="product.as_adult_flg" />允许儿童按成人操作</label></td>
								<td></td>
							</tr>
							<tr>
								<td style="width: 48%" colspan="2" rowspan="3"><label class="l">销售注意</label>
									<div class="ip">
										<textarea type="text" class="ip-default" rows="8" maxlength="200" data-bind="value: product().sale_attention"
											name="product.sale_attention" placeholder="技术交底"></textarea>
									</div></td>
								<td style="width: 48%" colspan="2" rowspan="3"><label class="l">儿童策略</label>
									<div class="ip">
										<textarea type="text" class="ip-default" rows="8" maxlength="200" data-bind="value: product().sale_strategy"
											name="product.sale_strategy" placeholder="收客建议"></textarea>
									</div></td>
							</tr>
						</table>
					</div>
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
	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>

	<script src="<%=basePath%>static/js/product/product-creation.js?v=1.001"></script>
</body>
</html>