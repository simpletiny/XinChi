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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/js/order/upload.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				非标订单决算<span data-bind="text:independent_msg"></span><a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		
		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
				<input type="hidden" id="key" value="<%=key%>" name="fnsOrder.pk"></input>
				<input type="hidden"  data-bind="value:order().independent_flg" name="fnsOrder.independent_flg"></input>
				<input type="hidden" value='Y' name="fnsOrder.confirm_flg"></input>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">客户</label>
							<div class="ip">
								<input type="text" id="txt-client-employee-name" disabled="disabled"  class="ip-" data-bind="value: employee().name,event:{click:choseClientEmployee}" placeholder="客户" required="required" />
							</div>
							<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: order().client_employee_pk" style="display: none" name="fnsOrder.client_employee_pk" id="client-employee-pk"
								required="required" />
						</div>
						<div class="col-md-6 required">
							<label class="l">产品名称</label>
							<div class="ip">
								<input type="text" class="ip- disabled" placeholder="产品名称" maxlength="20" data-bind="value: order().product_name" name="fnsOrder.product_name" required="required"/>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat required">
						<div class="col-md-6">
							<label class="l">出团日期</label>
							<div class="ip">
								<input type="text" id="departure" class="ip- disabled" data-bind="value: order().departure_date" placeholder="出团日期" name="fnsOrder.departure_date" required="required"/>
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">天数</label>
							<div class="ip">
								<input type="text" class="ip- disabled" data-bind="value: order().days" placeholder="天数" name="fnsOrder.days" required="required"/>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat required">
						<div class="col-md-6">
							<label class="l">总团款</label>
							<div class="ip">
								<input type="number" required="required" class="ip-" data-bind="value: order().receivable" placeholder="总团款" name="fnsOrder.receivable" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat ">
						<div class="col-md-6 required">
							<label class="l">成人</label>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip- disabled" id="people-count" data-bind="value:order().adult_count" placeholder="人数" name="fnsOrder.adult_count" required="required"/>
							</div>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="people-count" data-bind="value:order().adult_cost" placeholder="费用" name="fnsOrder.adult_cost" required="required"/>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">特殊</label>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip- disabled" id="people-count" data-bind="value:order().special_count" placeholder="人数" name="fnsOrder.special_count" />
							</div>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="people-count" data-bind="value:order().special_cost" placeholder="费用" name="fnsOrder.special_cost" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat requried">
						<div class="col-md-12">
							<label class="l">名单（请不要输入客人电话和产品名称，谢谢^0^）</label>
							<div class="ip">
								<textarea style="width:500px" type="text" id="txt-name-list" onblur="formatNameList(this)" class="ip- default disabled" rows="15" data-bind="value: order().name_list" name="fnsOrder.name_list" placeholder="姓名+身份证号"></textarea>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">FY</label>
							<div class="ip">
								<input type="number" data-bind="value:order().fy" name="fnsOrder.fy" class="ip-" placeholder="FY" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">其他费用</label>
							<div class="ip">
								<input type="number" data-bind="value:order().other_cost" name="fnsOrder.other_cost" class="ip-" placeholder="其他费用" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">费用说明</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="50" data-bind="value: order().other_cost_comment" name="fnsOrder.other_cost_comment" placeholder="费用说明" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat required">
						<div class="col-md-6">
							<label class="l">确认日期</label>
							<div class="ip">
								<input type="text" data-bind="value:confirm_date" name="fnsOrder.confirm_date" required="required" class="ip- disabled" placeholder="确认日期" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" maxlength="200" data-bind="value: order().comment" name="fnsOrder.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传确认件<input type="file"  name="file" /></a> <input type="hidden" id="txt-confirm-file"  data-bind="value:order().confirm_file" name="fnsOrder.confirm_file" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: updateOrder">确认</a>
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
							<tr data-bind="event: {click: function(){ $parent.pickClientEmployee($data.name,$data.pk)}}">
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
	<script src="<%=basePath%>static/js/order/non-standard-order-final-create.js"></script>
</body>
</html>