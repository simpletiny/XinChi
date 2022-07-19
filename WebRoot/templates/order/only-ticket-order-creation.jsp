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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/js/order/upload.css" />
<style>
#table-ticket th, #table-ticket td {
	text-align: center;
}

#table-ticket tr td input {
	width: 90%;
}

#table-ticket {
	border-collapse: separate;
	border-spacing: 0px 10px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				单机票<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">客户</label>
							<div class="ip">
								<input type="text" id="txt-client-employee-name" class="ip-"
									data-bind="value: order().client_employee_name,event:{click:choseClientEmployee}" placeholder="客户"
									required="required" />
							</div>
							<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: order().client_employee_pk"
								style="display: none" name="bnsOrder.client_employee_pk" id="client-employee-pk" required="required" />
						</div>
						<div class="col-md-6">
							<label class="l">总团款</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: order().receivable" placeholder="总团款"
									name="bnsOrder.receivable" />
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
								<input type="text" class="ip-" data-bind="value: order().days" placeholder="天数" name="bnsOrder.days" />
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
								<input type="number" class="ip-" data-bind="value:order().other_count" placeholder="费用"
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
					<hr />
					<h3>航班信息</h3>
					<div class="input-row clearfloat" id="air-ticket" style="width: 900px">
						<div class="col-md-12">
							<table style="width: 100%" id="table-ticket">
								<thead>
									<tr class="required">
										<th class="r" style="width: 6%">航段</th>
										<th class="r" style="width: 10%">日期</th>
										<th class="r" style="width: 20%">起飞城市</th>
										<th class="r" style="width: 20%">降落城市</th>
										<th style="width: 4%"></th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<input type="hidden" st="flight-index" value="1" />
										<td st="index">1</td>
										<td><input st="date" class="ip- date-picker" type="text" /></td>
										<td><input st="from-city" class="ip- " type="text" /></td>
										<td><input st="to-city" class="ip- " type="text" /></td>
										<td><input type="button" value="-" onclick="deleteRow(this)"></input></td>
									</tr>
								</tbody>
							</table>
							<div style="margin-top: 20px; float: right">
								<input type="button" value="+" style="width: 50px;" onclick="addRow()"></input>
							</div>
						</div>

					</div>
					<div class="input-row clearfloat" style="width: 900px">
						<div class="col-md-12">
							<label class="l">票务要求</label>
							<div class="ip">
								<textarea type="text" id="air-comment" class="ip-default air_comment" rows="5" maxlength="200"
									placeholder="票务需求"></textarea>
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
											<td><input type="text" data-bind="value:$data.id" onblur="autoCaculate();" style="width: 90%" st="id" /></td>
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
							<a href="javascript:;" class="a-upload">上传确认件<input type="file" accept=".jpg,.png" name="file" /></a> <input type="hidden"
								name="bnsOrder.confirm_file" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>

					<hr />
					<div class="input-row clearfloat">
						<div class="col-md-8">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="10" maxlength="200" data-bind="value: order().comment"
									name="bnsOrder.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
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
		$(".order-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/confirm-upload.js"></script>
	<script src="<%=basePath%>static/js/order/only-ticket-order-creation.js"></script>
</body>
</html>