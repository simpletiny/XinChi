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
			<h2>往来详表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: rollBack">打回重报</button>
								<!-- <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { reimbursement() }">费用报销</button> -->
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="ip">
							<div data-bind="foreach: allStatus" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},click:function(){$root.refresh();return true;}" name="detail.statuses" /><label
									data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>

					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">类型</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: paidTypes,optionsText:'name', optionsValue: 'key', optionsCaption: '全部',event:{change:refresh}"
									name="detail.type"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">订单号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" name="detail.order_number" />
							</div>
						</div>
						<div class="ip">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="detail.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="detail.money_to" />
							</div>
						</div>
						<div class="ip">
							<label class="col-md-1 control-label">精确金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="精确金额" name="detail.money" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">申请日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateFrom" placeholder="from"
									name="detail.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateTo" placeholder="to"
									name="detail.date_to" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">供应商</label>
							<div class="col-md-1" style="float: left">
								<input type="text" class="form-control" name="detail.supplier_name" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')">
							<div align="left">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-1" style="float: left">
									<input type="text" class="form-control" name="detail.create_user" />
								</div>
							</div>
						</s:if>
						<div style="padding-top: 3px;">
							<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>金额</th>
								<th>类型</th>
								<th>订单号</th>
								<th>供应商</th>
								<th>申请日期</th>
								<th>入账日期</th>
								<th>状态</th>
								<th>支付详情</th>
								<th>填报人</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: paids">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenPaids" /></td>
								<!-- ko if:$data.type=='STRIKEIN' || $data.type=='STRIKEOUT' -->
								<td data-bind="text: $data.money" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if:$data.type!='STRIKEIN' && $data.type!='STRIKEOUT' -->
								<td data-bind="text: $data.allot_money" class="rmb"></td>
								<!-- /ko -->
								<td style="color: green" data-bind="text: $root.typeMapping[$data.type]"></td>
								<!-- ko if:$data.isOne=='SUM' && $data.isOne!='SINGLE'-->
								<td><a href="javascript:void(0)" data-bind="click: function() {$root.viewDetail($data)} ">合</a></td>
								<!-- /ko -->
								<!-- ko if:$data.isOne=='SINGLE' && $data.isOne!='SUM'-->
									<!-- ko if:$data.type=='STRIKEIN' || $data.type=='STRIKEOUT' -->
										<td><a href="javascript:void(0)" data-bind="text:$data.order_number,click: function() {$root.viewStrikeDetail($data)} ">合</a></td>
									<!-- /ko -->
									<!-- ko if:$data.type!='STRIKEIN' && $data.type!='STRIKEOUT' -->
										<td data-bind="text:$data.order_number"></td>
									<!-- /ko -->
								<!-- /ko -->
								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD')"></td>
								<!-- ko if:$data.type!='BACK' -->
								<td data-bind="text: $data.time"></td>
								<!-- /ko -->
								<!-- ko if:$data.type=='BACK' -->
								<td data-bind="text: $data.confirm_time"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='I' -->
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='Y' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='P' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='N' -->
								<td><a href="javascript:void(0)" style="color: red" data-bind="text: $root.statusMapping[$data.status],click: function() {$root.viewRejectReason($data.related_pk)} "></a></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='P' -->
								<td><a href="javascript:void(0)" data-bind="click: function() {$root.viewPaidInfo($data.related_pk)} ">查看</a></td>
								<!-- /ko -->
								<!-- ko if:$data.status!='P' -->
								<td>-</td>
								<!-- /ko -->
								<td data-bind="text: $data.create_user"></td>
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
	<div id="sum_detail" style="display: none; width: 800px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">总金额</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().allot_money" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">订单号</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">供应商</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">类型</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">分配金额</label>
			</div>
		</div>
		<!-- ko foreach:sumDetails -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.team_number"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$root.typeMapping[$data.type]"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money" class="rmb"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<div id="strike_detail" style="display: none; width: 800px; padding-top: 30px;">
		<h3>冲账出</h3>
		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">订单号</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">供应商</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">冲出金额</label>
			</div>
		</div>
		<!-- ko foreach:strikeouts -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.team_number"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money" class="rmb"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
		
		<hr/>
		<h3>冲账入</h3>
		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">订单号</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">供应商</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">冲入金额</label>
			</div>
		</div>
		<!-- ko foreach:strikeins -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.team_number"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money" class="rmb"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<div id="comment" style="display: none; width: 800px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">团号</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().team_number"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">客户</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().client_employee_name" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">产品</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().product"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">人数</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().people_count" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">出团日期</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().departure_date"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">备注</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:comment()"></p>
				</div>
			</div>
		</div>
	</div>
	<div id="div_view_detail" style="display: none; width: 800px; height: 600px; padding-top: 30px; overflow: auto">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">审批人</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:detail().approve_user_name"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">审批时间</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:moment(detail().confirm_time-0).format('YYYY-MM-DD HH:mm')"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">付款人</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:detail().paid_user_name"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">付款时间</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:detail().time"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">付款凭证</label>
				<div data-bind="foreach: imgs" id="voucher-img">
					<input type="hidden" data-bind="value:$data" st="voucher-file-name" /> <img style="width: 400px; height: 400px"
						src="<%=basePath%>static/img/sorry.jpg" st="img" />
				</div>

			</div>
		</div>
	</div>
	<script>
		$(".product").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/sale/paid.js"></script>
</body>
</html>