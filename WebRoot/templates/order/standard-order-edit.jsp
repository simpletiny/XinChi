<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/js/order/upload.css" />

<style>
#name-table tr th, td {
	text-align: center;
	vertical-align: middle !important
}

.fix-width {
	width: 40% !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				编辑标准订单<span data-bind="text:independent_msg"></span><a href="javascript:void(0)"
					onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<input type="hidden" id="key" value="<%=key%>" name="bsOrder.pk"></input> <input type="hidden"
						data-bind="value:order().independent_flg" name="bsOrder.independent_flg"></input> <input type="hidden"
						data-bind="value:order().team_number" id="team-number" name="bsOrder.team_number" />
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">客户</label>
							<div class="ip fix-width">
								<input type="text" id="txt-client-employee-name" class="ip-"
									data-bind="value: employee().name,event:{click:choseClientEmployee}" placeholder="客户" />
							</div>
							<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: employee().pk"
								style="display: none" name="bsOrder.client_employee_pk" id="client-employee-pk" />
						</div>
						<div class="col-md-3">
							<label class="l">财务主体</label>
							<div class="ip fix-width">
								<p class="ip-default" id="txt-financial-body-name" data-bind="text:employee().financial_body_name"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">产品名称</label>
							<div class="ip fix-width">
								<input type="text" id="txt-product-name" class="ip-"
									data-bind="value: product().name,event:{click:choseProduct}" placeholder="产品" /> <input type="hidden"
									data-bind="value: product().pk" id="txt-product-pk" name="bsOrder.product_pk" />
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">产品型号</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:product().product_model" id="p-product-model"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat ">
						<div class="col-md-6">
							<label class="l">出团日期</label>
							<div class="ip">
								<input type="text" id="departure" class="ip- date-picker" data-bind="value: order().departure_date"
									placeholder="出团日期" name="bsOrder.departure_date" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">天数</label>
							<div class="ip">
								<p class="ip-default" data-bind="text:product().days"></p>
								<input type="hidden" data-bind="value: product().days" placeholder="天数" name="bsOrder.days" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat ">
						<div class="col-md-3">
							<label class="l">总团款</label>
							<div class="ip fix-width">
								<p class="ip-default" id="txt-auto-sum-money" data-bind="text:order().receivable"></p>
								<input type="hidden" class="ip- auto-1" id="auto-sum-money" data-bind="value: order().receivable"
									placeholder="总团款" name="bsOrder.receivable" />
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">其他费用</label>
							<div class="ip fix-width">
								<input type="number" class="ip- auto-1" id="other-cost" onkeyup="autoPrice()"
									data-bind="value: order().other_cost" placeholder="其他费用" name="bsOrder.other_cost" />
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">成人</label>
							<div class="ip fix-width">
								<p class="ip-default" id="txt-auto-adult-count" data-bind="text:order().adult_count">12312</p>
								<input type="hidden" class="ip- auto-1" id="auto-adult-count" data-bind="value: order().adult_count"
									name="bsOrder.adult_count" />
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">儿童</label>
							<div class="ip fix-width">
								<p class="ip-default" id="txt-auto-children-count" data-bind="text:order().special_count"></p>
								<input type="hidden" class="ip- auto-1" id="auto-children-count" data-bind="value: order().special_count"
									placeholder="儿童数" name="bsOrder.special_count" />
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">FLY</label>
							<div class="ip fix-width">
								<input type="text" data-bind="value:order().fy" name="bsOrder.fy" class="ip-" placeholder="FLY" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-4">
							<label class="l">团款备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="10" id="receivable-comment" maxlength="200"
									data-bind="value: order().receivable_comment" placeholder="团款备注" name="bsOrder.receivable_comment"></textarea>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">销售特请</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="10" data-bind="value:order().treat_comment" maxlength="200"
									name="bsOrder.treat_comment" placeholder="销售特请"></textarea>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">订单备注（仅自己可见）</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="10" maxlength="200" data-bind="value: order().comment"
									name="bsOrder.comment" placeholder="需要备注说明的信息（仅自己可见）"></textarea>
							</div>
						</div>
					</div>
					<hr />
					<h3>名单</h3>
					<s:include value="common/name-bat.jsp"></s:include>
					<s:include value="common/name-list.jsp"></s:include>
					<hr />
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传确认件<input type="file" accept=".jpg,.png" name="file" /></a> <input
								type="hidden" data-bind="value:order().confirm_file" id="txt-confirm-file" name="bsOrder.confirm_file" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: updateOrder">保存</a>
				</div>
			</div>
		</div>
	</div>
	<div id="client-pick" style="display: none;width:600px">
		<div class="form-horizontal search-panel">
			<div class="form-group">
				<div class="input-row clearfloat">
					<label class="col-md-2 control-label">姓名</label>
					<div class="col-md-6">
						<input type="text" id="client_name" class="form-control" placeholder="姓名" />
					</div>
					<button type="submit" class="btn btn-green col-md-1" style="float:right" data-bind="event:{click:searchClientEmployee }">搜索</button>
				</div>
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
				<tbody data-bind="foreach: clientEmployees">
					<tr data-bind="event: {click: function(){ $parent.pickClientEmployee($data)}}">
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
	<div id="product-pick" style="display: none; width: 1200px">
		<div class="form-horizontal search-panel">
			<div class="input-row clearfloat">
				<label class="col-md-1 control-label">产品名称</label>
				<div class="col-md-3">
					<input type="text" id="product-name" class="form-control" placeholder="产品名称" />
				</div>
				<label class="col-md-1 control-label">产品型号</label>
				<div class="col-md-3">
					<input type="text" id="product-model" class="form-control" placeholder="产品型号" />
				</div>
			</div>
			<div class="input-row clearfloat">
				<button type="submit" class="btn btn-green col-md-1" style="float: right" data-bind="event:{click:searchProduct }">搜索</button>
			</div>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover">
				<thead>
					<tr role="row">
						<th>产品名称</th>
						<th title="成人/儿童">分值</th>
						<th>产品线</th>
						<th>型号</th>
						<th>天数</th>
						<th>首段城市对</th>
						<th>直客报价</th>
						<th>儿童报价</th>
						<th>同业返利</th>
						<th>最大让利</th>
						<th>产品经理</th>
					</tr>
				</thead>
				<tbody data-bind="foreach: products">
					<tr data-bind="event: {click: function(){ $parent.pickProduct($data)}}">
						<td data-bind="text: $data.name"></td>
						<td data-bind="text: $data.product_value +'/'+($data.product_child_value?$data.product_child_value:'')"></td>
						<td data-bind="text: $data.location"></td>
						<td data-bind="text: $data.product_model"></td>
						<td data-bind="text: $data.days"></td>
						<td
							data-bind="text: ($data.first_air_start?$data.first_air_start:'') + '--' + ($data.first_air_end?$data.first_air_end:'')"></td>
						<td data-bind="text: $data.adult_price"></td>
						<td data-bind="text: $data.child_price"></td>
						<td data-bind="text: $data.business_profit_substract"></td>
						<td data-bind="text: $data.max_profit_substract"></td>
						<td data-bind="text: $data.product_manager"></td>
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
	<script>
		$(".order-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/confirm-upload.js"></script>
	<script src="<%=basePath%>static/js/order/passenger.js?v=1.001"></script>
	<script src="<%=basePath%>static/js/order/standard-order-edit.js?v=1.004"></script>
	<script src="<%=basePath%>static/js/order/standard-order-common.js?v=1.002"></script>
</body>
</html>