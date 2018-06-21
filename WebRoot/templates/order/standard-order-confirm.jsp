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
	text-align: center
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
						data-bind="value:order().independent_flg" name="bsOrder.independent_flg"></input> <input type="hidden" value='Y'
						name="bsOrder.confirm_flg"></input>
					<div class="input-row clearfloat">
						<div class="col-md-3 required">
							<label class="l">客户</label>
							<div class="ip fix-width">
								<input type="text" id="txt-client-employee-name" class="ip-" disabled="disabled"
									data-bind="value: employee().name,event:{click:choseClientEmployee}" placeholder="客户" required="required" />
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
								<input type="hidden" data-bind="value: product().pk" name="bsOrder.product_pk" required="required" /> <input
									type="hidden" data-bind="value: product().product_value" name="bsOrder.product_value" />
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
								<input type="number" class="ip- auto-1" id="other-cost" onkeyup="autoCaculate()"
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
									class="ip- date-picker" placeholder="确认日期" />
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
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<table style="width: 100%" class="table table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 7.5%"></th>
										<th style="width: 7.5%">航班号</th>
										<th style="width: 12.5%">起飞城市</th>
										<th style="width: 12.5%">机场</th>
										<th style="width: 12.5%">抵达城市</th>
										<th style="width: 12.5%">机场</th>
										<th style="width: 7.5%">起飞时间</th>
										<th style="width: 7.5%">抵达时间</th>
										<!-- ko if:order().next_day!=0 -->
										<th style="width: 10%">隔天达&nbsp;<input type="checkbox" checked="checked" id="chk-next-day"
											onclick="nextDay()" /></th>
										<!-- /ko -->
										<!-- ko if:order().next_day==0 -->
										<th style="width: 10%">隔天达&nbsp;<input type="checkbox" id="chk-next-day" onclick="nextDay()" /></th>
										<!-- /ko -->
										<th style="width: 10%">飞行时间</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>首航段：</td>
										<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().ticket_number"
											name="bsOrder.ticket_number" /></td>
										<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().start_city"
											name="bsOrder.start_city" /></td>
										<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().start_airport"
											name="bsOrder.start_airport" /></td>
										<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().end_city"
											name="bsOrder.end_city" /></td>
										<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().end_airport"
											name="bsOrder.end_airport" /></td>
										<td><input type="text" style="width: 90%" class="isTime" id="txt-off-time"
											data-bind="value:order().off_time" name="bsOrder.off_time" maxlength="5" onkeyup="caculate_fly_time()" /></td>
										<td><input type="text" style="width: 90%" class="isTime" data-bind="value:order().land_time"
											id="txt-land-time" name="bsOrder.land_time" maxlength="5" onkeyup="caculate_fly_time()" /></td>
										<!-- ko if:order().next_day==0 -->
										<td><input type="number" style="width: 90%" name="bsOrder.next_day" maxlength="2" min="1"
											id="txt-next-day" value="1" onkeyup="caculate_fly_time()" disabled="disabled" /></td>
										<!-- /ko -->
										<!-- ko if:order().next_day!=0 -->
										<td><input type="number" style="width: 90%" data-bind="value:order().next_day" name="bsOrder.next_day"
											maxlength="2" min="1" id="txt-next-day" onkeyup="caculate_fly_time()" /></td>
										<!-- /ko -->
										<td id="txt-fly-time"></td>
									</tr>
								</tbody>
							</table>
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
											<th style="width: 7%">姓名</th>
											<th style="width: 7%">性别</th>
											<th style="width: 5%" title="只按年份计算">年龄</th>
											<th style="width: 10%">手机号A</th>
											<th style="width: 10%">手机号B</th>
											<th style="width: 15%">证件号码</th>
											<th style="width: 7%">价格&nbsp;<input type="checkbox" id="chk-bind" onclick="bindFix()" title="选中修改所有价格" /></th>
											<th style="width: 10%">分房组</th>
											<th style="width: 9%"></th>
											<th style="width: 9%"></th>
											<th style="width: 3%"></th>
										</tr>
									</thead>
									<tbody data-bind="foreach: passengers">
										<tr>

											<td><input type="radio" data-bind="value:$data.chairman,checked:'Y'" name="team_chairman" /></td>
											<td st="name-index" data-bind="text:$data.name_index"></td>
											<td><input type="text" data-bind="value:$data.name" style="width: 90%" st="name" /></td>
											<td><select class="form-control" data-bind="value:$data.sex" style="height: 34px" st="sex">
													<option value="">选择</option>
													<option value="M">男</option>
													<option value="F">女</option>
											</select></td>
											<td><input type="text" data-bind="value:$data.age" style="width: 90%" st="age" /></td>
											<td><input type="text" data-bind="value:$data.cellphone_A" style="width: 90%" st="cellphone_A" /></td>
											<td><input type="text" data-bind="value:$data.cellphone_B" style="width: 90%" st="cellphone_B" /></td>
											<td><input type="text" data-bind="value:$data.id" onblur="autoPrice();autoCaculate()" style="width: 90%" st="id" /></td>
											<td><input type="text" style="width: 90%" st="price" onblur="autoCaculate()"
												data-bind="value:$data.price" /></td>
											<td><input type="text" style="width: 90%" value="分房组" /></td>
											<td><a href="javascript:;" class="a-upload">上传身份证<input type="file" name="file" /></a> <input
												type="hidden" /></td>
											<td><a href="javascript:;" class="a-upload">上传护照<input type="file" name="file" /></a> <input
												type="hidden" /></td>
											<td><input type="button" style="width: 50px" onclick="removeName(this)" title="删除名单" value="-" /></td>
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
							<a href="javascript:;" class="a-upload">上传确认件<input type="file" name="file" /></a> <input type="hidden"
								id="txt-confirm-file" data-bind="value:order().confirm_file" name="bsOrder.confirm_file" />
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
		$(".order-box").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/confirm-upload.js"></script>
	<script src="<%=basePath%>static/js/order/standard-order-confirm.js"></script>
</body>
</html>