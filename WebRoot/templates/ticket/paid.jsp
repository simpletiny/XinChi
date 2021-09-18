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
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function(){createDetail('R');}">添加收入</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function(){createDetail('P');}">添加支出</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function(){createDeduct();}">押金扣款</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: rollBack">打回重报</button>
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
							<label class="col-md-1 control-label">类型</label>
							<div class="col-md-2" style="float: left">
								<select class="form-control"
									data-bind="options: types, optionsText:function(item){return typeMapping[item]}, optionsCaption: '-- 请选择 --',event: {change:refresh}"
									name="option.type" required="required"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">入账日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateFrom" placeholder="from"
									name="option.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: dateTo" placeholder="to"
									name="option.date_to" />
							</div>
						</div>
						<!-- 						<div  align="left">
							<label class="col-md-1 control-label">产品经理</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.create_user" />
							</div>
						</div> -->
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
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
								<th>供应商员工</th>
								<th>收款方</th>
								<th>入账日期</th>
								<th>状态</th>
								<th>支付详情</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: paids">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.related_pk}, checked: $root.chosenPaids" /></td>
								<!-- ko if:$data.type=='STRIKEIN' || $data.type=='STRIKEOUT' || $data.type=='DSTRIKEIN' -->
								<td data-bind="text: $data.money" class="rmb"></td>
								<!-- /ko -->
								<!-- ko if:$data.type!='STRIKEIN' & $data.type!='STRIKEOUT'& $data.type!='DSTRIKEIN' -->
								<td data-bind="text: $data.allot_money" class="rmb"></td>
								<!-- /ko -->
								<td style="color: green" data-bind="text: $root.typeMapping[$data.type]"></td>
								<td data-bind="text: $data.supplier_employee_name"></td>
								<!-- ko if:$data.type!='DEDUCT' -->
								<td data-bind="text: $data.financial_body_name"></td>
								<!-- /ko -->
								<!-- ko if:$data.type=='DEDUCT' -->
								<td data-bind="text: $data.receiver"></td>
								<!-- /ko -->
								<td data-bind="text: $data.time"></td>
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
								<td style="color: red" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<td><a href="javascript:void(0)" data-bind="click:$root.viewDetail">查看</a></td>
								<td data-bind="text: $data.comment"></td>
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
	<div id="sum_detail" style="display: none; width: 1200px; height: 500px; overflow-y: auto; padding-top: 30px;">

		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">收款方</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">支付金额</label>
			</div>
		</div>
		<!-- ko foreach:details -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.financial_body_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
		<div class="input-row clearfloat">
			<div class="col-md-2">
				<label class="l" style="width: 100%">收支账户</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">收支时间</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">收款方账户</label>
			</div>
			<div class="col-md-2">
				<label class="l" style="width: 100%">收支金额</label>
			</div>
			<div class="col-md-2">
				<label class="l" style="width: 100%">凭证</label>
			</div>
		</div>
		<!-- ko foreach:paymentDetails -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.account"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.time"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.receiver"></p>
				</div>
			</div>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money"></p>
				</div>
			</div>

			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default">
						<a href="javascript:void(0)"
							data-bind="click: function() {$root.checkVoucherPic($data.voucher_file_name,$data.account_pk)} ">查看</a>
					</p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<!-- 无业务扣款 -->
	<div id="div-deduct" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
		<h3 style="margin-left: 100px; color: red">押金账</h3>
		<form id="form-deduct">
			<div style="min-height: 200px">
				<div class="input-row clearfloat">
					<div class="col-md-12">
						<a type="button" class="btn btn-green btn-r" style="float: right" data-bind="click: chooseDeposit">选择押金</a>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>付押账户</th>
								<th>押金方</th>
								<th>金额</th>
								<th>已退</th>
								<th>可用余额</th>
								<th>到期时间</th>
								<th>备注</th>
								<th style="color: red">扣款金额</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: usedDeposits" id="tbody-info">
							<tr>

								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.received" class="rmb"></td>
								<td data-bind="text: $data.balance" class="rmb" style="color: red"></td>
								<td data-bind="text: $data.return_date"></td>
								<td data-bind="text: $data.comment"></td>
								<td><input type="number" min="1" class="ip-" id="deduct-money" required="required" /> <input type="hidden"
									data-bind="value:$data.pk" id="deposit-pk" /> <input type="hidden" data-bind="value:$data.balance"
									id="deposit-balance" /></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l">扣款时间</label>
					<div class="ip" style="width: 20%">
						<input type="text" class="ip- date-picker date" id="deduct-date" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l">备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="3" maxlength="200" id="comment" placeholder="需要备注说明的信息"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: doCreateDeduct">提交</a> <a type="button"
							class="btn btn-green btn-r" data-bind="click: cancelDeduct">取消</a>
					</div>
				</div>
			</div>
		</form>
	</div>

	<!-- 选择航司押金 -->
	<div id="deposit-pick" style="display: none; width: 890px">
		<form class="form-horizontal search-panel" id="form-search-deposit">
			<div class="form-group">
				<div>
					<label class="col-md-1 control-label">供应商</label>
					<div class="col-md-3" style="float: left">
						<input type="text" class="form-control" name="deposit.supplier_name" />
					</div>
				</div>
				<div>
					<label class="col-md-1 control-label">支出账户</label>
					<div class="col-md-3" style="float: left">
						<select class="form-control" name="deposit.account"
							data-bind="options: ticketAccounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --',event:{change:refresh}"></select>
					</div>
				</div>
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: searchDeposit">搜索</button>
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: finishChoose">选择</button>
			</div>
		</form>
		<div class="list-result">
			<table class="table table-striped table-hover">
				<thead>
					<tr role="row">
						<th></th>
						<th>支出账户</th>
						<th>收款方</th>
						<th>金额</th>
						<th>已退</th>
						<th>可用余额</th>
						<th>到期时间</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody data-bind="foreach: deposits">
					<tr>
						<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenDeposits" /></td>
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
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/paid.js"></script>
</body>
</html>