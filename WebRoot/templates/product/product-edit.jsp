<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style>
#table-supplier th,#table-supplier td {
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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				修改产品<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<input type="hidden" id="key" value="<%=key%>" name="product.pk"></input>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">产品名称</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: product().name" placeholder="8字以内" maxlength="8" name="product.name" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">产品线</label>
							<div class="ip">
								<select class="form-control" style="height: 34px" data-bind="options: locations,value:product().location, optionsCaption: '--请选择--'" name="product.location" required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">天数</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: product().days" placeholder="天数" name="product.days" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">同业价格</label>
							<div class="ip">
								<input type="number" class="ip-" id="business-price" onkeyup="caculateGrossProfit()" data-bind="value: product().business_price" placeholder="同业价格" name="product.business_price" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">产品分值</label>
							<div class="ip">
								<input type="number" class="ip-" max="5" min="0" required ="required" value="1" data-bind="value: product().product_value" placeholder="产品分值" name="product.product_value" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">最大让利</label>
							<div class="ip">
								<input type="number" class="ip-" id="max-profit-substract" onkeyup="caculateGrossProfit()" required="required" data-bind="value: product().max_profit_substract" placeholder="最大让利" name="product.max_profit_substract" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">机票成本</label>
							<div class="ip">
								<input type="number" class="ip-" required ="required" id="air-ticket-cost" onkeyup="caculateGrossProfit()" data-bind="value: product().air_ticket_cost" placeholder="机票成本" name="product.air_ticket_cost" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">其它成本</label>
							<div class="ip">
								<input type="number" class="ip-" required ="required" data-bind="value: product().other_cost" id="other-cost" placeholder="自动汇总" name="product.other_cost" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">产品毛利</label>
							<div class="ip">
								<input type="number" class="ip-" id="gross-profit" required ="required" data-bind="value: product().gross_profit" placeholder="产品毛利" name="product.gross_profit" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">毛利率</label>
							 <div class="ip"><p class="ip-default" id="gross-profit-rate" data-bind="text: product().gross_profit_rate"  name="product.gross_profit_rate" ></p></div>
						</div>
					</div>
					

					<h3>供应商信息</h3>
					<div style="margin-top: 20px;">
						<table style="width: 90%" id="table-supplier">
							<thead>
								<tr class="required">
									<th style="width: 5%">序号</th>
									<th class="r" style="width: 10%">供应商</th>
									<th class="r" style="width: 10%">产品名称</th>
									<th class="r" style="width: 5%">价格</th>
									<th class="r" style="width: 9%">接团天次</th>
									<th style="width: 10%">接团方式</th>
									<th style="width: 10%">接团人</th>
									<th style="width: 10%">联系方式</th>
									<th class="r" style="width: 9%">送团天次</th>
									<th style="width: 10%">送团方式</th>
									<th style="width: 2%"></th>
								</tr>
							</thead>
							<!-- ko if:productSuppliers().length>0 -->
							<tbody data-bind="foreach:productSuppliers">
								<tr>
									<td st="index" data-bind="text:$data.supplier_index"></td>
									<td><input type="text" st="supplier-name" data-bind="value:$data.supplier_employee_name" onclick="choseSupplierEmployee(event)" /> <input type="text"
									 class="need" data-bind="value:$data.supplier_employee_pk" st="supplier-pk" style="display: none" /></td>
									<td><input class="need" st="supplier-product-name" maxlength="10" data-bind="value:$data.supplier_product_name" type="text" /></td>
									<td><input class="need" onkeyup="caculateOtherCost()" st="supplier-cost" data-bind="value:$data.supplier_cost" type="number" /></td>
									<td><input class="need" st="land-day" data-bind="value:$data.land_day" type="number" /></td>
									<td><input st="pick-type" data-bind="value:$data.pick_type" maxlength="50" type="text" /></td>
									<td><input st="picker" data-bind="value:$data.picker" maxlength="10" type="text" /></td>
									<td><input st="picker-cellphone" data-bind="value:$data.picker_cellphone" maxlength="15" type="number" /></td>
									<td><input class="need" st="off-day" type="number" data-bind="value:$data.off_day" /></td>
									<td><input st="send-type" maxlength="50" type="text" data-bind="value:$data.send_type" /></td>
									<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
								</tr>
							</tbody>
							<!-- /ko -->
							<!-- ko if:productSuppliers().length<1 -->
							<tbody>
								<tr>
									<td st="index">1</td>
									<td><input type="text" st="supplier-name" onclick="choseSupplierEmployee(event)" /> <input class="need" type="text" st="supplier-pk" style="display: none" /></td>
									<td><input class="need" st="supplier-product-name" maxlength="10" type="text" /></td>
									<td><input class="need" onkeyup="caculateOtherCost()" st="supplier-cost" type="number" /></td>
									<td><input class="need" st="land-day" type="number" /></td>
									<td><input st="pick-type" maxlength="50" type="text" /></td>
									<td><input st="picker" maxlength="10" type="text" /></td>
									<td><input st="picker-cellphone" maxlength="15" type="number" /></td>
									<td><input class="need" st="off-day" type="number" /></td>
									<td><input st="send-type" maxlength="50" type="text" /></td>
									<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
								</tr>
							</tbody>
							<!-- /ko -->


						</table>
						<div style="margin-top: 20px; padding-left: 200px">
							<input type="button" value="添加供应商" onclick="addRow()"></input>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" data-bind="value: product().comment" name="product.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: updateProduct">保存</a>
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
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>

	<script src="<%=basePath%>static/js/product/product-edit.js"></script>
</body>
</html>