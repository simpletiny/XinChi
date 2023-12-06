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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>费用详情</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="reimbursement.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="reimbursement.money_to" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">归属月份</label>
							<div class="col-md-1" style="float: left">
								<input type="text" class="form-control month-picker-st" st="st-date-1" placeholder="归属月份"
									name="reimbursement.month" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">日期</label>
							<div class="col-md-1" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="from"
									name="reimbursement.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-1" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="to" name="reimbursement.date_to" />
							</div>
						</div>
						<div align="right">
							<button type="submit" class="btn btn-green " data-bind="click: function() { reimbursement() }">费用申请</button>
							<button type="submit" class="btn btn-green " data-bind="click: function() { update() }">重新申请</button>
							<button type="submit" class="btn btn-green" data-bind="click: delete_reimbursement">删除</button>
						</div>

					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">精确金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="精确金额" name="reimbursement.money" />
							</div>
						</div>
						<div>
							<label class="col-md-1 control-label">项目</label>
							<div class="col-md-2">
								<select class="form-control"
									data-bind="options: items, optionsText:function(item){return payTypeMapping[item]}, optionsCaption: '-- 请选择 --',event: {change:refresh}"
									name="reimbursement.item" required="required"></select>
							</div>
						</div>

						<s:if test="#session.user.user_roles.contains('ADMIN')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--',event: {change:refresh}"
										name="reimbursement.apply_user"></select>
								</div>
							</div>
						</s:if>

						<div style="padding-top: 3px; float: right">
							<button type="submit" class="btn btn-green" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>日期</th>
								<th>项目</th>
								<th>归属月份</th>
								<th>金额</th>
								<th>备注</th>
								<th>状态</th>
								<th>审批人</th>
								<th>审批时间</th>
								<th>支付人</th>
								<th>支付时间</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')">
									<th>申请人</th>
								</s:if>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: reimbursements">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenReimbursements" /></td>
								<td data-bind="text: $data.date"></td>
								<td data-bind="text: payTypeMapping[$data.item]"></td>
								<td data-bind="text: $data.month"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.comment"></td>
								<!-- ko if:$data.status =="P" -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status =="N" -->
								<td><a href="javascript:void(0)" style="color: red"
									data-bind="text: $root.statusMapping[$data.status],click: function() {$root.viewRejectReason($data.pk)} "></a></td>
								<!-- /ko -->
								<!-- ko if:$data.status =="Y" || $data.status=="I" -->
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->

								<td data-bind="text: $data.approval_name"></td>
								<!-- ko if:$data.approval_time !=null && $data.approval_time!="" -->
								<td data-bind="text: moment($data.approval_time-0).format('YYYY-MM-DD HH:mm')"></td>
								<!-- /ko -->
								<!-- ko if:$data.approval_time ==null || $data.approval_time=="" -->
								<td></td>
								<!-- /ko -->
								<td data-bind="text: $data.pay_name"></td>
								<td data-bind="text: $data.pay_time"></td>
								<s:if test="#session.user.user_roles.contains('ADMIN')">
									<td data-bind="text: $data.apply_name"></td>
								</s:if>
							</tr>
						</tbody>
						<tr title="只计算已入账">
							<td></td>

							<td></td>
							<td></td>
							<td>合计(已入账)</td>
							<td data-bind="text:totalMoney" class="rmb"></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<s:if test="#session.user.user_roles.contains('ADMIN')">
								<td></td>
							</s:if>
						</tr>
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
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/accounting/accounting-constant.js?v=1.001"></script>
	<script src="<%=basePath%>static/js/accounting/reimbursement.js?v=1.002"></script>
</body>
</html>