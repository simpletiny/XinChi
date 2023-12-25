<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
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
			<h2>支出审批</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="ip">
							<div data-bind="foreach: allStatus" style="padding-top: 4px;">
								<em class="small-box"> <input name="option.statuses" type="checkbox"
									data-bind="value:$data, checked: $root.chosenStatus,click:function(){$root.refresh();return true;}" /><label
									data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="option.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="option.money_to" />
							</div>
						</div>
						<div>
							<label class="col-md-1 control-label">收款方</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.receiver" />
							</div>
						</div>
						<div>
							<label class="col-md-1 control-label">项目</label>
							<div class="col-md-2">
								<select class="form-control"
									data-bind="options: items, optionsText:function(item){return payTypeMapping[item]}, optionsCaption: '-- 请选择 --',event: {change:refresh}"
									name="option.item" required="required"></select>
							</div>
						</div>
						<button type="submit" style="float: right" " class="btn btn-green" data-bind="click: suspense">挂账</button>
					</div>

					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">申请人</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.apply_user" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">总余额</label>
							<div class="col-md-1">
								<p class="ip-default rmb" data-bind="text:sumCardBalance()"></p>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">待支付</label>
							<div class="col-md-1">
								<p class="ip-default rmb" data-bind="text:sum_waiting_for_paid()"></p>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">待审批</label>
							<div class="col-md-1">
								<p class="ip-default rmb" data-bind="text:sumBalance()"></p>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">挂起</label>
							<div class="col-md-1">
								<p class="ip-default rmb" data-bind="text:sumSuspense()"></p>
							</div>
						</div>
						<button type="submit" style="float: right" class="btn btn-green" data-bind="click: refresh">搜索</button>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>金额</th>
								<th>项目</th>
								<th>收款方</th>
								<th>备注</th>
								<th>申请日期</th>
								<th>支付时限</th>
								<th>状态</th>
								<th>申请人</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: paids">
							<tr>
								<td><input type="checkbox"
									data-bind="checkedValue:$data, checked: $root.chosenPaids" /></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: payTypeMapping[$data.item]"></td>
								<!-- ko if:$data.item!='M' -->
								<td data-bind="text: $data.receiver"></td>
								<!-- /ko -->
								<!-- ko if:$data.item=='M' -->
								<td><a href="javascript:void(0)"
									data-bind="text:$data.receiver,event:{click:function(){$root.checkEmployee($data)}}"></a></td>
								<!-- /ko -->
								<td data-bind="text: $data.comment"></td>
								<td data-bind="text: moment($data.apply_time-0).format('YYYY-MM-DD')"></td>

								<!-- ko if: $data.limit_time!=null &&  moment().isAfter($data.limit_time) -->
								<td style="color: red" data-bind="text: $data.limit_time"></td>
								<!-- /ko -->
								<!-- ko if:$data.limit_time!=null && moment().isBefore($data.limit_time) -->
								<td data-bind="text: $data.limit_time"></td>
								<!-- /ko -->
								<!-- ko if:$data.limit_time==null || $data.limit_time=="" -->
								<td data-bind="text: $data.limit_time"></td>
								<!-- /ko -->

								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<td data-bind="text: $data.apply_user"></td>

								<!-- ko if:$data.status=='I' || $data.status=='S'  -->
								<td><a href="javascript:void(0)" data-bind="event:{click:function(){$root.agree($data)}}">同意</a> <a
									href="javascript:void(0)" data-bind="event:{click:function(){$root.reject($data)}}">驳回</a></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='Y' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='N' -->
								<td style="color: red" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='P' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
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

	<div id="comment" style="display: none; width: 550px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 40%">驳回理由：</label>
				<div class="ip" style="width: 60%">
					<textarea maxlength="50" id="txt-comment" placeholder="50字内,不填写则默认为:账目不符" cols="40" rows="4" class="ip-default"></textarea>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: doReject">驳回</button>
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: cancel">取消</button>
		</div>
	</div>
	<div id="employee" style="display: none; width: 550px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l">客户</label>
				<div class="ip">
					<p class="ip-default" data-bind="text: employee().name"></p>
				</div>
				<label class="l">财务主体</label>
				<div class="ip">
					<p class="ip-default" data-bind="text: employee().financial_body_name"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l">所属销售</label>
				<div class="ip">
					<p class="ip-default" data-bind="text: employee().sales_name"></p>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/accounting/accounting-constant.js?v=1.001"></script>
	<script src="<%=basePath%>static/js/accounting/pay-approve.js?v=1.002"></script>
</body>
</html>