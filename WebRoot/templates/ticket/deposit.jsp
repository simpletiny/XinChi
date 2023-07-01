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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/upload.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.confirmed {
	font-weight: bold;
}

h2 {
	padding-left: 20px !important;
}
</style>

</head>

<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>航司押金</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: create">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: upload">批量上传</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: receive">退还申请</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: deduct">押金扣款</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: delete_deposit">删除</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: status" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="deposit.statuses" /><label data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">金额</label>
						<div class="col-md-1" style="float: left">
							<input type="number" class="form-control" placeholder="大于等于" name="deposit.money_from" />
						</div>
						<div class="col-md-1" style="float: left">
							<input type="number" class="form-control" placeholder="小于等于" name="deposit.money_to" />
						</div>
						<label class="col-md-1 control-label">供应商</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control" name="deposit.supplier_name" />
						</div>
						<label class="col-md-1 control-label">备注</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control" name="deposit.comment" placeholder="填写部分信息即可" />
						</div>
						
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">精确金额</label>
						<div class="col-md-2" style="float: left">
							<input type="number" class="form-control" style="width: 44%" placeholder="精确金额" name="deposit.money" />
						</div>
						<label class="col-md-1 control-label">支出账户</label>
						<div class="col-md-2" style="float: left">
							<select class="form-control" name="deposit.account"
								data-bind="options: ticketAccounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --',event:{change:refresh}"></select>
						</div>
						<button type="submit" style="float:right" class="btn btn-green" data-bind="click: refresh">搜索</button>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" id="table-main">
						<thead>
							<tr role="row">
								<th></th>
								<th>押金单号</th>
								<th>支出账户</th>
								<th>收款方</th>
								<th>金额</th>
								<th>已退</th>
								<th>剩余</th>
								<th>到期时间</th>
								<th>状态</th>
								<th>退还途径</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach:deposits">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenDeposits" /></td>
								<td data-bind="text: $data.deposit_number"></td>
								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.received" class="rmb"></td>
								<td data-bind="text: $data.balance" class="rmb"></td>
								<td data-bind="text: $data.return_date"></td>
								<!-- ko if:$data.status=='N' -->
								<td data-bind="text: $root.statusMapping[$data.status]" style="color: red"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='Y' -->
								<td data-bind="text: $root.statusMapping[$data.status]" style="color: green"></td>
								<!-- /ko -->
								<td><a href="javascript:void(0)" data-bind="text: $data.return_way_cn,click:$root.viewDetail"></a></td>
								<td data-bind="text: $data.comment"></td>
								<!-- ko if:$data.status=='I' -->
								<!-- <td data-bind="text: $root.statusMapping[$data.status]"></td> -->
								<!-- /ko -->
								<!-- <td><a href="javascript:void(0)" data-bind="click:$root.viewDetail">查看</a></td> -->
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
	<div id="div-upload" style="display: none; width: 500px; height: 200px; overflow-y: auto; padding-top: 30px;">
		<div class="input-row clearfloat" style="height: 40px">
			<div class="col-md-8 required">
				<input type="text" class="ip-default file-path" required="required" />
			</div>
			<div class="col-md-4">
				<a href="javascript:;" class="a-upload">选择文件<input type="file" class="file-office"
					accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" id="file-upload" /></a> <input
					type="hidden" id="office-file" />
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click:doUpload">上传</button>
			<button type="submit" class="btn btn-green col-md-1" data-bind="click:cancelUpload">取消</button>
		</div>
	</div>
	<div id="div-upload-confirm" style="display: none; width: 1200px; height: 700px; overflow-y: auto; padding-top: 30px;">
		<div class="input-row clearfloat" style="height: 40px">
			<label style="color: red">请确认每笔上传记录，不确认的视为错误数据，不进行记录!</label>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover">
				<thead>
					<tr role="row">
						<th>支付序</th>
						<th>支出账户</th>
						<th>收款方</th>
						<th>金额</th>
						<th>支付时间</th>
						<th>到期时间</th>
						<th>备注</th>
						<th>确认</th>
					</tr>
				</thead>
				<tbody id="tbody-data" data-bind="foreach:batDeposits">
					<tr>
						<td data-bind="text: $data.pay_index"></td>
						<td data-bind="text: $data.account"></td>
						<td data-bind="text: $data.supplier_name"></td>
						<td data-bind="text: $data.money" class="rmb"></td>
						<td data-bind="text: $data.time"></td>
						<td data-bind="text: $data.return_date"></td>
						<td data-bind="text: $data.comment"></td>
						<td><a href="javascript:void(0)" data-bind="event:{click:confirmUpload}">确认</a></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click:doSaveBat">保存</button>
			<button type="submit" class="btn btn-green col-md-1" data-bind="click:cancelSaveBat">取消</button>
		</div>
	</div>
	<div id="div-create" style="display: none; width: 1200px; height: 750px; overflow-y: auto; padding-top: 30px;">
		<form class="form-box info-form" id="form-create">
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">支出账户</label>
					<div class="ip">
						<select class="form-control" name="deposit.account"
							data-bind="options: ticketAccounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --'"
							required="required"></select>
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">供应商</label>
					<div class="ip">
						<input type="text" class="ip-" data-bind="click:choseFinancial" placeholder="点击选择" id="financial-body-name"
							required="required" /> <input type="text" class="ip-" data-bind="click:choseFinancial" style="display: none"
							name="deposit.supplier_pk" id="financial-body-pk" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">金额</label>
					<div class="ip">
						<input type="number" class="ip-default" placeholder="押金金额" name="deposit.money" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">入账时间</label>
					<div class="ip">
						<input type="text" name="deposit.time" placeholder="请准确填写避免冲突" class="form-control datetime-picker"
							required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">退还日期</label>
					<div class="ip">
						<input type="text" maxlength="10" class="ip-default date-picker" placeholder="归还日期" name="deposit.return_date"
							required="required" />
					</div>
				</div>
				<div class="col-md-6">
					<label class="l">备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="4" maxlength="200" name="deposit.comment" placeholder="需要备注说明的信息"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required" class="file-img"
						accept="image/jpeg,image/png" name="file2" /></a> <input type="hidden" name="deposit.voucher_file_name" />
				</div>
				<div class="col-md-6"></div>
			</div>
			<div class="input-row clearfloat"></div>
		</form>
		<div class="form-group">
			<div style="float: right">
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: doCreate">提交</button>
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: cancelCreate">取消</button>
			</div>
		</div>
	</div>
	<div id="div-receive" style="display: none; width: 1200px; height: 750px; overflow-y: auto; padding-top: 30px;">
		<form class="form-box info-form" id="form-receive">
			<div class="input-row clearfloat">
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">收款账户</label>
					<div class="ip" style="width: 70%">
						<select class="form-control" name="deposit.account"
							data-bind="options: accounts,value:account(), optionsCaption: '-- 请选择 --'" required="required"></select>
					</div>
				</div>
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">退还总金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" class="ip-default" placeholder="退还总金额" name="deposit.money" id="sum-money"
							required="required" data-bind="value:sum_money()" />
					</div>
				</div>
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">退还日期</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="deposit.time" placeholder="退还日期" class="form-control date-picker" required="required" />
					</div>
				</div>
			</div>

			<hr />
			<div data-bind="foreach: chosenDeposits" id="div-allot">
				<div>
					<input type="hidden" data-bind="value:$data.pk" st="deposit-pk" /> <input type="hidden"
						data-bind="value:$data.balance" st="deposit-balance" />
					<div class="input-row clearfloat">
						<div class="col-md-4">
							<label class="l" style="width: 30%">供应商</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text: $data.supplier_name"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l" style="width: 30%">剩余押金</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text:$data.balance"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l" style="width: 30%">押金备注</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text:$data.comment"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">分配金额</label>
							<div class="ip">
								<input type="number" class="ip-default" placeholder="押金金额"
									data-bind="attr:{'name':'money_'+$index()},value:$data.balance" st="money" min="1" required="required" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">备注</label>
							<div class="ip">
								<textarea rows="3" class="ip-default" cols="30" maxlength="200" st="comment" placeholder="备注"></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>
			<hr />
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required" class="file-img"
						accept="image/jpeg,image/png" name="file2" /></a> <input type="hidden" name="deposit.voucher_file_name" />
				</div>
				<div class="col-md-6"></div>
			</div>
			<div class="input-row clearfloat"></div>
		</form>
		<div class="form-group">
			<div style="float: right">
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: doReceive">提交</button>
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: cancelReceive">取消</button>
			</div>
		</div>
	</div>
	<!-- 选择供应商 -->
	<div id="financial-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">主体简称</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="主体简称" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchFinancial }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>财务主体简称</th>
								<th>负责人</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: suppliers">
							<tr data-bind="event: {click: function(){ $parent.pickFinancial($data.supplier_short_name,$data.pk)}}">
								<td data-bind="text: $data.supplier_short_name"></td>
								<td data-bind="text: $data.body_name"></td>
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
		</div>
	</div>
	<!-- 押金扣款 -->
	<div id="div-deduct" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
		<h3 style="margin-left: 100px; color: red">押金账</h3>
		<form id="form-deduct">
			<div style="min-height: 200px">
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
						<tbody data-bind="foreach: chosenDeposits" id="tbody-info">
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
				<div class="col-md-6 required">
					<label class="l">扣款时间</label>
					<div class="ip" style="width: 40%">
						<input type="text" class="ip- date-picker date" id="deduct-date" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">责任经理</label>
					<div class="ip" style="width: 40%">
						<select class="form-control" id="product-manager" required="required"
							data-bind="options: product_managers,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--请选择--'"></select>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">归属月份</label>
					<div class="ip" style="width: 40%">
						<input type="text" class="ip- month-picker-st" id="belong-month" value='${user.current_date.substring(0,7)}'
							placeholder="月份" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-8 required">
					<label class="l">备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="7" maxlength="200" id="comment" placeholder="需要备注说明的信息"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: doDeduct">提交</a> <a type="button"
							class="btn btn-green btn-r" data-bind="click: cancelDeduct">取消</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div id="div-return-detail" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
		<!-- ko if: detail_return_way().indexOf("T")>-1 -->
		<h2>押金退还</h2>
		<div class="input-row clearfloat">
			<label class="l">退还总额</label>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:sum_receiveds()"></p>
				</div>
			</div>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover" id="table-received">
				<thead>
					<tr role="row">
						<th>收款账户</th>
						<th>退还金额</th>
						<th>收入时间</th>
						<th>状态</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody id="tbody-data" data-bind="foreach:deposit_receiveds">
					<tr>
						<td data-bind="text: $data.card_account"></td>
						<td data-bind="text: $data.received" class="rmb"></td>
						<td data-bind="text: $data.received_time"></td>
						<td data-bind="text: $data.status"></td>
						<td data-bind="text: $data.comment"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<hr />
		<!-- /ko -->
		<!-- ko if: detail_return_way().indexOf("K")>-1 -->
		<h2>押金扣款</h2>
		<div class="input-row clearfloat">
			<label class="l">扣款总额</label>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:sum_deducts()"></p>
				</div>
			</div>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover" id="table-received">
				<thead>
					<tr role="row">
						<th>收款方</th>
						<th>扣款金额</th>
						<th>入账日期</th>
						<th>备注</th>
						<th>责任产品</th>
						<th>归属月份</th>
					</tr>
				</thead>
				<tbody id="tbody-data" data-bind="foreach:deposit_deducts">
					<tr>
						<td data-bind="text: $data.receiver"></td>
						<td data-bind="text: $data.money" class="rmb"></td>
						<td data-bind="text: $data.time"></td>
						<td data-bind="text: $data.comment"></td>
						<td data-bind="text: $data.product_manager" class="rmb"></td>
						<td data-bind="text: $data.belong_month" class="rmb"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<hr />
		<!-- /ko -->
		<!-- ko if:detail_return_way().indexOf("C")>-1 -->
		<h2>押金冲账</h2>
		<div class="input-row clearfloat">
			<label class="l">冲账总额</label>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:sum_strikes()"></p>
				</div>
			</div>
		</div>
		<div class="list-result">
			<table class="table table-striped table-hover" id="table-received">
				<thead>
					<tr role="row">
						<th>供应商</th>
						<th>冲账金额</th>
						<th>首航日期</th>
						<th>首航段</th>
						<th>入账日期</th>
					</tr>
				</thead>
				<tbody id="tbody-data" data-bind="foreach:deposit_strikes">
					<tr>
						<td data-bind="text: $data.financial_body_name"></td>
						<td data-bind="text: $data.money" class="rmb"></td>
						<td data-bind="text: $data.first_date"></td>
						<td data-bind="text: $data.frist_from_to"></td>
						<td data-bind="text: $data.time"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- /ko -->
	</div>


	<script>
		$(".ticket-operation").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/file-upload.js"></script>
	<script src="<%=basePath%>static/js/file-upload-office.js?v1.001"></script>
	<script src="<%=basePath%>static/js/ticket/deposit.js?v1.003"></script>
</body>
</html>