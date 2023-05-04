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
	vertical-align:middle !important
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
				标准订单确认<span data-bind="text:independent_msg"></span><a href="javascript:void(0)"
					onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<input type="hidden" id="key" value="<%=key%>" name="bsOrder.pk"></input> <input type="hidden"
						data-bind="value:order().independent_flg" name="bsOrder.independent_flg"></input>
						<input type="hidden" data-bind="value:order().team_number" id="team-number" name="bsOrder.team_number" />
					<div class="input-row clearfloat">
						<div class="col-md-3 required">
							<label class="l">客户</label>
							<div class="ip fix-width">
								<input type="text" id="txt-client-employee-name" class="ip-" disabled="disabled"
									data-bind="value: employee().name" placeholder="客户" required="required" />
							</div>
							<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: order().client_employee_pk"
								style="display: none" name="bsOrder.client_employee_pk" id="client-employee-pk" required="required" />
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
						<div class="col-md-6 required">
							<label class="l">出团日期</label>
							<div class="ip">
								<input type="text" id="departure" class="ip- date-picker" data-bind="value: order().departure_date"
									placeholder="出团日期" name="bsOrder.departure_date" required="required" />
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
								<p class="ip-default" id="txt-auto-adult-count" data-bind="text:order().adult_count"></p>
								<input type="hidden" class="ip- auto-1" id="auto-adult-count" data-bind="value: order().adult_count"
									placeholder="成人数" name="bsOrder.adult_count" />
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
					<div class="input-row clearfloat required">
						<div class="col-md-6">
							<label class="l">确认日期</label>
							<div class="ip">
								<input type="text" required="required" data-bind="value:confirm_date" name="bsOrder.confirm_date"
									class="ip- date-picker-confirm-date" placeholder="确认日期" />
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
					<div style="display: none; width: 600px" id="bat-passenger">
						<div class="input-row clearfloat">
							<div class="col-md-12">
								<textarea type="text" class="ip-default" id="txt-name-list" rows="10" placeholder="姓名+身份证号。"></textarea>
							</div>
							<div class="col-md-12" style="text-align: right; margin-top: 10px">
								<a type="submit" class="btn btn-green btn-r" onclick="cancelBat()">取消</a> <a type="submit"
									class="btn btn-green btn-r" onclick="formatNameList()">写入</a>
							</div>
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
											<th style="width: 16%">手机号A</th>
											<th style="width: 16%">手机号B</th>
											<th style="width: 22%">证件号码</th>
											<th style="width: 7%">价格&nbsp;<input type="checkbox" id="chk-bind" onclick="bindFix()" title="选中修改所有价格" /></th>
										<!-- 	<th style="width: 10%">分房组</th>
											<th style="width: 9%"></th>
											<th style="width: 9%"></th> -->
											<th style="width: 7%"></th>
										</tr>
									</thead>
									<tbody data-bind="foreach: passengers">
										<tr>

											<td><input type="radio" data-bind="value:$data.chairman,checked:'Y'" name="team_chairman" /></td>
											<td st="name-index" data-bind="text:$data.name_index"></td>
											<td><input type="text" class="ip-" data-bind="value:$data.name" style="width: 90%" st="name" /></td>
											<td><select class="form-control" data-bind="value:$data.sex" style="height: 34px" st="sex">
													<option value="">选择</option>
													<option value="M">男</option>
													<option value="F">女</option>
											</select></td>
											<td><input type="text" class="ip-" data-bind="value:$data.age" style="width: 90%" st="age" /></td>
											<td><input type="text" class="ip-" data-bind="value:$data.cellphone_A" style="width: 90%" st="cellphone_A" /></td>
											<td><input type="text" class="ip-" data-bind="value:$data.cellphone_B" style="width: 90%" st="cellphone_B" /></td>
											<td><input type="text" class="ip-" data-bind="value:$data.id" maxlength="18" oninput="autoPrice();autoCaculate()" style="width: 90%" st="id" /></td>
											<td><input type="text" class="ip-" style="width: 90%" st="price" oninput="autoPrice()"
												data-bind="value:$data.price" /></td>
											<td><input type="button" style="width: 60%" onclick="removeName(this)" title="删除名单" value="—" /></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div align="right">
							<a type="submit" class="btn btn-green btn-r" data-bind="click: batName">批量导入</a> <a type="submit"
								class="btn btn-green btn-r" data-bind="click: addName">添加名单</a>
						</div>
					</div>
					<hr />
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传确认件<input type="file" accept=".jpg,.png" name="file" /></a> <input type="hidden"
								id="txt-confirm-file" data-bind="value:order().confirm_file" name="bsOrder.confirm_file" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
				</form>

				<div align="right"> 
					<a type="submit" class="btn btn-green btn-r" data-bind="click: confirmOrder">确认订单</a>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/confirm-upload.js"></script>
	<script src="<%=basePath%>static/js/order/standard-order-confirm.js?v=1.1"></script>
	<script src="<%=basePath%>static/js/order/standard-order-common.js?v=1.1"></script>
</body>
</html>