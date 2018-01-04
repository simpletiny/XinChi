<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String independent_flg = request.getParameter("independent_flg");
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
				新建非标订单<span data-bind="text:independent_msg"></span><a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<input type="hidden" id="independent_flg" value="<%=independent_flg%>"></input>
		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">客户</label>
							<div class="ip">
								<input type="text" id="txt-client-employee-name" class="ip-" data-bind="value: order().client_employee_name,event:{click:choseClientEmployee}" placeholder="客户" required="required" />
							</div>
							<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: order().client_employee_pk" style="display: none" name="bnsOrder.client_employee_pk" id="client-employee-pk"
								required="required" />
						</div>
						<div class="col-md-6">
							<label class="l">产品名称</label>
							<div class="ip">
								<input type="text" class="ip-" placeholder="产品名称" maxlength="20" data-bind="value: order().product_name" name="bnsOrder.product_name" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat ">
						<div class="col-md-6">
							<label class="l">出团日期</label>
							<div class="ip">
								<input type="text" id="departure" class="ip- date-picker" data-bind="value: order().departure_date" placeholder="出团日期" name="bnsOrder.departure_date" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">天数</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: order().days" placeholder="天数" name="bnsOrder.days" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat ">
						<div class="col-md-6">
							<label class="l">总团款</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: order().receivable" placeholder="总团款" name="bnsOrder.receivable" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">成人</label>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="people-count" data-bind="value:order().adult_count" placeholder="人数" name="bnsOrder.adult_count" />
							</div>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="people-count" data-bind="value:order().other_count" placeholder="费用" name="bnsOrder.adult_cost" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">特殊</label>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="people-count" data-bind="value:order().special_count" placeholder="人数" name="bnsOrder.special_count" />
							</div>
							<div class="ip" style="width: 30%">
								<input type="number" class="ip-" id="people-count" data-bind="value:order().special_cost" placeholder="费用" name="bnsOrder.special_cost" />
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
								<input type="number" data-bind="value:order().other_cost" name="bnsOrder.other_cost" class="ip-" placeholder="其他费用" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">费用说明</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="50" data-bind="value: order().other_cost_comment" name="bnsOrder.other_cost_comment" placeholder="费用说明" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" maxlength="200" data-bind="value: order().comment" name="bnsOrder.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
					<hr />
					<h3>名单</h3>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">名单录入</label>
							<div class="ip">
								<textarea type="text" class="ip-default" id="txt-name-list" rows="10" data-bind="value: order().name_list" name="bnsOrder.name_list"
									placeholder="姓名+身份证号。"></textarea>
							</div>
							<a type="submit" class="btn btn-green btn-r" onclick="formatNameList()">写入</a>
						</div>
					</div>
					<div id="air-ticket-check">
						<div class="input-row clearfloat">
							<div class="col-md-12">
								<table style="width: 100%" id="name-table" class="table table-striped table-hover">
									<thead>
										<tr>
											<th style="width: 4%">团长</th>
											<th style="width: 4%">序号</th>
											<th style="width: 10%">姓名</th>
											<th style="width: 7%">性别</th>
											<th style="width: 5%" title="只按年份计算">年龄</th>
											<th style="width: 10%">手机号A</th>
											<th style="width: 10%">手机号B</th>
											<th style="width: 15%">证件号码</th>
											<th style="width: 10%">分房组</th>
											<th style="width: 10%"></th>
											<th style="width: 10%"></th>
											<th style="width: 5%"></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input type="radio" checked="checked" name="team_chairman" /></td>
											<td st="name-index">1</td>
											<td><input type="text" style="width: 90%" st="name" /></td>
											<td><select class="form-control" style="height: 34px" st="sex">
												<option value="">选择</option>
												<option value="M">男</option>
												<option value="F">女</option>
											</select></td>
											<td><input type="text" style="width: 90%" st="age" /></td>
											<td><input type="text" style="width: 90%" st="cellphone_A" /></td>
											<td><input type="text" style="width: 90%" st="cellphone_B" /></td>
											<td><input type="text" style="width: 90%" st="id" /></td>
											<td><input type="text" style="width: 90%" value="分房组" /></td>
											<td><a href="javascript:;" class="a-upload">上传身份证<input type="file" name="file" /></a> <input
												type="hidden"/></td>
											<td><a href="javascript:;" class="a-upload">上传护照<input type="file" name="file" /></a> <input
												type="hidden"/></td>
											<td><input type="button" style="width: 50px" onclick="removeName(this)" title="删除名单" value="-" /></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div align="right">
							<a type="submit" class="btn btn-green btn-r" data-bind="click: addName">添加名单</a>
						</div>
					</div>
					<hr />
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传确认件<input type="file"  name="file" /></a> <input type="hidden" name="bnsOrder.confirm_file" />
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
		$(".product-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/confirm-upload.js"></script>
	<script src="<%=basePath%>static/js/order/non-standard-order-creation.js"></script>
</body>
</html>