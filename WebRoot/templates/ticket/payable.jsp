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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/upload.css" />
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
	right: 0px;
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
			<h2>机票往来</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">首航日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="首航日期" name="option.first_date" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">首航段</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="首航段" name="option.from_to_city" />
							</div>
						</div>
						<label class="col-md-1 control-label">首航月份</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control month-picker-st" placeholder="首航月份" name="option.first_month" />
						</div>
						<div class="span6">
							<div data-bind="foreach: payableTypes" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenTypes,click:function(){$root.refresh();return true;}"
									name="option.payable_types" /><label data-bind="text: $root.payableTypesMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">供应商</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="供应商" name="option.supplier_employee_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">财务主体</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="财务主体" name="option.financial_body_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">备注</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="备注" name="option.comment" />
							</div>
						</div>
						<div class="span6">
							<em class="small-box"> <label>尾款为0</label> <input type="checkbox" value="Y" name="option.zero_flg"
								data-bind="event:{click:zeroBalance}" />
							</em>
						</div>
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>供应商</th>
								<th>主体</th>
								<th>类型</th>
								<th>PNR</th>
								<th>首航日期</th>
								<th>首航段</th>
								<th>名单</th>
								<th>应付款</th>
								<th>已付款</th>
								<th>尾款</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: payables">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenPayables" /></td>
								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
								<td data-bind="text: $root.payableTypesMapping[$data.payable_type]"></td>
								<td data-bind="text: $data.PNR"></td>
								<td><a href="javascript:void(0)"
									data-bind="click:function(){$root.checkTicketInfo($data);},text: $data.first_date"></a></td>
								<td><a href="javascript:void(0)"
									data-bind="click:function(){$root.checkTicketInfo($data);},text: $data.from_to_city"></a></td>
								<td><a href="javascript:void(0)"
									data-bind="click:function(){$root.checkPassengers($data);},text: $data.passenger"></a></td>
								<td data-bind="text:$data.budget_payable" class="rmb"></td>
								<td data-bind="text: $data.paid" class="rmb"></td>
								<td data-bind="text:$data.budget_balance" class="rmb"></td>
								<td data-bind="text:$data.comment"></td>
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
							<td data-bind="text:totalBudgetPayable" class="rmb"></td>
							<td data-bind="text:totalPaid" class="rmb"></td>
							<td data-bind="text:totalBudgetBalance" class="rmb"></td>
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
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { pay()}">支付</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { receive() }">收入</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { businessStrike() }">业务冲抵</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { depositStrike() }">押金冲抵</button>
					</div>
				</div>
			</div>

		</div>
	</div>

	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 查看机票信息-->
	<div id="infos-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">日期</th>
							<th style="width: 10%">时间</th>
							<th style="width: 10%">城市对</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:infos">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.ticket_date"></td>
							<td data-bind="text:$data.from_to_time"></td>
							<td data-bind="text:$data.from_to_city"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 返款收入 -->
	<div id="receive" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
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
						<select class="form-control" data-bind="options: cards, optionsCaption: '-- 请选择 --'" name="detail.account"
							required="required"></select>
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" name="detail.allot_money" class="ip-" st="sum_received" data-bind="value:totalBack()"
							required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">收入时间</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="detail.time" placeholder="收入时间" class="form-control datetime-picker" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-2">
					<label class="l" style="width: 100%">供应商</label>
				</div>
				<div class="col-md-2">
					<label class="l" style="width: 100%">应返款</label>
				</div>
				<div class="col-md-2 required">
					<label class="l" style="width: 100%">分配金额</label>
				</div>
			</div>
			<!-- ko foreach:chosenPayables -->
			<div class="input-row clearfloat" st="back_allot">
				<input type="hidden" data-bind="value: $data.pk" st="base-pk" />
				<div class="col-md-2">
					<div class="ip">
						<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						<input type="hidden" data-bind="value:$data.supplier_employee_pk" st="supplier_employee_pk" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<p class="ip-default rmb" data-bind="text:$data.final_balance*-1"></p>
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<p class="ip-default rmb" data-bind="text:$data.budget_balance*-1"></p>
						<!-- /ko -->
					</div>
				</div>
				<div class="col-md-2">
					<div class="ip">
						<!-- ko if:$data.final_flg=="Y" -->
						<input type="number" class="form-control" st="back_receive"
							data-bind="attr:{'name':'name-'+$data.pk},value: $data.final_balance*-1" required="required" />
						<!-- /ko -->
						<!-- ko if:$data.final_flg=="N" -->
						<input type="number" class="form-control" st="back_receive"
							data-bind="attr:{'name':'name-'+$data.pk},value: $data.budget_balance*-1" required="required" />
						<!-- /ko -->

					</div>
				</div>
			</div>
			<!-- /ko -->
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required"
						accept="image/jpeg,image/png" name="file2" /></a> <input type="hidden" name="detail.voucher_file" />
				</div>
				<div class="col-md-6"></div>
			</div>
			<div class="input-row clearfloat"></div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: applyReceive">提交</a>
					</div>
				</div>
			</div>
		</form>
	</div>

	<!-- 业务冲账 -->
	<div id="business-strike" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
		<form id="form-business-strike">
			<h3 style="margin-left: 100px; color: red">冲出</h3>
			<div data-bind="foreach:negativePayables()" id="div-strike-out">
				<div>
					<input type="hidden" data-bind="value:$data.pk" st="payable-pk" /> <input type="hidden"
						data-bind="value:$data.budget_balance" st="budget-balance" />
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l" style="width: 30%">供应商</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text:$data.financial_body_name"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l" style="width: 30%">应付款</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default rmb" data-bind="text:$data.budget_payable"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l" style="width: 30%">尾款</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l" style="width: 40%">冲出金额</label>
							<div class="ip" style="width: 60%">
								<input type="number" data-bind="attr:{'name':'out_money_'+$index()}" min="1" class="ip-" st="strike-out-money"
									required="required" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<hr />
			<h3 style="margin-left: 100px; color: green">冲入</h3>
			<div data-bind="foreach:positivePayables()" id="div-strike-in">
				<div>
					<input type="hidden" data-bind="value:$data.pk" st="payable-pk" /> <input type="hidden"
						data-bind="value:$data.budget_balance" st="budget-balance" />
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l" style="width: 30%">供应商</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text:$data.financial_body_name"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l" style="width: 30%">应付款</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default rmb" data-bind="text:$data.budget_payable"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l" style="width: 30%">尾款</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l" style="width: 40%">冲入金额</label>
							<div class="ip" style="width: 60%">
								<input type="number" data-bind="attr:{'name':'in_money_'+$index()}" min="1" class="ip-" st="strike-in-money"
									required="required" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="input-row clearfloat">
			<div class="col-md-12" style="margin-top: 10px">
				<div align="right">
					<a type="button" class="btn btn-green btn-r" data-bind="click: doBusinessStrike">提交</a> <a type="button"
						class="btn btn-green btn-r" data-bind="click: cancelBusinessStrike">取消</a>
				</div>
			</div>
		</div>
	</div>

	<!-- 押金冲账 -->
	<div id="deposit-strike" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
		<form id="form-deposit-strike">
			<h3 style="margin-left: 100px; color: red">冲出</h3>
			<div style="min-height: 300px">
				<div class="input-row clearfloat">
					<div class="col-md-12">
						<a type="button" class="btn btn-green btn-r" style="float: right" data-bind="click: chooseDeposit">选择冲出押金</a>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>支出账户</th>
								<th>收款方</th>
								<th>金额</th>
								<th>已退</th>
								<th>可用冲账</th>
								<th>到期时间</th>
								<th>备注</th>
								<th style="color: red">冲出金额</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: usedDeposits" id="table-deposit-out">
							<tr>

								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.received" class="rmb"></td>
								<td data-bind="text: $data.balance" class="rmb" style="color: red"></td>
								<td data-bind="text: $data.return_date"></td>
								<td data-bind="text: $data.comment"></td>
								<td><input type="number" data-bind="attr:{'name':'deposit_out_money_'+$index()}" min="1" class="ip-"
									st="deposit-out-money" required="required" /> <input type="hidden" data-bind="value:$data.pk" st="deposit-pk" />
									<input type="hidden" data-bind="value:$data.balance" st="deposit-balance" /></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>

			<hr />
			<h3 style="margin-left: 100px; color: red">冲入</h3>
			<div data-bind="foreach:chosenPayables()" id="div-deposit-in">
				<div>
					<input type="hidden" data-bind="value:$data.pk" st="payable-pk" /> <input type="hidden"
						data-bind="value:$data.budget_balance" st="budget-balance" />
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l" style="width: 30%">供应商</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text:$data.financial_body_name"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l" style="width: 30%">应付款</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default rmb" data-bind="text:$data.budget_payable"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l" style="width: 30%">尾款</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default rmb" data-bind="text:$data.budget_balance"></p>
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l" style="width: 40%">冲入金额</label>
							<div class="ip" style="width: 60%">
								<input type="number" data-bind="attr:{'name':'deposit_in_money_'+$index()}" min="1" class="ip-"
									st="deposit-in-money" required="required" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<hr />
		</form>
		<div class="input-row clearfloat">
			<div class="col-md-12" style="margin-top: 10px">
				<div align="right">
					<a type="button" class="btn btn-green btn-r" data-bind="click: doDepositStrike">提交</a> <a type="button"
						class="btn btn-green btn-r" data-bind="click: cancelDepositStrike">取消</a>
				</div>
			</div>
		</div>
	</div>

	<!-- 选择航司押金 -->
	<div id="deposit-pick" style="display: none; width: 1210px; height: 600px; overflow: auto">
		<form class="form-horizontal search-panel" id="form-search-deposit">
			<div class="form-group">
				<label class="col-md-1 control-label">供应商</label>
				<div class="col-md-2" style="float: left">
					<input type="text" class="form-control" name="deposit.supplier_name" />
				</div>
				<label class="col-md-1 control-label">支出账户</label>
				<div class="col-md-3" style="float: left">
					<select class="form-control" name="deposit.account"
						data-bind="options: ticketAccounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --',event:{change:refresh}"></select>
				</div>
				<label class="col-md-1 control-label">押金单号</label>
				<div class="col-md-3" style="float: left">
					<input type="text" class="form-control" name="deposit.deposit_number" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-1 control-label">押金金额</label>
				<div class="col-md-2" style="float: left">
					<input type="text" class="form-control" name="deposit.money" />
				</div>
				<label class="col-md-1 control-label">备注</label>
				<div class="col-md-2" style="float: left">
					<input type="text" class="form-control" name="deposit.comment" placeholder="填写部分信息即可" />
				</div>
				<div>
					<button type="submit" class="btn btn-green" data-bind="click: searchDeposit">搜索</button>
					<button type="submit" class="btn btn-green" data-bind="click: finishChoose">选择</button>
				</div>
			</div>
		</form>
		<div class="list-result">
			<table class="table table-striped table-hover">
				<thead>
					<tr role="row">
						<th></th>
						<th>押金单号</th>
						<th>支出账户</th>
						<th>收款方</th>
						<th>金额</th>
						<th>已退</th>
						<th>可用冲账</th>
						<th>到期时间</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody data-bind="foreach: deposits">
					<tr>
						<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenDeposits" /></td>
						<td data-bind="text: $data.deposit_number"></td>
						<td data-bind="text: $data.account"></td>
						<td data-bind="text: $data.supplier_name"></td>
						<td data-bind="text: $data.money" class="rmb"></td>
						<td data-bind="text: $data.received" class="rmb"></td>
						<td data-bind="text: $data.balance" class="rmb" style="color: red"></td>
						<td data-bind="text: $data.return_date"></td>
						<td data-bind="text: $data.comment"></td>
					</tr>
				</tbody>
			</table>
			<div class="pagination clearfloat">
				<a data-bind="click: previousPage1, enable: currentPage1() > 1" class="prev">Prev</a>
				<!-- ko foreach: pageNums1 -->
				<!-- ko if: $data == $root.currentPage1() -->
				<span class="current" data-bind="text: $data"></span>
				<!-- /ko -->
				<!-- ko ifnot: $data == $root.currentPage1() -->
				<a data-bind="text: $data, click: $root.turnPage1"></a>
				<!-- /ko -->
				<!-- /ko -->
				<a data-bind="click: nextPage1, enable: currentPage1() < pageNums1().length" class="next">Next</a>
			</div>
		</div>
	</div>
	<script>
		$(".ticket-finance").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/file-upload.js"></script>
	<script src="<%=basePath%>static/js/ticket/payable.js?v=1.002"></script>
</body>
</html>