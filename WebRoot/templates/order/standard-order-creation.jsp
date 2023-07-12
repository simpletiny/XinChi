<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
	String independent_flg = request.getParameter("independent_flg");
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
				新建标准订单<span data-bind="text:independent_msg"></span><a href="javascript:void(0)"
					onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<input type="hidden" id="key" value="<%=key%>"></input> <input type="hidden" id="independent_flg"
			value="<%=independent_flg%>"></input>
		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<input type="hidden" data-bind="value:product().as_adult_flg" name="bsOrder.as_adult_flg" />
					<div class="input-row clearfloat">
						<div class="col-md-3 required">
							<label class="l">客户</label>
							<div class="ip fix-width">
								<input type="text" id="txt-client-employee-name" class="ip-"
									data-bind="value: order().client_employee_name,event:{click:choseClientEmployee}" placeholder="客户"
									required="required" />
							</div>
							<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: order().client_employee_pk"
								style="display: none" name="bsOrder.client_employee_pk" id="client-employee-pk" required="required" />
						</div>
						<div class="col-md-3">
							<label class="l">财务主体</label>
							<div class="ip fix-width">
								<p class="ip-default" id="txt-financial-body-name"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">产品名称</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:product().name"></p>
								<input type="hidden" data-bind="value: product().pk" name="bsOrder.product_pk" required="required" />
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">产品型号</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:product().product_number"></p>
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
								<p class="ip-default" id="txt-auto-sum-money">0</p>
								<input type="hidden" class="ip- auto-1" id="auto-sum-money" data-bind="value: order().receivable"
									name="bsOrder.receivable" />
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
								<p class="ip-default" id="txt-auto-adult-count">0</p>
								<input type="hidden" class="ip- auto-1" id="auto-adult-count" data-bind="value: order().adult_count"
									placeholder="成人数" name="bsOrder.adult_count" />
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">儿童</label>
							<div class="ip fix-width">
								<p class="ip-default" id="txt-auto-children-count">0</p>
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
							<label class="l">接待备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="10" data-bind="value:order().treat_comment" maxlength="200"
									name="bsOrder.treat_comment" placeholder="接待备注"></textarea>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">订单备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="10" maxlength="200" data-bind="value: order().comment"
									name="bsOrder.comment" placeholder="需要备注说明的信息"></textarea>
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
								type="hidden" name="bsOrder.confirm_file" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createOrder">保存</a>
				</div>
			</div>
		</div>
	</div>
	<div id="client-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">姓名</label>
						<div class="col-md-6">
							<input type="text" id="client_name" class="form-control" placeholder="姓名" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchClientEmployee }">搜索</button>
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
	<script src="<%=basePath%>static/js/order/standard-order-creation.js?v=1.001"></script>
	<script src="<%=basePath%>static/js/order/standard-order-common.js?v=1.002"></script>
</body>
</html>