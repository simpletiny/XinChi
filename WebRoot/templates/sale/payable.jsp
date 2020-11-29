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
			<h2>地接往来</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">供应商</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="供应商" name="payable.supplier_employee_name" />
							</div>
						</div>

						<div class="span6">
							<label class="col-md-1 control-label">接团月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" placeholder="接团月份" name="payable.departure_month" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">状态</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px" data-bind="options: teamStatus, optionsCaption: '全部'" name="payable.team_status"></select>
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
								<input type="text" class="form-control" placeholder="财务主体" name="payable.supplier_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="团号" name="payable.team_number" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" data-bind="options: sales, optionsCaption: '全部',optionsText:'user_name',optionsValue:'user_number',value:chosenSales,event:{change:function(){fetchSummary();search();}}"
										name="payable.create_user"></select>
								</div>
							</div>
						</s:if>

					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">按出团日期</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px" data-bind="options: sortTypes" name="payable.sort_type"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">尾款为0</label>
							<div class="col-md-2">
								<input type="checkbox" value="Y" name="payable.zero_flg" data-bind="event:{click:zeroBalance}" />
							</div>
						</div>
						<div style="padding-top: 3px; float: right">
							<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: search">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row" data-bind="foreach: titleMonth">
								<th data-bind="text:$data"></th>
							</tr>
						</thead>
						<tbody id="tbody-data">
							<tr>
								<td>单数</td>
								<td data-bind="text:paysum().all_count"></td>
								<td data-bind="text:paysum().current_month_count"></td>
								<td data-bind="text:paysum().one_month_count"></td>
								<td data-bind="text:paysum().two_month_count"></td>
								<td data-bind="text:paysum().three_month_count"></td>
								<td data-bind="text:paysum().earlier_month_count"></td>
							</tr>
							<tr>
								<td>尾款</td>
								<td data-bind="text:paysum().all_balance"></td>
								<td st="all" data-bind="text:paysum().current_month_balance" class="rmb"></td>
								<td st="all" data-bind="text:paysum().one_month_balance" class="rmb"></td>
								<td st="all" data-bind="text:paysum().two_month_balance" class="rmb"></td>
								<td st="all" data-bind="text:paysum().three_month_balance" class="rmb"></td>
								<td st="all" data-bind="text:paysum().earlier_month_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:paysum().current_month_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:paysum().one_month_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:paysum().two_month_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:paysum().three_month_budget_balance" class="rmb"></td>
								<td st="budget" style="display: none" data-bind="text:paysum().earlier_month_budget_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:paysum().current_month_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:paysum().one_month_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:paysum().two_month_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:paysum().three_month_final_balance" class="rmb"></td>
								<td st="final" style="display: none" data-bind="text:paysum().earlier_month_final_balance" class="rmb"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th><input type="checkbox" id="chk-all" onclick="checkAll(this)" />全选</th>
								<th>供应商</th>
								<th>主体</th>
								<th>回团天数</th>
								<th>决否</th>
								<th>接团日期</th>
								<th>产品</th>
								<th>人数</th>
								<th>团号</th>
								<th>总款项</th>
								<th>已付款</th>
								<th>应付款</th>

								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>产品经理</th>
								</s:if>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: payables">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenOrders" /></td>
								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.back_days"></td>
								<td data-bind="text: $data.final_flg"></td>
								<td data-bind="text: $data.pick_date"></td>
								<td data-bind="text: $data.product"></td>
								<td data-bind="text: $data.people_count"></td>
								<td data-bind="text: $data.team_number"></td>

								<td st="budget" style="display: none" data-bind="text:$data.budget_payable" class="rmb"></td>

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="final" style="display: none" data-bind="text:$data.final_payable" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="final" style="display: none">未决算</td>
								<!-- /ko -->

								<!-- ko if: $data.final_flg=="Y" -->
								<td st="all" data-bind="text:$data.final_payable" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if: $data.final_flg=="N" -->
								<td st="all" data-bind="text:$data.budget_payable" class="rmb"></td>
								<!-- /ko -->

								<td data-bind="text: $data.paid"></td>

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
									<td data-bind="text: $data.product_manager"></td>
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
							<td>合计</td>
							<td data-bind="text:totalPeople"></td>
							<td></td>
							<td st="all" data-bind="text:totalPayable" class="rmb"></td>
							<td st="budget" style="display: none" data-bind="text:totalBudgetPayable" class="rmb"></td>
							<td st="final" style="display: none" data-bind="text:totalFinalPayable" class="rmb"></td>
							<td data-bind="text:totalPaid" class="rmb"></td>
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
					<div style="margin-top: 5px">
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deduct() }">扣款申请</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { strike() }">冲账申请</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { receive() }">收入</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { pay()}">支付申请</button>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- 返款收入 -->
	<div id="receive" style="display: none; width: 1000px; height: 700px; overflow: auto; padding-top: 30px;">
		<form id="form-receive">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">供应商</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:supplier_name()"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">应返款总额</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default rmb" data-bind="text:totalBack()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">收入账户</label>
					<div class="ip" style="width: 70%">
						<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --'" name="detail.card_account" required="required"></select>
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.allot_money" class="ip-" st="sum_received" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">入账时间</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="detail.time" class="form-control datetime-picker" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-3">
					<label class="l" style="width: 100%">团号</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">操作</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">出团日期</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">应付款</label>
				</div>
				<div class="col-md-2 required">
					<label class="l" style="width: 100%">分配金额</label>
				</div>
			</div>
			<!-- ko foreach:chosenOrders -->
			<div class="input-row clearfloat" st="back_allot">
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.team_number"></p>
						<input type="hidden" data-bind="value:$data.team_number" st="team_number" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						<input type="hidden" data-bind="value:$data.supplier_employee_pk" st="supplier_employee_pk" /> <input type="hidden" data-bind="value:$data.supplier_employee_name" st="supplier_employee_name" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.departure_date"></p>
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<p class="ip-default rmb" data-bind="text:$data.final_balance"></p>
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
						<!-- /ko -->
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<input type="number" class="form-control" st="back_receive" data-bind="attr:{'name':'name-'+$data.pk},value: $data.final_balance*-1" required="required" />
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<input type="number" class="form-control" st="back_receive" data-bind="attr:{'name':'name-'+$data.pk},value: $data.budget_balance*-1" required="required" />
						<!-- /ko -->

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

	<!-- 支付申请-->
	<div id="pay" style="display: none; width: 1000px; height: 700px; overflow: auto; padding-top: 30px;">
		<form id="form-pay">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">供应商</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:supplier_name()"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">应付款总额</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default rmb" data-bind="text:totalPay()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">支付时限</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="detail.limit_time" class="form-control date-picker" data-bind="value:today()" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">支付总额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.allot_money" class="ip-" st="sum_paid" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-3">
					<label class="l" style="width: 100%">团号</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">操作</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">出团日期</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">应付款</label>
				</div>
				<div class="col-md-2 required">
					<label class="l" style="width: 100%">支付金额</label>
				</div>
			</div>

			<!-- ko foreach:chosenOrders -->
			<div class="input-row clearfloat" st="pay_allot">
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.team_number"></p>
						<input type="hidden" data-bind="value:$data.team_number" st="team_number" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						<input type="hidden" data-bind="value:$data.supplier_employee_pk" st="supplier_employee_pk" /> <input type="hidden" data-bind="value:$data.supplier_employee_name" st="supplier_employee_name" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.departure_date"></p>
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<p class="ip-default rmb" data-bind="text:$data.final_balance"></p>
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
						<!-- /ko -->
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<input type="number" class="form-control" st="paid" data-bind="attr:{'name':'name-'+$data.pk},value: $data.final_balance" required="required" />
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<input type="number" class="form-control" st="paid" data-bind="attr:{'name':'name-'+$data.pk},value: $data.budget_balance" required="required" />
						<!-- /ko -->

					</div>
				</div>
			</div>
			<!-- /ko -->
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
	<div id="strike" style="display: none; width: 900px; padding-top: 30px;">
		<form id="form-strike">
			<div class="input-row clearfloat">
				<div class="col-md-4">
					<label class="l" style="width: 30%">供应商</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:supplier_employee_name()"></p>
						<input type="hidden"  name="detail.supplier_employee_pk" data-bind="value:supplier_employee_pk()" />
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
					<label class="l" style="width: 100%">供应商</label>
				</div>
				<div class="col-md-3">
					<label class="l" style="width: 100%">尾款</label>
				</div>
				<div class="col-md-3 required">
					<label class="l" style="width: 100%">分配金额</label>
				</div>
			</div>
			<!-- ko foreach:positivePayables -->
			<div class="input-row clearfloat" st="strike-allot">
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.team_number"></p>
						<input type="hidden" data-bind="value:$data.team_number" st="strike-team_number" />
					</div>
				</div>
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						<input type="hidden" st="supplier-employee-pk"  data-bind="value:$data.supplier_employee_pk" />
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
						<input type="number" class="form-control" st="strike-paid" required="required" />
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

	<!-- 扣款申请-->
	<div id="deduct" style="display: none; width: 1000px; height: 700px; overflow: auto; padding-top: 30px;">
		<form id="form-deduct">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">供应商</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:supplier_name()"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">应付款总额</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default rmb" data-bind="text:totalPay()"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">扣款金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.allot_money" class="ip-" st="sum_deduct" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-3">
					<label class="l" style="width: 100%">团号</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">操作</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">出团日期</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">应付款</label>
				</div>
				<div class="col-md-2 required">
					<label class="l" style="width: 100%">扣款分配</label>
				</div>
			</div>
			<!-- ko foreach:chosenOrders -->
			<div class="input-row clearfloat" st="deduct_allot">
				<div class="col-md-3">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.team_number"></p>
						<input type="hidden" data-bind="value:$data.team_number" st="team_number" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						<input type="hidden" data-bind="value:$data.supplier_employee_pk" st="supplier_employee_pk" /> <input type="hidden" data-bind="value:$data.supplier_employee_name" st="supplier_employee_name" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.departure_date"></p>
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<p class="ip-default rmb" data-bind="text:$data.final_balance"></p>
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
						<!-- /ko -->
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<input type="number" class="form-control" st="deduct" data-bind="attr:{'name':'name-'+$data.pk},value: $data.final_balance" required="required" />
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<input type="number" class="form-control" st="deduct" data-bind="attr:{'name':'name-'+$data.pk},value: $data.budget_balance" required="required" />
						<!-- /ko -->

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
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyDeduct">申请</a>
					</div>
				</div>
			</div>
		</form>
	</div>

	<script>
		$(".product").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/sale/payable.js"></script>
</body>
</html>