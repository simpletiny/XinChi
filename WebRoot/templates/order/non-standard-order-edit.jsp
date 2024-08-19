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
textarea {
	width:200px !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				编辑非标订单<span data-bind="text:independent_msg"></span><a href="javascript:void(0)"
					onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<input type="hidden" id="key" value="<%=key%>" name="bnsOrder.pk"></input>
					<input type="hidden" data-bind="value:order().create_user" name="bnsOrder.create_user"></input>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">客户</label>
							<div class="ip fix-width">
								<input type="text" id="txt-client-employee-name" class="ip-"
									data-bind="value: employee().name,event:{click:choseClientEmployee}" placeholder="客户" />
							</div>
							<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: employee().pk"
								style="display: none" name="bnsOrder.client_employee_pk" id="client-employee-pk" />
						</div>
						<div class="col-md-3">
							<label class="l">财务主体</label>
							<div class="ip fix-width">
								<p class="ip-default" id="txt-financial-body-name" data-bind="text:employee().financial_body_name"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">产品名称</label>
							<div class="ip">
								<input type="text" class="ip-" placeholder="产品名称" maxlength="20" data-bind="value: order().product_name"
									name="bnsOrder.product_name" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat ">
						<div class="col-md-6">
							<label class="l">出团日期</label>
							<div class="ip">
								<input type="text" id="departure" class="ip- date-picker" data-bind="value: order().departure_date"
									placeholder="出团日期" name="bnsOrder.departure_date" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">天数</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: order().days" placeholder="天数" name="bnsOrder.days" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat ">
						<div class="col-md-6">
							<label class="l">总团款</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: order().receivable" placeholder="总团款"
									name="bnsOrder.receivable" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">成人</label>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="people-count" data-bind="value:order().adult_count" placeholder="人数"
									name="bnsOrder.adult_count" />
							</div>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" data-bind="value:order().adult_cost" placeholder="费用"
									name="bnsOrder.adult_cost" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">特殊</label>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="special-count" data-bind="value:order().special_count" placeholder="人数"
									name="bnsOrder.special_count" />
							</div>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" data-bind="value:order().special_cost" placeholder="费用"
									name="bnsOrder.special_cost" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">FY</label>
							<div class="ip">
								<input type="number" data-bind="value:order().fy" name="bnsOrder.fy" class="ip-" placeholder="FY" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">其他费用</label>
							<div class="ip">
								<input type="number" data-bind="value:order().other_cost" name="bnsOrder.other_cost" class="ip-"
									placeholder="其他费用" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">费用说明</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="50" data-bind="value: order().other_cost_comment"
									name="bnsOrder.other_cost_comment" placeholder="费用说明" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">团款备注</label>
							<div class="ip fix-width">
								<textarea type="text" class="ip-default" rows="7" maxlength="200" data-bind="value: order().receivable_comment"
									placeholder="团款备注" name="bnsOrder.receivable_comment"></textarea>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">用房说明</label>
							<div class="ip fix-width">
								<textarea type="text" class="ip-default" rows="7" data-bind="value:order().hotel_comment" maxlength="200"
									name="bnsOrder.hotel_comment" placeholder="用房说明"></textarea>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">销售特请</label>
							<div class="ip fix-width">
								<textarea type="text" class="ip-default" rows="7" data-bind="value:order().treat_comment" maxlength="200"
									name="bnsOrder.treat_comment" placeholder="销售特请"></textarea>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">订单备注（仅自己可见）</label>
							<div class="ip fix-width">
								<textarea type="text" class="ip-default" rows="7" maxlength="200" data-bind="value: order().comment"
									name="bnsOrder.comment" placeholder="需要备注说明的信息（仅自己可见）"></textarea>
							</div>
						</div>
					</div>
					<hr />
					<h3>名单</h3>
					<s:include value="common/name-bat.jsp"></s:include>
					<s:include value="common/name-list-nos.jsp"></s:include>
					<hr />
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传确认件<input type="file" name="file" accept=".jpg,.png" /></a> <input
								type="hidden" id="txt-confirm-file" data-bind="value:order().confirm_file" name="bnsOrder.confirm_file" />
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
	<script src="<%=basePath%>static/js/order/non-standard-order-edit.js?v=1.003"></script>
	<script src="<%=basePath%>static/js/order/non-standard-order-common.js?v=1.002"></script>
</body>
</html>