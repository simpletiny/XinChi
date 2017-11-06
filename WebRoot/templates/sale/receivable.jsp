<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
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

.fixed {
	font-size: 12px;
	display: block;
	position: fixed;
	right: 20px;
	top: 200px;
	margin-left: 10px;
	z-index: 100;
	width: 100px;
}

.fixed button {
	width: 80px;
	margin-top: 5px;
	display: block;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>应收款</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">客户</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="客户" name="receivable.client_employee_name" />
							</div>
						</div>

						<div class="span6">
							<label class="col-md-1 control-label">出团月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" placeholder="出团月份" name="receivable.departure_month" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">状态</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px" data-bind="options: teamStatus, optionsCaption: '全部'" name="receivable.team_status"></select>
							</div>
						</div>
						<div class="span6">
							<div data-bind="foreach: types">
								<em class="small-box"> <input type="checkbox" required="required" data-bind="attr: {'value': $data}, checked: $root.chosenTypes,event:{click:$root.changeType}" /><label
									data-bind="text: $data"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">财务主体</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="财务主体" name="receivable.financial_body_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="团号" name="receivable.team_number" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales" data-bind="options: sales, optionsCaption: '全部',optionsText:'user_name',optionsValue:'user_number',value:$root.chosenSales,event:{change:function(){fetchSummary();search()}}"
										name="receivable.sales"></select>
								</div>
							</div>
						</s:if>
						<div class="span6">
							<label class="col-md-1 control-label">按出团日期</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px" id="select-sales" data-bind="options: sortTypes" name="receivable.sort_type"></select>
							</div>
						</div>

					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">尾款为0</label>
							<div class="col-md-2">
								<input type="checkbox" value="Y" name="receivable.zero_flg" data-bind="event:{click:zeroBalance}" />
							</div>
						</div>
						<div style="padding-top: 3px; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: search">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>全部</th>
								<th>未回团</th>
								<th>不足1月</th>
								<th>1个月~2个月</th>
								<th>2个月~6个月</th>
								<th>坏账</th>
							</tr>
						</thead>
						<tbody id="tbody-data">
							<tr>
								<td>单数</td>
								<td data-bind="text:recsum().all_count"></td>
								<td data-bind="text:recsum().before_count"></td>
								<td data-bind="text:recsum().one_month_count"></td>
								<td data-bind="text:recsum().two_month_count"></td>
								<td data-bind="text:recsum().six_month_count"></td>
								<td data-bind="text:recsum().bad_month_count"></td>
							</tr>
							<tr>
								<td>尾款</td>
								<td data-bind="text:recsum().all_balance"></td>
								<td st="all" data-bind="text:recsum().before_balance" class="rmb"></td>
								<td st="all" data-bind="text:recsum().one_month_balance" class="rmb"></td>
								<td st="all" data-bind="text:recsum().two_month_balance" class="rmb"></td>
								<td st="all" data-bind="text:recsum().six_month_balance" class="rmb"></td>
								<td st="all" data-bind="text:recsum().bad_month_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:recsum().before_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:recsum().one_month_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:recsum().two_month_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:recsum().six_month_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:recsum().bad_month_budget_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:recsum().before_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:recsum().one_month_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:recsum().two_month_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:recsum().six_month_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:recsum().bad_month_final_balance" class="rmb"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>团号</th>
								<th>回团天数</th>
								<th>决否</th>
								<th>客户</th>
								<th>主体</th>
								<th>出团日期</th>
								<th>产品</th>
								<th>人数</th>
								<th>总团款</th>
								<th>已收款</th>
								<th>尾款</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>销售</th>
								</s:if>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: receivables">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue: $data, checked: $root.chosenOrders" /></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.back_days"></td>
								<td data-bind="text: $data.final_flg"></td>
								<td data-bind="text: $data.client_employee_name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.product"></td>
								<td data-bind="text: $data.people_count"></td>

								<td st="budget" style="display: none" data-bind="text:$data.budget_receivable" class="rmb"></td>

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="final" style="display: none" data-bind="text:$data.final_receivable" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="final" style="display: none">未决算</td>
								<!-- /ko -->

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="all" data-bind="text:$data.final_receivable" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="all" data-bind="text:$data.budget_receivable" class="rmb"></td>
								<!-- /ko -->

								<td data-bind="text: $data.received" class="rmb"></td>

								<td st="budget" style="display: none" data-bind="text:$data.budget_balance" class="rmb"></td>

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="final" style="display: none" data-bind="text:$data.final_balance" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="final" style="display: none">未决算</td>
								<!-- /ko -->

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="all" data-bind="text:$data.final_balance" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="all" data-bind="text:$data.budget_balance" class="rmb"></td>
								<!-- /ko -->
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.sales_name"></td>
								</s:if>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>合计</td>
							<td data-bind="text:totalPeople"></td>
							<td st="all" data-bind="text:totalReceivable" class="rmb"></td>
							<td st="budget" style="display: none" data-bind="text:totalBudgetReceivable" class="rmb"></td>
							<td st="final" style="display: none" data-bind="text:totalFinalReceivable" class="rmb"></td>
							<td data-bind="text:totalReceived" class="rmb"></td>
							<td st="all" data-bind="text:totalBalance" class="rmb"></td>
							<td st="budget" style="display: none" data-bind="text:totalBudgetBalance" class="rmb"></td>
							<td st="final" style="display: none" data-bind="text:totalFinalBalance" class="rmb"></td>
							<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
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

				<div class="fixed">
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { ridTail() }">抹零申请</button>
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { pay() }">支出申请</button>
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { strike() }">冲账申请</button>
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { collect()}">代收</button>
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { receive()}">收入</button>
				</div>
			</div>

		</div>
	</div>
	<!-- 抹零申请 -->
	<div id="tail_submit" style="display: none; width: 1100px; padding-top: 30px;">
		<form id="form-tail">
			<div class="input-row clearfloat">
				<div class="col-md-4">
					<label class="l" style="width: 30%">抹零金额</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default rmb" data-bind="text:tailMoney()"></p>
						<input name="detail.received" type="hidden" data-bind="value:tailMoney()" />
					</div>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 30%">团号</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:team_number()"></p>
						<input name="detail.team_number" type="hidden" data-bind="value:team_number()" />
					</div>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 30%">客户</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:client_employee_name()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" style="width: 10%">说明</label>
					<div class="ip" style="width: 90%">
						<input type="text" maxlength="200" class="ip-" placeholder="说明" name="detail.comment" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyRidTail">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- 代收申请 -->
	<div id="collect-submit" style="display: none; width: 1100px; padding-top: 30px;">
		<form id="form-collect">
			<div class="input-row clearfloat">
				<div class="col-md-4">
					<label class="l" style="width: 30%">尾款</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default rmb" data-bind="text:tailMoney()"></p>
					</div>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 30%">团号</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:team_number()"></p>
						<input name="detail.team_number" type="hidden" data-bind="value:team_number()" />
					</div>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 30%">客户</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:client_employee_name()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">代收金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.received" id="collect-money" class="form-control" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" style="width: 10%">说明</label>
					<div class="ip" style="width: 90%">
						<input type="text" maxlength="200" class="ip-" placeholder="说明" name="detail.comment" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyCollect">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>

	<!-- 支出申请 -->
	<div id="pay-sumbit" style="display: none; width: 900px; padding-top: 30px;">
		<form id="form-pay">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">团号</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:team_number()"></p>
						<input name="detail.team_number" type="hidden" data-bind="value:team_number()" />
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">客户</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:client_employee_name()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-4">
					<label class="l" style="width: 30%">多付金额</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default rmb" data-bind="text:more_money()"></p>
					</div>
				</div>
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">返还金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.received" class="form-control" required="required" />
					</div>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 30%">支付时限</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="detail.limit_time" class="form-control datetime-picker" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" style="width: 10%">备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="15" name="detail.comment" maxlength="200" placeholder="备注" required="required"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyPay">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>

	<!-- 冲账申请 -->
	<div id="strike-submit" style="display: none; width: 900px; padding-top: 30px;">
		<form id="form-strike">
			<div class="input-row clearfloat">
				<div class="col-md-4">
					<label class="l" style="width: 30%">客户</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:client_employee_name()"></p>
					</div>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 30%">团号</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:team_number()"></p>
						<input name="detail.team_number" type="hidden" data-bind="value:team_number()" />
					</div>
				</div>
				<div class="col-md-4">
					<label class="l" style="width: 30%">冲账总金额</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:max_strike_money()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-3">
					<label class="l" style="width: 100%">团号</label>
				</div>
				<div class="col-md-3">
					<label class="l" style="width: 100%">客户</label>
				</div>
				<div class="col-md-3">
					<label class="l" style="width: 100%">尾款</label>
				</div>
				<div class="col-md-3 required">
					<label class="l" style="width: 100%">分配金额</label>
				</div>
			</div>
			<!-- ko foreach:positiveReceivables -->
			<div class="input-row clearfloat" st="strike-allot">
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.team_number"></p>
						<input type="hidden" data-bind="value:$data.team_number" st="strike-team_number" />
					</div>
				</div>
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.client_employee_name"></p>
					</div>
				</div>
				<div class="col-md-3">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<p class="ip-default rmb" data-bind="text:$data.final_balance"></p>
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
						<!-- /ko -->
					</div>
				</div>
				<div class="col-md-3">
					<div class="ip">
						<input type="number" class="form-control" st="strike-received" required="required" />
					</div>
				</div>
			</div>
			<!-- /ko -->
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" style="width: 10%">说明</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="15" name="detail.comment" placeholder="需要说明的信息" required="required"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyStrike">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>

	<!-- 收入申请 -->
	<div id="receive_submit" style="display: none; width: 800px; padding-top: 30px;">
		<form id="form-receive">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">团号</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:team_number()"></p>
						<input name="detail.team_number" type="hidden" data-bind="value:team_number()" />
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">客户</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:client_employee_name()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">账户</label>
					<div class="ip" style="width: 70%">
						<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --'" name="detail.card_account" required="required"></select>
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.received" class="form-control" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">入账时间</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="detail.received_time" class="form-control datetime-picker" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyReceive">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div id="receive_sum_submit" style="display: none; width: 800px; height: 700px; overflow: auto; padding-top: 30px;">
		<form id="form-receive-sum">
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">账户</label>
					<div class="ip" style="width: 70%">
						<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --'" name="detail.card_account" required="required"></select>
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">入账总金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.sum_received" class="ip- amountRangeStart1" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">入账时间</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="detail.received_time" class="form-control datetime-picker" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-3">
					<label class="l" style="width: 100%">团号</label>
				</div>
				<div class="col-md-3">
					<label class="l" style="width: 100%">客户</label>
				</div>
				<div class="col-md-3">
					<label class="l" style="width: 100%">尾款</label>
				</div>
				<div class="col-md-3 required">
					<label class="l" style="width: 100%">分配金额</label>
				</div>
			</div>
			<!-- ko foreach:chosenOrders -->
			<div class="input-row clearfloat" st="receive_allot">
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.team_number"></p>
						<input type="hidden" data-bind="value:$data.team_number" st="team_number" />
					</div>
				</div>
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.client_employee_name"></p>
					</div>
				</div>
				<div class="col-md-3">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<p class="ip-default rmb" data-bind="text:$data.final_balance"></p>
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
						<!-- /ko -->
					</div>
				</div>
				<div class="col-md-3">
					<div class="ip">
						<input type="number" class="form-control" st="receive_received" data-bind="attr:{'name':'name-'+$data.pk}" required="required" />
					</div>
				</div>
			</div>
			<!-- /ko -->
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyReceive">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<script>
		$(".sale").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/sale/receivable.js"></script>
</body>
</html>